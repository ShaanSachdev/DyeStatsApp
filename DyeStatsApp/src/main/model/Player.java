package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

//Represents players in the application. Includes their name and stats, as well as 
// which leagues they are in.
public class Player implements Writable {
    
    private String playerName;
    private int played;
    private int wins; 
    private int losses; 
    private int selfSinks;
    private int winRate;
    private int selfSinkRate;
    private ArrayList<League> playerLeagues = new ArrayList<>();
    private static ArrayList<Player> lop = new ArrayList<Player>();
    
    //EFFECTS: assigns String to playerName from user,  
    //         and assigns 0 to all other fields. Creates an arraylist that 
    //         is null and will consist of all the leagues the player is in. 
    //         Also adds player into static list of player.
    public Player(String playerName) {
        this.playerName = playerName;
        this.played = 0;
        this.wins = 0;
        this.losses = 0;
        this.selfSinks = 0;
        this.winRate = 0;
        this.selfSinkRate = 0;
        this.playerLeagues = new ArrayList<League>();
        lop.add(this);
        EventLog.getInstance().logEvent(new Event("New player called " + playerName + " was created."));
    }
    
    //EFFECTS: Constructs player from stored data.
    public Player(String playerName, int played, int wins, int losses, int selfSinks, int winRate, int selfSinkRate) {
        this.playerName = playerName;
        this.played = played;
        this.wins = wins;
        this.losses = losses;
        this.selfSinks = selfSinks;
        this.winRate = winRate;
        this.selfSinkRate = selfSinkRate;
        this.playerLeagues = new ArrayList<League>();
        lop.add(this);
    }

    //REQUIRES: Input from user stating whether they won (boolean), if/how many they self-sank (int >= 0)
    //MODIFIES: this.
    //EFFECT: Updates a players statistics (played, wins/losses, selfSinks, winRate, selfSinkRate)
    public void update(boolean result, int selfSinks) {
        played++;
        this.selfSinks += selfSinks;

        if (result == true) {
            wins++;
        } else {
            losses++;
        }

        selfSinkRate = (int) (((double) this.selfSinks / played) * 100);
        winRate = (int) (((double) wins / played) * 100);
        EventLog.getInstance().logEvent(new Event(playerName + " was updated"));
    }

     
    //EFFECTS: allows player to select which league they want to view/join
    public League selectLeague(String leagueName) {
        for (League league : playerLeagues) {
            if (league.getLeagueName().equalsIgnoreCase(leagueName)) {
                return league;
            }
        }
        return null;
    
    }
    

    
    //REQUIRES: playerName from user. Not case sensitive
    //MODIFIES: this
    //EFFECTS: allows user to select which player they want to use
    public static Player selectPlayer(String playerName) {
       
        for (Player p : lop) {
            if (p.getPlayerName().equalsIgnoreCase(playerName)) {
                return p;
            }
        }
        return null;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getPlayed() {
        return this.played;
    }

    public int getWins() {
        return this.wins;
    }

    public int getLosses() {
        return this.losses;
    }

    public int getSelfSinks() {
        return this.selfSinks;
    }

    public int getWinRate() {
        return this.winRate;
    }

    public int getSelfSinkRate() {
        return this.selfSinkRate;
    }

    public ArrayList<League> getPlayerLeagues() {
        return playerLeagues;
    }
   
    public static ArrayList<Player> getLop() {
        return lop;
    }

    public static void setLop(ArrayList<Player> list) {
        lop = list;
    }
    
    //EFFECTS: EFFECTS: Returns a JSONObject representation of this Player, containing 
    //        fields for playerName, played, wins, losses, selfSinks, winRate, 
    //        selfSinkRate, and playerLeagues as JSON objects.
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("playerName", playerName);
        json.put("played", played);
        json.put("wins", wins);
        json.put("losses", losses);
        json.put("selfSinks", selfSinks);
        json.put("winRate", winRate);
        json.put("selfSinkRate", selfSinkRate);
        json.put("playerLeagues", leaguesToJson());
        return json;

    }

    // EFFECTS: returns playerLeagues as json objects
    public JSONArray leaguesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (League league : playerLeagues) {
            jsonArray.put(league.toJson());
        }

        EventLog.getInstance().logEvent(new Event("Player " + playerName +  " was saved."));
        return jsonArray;
    }
}
