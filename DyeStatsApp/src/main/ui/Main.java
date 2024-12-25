package ui;

import java.io.FileNotFoundException;

//Main Class, Starts the program.
public class Main {
    public static void main(String[] args) throws Exception {
        try {
            new DyeStatsApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
