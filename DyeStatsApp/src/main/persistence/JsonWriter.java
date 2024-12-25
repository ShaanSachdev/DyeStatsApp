// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

package persistence;

import org.json.JSONArray;
import org.json.JSONObject;

import model.League;
import model.Player;

import java.io.*;
import java.util.List;

// Represents a writer that writes JSON representation of players and leagues to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    //EFFECTS: constructs writer to write to destination file.
    public JsonWriter(String destination) {
        this.destination = destination;
    }


    //MODIFIES: This.
    //EFFECTS: Opens writer so that changes can be written. 
    // Throws FileNotFoundException if file is not found.  
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this.
    // EFFECTS: Writes JSON representation of all players and leagues to file
    public void write(List<Player> players, List<League> leagues) {
        JSONObject json = new JSONObject();
        JSONArray playerJson = new JSONArray();
        for (Player player : players) {
            playerJson.put(player.toJson());
        }

        JSONArray leagueJson = new JSONArray();
        for (League league : leagues) {
            leagueJson.put(league.toJson());
        }
        json.put("lop", playerJson);
        json.put("lol", leagueJson);
        saveToFile(json.toString(TAB));

    }


    // MODIFIES: this.
    // EFFECTS: closes writer.
    public void close() {
        writer.close();
    }

    //EFFECTS: saves all players and leagues Json String to file.
    public void saveToFile(String json) {
        writer.print(json);
    }
}



