package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Player;

// Represents panel that displays the bar chart 
class WinRateChartPanel extends JPanel {

    private final ArrayList<Player> players;

    //EFFECTS: Constructor that initializes field of players
    public WinRateChartPanel(ArrayList<Player> players) {
        this.players = players;
    }

    // MODIFIES: This.
    // EFFECTS: Renders a bar chart representing each player's win rate on this panel with the player's name 
    //          below their respective bar and their win-rate on top of each one.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphic = (Graphics2D) g;
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int barWidth = panelWidth / (players.size() * 3);
        for (int count = 0; count < players.size(); count++) {
            Player player = players.get(count);
            double winRate = player.getWinRate();
            int barHeight = (int) (winRate * 2);
            int xaxis = (count * 2 + 1) * barWidth;
            int yaxis = panelHeight - barHeight - 30;
            graphic.setColor(Color.BLUE);
            graphic.fillRect(xaxis, yaxis, barWidth, barHeight);
            graphic.setColor(Color.BLACK);
            graphic.drawString(player.getPlayerName(), xaxis, panelHeight - 10);
            graphic.drawString(String.format("%.0f%%", winRate), xaxis, yaxis - 5);
        }
    }
}