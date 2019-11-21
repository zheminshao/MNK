/*
A Grid organizes a 2-D array of Intersections (the state).
*/

import java.awt.event.*;
import java.util.*;

public class Grid {
   private final int rows;
   private final int columns;
   private Intersection[][] state;
   private int pieceCount;
   private Intersection lastMove;
   private int numToWin;
   private String blackWin;
   private String whiteWin;
   private String redWin;
   private String blueWin;
   
   public Grid(int rows, int columns, int numToWin) {
      this.rows = rows;
      this.columns = columns;
      this.state = new Intersection[rows][columns];
      for (int r = 0; r < state.length; r++) {
         for (int c = 0; c < state[r].length; c++) {
            state[r][c] = new Intersection(r, c);
         }
      }
      this.pieceCount = 0;
      this.lastMove = state[0][0];
      this.blackWin = "";
      this.whiteWin = "";
      this.redWin = "";
      this.blueWin = "";
      this.numToWin = numToWin;
      for (int i = 0; i < numToWin; i++) {
         blackWin += "1";
         whiteWin += "2";
         redWin += "3";
         blueWin += "4";
      }
   }
   
   public Grid(int rows, int columns, Intersection[][] state) {
      this.rows = rows;
      this.columns = columns;
      this.state = state;
   }   
   
   //Prints the state to the console. Used for testing purposes.
   public void print() {
      for (Intersection[] row: state) {
         for (Intersection location: row) {
            System.out.print(location);
         }
         System.out.println();
      }
   }
   
   public Intersection[][] getState() {
      return state;
   }
   
   public int getNumToWin() {
      return numToWin;
   }
   
   //Returns the states of the Intersections in a given row as a String. 
   //Used for detecting key Strings.
   public String stringRow(int row) {
      String line = "";
      for (int c = 0; c < columns; c++) {
         line += state[row][c];
      }
      return line;
   }
   
   //Returns the states of the Intersections in a given column as a String.
   //Used for detecting key Strings.
   public String stringColumn(int column) {
      String line = "";
      for (int r = 0; r < rows; r++) {
         line += state[r][column];
      }
      return line;
   }
   
   //Returns the states of the Intersections in the lower-left to upper-right diagonal containing 
   //the specified location.
   //Used for detecting key Strings.
   public String stringPositiveDiagonal(int row, int column) {
      int r = row + column;
      int c = 0;
      if (r >= rows) {
         c = r - rows + 1;
         r = rows - 1;
      }
      String line = "";
      while (r >= 0 && c < columns) {
         line += state[r][c];
         r--;
         c++;
      }
      return line;
   }

   //Returns the states of the Intersections in the upper-left to lower-right diagonal containing 
   //the specified location.
   //Used for detecting key Strings.   
   public String stringNegativeDiagonal(int row, int column) {
      int r = row - column;
      int c = 0;
      if (r < 0) {
         c -= r;
         r = 0;
      }
      String line = "";
      while (r < rows && c < columns) {
         line += state[r][c];
         r++;
         c++;
      }
      return line;
   }   
   
   //Updates the entire state
   public void stateEdit(Intersection[][] state) {
      this.state = state;
   }
   
   //Edits one Intersection in the state
   public void edit(int row, int column, int color) {
      state[row][column].setColor(color);
      pieceCount++;
      lastMove = state[row][column];
   }
   
   //Returns an integer value representing which player, if any, has achieved numToWin-in-a-row
   public int detectVictory() {
      ArrayList<String> lines = new ArrayList<String>();
      lines.add(stringRow(lastMove.getRow()));
      lines.add(stringColumn(lastMove.getColumn()));
      lines.add(stringPositiveDiagonal(lastMove.getRow(), lastMove.getColumn()));
      lines.add(stringNegativeDiagonal(lastMove.getRow(), lastMove.getColumn()));
      for (String l: lines) {
         if (l.contains(blackWin)) {
            return 1;
         } else if (l.contains(whiteWin)) {
            return 2;
         } else if (l.contains(redWin)) {
            return 3;
         } else if (l.contains(blueWin)) {
            return 4;
         }
      }
      if (pieceCount == rows * columns) {
         return -1;
      }
      return 0;
   }
}