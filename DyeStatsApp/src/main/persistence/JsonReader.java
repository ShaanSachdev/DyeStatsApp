package persistence;


// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemopackage persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import model.League;
import model.Player;


// Represents a reader that reads players and leagues from JSON data stored in file
public class JsonReader {
    private String source;


    // EFFECTS: Constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: Reads players from file and returns them. 
    // Throws IOException if an error occurs reading data from file
    public ArrayList<Player> readPlayers() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePlayers(jsonObject);
    }

    // EFFECTS: Reads leagues from file and returns them. 
    // Throws IOException if an error occurs reading data from file
    public ArrayList<League> readLeagues() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLeagues(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses players from JSON object and returns it
    private ArrayList<Player> parsePlayers(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("lop");
        ArrayList<Player> players = new ArrayList<Player>();
        addPlayers(players, jsonArray);
        return players;

    }

    // EFFECTS: parses leagues from JSON object and returns it
    private ArrayList<League> parseLeagues(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("lol");
        ArrayList<League> leagues = addLeagues(jsonArray);
        return leagues;
    }
    
    // MODIFIES: player
    // EFFECTS: parses players from JSON object and adds them to jsonArray
    private void addPlayers(ArrayList<Player> players, JSONArray jsonArray) {

        for (Object json : jsonArray) {
            JSONObject nextPlayer = (JSONObject) json;
            players.add(addPlayer(nextPlayer));
        }
    }

    // MODIFIES: player
    // EFFECTS: parses player from JSON object and adds them 
    private Player addPlayer(JSONObject jsonObject) {
        String name = jsonObject.getString("playerName");
        int played = jsonObject.getInt("played");
        int wins = jsonObject.getInt("wins");
        int losses = jsonObject.getInt("losses");
        int selfSinks = jsonObject.getInt("selfSinks");
        int winRate = jsonObject.getInt("winRate");
        int selfSinkRate = jsonObject.getInt("selfSinkRate");
        Player player = new Player(name, played, wins, losses, selfSinks, winRate, selfSinkRate);
        JSONArray jsonArray = jsonObject.getJSONArray("playerLeagues");
        ArrayList<League> leagues = addLeagues(jsonArray);

        for (League league : leagues) {
            league.add2League(player, league.getKey());
        }

        return player;
    }
    
    // MODIFIES: league
    // EFFECTS: parses leagues from JSON object and adds them to jsonArray
    private ArrayList<League> addLeagues(JSONArray jsonArray) {
        ArrayList<League> leagues = new ArrayList<League>();
        for (Object json : jsonArray) {
            JSONObject nextLeague = (JSONObject) json;
            leagues.add(addLeague(nextLeague));
        }

        return leagues;
    }

    // MODIFIES: league
    // EFFECTS: parses league from JSON object and adds them
    private League addLeague(JSONObject jsonObject) {
        String key = jsonObject.getString("key");
        String leagueName = jsonObject.getString("leagueName");
        League newLeague;
        if (League.findLeague(leagueName) == null) {
            newLeague = new League(key, leagueName);
        } else {
            newLeague = League.findLeague(leagueName);
        }
        return newLeague;
    }
}

