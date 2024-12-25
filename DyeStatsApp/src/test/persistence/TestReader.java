package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;



import org.junit.jupiter.api.Test;

import model.Player;
import model.League;


public class TestReader {
 
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.readPlayers();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderGeneral() {
        JsonReader reader = new JsonReader("./data/generalData.json");
        try {
            ArrayList<Player> players = reader.readPlayers();
            ArrayList<League> leagues = reader.readLeagues();
            assertEquals(3, players.size());
            assertEquals("Jack", players.get(0).getPlayerName());
            assertEquals("John", players.get(1).getPlayerName());
            assertEquals("Curtis", players.get(2).getPlayerName());
            assertEquals(3, leagues.size());
            assertEquals("legends", leagues.get(0).getLeagueName());
            assertEquals("legends2", leagues.get(1).getLeagueName());
            assertEquals("legends3", leagues.get(2).getLeagueName());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReadEmpty() {
        JsonReader reader = new JsonReader("./data/empty.json");
        try {
            ArrayList<Player> players = reader.readPlayers();
            ArrayList<League> leagues = reader.readLeagues();
            assertEquals(0, players.size());
            assertEquals(0, leagues.size());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


}
