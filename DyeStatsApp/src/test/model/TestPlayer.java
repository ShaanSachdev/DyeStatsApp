package model;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPlayer {
    Player testP;
    Player testP2;
    String name = "John";

    @BeforeEach
    public void beforeEach() {
        Player.getLop().clear();
        testP = new Player(name);
        testP2 = new Player("Zack");

    }

    @Test
    public void testPlayer() {
        assertEquals(testP.getPlayerName(), "John");
        assertEquals(testP.getPlayed(), 0);
        assertEquals(testP.getWins(), 0);
        assertEquals(testP.getLosses(), 0);
        assertEquals(testP.getSelfSinks(), 0);
        assertEquals(testP.getWinRate(), 0);
        assertEquals(testP.getSelfSinkRate(), 0);
        assertEquals(0, testP.getPlayerLeagues().size());
        assertEquals(testP, Player.getLop().get(0));

        assertEquals(testP2, Player.getLop().get(1));
    }

    @Test
    public void testUpdateWin() {
        testP.update(true, 0);
        assertEquals(testP.getSelfSinks(), 0);
        assertEquals(testP.getWins(), 1);
        assertEquals(testP.getLosses(), 0);
        assertEquals(100, testP.getWinRate());
        assertEquals(0, testP.getSelfSinkRate());

        testP.update(true, 1);
        assertEquals(testP.getSelfSinks(), 1);
        assertEquals(testP.getWins(), 2);
        assertEquals(testP.getLosses(), 0);
        assertEquals(100, testP.getWinRate());
        assertEquals(50, testP.getSelfSinkRate());
    }

    @Test
    public void testUpdateLoss() {
        testP.update(false, 0);
        assertEquals(testP.getSelfSinks(), 0);
        assertEquals(testP.getWins(), 0);
        assertEquals(testP.getLosses(), 1);
        assertEquals(0, testP.getWinRate());

        testP.update(false, 1);
        assertEquals(testP.getSelfSinks(), 1);
        assertEquals(testP.getWins(), 0);
        assertEquals(testP.getLosses(), 2);
        assertEquals(0, testP.getWinRate());
    }

    @Test
    public void testUpdateWinLoss() {
        testP.update(true, 1);
        assertEquals(testP.getSelfSinks(), 1);
        assertEquals(testP.getWins(), 1);
        assertEquals(testP.getLosses(), 0);
        assertEquals(100, testP.getWinRate());
        assertEquals(100, testP.getSelfSinkRate());

        testP.update(false, 1);
        assertEquals(testP.getSelfSinks(), 2);
        assertEquals(testP.getWins(), 1);
        assertEquals(testP.getLosses(), 1);
        assertEquals(testP.getPlayed(), 2);
        assertEquals(50, testP.getWinRate());
        assertEquals(100, testP.getSelfSinkRate());
    }

    @Test
    public void testSelectPlayer() {
        assertEquals(2, Player.getLop().size());
        Player sp = Player.selectPlayer("Zack");
        assertEquals(sp, testP2);
        Player sp2 = Player.selectPlayer("Rocky");
        assertNull(sp2);

    }

    @Test
    public void testSelectLeague() {
        League leag1 = new League("abcde", "one");
        League leag2 = new League("ab", "two");
        testP.getPlayerLeagues().add(leag1);
        testP.getPlayerLeagues().add(leag2);
        League resultL = testP.selectLeague("two");
        assertEquals(leag2, resultL);
        League resultL2 = testP.selectLeague("three");
        assertNull(resultL2);
    }

    @Test
    public void testSetLop() {
        assertEquals(2, Player.getLop().size());
        ArrayList<Player> newLop = new ArrayList<Player>();
        newLop.add(new Player("Z"));
        newLop.add(new Player("A"));
        newLop.add(new Player("B"));
        Player.setLop(newLop);
        assertEquals(3, Player.getLop().size());
    }
}
