
package persistence;

import model.Player;
import model.League;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class TestWriter {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyPlayersAndLeagues() {
        try {
            ArrayList<Player> players = new ArrayList<Player>();
            ArrayList<League> leagues = new ArrayList<League>();
            JsonWriter writer = new JsonWriter("./data/empty.json");
            writer.open();
            writer.write(players, leagues);
            writer.close();

            JsonReader reader = new JsonReader("./data/empty.json");
            players = reader.readPlayers();
            leagues = reader.readLeagues();
            assertEquals(0, players.size());
            assertEquals(0, leagues.size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneral() {
        try {
            ArrayList<Player> pl = new ArrayList<Player>();
            ArrayList<League> le = new ArrayList<League>();
            Player p1 = new Player("Vonduski", 100, 100, 0, 0, 100, 0);
            Player p2 = new Player("BBBK", 1, 1, 0, 0, 1, 0);
            League l1 = new League("1", "legends");
            l1.add2League(p1, "1");
            l1.add2League(p2, "1");
            pl.add(p1);
            pl.add(p2);
            le.add(l1);
            JsonWriter writer = new JsonWriter("./data/testWriteGeneral.json");
            writer.open();
            writer.write(pl, le);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriteGeneral.json");
            ArrayList<Player> players = reader.readPlayers();
            ArrayList<League> leagues = reader.readLeagues();
            checkGeneral(players, leagues);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    void checkGeneral(ArrayList<Player> players, ArrayList<League> leagues) {
        assertEquals(2, players.size());
        assertEquals(1, players.get(0).getPlayerLeagues().size());
        assertEquals("Vonduski", players.get(0).getPlayerName());
        assertEquals(1, players.get(1).getPlayerLeagues().size());
        assertEquals("BBBK", players.get(1).getPlayerName());
        assertEquals(1, leagues.size());
    }
}
