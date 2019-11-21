/* 
An NMK AI selects a move given the current configuration of pieces.
It does this by evaluating hypothetical positions based on predetermined key Strings 
that indicate desirable outcomes for itself, as well as potential threats from the opponent.
Using this method, a score is assigned to each potential move, and the move with the highest score
is returned as an Intersection.
*/

import java.util.*;

public class AI {
   private final Grid g;
   private int color;
   private int oppColor;
   private int numToWin;
   private final int rows;
   private final int columns;
   private ArrayList<String> S0;
   private ArrayList<String> S1;
   private ArrayList<String> S2;
   private ArrayList<String> S3;
   private ArrayList<String> S4;
   private ArrayList<String> O0;
   private ArrayList<String> O1;
   private ArrayList<String> O2;
   private ArrayList<String> O3;
   private ArrayList<String> O4;
   private Grid potentialGrid;
   private int[][] scores;
   private int maxScore;
   
   public AI(Grid g, int color) {
      this.g = g;
      this.color = color;
      if (color == 1) {
         this.oppColor = 2;
      } else {
         this.oppColor = 1;
      }
      this.numToWin = g.getNumToWin();
      this.rows = g.getState().length;
      this.columns = g.getState()[0].length;
      this.S0 = create0(color);
      this.S1 = create1(color);
      this.S2 = create2(color);
      this.S3 = create3(color);
      this.S4 = create4(color);
      this.O0 = create0(oppColor);
      this.O1 = create1(oppColor);
      this.O2 = create2(oppColor);
      this.O3 = create3(oppColor);
      this.O4 = create4(oppColor);
   }
   
   public int getColor() {
      return color;
   }
   
   public int getMaxScore() {
      return maxScore;
   }
   
   //Creates an ArrayList of keyStrings containing numToWin pieces
   private ArrayList<String> create0(int color) {
      ArrayList<String> keyStrings = new ArrayList<String>();
      String line = "";
      for (int i = 0; i < numToWin; i++) {
         line += color;
      }
      keyStrings.add(line);
      return keyStrings;
   }
   
   //Creates an ArrayList of key Strings containing numToWin - 1 pieces in an open arrangement
   //(open spaces on both ends)
   private ArrayList<String> create1(int color) {
      ArrayList<String> keyStrings = new ArrayList<String>();
      if (numToWin < 3) {
         return keyStrings;
      }
      String line = "";
      for (int i = 0; i < numToWin - 1; i++) {
         line += color;
      }
      keyStrings.add("0" + line + "0");
      return keyStrings;
   }
   
   //Creates an ArrayList of key Strings containing numToWin - 1 pieces in a closed arrangement
   //(does not meet the criteria for an open arrangement as described above, but has the potential
   //to become numToWin in a row)
   private ArrayList<String> create2(int color) {
      ArrayList<String> keyStrings = new ArrayList<String>();
      if (numToWin < 3) {
         return keyStrings;
      }
      int oColor;
      if (color == 1) {
         oColor = 2;
      } else {
         oColor = 1;
      }
      String line;
      for (int i = 0; i < numToWin - 2; i++) {
         line = "";
         for (int j = 0; j < i + 1; j++) {
            line += color;
         }
         line += 0;
         for (int j = 0; j < numToWin - i - 2; j++) {
            line += color;
         }
         keyStrings.add(line + oColor);
         keyStrings.add(oColor + line);
      }
      line = "";
      for (int i = 0; i < numToWin - 1; i++) {
         line += color;
      }
      keyStrings.add("0" + line + oColor);
      keyStrings.add(oColor + line + "0");
      return keyStrings;
   }
   
   //Creates an ArrayList of key Strings containing numToWin - 2 pieces in an open arrangement
   //(one end has at least one open space, the other end has at least two)
   public ArrayList<String> create3(int color) {
      ArrayList<String> keyStrings = new ArrayList<String>();
      if (numToWin < 4) {
         return keyStrings;
      }
      String line;
      for (int i = 0; i < numToWin - 1; i++) {
         line = "";
         for (int j = 0; j < i; j++) {
            line += color;
         }
         line += 0;
         for (int j = 0; j < numToWin - i - 2; j++) {
            line += color;
         }
         keyStrings.add("0" + line + "0");
      }
      return keyStrings;   
   }
   
   //Creates an ArrayList of key Strings containing numToWin - 2 pieces in a closed arrangement
   //(does not meet the criteria for an open arrangement as described above, but has the potential
   //to become numToWin in a row)
   public ArrayList<String> create4(int color) {
      ArrayList<String> keyStrings = new ArrayList<String>();
      if (numToWin < 4) {
         return keyStrings;
      }
      int oColor;
      if (color == 1) {
         oColor = 2;
      } else {
         oColor = 1;
      }      
      String line;
      for (int i = 0; i < numToWin - 1; i++) {
         for (int j = i + 1; j < numToWin; j++) {
            line = "";
            for (int k = 0; k < numToWin; k++) {
               if (k == i || k == j) {
                  line += 0;
               } else {
                  line += color;
               }
            }
            if (line.startsWith("" + color)) {
               keyStrings.add(oColor + line);
            }
            if (line.endsWith("" + color)) {
               keyStrings.add(line + oColor);
            }
         }
      }
      line = "";
      for (int i = 0; i < numToWin - 2; i++) {
         line += color;
      }
      keyStrings.add(oColor + "0" + line + "0" + oColor);
      return keyStrings;
   }
   
