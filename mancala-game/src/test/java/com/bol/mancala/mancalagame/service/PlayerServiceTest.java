package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.repository.PlayerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class PlayerServiceTest {
    private PlayerService playerService;
    @Mock
    private PlayerRepository playerRepository;

    @Before
    public void init() {
        playerService = new PlayerService(playerRepository);
    }

    @Test
    public void createPlayerTest() {
        Player player = playerService.createPlayer("Merlin");

        assertEquals(player.getName(), "Merlin");

        verify(playerRepository, times(1)).save(player);
    }

    @Test
    public void deleteAllPlayersTest() {
        playerService.clearPlayerDetails();

        verify(playerRepository, times(1)).deleteAll();
    }

}
