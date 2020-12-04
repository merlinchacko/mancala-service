package com.bol.mancala.mancalagame.endpoint;

import com.bol.mancala.mancalagame.enums.GameStatus;
import com.bol.mancala.mancalagame.exception.InvalidPitException;
import com.bol.mancala.mancalagame.model.Game;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.service.MancalaGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MancalaGameEndpoint.class)
public class MancalaGameEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MancalaGameService mancalaGameService;

    @Test
    public void createGameTest() throws Exception {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        Game game = new Game(firstPlayer, secondPlayer, firstPlayer, GameStatus.START_GAME);

        Mockito.when(this.mancalaGameService.createGame("Merlin", "Sunil"))
                .thenReturn(game);

        this.mockMvc.perform(post("/mancala-game/Merlin/Sunil"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(game))))
                .andReturn();
    }

    @Test
    public void sowGameTest() throws Exception {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        Game game = new Game(firstPlayer, secondPlayer, firstPlayer, GameStatus.START_GAME);

        Mockito.when(this.mancalaGameService.loadGame(1L))
                .thenReturn(game);
        Mockito.when(this.mancalaGameService.sowGame(game, 12))
                .thenReturn(game);

        this.mockMvc.perform(put("/mancala-game/1/pits/12"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(new ObjectMapper().writeValueAsString(game))))
                .andReturn();
    }

    @Test
    public void sowGameTestIfInvalidPitPosition() throws Exception {
        this.mockMvc.perform(put("/mancala-game/1/pits/13"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidPitException))
                .andExpect(result -> assertEquals("Invalid pit position! It should be between 0-5 or 7-12", result.getResolvedException().getMessage()));
    }

}
