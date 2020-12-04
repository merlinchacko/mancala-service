package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.enums.GameStatus;
import com.bol.mancala.mancalagame.enums.PitType;
import com.bol.mancala.mancalagame.exception.GameNotFoundException;
import com.bol.mancala.mancalagame.exception.PitNotFoundException;
import com.bol.mancala.mancalagame.model.Game;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.repository.GameRepository;
import com.bol.mancala.mancalagame.repository.PitRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class GameServiceTest {
    private GameService gameService;
    @Mock
    private GameRepository gameRepository;

    Player firstPlayer, secondPlayer;

    @Before
    public void init() {
        gameService = new GameService(gameRepository);
        firstPlayer = new Player("Merlin");
        secondPlayer = new Player("Sunil");
    }

    @Test
    public void createGameTest() {
        Game game = gameService.createGame(firstPlayer, secondPlayer);

        assertEquals(game.getFirstPlayer().getName(), "Merlin");
        assertEquals(game.getPlayerTurn().getName(), "Merlin");

        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void loadGameTest() {
        Game game = new Game(firstPlayer, secondPlayer, secondPlayer, GameStatus.IN_PROGRESS);
        when(gameRepository.findById(12L)).thenReturn(Optional.of(game));
        Game result = gameService.loadGame(12L);

        assertEquals(result.getPlayerTurn(), game.getPlayerTurn());
    }


    @Test(expected = GameNotFoundException.class)
    public void loadGameTestIfGameNotPresent() {
        when(gameRepository.findById(12L)).thenThrow(new GameNotFoundException("not found!"));
        gameService.loadGame(12L);
    }

    @Test
    public void updatePitByStonesTest() {
        Game game = new Game(firstPlayer, secondPlayer, secondPlayer, GameStatus.IN_PROGRESS);
        gameService.updateGame(game);

        verify(gameRepository, times(1)).save(game);
    }

    @Test
    public void deleteAllGamesTest() {
        gameService.clearGameDetails();

        verify(gameRepository, times(1)).deleteAll();
    }

}
