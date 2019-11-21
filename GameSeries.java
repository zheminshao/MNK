/*
A GameSeries organizes a series of games by creating GameControls.
*/

import java.util.*;
import java.io.*;

public class GameSeries {
   private int rows;
   private int columns;
   private int numToWin;
   private int mode;
   private int games;
   private int blackWins;
   private int whiteWins;
   private int redWins;
   private int blueWins;
   private int draws;
   private double userWins;
   private double comWins;
   
   public GameSeries() {
      Scanner console = new Scanner(System.in);
      System.out.println("Enter game specifications: ");
      this.rows = prompt(console, "Rows");
      this.columns = prompt(console, "Columns");
      this.numToWin = rows + columns;
      while (numToWin > rows && numToWin > columns) {
         numToWin = prompt(console, "How many in a row to win? (cannot be greater than both #rows "
         + "and #columns)");
      }
      this.games = prompt(console, "Games");
      this.mode = prompt(console, "Mode (1. player vs. player; 2. player vs. computer; "
         + "3. 3 players; 4. 4 players; 5. computer vs. computer)");
      this.blackWins = 0;
      this.whiteWins = 0;
      this.redWins = 0;
      this.blueWins = 0;
      this.draws = 0;
      this.userWins = 0.0;
      this.comWins = 0.0;
      System.out.println();
   }
   
   //Prompts the user for a positive integer value
   public int prompt(Scanner console, String prompt) {
      int x = 0;
      do {
         try {
            System.out.print(prompt + " ");
            x = console.nextInt();
            if (x <= 0) {
               System.out.println("Please enter a positive integer.");
            }
         } catch (InputMismatchException e) {
            System.out.println("Please enter a positive integer.");
            console.nextLine();
         }
      } while (x <= 0);
      return x;
   }
   
   //Creates and executes an individual game
   public void start() throws FileNotFoundException {
      for (int i = 0; i < games; i++) {
         GameControl control = new GameControl(rows, columns, numToWin, mode);
         Grid g = control.getGrid();
         while (g.detectVictory() == 0) {
            if (control.callComMove()) {
               control.comMove();
            }
         }
         control.endGame();
         int victory = control.getGrid().detectVictory();
         int deepZColor = control.getDeepZColor();
         endGame(victory, deepZColor);
      }
      endSeries();
   }
   
   //Updates counters after eaech individual game
   public void endGame(int victory, int deepZColor) {
      if (victory == 1) {
         blackWins++;
      } else if (victory == 2) {
         whiteWins++;
      } else if (victory == 3) {
         redWins++;
      } else if (victory == 4) {
         blueWins++;
      } else {
         draws++;
      }
         
      if (mode == 2) {
         if (victory == -1) {
            userWins += 0.5;
            comWins += 0.5;
         } else if (victory == deepZColor) {
            comWins++;
         } else {
            userWins++;
         }
      }
   }
   
   //Displays information from the series
   public void endSeries() throws FileNotFoundException {
      System.out.println();
      System.out.println("Black wins: " + blackWins);
      System.out.println("White wins: " + whiteWins);
      if (mode == 3 || mode == 4) {
         System.out.println("Red wins: " + redWins);
      }
      if (mode == 4) {
         System.out.println("Blue wins: " + blueWins);
      }
      System.out.println("Draws: " + draws);
      
      if (mode == 2) {
         System.out.println();
         System.out.println("You " + userWins + " - " + comWins + " deepZ");
         if (userWins > comWins) {
            System.out.println("You were lucky this time...");
         } else if (userWins < comWins) {
            System.out.println("Don't worry, I'm still (almost) undefeated against humans!");
         } else {
            System.out.println("I don't like draws. Unfortunately, this is one.");
         }
         if (rows == 15 && columns == 15 && numToWin == 5) {
            updateAIFile(userWins, comWins);
         }
      }
   }
   
   //Updates the file (deepZ.txt) containing the AI's record for the classic 15,15,5-game.
   public void updateAIFile(double userWins, double comWins) throws FileNotFoundException {
      File f = new File("deepZ.txt");
      Scanner s = new Scanner(f);
      System.out.println();
      double iUserWins = 0;
      double iComWins = 0;
      String time = "";
      if (s.hasNextLine()) {
         time = s.nextLine();
         iUserWins = s.nextDouble();
         iComWins = s.nextDouble();
      }
      double totalUserWins = iUserWins + userWins;
      double totalComWins = iComWins + comWins;
      PrintStream p = new PrintStream(f);
      p.println(time);
      p.println(totalUserWins);
      p.println(totalComWins);
      System.out.println("Since " + time + ", my overall record for the 15,15,5-game is: ");
      System.out.println("Humans " + totalUserWins + " - " + totalComWins + " deepZ");
   }
}