package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.enums.GameStatus;
import com.bol.mancala.mancalagame.enums.PitType;
import com.bol.mancala.mancalagame.model.Game;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MancalaGameServiceTest {

    private MancalaGameService mancalaGameService;
    @Mock
    private GameService gameService;
    @Mock
    private PitService pitService;
    @Mock
    private PlayerService playerService;

    @Before
    public void init() {
        mancalaGameService = new MancalaGameService(gameService, pitService, playerService);
    }

    @Test
    public void createGameTest() {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        Game gameMock = new Game(firstPlayer, secondPlayer, firstPlayer, GameStatus.START_GAME);
        when(playerService.createPlayer(anyString())).thenReturn(firstPlayer);
        when(gameService.createGame(any(), any())).thenReturn(gameMock);
        when(gameService.updateGame(any())).thenReturn(gameMock);
        when(pitService.createPit(any(), anyInt(), anyInt(), any())).thenReturn(new Pit(gameMock, 0, 6, PitType.SMALL_PIT));
        Game game = mancalaGameService.createGame("Merlin", "Sunil");

        Assert.assertEquals(game.getFirstPlayer().getName(), "Merlin");
        Assert.assertEquals(game.getSecondPlayer().getName(), "Sunil");
        Assert.assertEquals(game.getPlayerTurn(), game.getFirstPlayer());
    }

    @Test
    public void loadGameTest() throws Exception {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        when(gameService.loadGame(anyLong())).thenReturn(new Game(firstPlayer, secondPlayer, firstPlayer, GameStatus.IN_PROGRESS));
        Game game = mancalaGameService.loadGame(1L);

        Assert.assertEquals(game.getGameStatus(), GameStatus.IN_PROGRESS);
    }

    @Test
    public void sowGameTestIfPositionIsABigPit() throws Exception {
        Player player = mock(Player.class);
        Pit pit = mock(Pit.class);
        Game game = mancalaGameService.sowGame(new Game(player, player, player, GameStatus.IN_PROGRESS), 6);

        verify(pitService, never()).updatePitByStones(1,6);
        verify(gameService, never()).updateGame(game);
    }

    @Test
    public void sowGameTestForFirstPlayer() {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        Game gameMock = new Game(firstPlayer, secondPlayer, firstPlayer, GameStatus.IN_PROGRESS);
        when(pitService.getPit(0)).thenReturn(new Pit(gameMock, 0, 4, PitType.SMALL_PIT));
        when(pitService.getPit(1)).thenReturn(new Pit(gameMock, 1, 4, PitType.SMALL_PIT));
        when(pitService.getPit(2)).thenReturn(new Pit(gameMock, 2, 4, PitType.SMALL_PIT));
        when(pitService.getPit(3)).thenReturn(new Pit(gameMock, 3, 4, PitType.SMALL_PIT));
        when(pitService.getPit(4)).thenReturn(new Pit(gameMock, 4, 4, PitType.SMALL_PIT));
        when(pitService.getPit(5)).thenReturn(new Pit(gameMock, 5, 4, PitType.SMALL_PIT));
        when(pitService.getPit(6)).thenReturn(new Pit(gameMock, 6, 0, PitType.BIG_PIT));
        when(pitService.getPit(7)).thenReturn(new Pit(gameMock, 7, 4, PitType.SMALL_PIT));
        when(pitService.getPit(8)).thenReturn(new Pit(gameMock, 8, 4, PitType.SMALL_PIT));
        when(pitService.getPit(9)).thenReturn(new Pit(gameMock, 9, 4, PitType.SMALL_PIT));
        when(pitService.getPit(10)).thenReturn(new Pit(gameMock, 10, 4, PitType.SMALL_PIT));
        when(pitService.getPit(11)).thenReturn(new Pit(gameMock, 11, 4, PitType.SMALL_PIT));
        when(pitService.getPit(12)).thenReturn(new Pit(gameMock, 12, 4, PitType.SMALL_PIT));
        when(pitService.getPit(13)).thenReturn(new Pit(gameMock, 13, 0, PitType.BIG_PIT));

        Game game = mancalaGameService.sowGame(gameMock, 1);

        verify(pitService, times(1)).updatePitByStones(1, 0);
        verify(pitService, times(1)).updatePitByStones(2, 5);
        verify(pitService, times(1)).updatePitByStones(3, 5);
        verify(pitService, times(1)).updatePitByStones(4, 5);
        verify(pitService, times(1)).updatePitByStones(5, 5);
        verify(gameService, times(1)).updateGame(game);
        Assert.assertEquals(game.getPlayerTurn().getName(), "Sunil");
    }

    @Test
    public void sowGameTestForSecondPlayer() {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        Game gameMock = new Game(firstPlayer, secondPlayer, secondPlayer, GameStatus.IN_PROGRESS);
        when(pitService.getPit(0)).thenReturn(new Pit(gameMock, 0, 4, PitType.SMALL_PIT));
        when(pitService.getPit(1)).thenReturn(new Pit(gameMock, 1, 4, PitType.SMALL_PIT));
        when(pitService.getPit(2)).thenReturn(new Pit(gameMock, 2, 4, PitType.SMALL_PIT));
        when(pitService.getPit(3)).thenReturn(new Pit(gameMock, 3, 4, PitType.SMALL_PIT));
        when(pitService.getPit(4)).thenReturn(new Pit(gameMock, 4, 4, PitType.SMALL_PIT));
        when(pitService.getPit(5)).thenReturn(new Pit(gameMock, 5, 4, PitType.SMALL_PIT));
        when(pitService.getPit(6)).thenReturn(new Pit(gameMock, 6, 0, PitType.BIG_PIT));
        when(pitService.getPit(7)).thenReturn(new Pit(gameMock, 7, 4, PitType.SMALL_PIT));
        when(pitService.getPit(8)).thenReturn(new Pit(gameMock, 8, 4, PitType.SMALL_PIT));
        when(pitService.getPit(9)).thenReturn(new Pit(gameMock, 9, 4, PitType.SMALL_PIT));
        when(pitService.getPit(10)).thenReturn(new Pit(gameMock, 10, 4, PitType.SMALL_PIT));
        when(pitService.getPit(11)).thenReturn(new Pit(gameMock, 11, 4, PitType.SMALL_PIT));
        when(pitService.getPit(12)).thenReturn(new Pit(gameMock, 12, 4, PitType.SMALL_PIT));
        when(pitService.getPit(13)).thenReturn(new Pit(gameMock, 13, 0, PitType.BIG_PIT));

        Game game = mancalaGameService.sowGame(gameMock, 11);

        verify(pitService, times(1)).updatePitByStones(11, 0);
        verify(pitService, times(1)).updatePitByStones(12, 5);
        verify(pitService, times(1)).updatePitByStones(13, 1);
        verify(pitService, times(1)).updatePitByStones(0, 5);
        verify(pitService, times(1)).updatePitByStones(1, 5);
        verify(gameService, times(1)).updateGame(game);
        Assert.assertEquals(game.getPlayerTurn().getName(), "Merlin");
    }

    @Test
    public void sowGameTestForSecondPlayerCaptures() {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        Game gameMock = new Game(firstPlayer, secondPlayer, secondPlayer, GameStatus.IN_PROGRESS);
        when(pitService.getPit(0)).thenReturn(new Pit(gameMock, 0, 0, PitType.SMALL_PIT));
        when(pitService.getPit(1)).thenReturn(new Pit(gameMock, 1, 2, PitType.SMALL_PIT));
        when(pitService.getPit(2)).thenReturn(new Pit(gameMock, 2, 0, PitType.SMALL_PIT));
        when(pitService.getPit(3)).thenReturn(new Pit(gameMock, 3, 5, PitType.SMALL_PIT));
        when(pitService.getPit(4)).thenReturn(new Pit(gameMock, 4, 0, PitType.SMALL_PIT));
        when(pitService.getPit(5)).thenReturn(new Pit(gameMock, 5, 7, PitType.SMALL_PIT));
        when(pitService.getPit(6)).thenReturn(new Pit(gameMock, 6, 8, PitType.BIG_PIT));
        when(pitService.getPit(7)).thenReturn(new Pit(gameMock, 7, 3, PitType.SMALL_PIT));
        when(pitService.getPit(8)).thenReturn(new Pit(gameMock, 8, 3, PitType.SMALL_PIT));
        when(pitService.getPit(9)).thenReturn(new Pit(gameMock, 9, 8, PitType.SMALL_PIT));
        when(pitService.getPit(10)).thenReturn(new Pit(gameMock, 10, 1, PitType.SMALL_PIT));
        when(pitService.getPit(11)).thenReturn(new Pit(gameMock, 11, 0, PitType.SMALL_PIT));
        when(pitService.getPit(12)).thenReturn(new Pit(gameMock, 12, 8, PitType.SMALL_PIT));
        when(pitService.getPit(13)).thenReturn(new Pit(gameMock, 13, 3, PitType.BIG_PIT));

        Game game = mancalaGameService.sowGame(gameMock, 10);

        verify(pitService, times(1)).updatePitByStones(10, 0);
        verify(pitService, times(1)).updatePitByStones(13, 6);
        verify(pitService, times(1)).updatePitByStones(1, 0);
        verify(gameService, times(1)).updateGame(game);
        Assert.assertEquals(game.getPlayerTurn().getName(), "Merlin");
    }

    @Test
    public void sowGameTestForFirstPlayerCaptures() {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        Game gameMock = new Game(firstPlayer, secondPlayer, firstPlayer, GameStatus.IN_PROGRESS);
        when(pitService.getPit(0)).thenReturn(new Pit(gameMock, 0, 0, PitType.SMALL_PIT));
        when(pitService.getPit(1)).thenReturn(new Pit(gameMock, 1, 1, PitType.SMALL_PIT));
        when(pitService.getPit(2)).thenReturn(new Pit(gameMock, 2, 0, PitType.SMALL_PIT));
        when(pitService.getPit(3)).thenReturn(new Pit(gameMock, 3, 5, PitType.SMALL_PIT));
        when(pitService.getPit(4)).thenReturn(new Pit(gameMock, 4, 0, PitType.SMALL_PIT));
        when(pitService.getPit(5)).thenReturn(new Pit(gameMock, 5, 7, PitType.SMALL_PIT));
        when(pitService.getPit(6)).thenReturn(new Pit(gameMock, 6, 8, PitType.BIG_PIT));
        when(pitService.getPit(7)).thenReturn(new Pit(gameMock, 7, 3, PitType.SMALL_PIT));
        when(pitService.getPit(8)).thenReturn(new Pit(gameMock, 8, 3, PitType.SMALL_PIT));
        when(pitService.getPit(9)).thenReturn(new Pit(gameMock, 9, 8, PitType.SMALL_PIT));
        when(pitService.getPit(10)).thenReturn(new Pit(gameMock, 10, 11, PitType.SMALL_PIT));
        when(pitService.getPit(11)).thenReturn(new Pit(gameMock, 11, 6, PitType.SMALL_PIT));
        when(pitService.getPit(12)).thenReturn(new Pit(gameMock, 12, 2, PitType.SMALL_PIT));
        when(pitService.getPit(13)).thenReturn(new Pit(gameMock, 13, 3, PitType.BIG_PIT));

        Game game = mancalaGameService.sowGame(gameMock, 1);

        verify(pitService, times(1)).updatePitByStones(1, 0);
        verify(pitService, times(1)).updatePitByStones(6, 20);
        verify(pitService, times(1)).updatePitByStones(10, 0);
        verify(gameService, times(1)).updateGame(game);
        Assert.assertEquals(game.getPlayerTurn(), game.getSecondPlayer());
    }

    @Test
    public void checkGameFinishedTest() {
        Player firstPlayer = new Player("Merlin");
        Player secondPlayer = new Player("Sunil");
        Game gameMock = new Game(firstPlayer, secondPlayer, secondPlayer, GameStatus.IN_PROGRESS);
        when(pitService.getPit(0)).thenReturn(new Pit(gameMock, 0, 0, PitType.SMALL_PIT));
        when(pitService.getPit(1)).thenReturn(new Pit(gameMock, 1, 0, PitType.SMALL_PIT));
        when(pitService.getPit(2)).thenReturn(new Pit(gameMock, 2, 0, PitType.SMALL_PIT));
        when(pitService.getPit(3)).thenReturn(new Pit(gameMock, 3, 0, PitType.SMALL_PIT));
        when(pitService.getPit(4)).thenReturn(new Pit(gameMock, 4, 0, PitType.SMALL_PIT));
        when(pitService.getPit(5)).thenReturn(new Pit(gameMock, 5, 0, PitType.SMALL_PIT));
        when(pitService.getPit(6)).thenReturn(new Pit(gameMock, 6, 8, PitType.BIG_PIT));
        when(pitService.getPit(7)).thenReturn(new Pit(gameMock, 7, 0, PitType.SMALL_PIT));
        when(pitService.getPit(8)).thenReturn(new Pit(gameMock, 8, 0, PitType.SMALL_PIT));
        when(pitService.getPit(9)).thenReturn(new Pit(gameMock, 9, 0, PitType.SMALL_PIT));
        when(pitService.getPit(10)).thenReturn(new Pit(gameMock, 10, 1, PitType.SMALL_PIT));
        when(pitService.getPit(11)).thenReturn(new Pit(gameMock, 11, 0, PitType.SMALL_PIT));
        when(pitService.getPit(12)).thenReturn(new Pit(gameMock, 12, 8, PitType.SMALL_PIT));
        when(pitService.getPit(13)).thenReturn(new Pit(gameMock, 13, 3, PitType.BIG_PIT));

        Game game = mancalaGameService.sowGame(gameMock, 10);

        verify(pitService, times(1)).updatePitByStones(10, 0);
        verify(pitService, times(1)).updatePitByStones(12, 0);
        verify(pitService, times(1)).updatePitByStones(13, 12);
        verify(gameService, times(1)).updateGame(game);
        Assert.assertEquals(game.getGameStatus(), GameStatus.FINISHED);
    }


}
