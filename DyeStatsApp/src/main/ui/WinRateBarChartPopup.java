package ui;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import model.Player;

// Represents JFrame that displays a bar chart representing the win rates of players in a given list
public class WinRateBarChartPopup extends JFrame {

    //EFFECTS: Initializes a JFrame window titled "Win Rate Bar Chart" with a specified size.
    //         If the players list is null or empty, displays a message dialog indicating "No players to display"
    //          and does not render the bar chart. If players are present, adds a WinRateChartPanel containing
    //          the bar chart for the players' win rates and makes the window visible.
    public WinRateBarChartPopup(ArrayList<Player> players) {
        setTitle("Win Rate Bar Chart");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        
        if (players == null || players.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No players to display");
            return;
        }

        add(new WinRateChartPanel(players));
        setVisible(true);
    }
}
