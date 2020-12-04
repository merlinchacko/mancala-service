package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.constants.MancalaGameConstants;
import com.bol.mancala.mancalagame.enums.GameStatus;
import com.bol.mancala.mancalagame.enums.PitType;
import com.bol.mancala.mancalagame.model.Game;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Player;
import org.springframework.stereotype.Service;

@Service
public class MancalaGameService {

    private final GameService gameService;
    private final PitService pitService;
    private final PlayerService playerService;

    public MancalaGameService(GameService gameService, PitService pitService, PlayerService playerService) {
        this.gameService = gameService;
        this.pitService = pitService;
        this.playerService = playerService;
    }

    public Game createGame(String player1Name, String player2Name) {
        Player player1 = playerService.createPlayer(player1Name);
        Player player2 = playerService.createPlayer(player2Name);

        Game game = gameService.createGame(player1, player2);

        for (int i = MancalaGameConstants.PLAYER1_START; i <= MancalaGameConstants.PLAYER1_END; i++) {
            pitService.createPit(game, i, MancalaGameConstants.INITIAL_SMALL_PIT_STONES, PitType.SMALL_PIT);
        }
        pitService.createPit(game, MancalaGameConstants.PLAYER1_BIG_PIT, MancalaGameConstants.INITIAL_BIG_PIT_STONES, PitType.BIG_PIT);

        for (int i = MancalaGameConstants.PLAYER2_START; i <= MancalaGameConstants.PLAYER2_END; i++) {
            pitService.createPit(game, i, MancalaGameConstants.INITIAL_SMALL_PIT_STONES, PitType.SMALL_PIT);
        }
        pitService.createPit(game, MancalaGameConstants.PLAYER2_BIG_PIT, MancalaGameConstants.INITIAL_BIG_PIT_STONES, PitType.BIG_PIT);

        game.setPits(pitService.getAllPits());
        return gameService.updateGame(game);
    }

    public Game loadGame(Long gameId) throws Exception {
        return gameService.loadGame(gameId);
    }

    public Game sowGame(Game game, Integer position) {
        if (position != MancalaGameConstants.PLAYER1_BIG_PIT && position != MancalaGameConstants.PLAYER2_BIG_PIT && game.getGameStatus() != GameStatus.FINISHED) {
            int stonesCount = pitService.getPit(position).getNumberOfStones();
            if (stonesCount > 0) {
                updateStartPit(position);
                setNextPlayerTurn(sowStonesToRight(position, stonesCount, game), game);
                checkGameFinished(game);
                gameService.updateGame(game);
            }
        }
        return game;
    }

    private void checkGameFinished(Game game) {
        if (isGameFinished(game)) {
            emptyAllPits();
            game.setGameStatus(GameStatus.FINISHED);
        } else {
            game.setGameStatus(GameStatus.IN_PROGRESS);
        }
    }

    private void updateStartPit(Integer position) {
        Pit startPit = pitService.getPit(position);
        startPit.clear();
        pitService.updatePitByStones(startPit.getPosition(), startPit.getNumberOfStones());
    }

    private int sowStonesToRight(int currentPosition, int stonesCount, Game game) {
        return sowLastStone(sowTillLastStone(currentPosition, stonesCount, game), game);
    }

    private int sowLastStone(int currentPosition, Game game) {
        int destinationPosition = currentPosition + 1;
        Pit destinationPit = pitService.getPit(destinationPosition);
        Pit oppositePit = pitService.getPit(MancalaGameConstants.NUMBER_OF_PITS - (destinationPosition + 2));
        if (destinationPit.getPitType().equals(PitType.SMALL_PIT) && destinationPit.isEmpty() && !oppositePit.isEmpty()) {

            if (game.getPlayerTurn().equals(game.getSecondPlayer()) && destinationPit.getPosition() >= MancalaGameConstants.PLAYER2_START && destinationPit.getPosition() <= MancalaGameConstants.PLAYER2_END) {
                captureStones(oppositePit, MancalaGameConstants.PLAYER2_BIG_PIT);
            } else if (game.getPlayerTurn().equals(game.getFirstPlayer()) && destinationPit.getPosition() >= MancalaGameConstants.PLAYER1_START && destinationPit.getPosition() <= MancalaGameConstants.PLAYER1_END) {
                captureStones(oppositePit, MancalaGameConstants.PLAYER1_BIG_PIT);
            }
            else {
                updateDestinationPit(destinationPit);
            }
            return destinationPosition;
        }
        updateDestinationPit(destinationPit);
        return destinationPosition;
    }

