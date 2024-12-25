package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.Event;
import model.EventLog;
import model.League;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;


// Class responsible for the main GUI window
public class MainWindow {
    
    private JFrame window;
    private JPanel menuPanel;
    private JScrollPane dataPane;
    private JButton createPlayerB;
    private JButton updatePlayerB;
    private JButton viewLeagueTableB;
    private JButton viewLeagueGraphB;
    private JButton createLeagueB;
    private JButton addPlayer2LeagueB;
    private JButton removePlayerB;
    private JButton loadB;
    private JButton saveB;
    private JButton endB;
    
    private League currLeague;
    private static final String JSON_STORE = "./data/dyeappGUI.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //Constructor
    public MainWindow() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        initialize();
    }
    
    //MODIFIES: This.
    //EFFECTS: initializes JFrame and calls Methods to add Panel/s 
    //         and add functionality to Buttons
    private void initialize() {
        window = new JFrame("DyeStatsApp");
        window.setSize(800,800);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        createMenuPanel();
        addFunctionality();
        
        window.setVisible(true);
    }

    //MODIFIES: This.
    //EFFECTS: Initializes Menu Panel, setting color and size. Calls helper to create buttons
    private void createMenuPanel() {
        menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setBackground(Color.LIGHT_GRAY);
        menuPanel.setPreferredSize(new Dimension(250,800));
        createButtonsForPanel();
    }

    //MODIFIES: This.
    //EFFECTS: Creates and adds buttons to menuPanel
    private void createButtonsForPanel() {
        createPlayerB = new JButton("Create Player");
        menuPanel.add(createPlayerB);
        updatePlayerB = new JButton("Update Player");
        menuPanel.add(updatePlayerB);
        viewLeagueTableB = new JButton("View League Table");
        menuPanel.add(viewLeagueTableB);
        viewLeagueGraphB = new JButton("View League Graph");
        menuPanel.add(viewLeagueGraphB);
        createLeagueB = new JButton("Create League");
        menuPanel.add(createLeagueB);
        addPlayer2LeagueB = new JButton("Add Player to League");
        menuPanel.add(addPlayer2LeagueB);
        removePlayerB = new JButton("Remove Player from League");
        menuPanel.add(removePlayerB);
        loadB = new JButton("Load Data    ");
        menuPanel.add(loadB);
        saveB = new JButton("Save Changes ");
        menuPanel.add(saveB);
        endB = new JButton("Close Application");
        menuPanel.add(endB);
        window.add(menuPanel, BorderLayout.WEST);
    }


    //EFFECTS: Calls appropriate methods to add functionality to buttons in menuPanel
    private void addFunctionality() {
        addCreatePlayerFunctionality();
        addUpdatePlayerFunctionality();
        addviewLeagueTableFunctionality();
        addViewLeagueGraphFunctionality();
        addCreateLeagueFunctionality();
        addAdd2LeagueFunctionality();
        addRemoveFromLeagueFunctionality();
        addLoadFunctionality();
        addSaveFunctionality();
        addCloseFunctionality();
    }

    
    //EFFECTS: Adds action listener to createPlayerB
    private void addCreatePlayerFunctionality() {
        createPlayerB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPlayer();
            }
        });
    }

    //MODIFIES: Player, League
    //EFFECTS: adds a new player to system using UI
    private void createPlayer() {
        String enteredPName = JOptionPane.showInputDialog(window, "Enter the new player's name:");

        if (enteredPName != null && !enteredPName.trim().isEmpty()) {
            if (Player.selectPlayer(enteredPName) == null) {
                new Player(enteredPName);
                JOptionPane.showMessageDialog(window, "Player created successfully!");
            } else {
                JOptionPane.showMessageDialog(window, 
                                "That player name is already in use. Please try again with a different name.");
            }
        } else {
            JOptionPane.showMessageDialog(window, "Player creation cancelled or invalid input.");
        }
    }
    
    //EFFECTS: Adds action listener to updatePlayerB
    private void addUpdatePlayerFunctionality() {
        updatePlayerB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePlayer();
            }    
        });
    }

    //MODIFIES: Player
    //EFFECTS: updates player in system using UI
    private void updatePlayer() {
        String enteredPName = JOptionPane.showInputDialog(window, "Enter the player's name to update:");
        if (enteredPName != null && !enteredPName.trim().isEmpty()) {
            Player player = Player.selectPlayer(enteredPName.trim());
            if (player != null) {
                int confirmWin = JOptionPane.showConfirmDialog(window, "Did the player win?", 
                                "Player Win Confirmation", JOptionPane.YES_NO_OPTION);
                boolean isWin = (confirmWin == JOptionPane.YES_OPTION);
                
                String selfSinksInput = JOptionPane.showInputDialog(window, "Enter the number of self-sinks:");
                try {
                    int selfSinks = Integer.parseInt(selfSinksInput.trim());
                    player.update(isWin, selfSinks);
                    JOptionPane.showMessageDialog(window, "Player updated successfully!");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(window, "Invalid input for self-sinks. Please enter a valid number.");
                }
            } else {
                JOptionPane.showMessageDialog(window, "Player not found. Please check the name and try again.");
            }
        } else {
            JOptionPane.showMessageDialog(window, "Update cancelled or invalid input.");
        }
    }

    //EFFECTS: Adds action listener to viewLeagueB
    private void addviewLeagueTableFunctionality() {
        viewLeagueTableB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewLeagueTable();
            }    
        });
    }

    //EFFECTS: Prompts user to enter league name. If the league exists it calls makeStandingsTable() 
    //         with appropriate sorting, if not, the user is informed.
    private void viewLeagueTable() {
        String enteredLName = JOptionPane.showInputDialog(window, "Enter the name of the league you want to view:");
        if (enteredLName != null && !enteredLName.trim().isEmpty()) {
            currLeague = League.findLeague(enteredLName);
            if (currLeague != null) {
                String viewType = JOptionPane.showInputDialog(window, "Enter w (Win-rate), l (Losses), s (Self-Sinks)");
                if (viewType.equalsIgnoreCase("w")) {
                    currLeague.sortWRStandings();
                    makeStandingsTable();
                } else if (viewType.equalsIgnoreCase("l")) {
                    currLeague.sortLStandings();
                    makeStandingsTable();
                } else if (viewType.equalsIgnoreCase("s")) {
                    currLeague.sortSSStandings();
                    makeStandingsTable();
                } else {
                    JOptionPane.showMessageDialog(window, "Not a valid input.");
                }
            } else {
                JOptionPane.showMessageDialog(window, "League cannot be found.");
            }

        }
    }


    //EFFECTS: Produces JTable with standings of desired league 
    private void makeStandingsTable() {
        String[] columns = {"Standing", "Name", "Wins", "Losses", "Self-Sinks", "Win Rate", "Self-Sink Rate"};
        Object[][] data = new Object[currLeague.getLeague().size()][8];
        ArrayList<Player> players = currLeague.getLeague();
        int rank = 1;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            data[i][0] = rank++;
            data[i][1] = player.getPlayerName();
            data[i][2] = player.getWins();
            data[i][3] = player.getLosses();
            data[i][4] = player.getPlayed();
            data[i][5] = player.getSelfSinks();
            data[i][6] = String.format("%.2f%%", (double) player.getWinRate());
            data[i][7] = String.format("%.2f%%", (double) player.getSelfSinkRate());
        }
        
        if (dataPane != null) {
            window.remove(dataPane);  
        }
        JTable table = new JTable(data, columns);
        dataPane = new JScrollPane(table);
        window.add(dataPane, BorderLayout.CENTER);
        window.revalidate();
        window.repaint();
    }

    //EFFECTS: Adds functionality to viewLeagueGraphB. creates an instance of WinRateBarChartPopup 
    //         if entered league exists
    private void addViewLeagueGraphFunctionality() {
        viewLeagueGraphB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredLName = JOptionPane.showInputDialog(window, "Enter the name of the league to view:");
                if (enteredLName != null && !enteredLName.trim().isEmpty()) {
                    currLeague = League.findLeague(enteredLName);
                    if (currLeague != null) {
                        ArrayList<Player> players = currLeague.getLeague();
                        new WinRateBarChartPopup(players);
                    }
                }
            }    
        });
    }



    //EFFECTS: Adds action listener to createLeagueB
    private void addCreateLeagueFunctionality() {
        createLeagueB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createLeague();
            }
        });
    }

    //EFFECTS: adds a new League to system using UI
    private void createLeague() {
        String enteredLName = JOptionPane.showInputDialog(window, "Enter the new league's name:");
        if (enteredLName != null && !enteredLName.trim().isEmpty()) {
            if (League.findLeague(enteredLName) == null) {
                String key = JOptionPane.showInputDialog(window, "Enter the new league's desired key:");
                if (key != null && !key.trim().isEmpty()) {
                    new League(key, enteredLName);
                    JOptionPane.showMessageDialog(window, "League created successfully!");
                } else {
                    JOptionPane.showMessageDialog(window, "Invalid key. Please try again");
                }
            } else {
                JOptionPane.showMessageDialog(window, "That league name is already in use. Please try again.");
            }
        } else {
            JOptionPane.showMessageDialog(window, "League creation cancelled or invalid input.");
        }
    }

    //EFFECTS: Adds action listener to addPlayer2LeagueB
    private void addAdd2LeagueFunctionality() {
        addPlayer2LeagueB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayer2League();
            }
        });
    }

    // MODIFIES: LEAGUE and PLAYER
    // Effects: Adds desired player to desired league using GUI
    private void addPlayer2League() {
        String enteredPName = JOptionPane.showInputDialog(window, 
                        "Enter the name of the player you want to add to a league:");
        if (enteredPName != null && !enteredPName.trim().isEmpty()) {
            Player player = Player.selectPlayer(enteredPName.trim());
            if (player != null) {
                String enteredLName = JOptionPane.showInputDialog(window, 
                                "Enter the league you want the player to join:");
                if (enteredLName != null && !enteredLName.trim().isEmpty()) {
                    League league = League.findLeague(enteredLName);
                    if (league != null) {
                        keyCheck(player, league);
                    } else {
                        JOptionPane.showMessageDialog(window, "League not found");
                    }
                } else {
                    JOptionPane.showMessageDialog(window, "Player not found");
                }
            }
        } else {
            JOptionPane.showMessageDialog(window, "Invalid Player Name");
        }
    }

    //EFFECTS: Adds player to league if key inputted matches league's key
    private void keyCheck(Player player, League league) {
        String key = JOptionPane.showInputDialog(window, "Enter the league's key:");
        if (key != null && !key.trim().isEmpty()) {
            boolean result = league.add2League(player, key);
            if (result) {
                JOptionPane.showMessageDialog(window, "Player added successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(window, "Invalid Key");
        }
    }

    //EFFECTS: Adds action listener to removePlayerB
    private void addRemoveFromLeagueFunctionality() {
        removePlayerB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePlayerFromLeague();
            }
        });
    }

    //MODIFIES: Player, League
    //EFFECTS: Retrieves player (to remove), league (to remove from) and key from user. 
    private void removePlayerFromLeague() {
        String enteredPName = JOptionPane.showInputDialog(window, 
                        "Enter the name of the player you want to remove from a league:");
        if (enteredPName != null && !enteredPName.trim().isEmpty()) {
            Player player = Player.selectPlayer(enteredPName.trim());
            if (player != null) {
                String enteredLName = JOptionPane.showInputDialog(window, 
                                "Enter the league you want to remove the player from:");
                if (enteredLName != null && !enteredLName.trim().isEmpty()) {
                    League league = League.findLeague(enteredLName);
                    if (league != null) {
                        String key = JOptionPane.showInputDialog(window, "Enter the league's key:");
                        league.removeFromLeague(player, key);
                    }
                }
            }
        } 
            
    }



    //EFFECTS: Adds action listener to loadB
    private void addLoadFunctionality() {
        loadB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
    }

    //EFFECTS: Loads data from ./data/dyeappGUI.json
    private void loadData() {
        try {
            ArrayList<Player> lop = Player.getLop();
            ArrayList<League> lol = League.getLol();
            lop = jsonReader.readPlayers();
            lol = jsonReader.readLeagues();
            Player.setLop(lop);
            League.setLol(lol);
            JOptionPane.showMessageDialog(window,"Loaded successfully from " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(window,"Unable to read from file: " + JSON_STORE);
        }
    }

    //EFFECTS: Adds action listener to saveB
    private void addSaveFunctionality() {
        saveB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
    }

    //EFFECTS: saves data from ./data/dyeappGUI.json
    private void saveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(Player.getLop(), League.getLol());
            jsonWriter.close();
            JOptionPane.showMessageDialog(window,"Saved your changes to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(window,"Unable to write to file: " + JSON_STORE);
        }
    }

    //EFFECTS: Adds action listener to endB
    private void addCloseFunctionality() {
        endB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    printLog(EventLog.getInstance());
                } catch (LogException e1) {
                    System.out.println("failed");
                } finally {
                    System.exit(0);
                }
            }
        });
    }
    
    //EFFECTS: prints the log via traversing through it entirely
    private void printLog(EventLog el) throws LogException {
        System.out.println("Event Log");
        for (Event next : el) {
            System.out.println(next.toString());
        }
    }

}
