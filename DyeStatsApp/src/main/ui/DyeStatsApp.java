package ui;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import model.League;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

//EFFECTS: provides base and console UI to run app.
public class DyeStatsApp {
    Scanner input = new Scanner(System.in); 

    private static final String JSON_STORE = "./data/dyeappGUI.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    boolean end = false; 

    //Runs Dye Stats application
    public DyeStatsApp() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
        runDyeStats();
    }

    //EFFECTS: Prints menu and faciliates menu selection through helper methods.
    private void runDyeStats() { 
        while (end == false) {
            printMenu();
            int menu = input.nextInt();
            input.nextLine();
            menuSelection(menu);          
        }
    }

    //EFFECTS: Prints menu in terminal for user to see.
    private void printMenu() {
        System.out.println("**MENU**");
        System.out.println(" 1. Create New Player\n 2. Update Player\n 3. View a League");
        System.out.println(" 4. Create a League\n 5. Add a Player to a League");
        System.out.println(" 6. Remove Player from a League\n 7. Load Data\n 8. Save Changes\n 9. END");
    }



    //EFFECTS: Uses user input that has been passed as a parameter to determine  
    //         which function to call and calls it
    private void menuSelection(int menu) {
        if (menu == 1) {
            createPlayer();
        } else if (menu == 2) {
            updatePlayer();
        } else if (menu == 3) {
            viewALeague();
        } else if (menu == 4) {
            createLeague();
        } else if (menu == 5) {
            addPlayer2League();
        } else if (menu == 6) {
            removePlayerfromLeague();
        } else if (menu == 7) {
            loadData();
        } else if (menu == 8) {
            saveData();
        } else if (menu == 9) {
            end = endApp();
        } else {
            System.out.println("Please enter appropriate input.");
        }  
    }
    
    //EFFECTS: Uses user Input to create new Player object.
    private void createPlayer() {
        System.out.print("\nEnter the new player's name: ");
        String enteredPName = input.nextLine();
        if (Player.selectPlayer(enteredPName) == null) {
            new Player(enteredPName);
        } else {
            System.out.println("That player name is already in use. Please try again with a different name.\n");
        }
        
    }

    //REQUIRES: The entered player name must exist in the list of players.
    //MODIFIES: The player object that is associated with the name inputted.
    //EFFECTS: Prompts the user to enter the player's name to update, checks if they won, and prompts 
    //         for the number of self-sinks. Updates the player's win/loss status and self-sinks count.
    private void updatePlayer() {
        boolean result;
        System.out.print("\nEnter the player's name which you want to update: ");            
        String enteredPName = input.nextLine();
        Player player = Player.selectPlayer(enteredPName);
        System.out.print("\nDid they win? ");
        String res = input.nextLine();
        if (res.equalsIgnoreCase("yes")) {
            result = true;
        } else {
            result = false;
        }
        System.out.print("\nEnter the amount of self-sinks: ");
        int selfSinks = input.nextInt();
        input.nextLine();
        player.update(result, selfSinks);
    }

    //EFFECTS: Prompts user to enter league name. If the league exists it calls printStandings(), if not, 
    //         the user is informed.
    private void viewALeague() {
        System.out.print("\nEnter which league you want to view: ");
        String viewLName = input.nextLine();
        League viewLeague = League.findLeague(viewLName);
        if (viewLeague != null) {
            viewLeague.sortWRStandings();
            printStandings(viewLeague);
        } else {
            System.out.println("That league does not exist.\n");
        }
    }

    //EFFECTS: Uses user Input to create new League object.
    private void createLeague() {
        System.out.print("\nEnter desired league name: ");
        String enteredLName = input.nextLine();
        League leagueCheck = League.findLeague(enteredLName);
        if (leagueCheck == null) {
            System.out.print("\nEnter desired league key: ");
            String enteredLKey = input.nextLine();
            new League(enteredLKey, enteredLName);
        } else {
            System.out.println("That league name is already in use, please try again.\n");
        }
    }

    //REQUIRES: player and league names inputted to relate to existing respective objects.
    //MODIFIES: Player, League.
    //EFFECTS: Adds given player to given league if the inputted key matches the given league's key.
    private void addPlayer2League() {
        System.out.print("\nEnter which player you want to add to a league : ");
        String enteredPName = input.nextLine();
        Player player = Player.selectPlayer(enteredPName);  
        System.out.print("\nEnter the name of the league would like to add them to: "); 
        String enteredLName = input.nextLine();
        League league2add = League.findLeague(enteredLName);
        System.out.print("\nEnter the league key: ");
        String enteredLKey = input.nextLine();
        boolean result = league2add.add2League(player, enteredLKey);

        if (result == true) {
            System.out.println("Success!");
        } else {
            System.out.println("Invalid Key or Duplicate Player");
        }
    }


    //MODIFIES: League, Player
    //EFFECTS: If inputted league and player names correspond to existing objects and the inputted key matches 
    //         that of the league, the player is removed from league and the league is removed from player's leagues
    private void removePlayerfromLeague() {
        System.out.print("\nEnter which player you want to remove from a league : ");
        String enteredPName = input.nextLine();
        Player player = Player.selectPlayer(enteredPName);
        if (player != null) {
            System.out.print("\nEnter the name of the league would like to remove them from: "); 
            String enteredLName = input.nextLine();
            League league2remove = League.findLeague(enteredLName);
            if (league2remove != null) {
                System.out.print("\nEnter the league key: ");
                String enteredLKey = input.nextLine();
                if (league2remove.getKey().equals(enteredLKey) && league2remove.getLeague().contains(player)) {
                    int count = 0;
                    while (league2remove.getLeague().contains(player)) {
                        if (league2remove.getLeague().get(count).equals(player)) {
                            league2remove.removeFromLeague(player, league2remove.getKey());
                        } else {
                            count++;
                        }
                    }
                }
            }
        }
    }

    //EFFECTS: terminates application when true is returned.
    private boolean endApp() {
        boolean end = true;
        input.close();
        return end;
    }

    //EFFECTS: Prints league standings for league that is passed as a parameter
    private void printStandings(League viewLeague) {
        int countL = 1;
        System.out.print("  GP  W  L  SS  WR  SSR  Player Name");
        for (Player p : viewLeague.getLeague()) {
            System.out.print("\n" + countL + ".  ");
            System.out.print(p.getPlayed() + "  ");
            System.out.print(p.getWins() + "  ");
            System.out.print(p.getLosses() + "  ");
            System.out.print(p.getSelfSinks() + "  ");
            System.out.print(p.getWinRate() + "%   ");
            System.out.print(p.getSelfSinkRate() + "%   ");
            System.out.print(p.getPlayerName() + "\n");
            countL++;
        }
    }

    //MODIFIES: League (lol), Player (lop)
    //EFFECTS: Attempts to read data from designated JSON file into the system
    public void loadData() {
        try {
            ArrayList<Player> lop = Player.getLop();
            ArrayList<League> lol = League.getLol();
            lop = jsonReader.readPlayers();
            lol = jsonReader.readLeagues();
            Player.setLop(lop);
            League.setLol(lol);
            System.out.println("Loaded successfully from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


    //MODIFIES: The JSON file specified by JSON_STORE
    //EFFECTS: Attempts to read lol and lop from system to JSON_STORE
    private void saveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(Player.getLop(), League.getLol());
            jsonWriter.close();
            System.out.println("Saved your changes to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

}
