package model;

import java.util.ArrayList;

import org.json.JSONObject;

import persistence.Writable;

//Represents a League. Includes league name, key and players within.
public class League implements Writable {
    private ArrayList<Player> league;
    private String key;
    private String leagueName;
    private static ArrayList<League> lol = new ArrayList<League>();

    //REQUIRES: name that is unique.
    //EFFECTS: creates new empty arraylist and assigns user inputed 
    //         key and name for the league, add league to a list of leagues
    public League(String key, String leagueName) {
        league = new ArrayList<Player>();
        this.leagueName = leagueName;
        this.key = key;
        lol.add(this);
        EventLog.getInstance().logEvent(new Event("League (" + leagueName + ") was created"));
    }

    //REQUIRES: Player that is not in the leauge 
    //MODIFIES: this.
    //EFFECTS: Adds player into the league Arraylist, if key entered matches by the player matches . 
    //         Also adds the leauge to the playersLeague list. Returns true if player is added.
    public boolean add2League(Player player, String key) {
        if (!league.contains(player) && this.key.equals(key)) {
            league.add(player);
            player.getPlayerLeagues().add(this);
            EventLog.getInstance().logEvent(new Event(player.getPlayerName() + " was added to " + leagueName));
            return true;
        } else {
            return false;
        }
        
    }

    
    //MODIFIES: this.
    //EFFECTS: Adds player into the league Arraylist, if key entered matches by the player matches . 
    //         Also adds the leauge to the playersLeague list. Returns true if player is added.
    public boolean removeFromLeague(Player player, String key) {
        if (league.contains(player) && this.key.equals(key)) {
            league.remove(player);
            player.getPlayerLeagues().remove(this);
            EventLog.getInstance().logEvent(new Event(player.getPlayerName() + " was removed from " + leagueName));
            return true;
        } else {
            return false;
        }
        
    }

    //MODIFIES: this.
    //EFFECTS: Sorts league by each Player's winRate in descending order.
    public void sortWRStandings() {
        league.sort((p1, p2) -> Double.compare(p2.getWinRate(), p1.getWinRate()));
        EventLog.getInstance().logEvent(new Event(leagueName + " was sorted and viewed by Win-Rate."));
    }

    //MODIFIES: this.
    //EFFECTS: Sorts league by each Player's Losses in descending order.
    public void sortLStandings() {
        league.sort((p1, p2) -> Double.compare(p2.getLosses(), p1.getLosses()));
        EventLog.getInstance().logEvent(new Event(leagueName + " was sorted and viewed by Losses."));
    }

    //MODIFIES: this.
    //EFFECTS: Sorts league by each Player's Self-Sinks in descending order.
    public void sortSSStandings() {
        league.sort((p1, p2) -> Double.compare(p2.getSelfSinks(), p1.getSelfSinks()));
        EventLog.getInstance().logEvent(new Event(leagueName + " was sorted and viewed by Self-Sinks."));
    }

    //REQUIRES: League name from user
    //MODIFIES: this
    //EFFECTS: finds league player wants to be added to 
    public static League findLeague(String name) {
        for (League league : lol) {
            if (league.getLeagueName().equalsIgnoreCase(name)) {
                return league;
            }
        }
        return null;
    }

   
    public ArrayList<Player> getLeague() {
        
        return league;
    }

    public String getKey() {
        
        return key;
    }

    public String getLeagueName() {
        
        return leagueName;
    }

    public static ArrayList<League> getLol() {
        return lol;
    }

    public static void setLol(ArrayList<League> list) {
        lol = list;
    }
    
    // EFFECTS: returns league object as a json one.
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("key", key);
        json.put("leagueName", leagueName);
        return json;
    }




}