   //Prints the key Strings, used as part of testing process
   public void printKeyStrings() {
      System.out.println("Self win x: " + S0);
      System.out.println("Self open x-1:  " + S1);
      System.out.println("Self closed x-1: " + S2);
      System.out.println("Self (fully) open x-2: " + S3);
      System.out.println("Self closed x-2: " + S4);
      System.out.println("Opponent win x: " + O0);
      System.out.println("Opponent open x-1: " + O1);
      System.out.println("Opponent closed x-1: " + O2);
      System.out.println("Opponent (fully) open x-2: " + O3);
      System.out.println("Opponent closed x-2: " + O4);
   }
   
   //Selects a move and returns it as an Intersection
   public Intersection move() {
      scores = new int[rows][columns];
      Intersection[][] copyOfState;
      ArrayList<String> lines;
      
      //Analyzes every possible Intersection
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            if (g.getState()[i][j].getColor() == 0) {
               //Creates a copy of the current state for analysis
               copyOfState = new Intersection[rows][columns];
               for (int r = 0; r < rows; r++) {
                  for (int c = 0; c < columns; c++) {
                     copyOfState[r][c] = new Intersection(g.getState()[r][c].getRow(), 
                        g.getState()[r][c].getColumn(), g.getState()[r][c].getColor());
                  }
               }
               
               //Obtains the revelant resulting lines if the AI moves on this Intersection
               lines = new ArrayList<String>();
               copyOfState[i][j] = new Intersection(i, j, color);
               potentialGrid = new Grid(rows, columns, copyOfState);
               lines.add(oppColor + potentialGrid.stringRow(i) + oppColor);
               lines.add(oppColor + potentialGrid.stringColumn(j) + oppColor);
               lines.add(oppColor + potentialGrid.stringPositiveDiagonal(i, j) + oppColor);
               lines.add(oppColor + potentialGrid.stringNegativeDiagonal(i, j) + oppColor);
               
               //Adds to this Intersection's score based on which key Strings are observed
               for (String line: lines) {
                  for (String keyString: S0) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 1000000;
                        break;
                     }
                  }
                  for (String keyString: S1) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 10000;
                        break;
                     }
                  }
                  for (String keyString: S2) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 700;
                        break;
                     }
                  }
                  for (String keyString: S3) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 400;
                        break;
                     }
                  }
                  for (String keyString: S4) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 80;
                        break;
                     }
                  }
               }
               
               //Obtains the revelant resulting lines if the opponent moves on this Intersection
               lines = new ArrayList<String>();
               copyOfState[i][j] = new Intersection(i, j, oppColor);
               potentialGrid = new Grid(rows, columns, copyOfState);
               lines.add(color + potentialGrid.stringRow(i) + color);
               lines.add(color + potentialGrid.stringColumn(j) + color);
               lines.add(color + potentialGrid.stringPositiveDiagonal(i, j) + color);
               lines.add(color + potentialGrid.stringNegativeDiagonal(i, j) + color);
               
               //Adds to this Intersection's score based on which key Strings are observed
               for (String line: lines) {
                  for (String keyString: O0) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 100000;
                        break;
                     }
                  }
                  for (String keyString: O1) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 1300;
                        break;
                     }
                  }
                  for (String keyString: O2) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 450; 
                        break;
                     }
                  }
                  for (String keyString: O3) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 375;
                        break;
                     }
                  }
                  for (String keyString: O4) {
                     if (line.contains(keyString)) {
                        scores[i][j] += 60;
                        break;
                     }
                  }
               }
               
               //Adds to this Intersection's score based on the presence of adjacent pieces
               for (int r = i - 1; r < i + 2; r++) {
                  for (int c = j - 1; c < j + 2; c++) {
                     if (r >= 0 && r < rows && c >= 0 && c < columns) {
                        if (r == i && c == j) {
                        } else if (copyOfState[r][c].getColor() == color) {
                           scores[i][j] += 40;
                        } else if (copyOfState[r][c].getColor() == oppColor) {
                           scores[i][j] += 20;
                        }
                     }
                  }
               }
               
               //Adds to the score if this is the center of the grid. Used as a "tiebreaker" so
               //that the AI plays in the center as the first move.
               if (i == rows/2 && j == columns/2) {
                  scores[i][j] += 1;
               }
               
               //Makes a slight adjustment for tic-tac-toe (the 3,3,3-game)
               if (rows == 3 && columns == 3 && numToWin == 3) {
                  if (i != 1 && (i + j == 0 || i + j == 2 || i + j == 4) && 
                     copyOfState[1][1].getColor() != oppColor) {
                     scores[i][j] -= 500;
                  }
               }
            }
         }
      }
      
      //Creates an ArrayList of the Intersections with the highest score
      ArrayList<Intersection> moves = new ArrayList<Intersection>();
      maxScore = 0;
      for (int i = 0; i < scores.length; i++) {
         for (int j = 0; j < scores[0].length; j++) {
            if (scores[i][j] > maxScore) {
               maxScore = scores[i][j];
               moves = new ArrayList<Intersection>();
               moves.add(new Intersection(i, j));
            } else if (scores[i][j] == maxScore) {
               moves.add(new Intersection(i, j));
            }
         }
      }
      
      //Selects and returns a random Intersection from those with the highest score
      Intersection move = moves.get((int) (Math.random() * moves.size()));
      return move;
   }
}