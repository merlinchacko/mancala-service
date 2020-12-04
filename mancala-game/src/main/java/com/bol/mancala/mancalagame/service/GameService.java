package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.enums.GameStatus;
import com.bol.mancala.mancalagame.exception.GameNotFoundException;
import com.bol.mancala.mancalagame.model.Game;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {

    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(Player player1, Player player2) {
        Game game = new Game(player1, player2, player1, GameStatus.START_GAME);
        gameRepository.save(game);
        return game;
    }

    public Game loadGame(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException("Game not present"));
    }

    public Game updateGame(Game game) {

        return gameRepository.save(game);
    }

    public void clearGameDetails() {

        gameRepository.deleteAll();
    }
}
