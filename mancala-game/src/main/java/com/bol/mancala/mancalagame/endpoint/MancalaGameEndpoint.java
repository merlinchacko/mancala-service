package com.bol.mancala.mancalagame.endpoint;

import com.bol.mancala.mancalagame.constants.MancalaGameConstants;
import com.bol.mancala.mancalagame.exception.InvalidPitException;
import com.bol.mancala.mancalagame.model.Game;
import com.bol.mancala.mancalagame.service.MancalaGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/mancala-game")
@CrossOrigin(origins = "http://localhost:4200")
public class MancalaGameEndpoint {

    private MancalaGameService mancalaGameService;

    public MancalaGameEndpoint(MancalaGameService mancalaGameService) {
        this.mancalaGameService = mancalaGameService;
    }

    @PostMapping("/{player1Name}/{player2Name}")
    public ResponseEntity<Game> createGame(@PathVariable String player1Name, @PathVariable String player2Name) throws Exception {
        Game game = mancalaGameService.createGame(player1Name, player2Name);
        log.info(new ObjectMapper().writeValueAsString(game));
        return ResponseEntity.ok(game);
    }

    @PutMapping("{gameId}/pits/{position}")
    public ResponseEntity<Game> sowGame(@PathVariable Long gameId, @PathVariable Integer position) throws Exception {
        if (position == null || position < 0 || position == MancalaGameConstants.PLAYER1_BIG_PIT || position == MancalaGameConstants.PLAYER2_BIG_PIT) {
            throw new InvalidPitException("Invalid pit position! It should be between 0-5 or 7-12");
        }

        Game game = mancalaGameService.loadGame(gameId);
        game = mancalaGameService.sowGame(game, position);

        log.info(new ObjectMapper().writeValueAsString(game));
        return ResponseEntity.ok(game);
    }

    @DeleteMapping
    public ResponseEntity<String> clearGameDetails() throws Exception {
        mancalaGameService.clearGameDetails();
        return ResponseEntity.ok("Cleared details");
    }
}