    private void updateDestinationPit(Pit destinationPit) {
        destinationPit.sow();
        pitService.updatePitByStones(destinationPit.getPosition(), destinationPit.getNumberOfStones());
    }

    private void captureStones(Pit oppositePit, int bigPitPosition) {
        int oppositePitStones = oppositePit.getNumberOfStones();
        oppositePit.clear();
        pitService.updatePitByStones(oppositePit.getPosition(), oppositePit.getNumberOfStones());
        Pit destinationBigPit = pitService.getPit(bigPitPosition);
        destinationBigPit.addStones(oppositePitStones + 1);
        pitService.updatePitByStones(destinationBigPit.getPosition(), destinationBigPit.getNumberOfStones());
    }

    private int sowTillLastStone(int currentPosition, int stonesCount, Game game) {
        boolean isLastStone = false;
        while (stonesCount != 1) {
            currentPosition = currentPosition + 1;
            if ((currentPosition == MancalaGameConstants.PLAYER1_BIG_PIT && game.getPlayerTurn().equals(game.getSecondPlayer())) ||
                    (currentPosition == MancalaGameConstants.PLAYER2_BIG_PIT && game.getPlayerTurn().equals(game.getFirstPlayer())))
                currentPosition = currentPosition + 1;
            Pit destinationPit = pitService.getPit(currentPosition);
            if (!isLastStone || currentPosition == MancalaGameConstants.PLAYER1_BIG_PIT || currentPosition == MancalaGameConstants.PLAYER2_BIG_PIT) {
                updateDestinationPit(destinationPit);
            }
            if (currentPosition == MancalaGameConstants.PLAYER2_BIG_PIT) {
                currentPosition = -1; //resetting to initial position 0 after last position 13
            }
            stonesCount--;
            isLastStone = stonesCount == 1;
        }
        return currentPosition;
    }

    private void setNextPlayerTurn(int finalPitPosition, Game game) {
        if (finalPitPosition != MancalaGameConstants.PLAYER1_BIG_PIT && finalPitPosition != MancalaGameConstants.PLAYER2_BIG_PIT) {
            if (game.getPlayerTurn().equals(game.getFirstPlayer()))
                game.setPlayerTurn(game.getSecondPlayer());
            else
                game.setPlayerTurn(game.getFirstPlayer());
        }
    }

    private boolean isGameFinished(Game game) {
        if (game.getPlayerTurn() == game.getFirstPlayer()) {
            return checkAllPitsAreEmpty(MancalaGameConstants.PLAYER1_START, MancalaGameConstants.PLAYER1_END);
        } else {
            return checkAllPitsAreEmpty(MancalaGameConstants.PLAYER2_START, MancalaGameConstants.PLAYER2_END);
        }
    }

    private Boolean checkAllPitsAreEmpty(int start, int end) {
        for (int i = start; i <= end; i++) {
            if (pitService.getPit(i).getNumberOfStones() > 0) {
                return false;
            }
        }
        return true;
    }

    private void emptyAllPits() {
        emptyPlayerPits(MancalaGameConstants.PLAYER1_START, MancalaGameConstants.PLAYER1_END, MancalaGameConstants.PLAYER1_BIG_PIT);
        emptyPlayerPits(MancalaGameConstants.PLAYER2_START, MancalaGameConstants.PLAYER2_END, MancalaGameConstants.PLAYER2_BIG_PIT);
    }

    private void emptyPlayerPits(int start, int end, int bigPitPosition) {
        for (int i = start; i <= end; i++) {
            int stonesLeft = pitService.getPit(i).getNumberOfStones();
            if (stonesLeft > 0) {
                Pit smallPit = pitService.getPit(i);
                smallPit.clear();
                pitService.updatePitByStones(smallPit.getPosition(), smallPit.getNumberOfStones());
                Pit bigPit = pitService.getPit(bigPitPosition);
                bigPit.addStones(stonesLeft);
                pitService.updatePitByStones(bigPit.getPosition(), bigPit.getNumberOfStones());
            }
        }
    }

    public void clearGameDetails() {
        pitService.clearPitDetails();
        gameService.clearGameDetails();
        playerService.clearPlayerDetails();
    }
}
