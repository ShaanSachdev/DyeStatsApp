package model;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestLeague {
    private League testL;
    private League testL2; 
    private String testKey = "abcde";
    private String testLeagueName = "Best";


    @BeforeEach
    public void beforeEach() {
        League.getLol().clear();
        testL = new League(testKey, testLeagueName);
        testL2 = new League("abcd", "Worst");        
    }

    @Test
    public void testLeague() {
        assertTrue(testL.getLeague().isEmpty());
        assertEquals("abcde", testL.getKey());
        assertEquals("Best", testL.getLeagueName());
        assertEquals("Best", League.getLol().get(0).getLeagueName());

        assertTrue(testL2.getLeague().isEmpty());
        assertEquals("abcd", testL2.getKey());
        assertEquals("Worst", testL2.getLeagueName());
        assertEquals("Worst", League.getLol().get(1).getLeagueName());
        
    }

    @Test
    public void testAdd2League() { 
        assertEquals(0, testL.getLeague().size());
        Player pl = new Player("John");
        boolean bool = testL.add2League(pl, "abcde");
        assertTrue(bool);
        assertEquals(1, testL.getLeague().size());
        assertEquals(pl.getPlayerLeagues().get(0), testL);
        
        bool = testL.add2League(pl, "abcde");
        assertFalse(bool); //checks that duplicate doesn't get entered again
        Player pl2 = new Player("Jack");
        testL.add2League(pl2, "abc");
        assertEquals(1, testL.getLeague().size());
    }
    
    @Test
    public void removeFromLeagueTest() { 
        Player pl = new Player("John");
        Player pl2 = new Player("Cash");
        testL.add2League(pl, "abcde");
        
        testL.removeFromLeague(pl2, "abcde");
        assertFalse(testL.getLeague().contains(pl2));
        assertEquals(1, testL.getLeague().size());
        
    }

    @Test
    public void testSortWStandings() {
        Player pl = new Player("John");
        Player pl2 = new Player("Jack");
        testL.add2League(pl, "abcde");
        testL.add2League(pl2, "abcde");
        pl2.update(true, 0);
        testL.sortWRStandings();
        assertEquals(pl2, testL.getLeague().get(0));
    }

    @Test
    public void testSortLStandings() {
        Player pl = new Player("John");
        Player pl2 = new Player("Jack");
        testL.add2League(pl, "abcde");
        testL.add2League(pl2, "abcde");
        pl2.update(false, 0);
        testL.sortLStandings();
        assertEquals(pl2, testL.getLeague().get(0));
    }

    @Test
    public void testSortSSStandings() {
        Player pl = new Player("John");
        Player pl2 = new Player("Jack");
        testL.add2League(pl, "abcde");
        testL.add2League(pl2, "abcde");
        pl2.update(false, 1);
        testL.sortSSStandings();
        assertEquals(pl2, testL.getLeague().get(0));
    }

    @Test
    public void testFindLeague() {
        assertEquals(2, League.getLol().size());
        League worst = League.findLeague("Worst");
        assertEquals(testL2, worst);
        League fail = League.findLeague("Fail");
        assertNull(fail);
    }

    @Test
    void tetsSetLol() {
        assertEquals(2, League.getLol().size());
        ArrayList<League> newLol = new ArrayList<League>();
        newLol.add(new League("Z", "yyy"));
        newLol.add(new League("A", "bbb"));
        newLol.add(new League("B", "aaa"));
        League.setLol(newLol);
        assertEquals(3, League.getLol().size());
    }

}
