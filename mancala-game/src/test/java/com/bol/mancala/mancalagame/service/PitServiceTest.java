package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.enums.PitType;
import com.bol.mancala.mancalagame.exception.PitNotFoundException;
import com.bol.mancala.mancalagame.model.Game;
import com.bol.mancala.mancalagame.model.Pit;
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
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class PitServiceTest {
    private PitService pitService;
    @Mock
    private PitRepository pitRepository;

    @Before
    public void init() {
        pitService = new PitService(pitRepository);
    }

    @Test
    public void createSmallPitTest() {
        Game game = mock(Game.class);
        Pit resultPit = pitService.createPit(game, 1, 6, PitType.SMALL_PIT);

        assertEquals(resultPit.getGame(), game);
        assertEquals(resultPit.getPitType(), PitType.SMALL_PIT);
        assertEquals(resultPit.getPosition(), 1);
        assertEquals(resultPit.getNumberOfStones(), 6);

        verify(pitRepository, times(1)).save(any(Pit.class));
    }

    @Test
    public void createBigPitTest() {
        Game game = mock(Game.class);
        Pit resultPit = pitService.createPit(game, 13, 0, PitType.BIG_PIT);

        assertEquals(resultPit.getGame(), game);
        assertEquals(resultPit.getPitType(), PitType.BIG_PIT);
        assertEquals(resultPit.getPosition(), 13);
        assertEquals(resultPit.getNumberOfStones(), 0);

        verify(pitRepository, times(1)).save(any(Pit.class));
    }

    @Test
    public void getPitTest() {
        Game game = mock(Game.class);
        Pit pit = new Pit(game, 12, 1, PitType.SMALL_PIT);
        when(pitRepository.findByPosition(12)).thenReturn(Optional.of(pit));
        Pit result = pitService.getPit(12);

        assertEquals(result.getGame(), game);
        assertEquals(result.getPosition(), 12);
        assertEquals(result.getNumberOfStones(), 1);
    }


    @Test(expected = PitNotFoundException.class)
    public void getPitTestIfPitNotPresent() {
        when(pitRepository.findByPosition(12)).thenThrow(new PitNotFoundException("not found!"));
        pitService.getPit(12);
    }

    @Test
    public void updatePitByStonesTest() {
        Game game = mock(Game.class);
        Pit pit = new Pit(game, 12, 1, PitType.SMALL_PIT);
        when(pitRepository.findByPosition(12)).thenReturn(Optional.of(pit));
        Pit result = pitService.updatePitByStones(12, 12);

        assertEquals(result.getGame(), game);
        assertEquals(result.getPosition(), 12);
        assertEquals(result.getNumberOfStones(), 12);

        verify(pitRepository, times(1)).save(any(Pit.class));
    }

    @Test
    public void getAllPitsTest() {
        Game game = mock(Game.class);
        Pit pit = new Pit(game, 12, 1, PitType.SMALL_PIT);
        when(pitRepository.findAll()).thenReturn(Arrays.asList(pit, pit));

        assertEquals(pitService.getAllPits().size(), 2);
    }

    @Test
    public void deleteAllPitsTest() {
        pitService.clearPitDetails();

        verify(pitRepository, times(1)).deleteAll();
    }

}
