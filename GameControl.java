/*
A GameControl organizes an individual game.
*/

import javax.swing.*;

public class GameControl {
   private int rows;
   private int columns;
   private int numToWin;
   private int mode;
   private int turn;
   private int deepZColor;
   private int deepJColor;
   private boolean inGame;
   private boolean callComMove;
   private int numPieces;
   private int pieceIndex;
   private int players;
   private Grid g;
   private Board b;
   private MyMouseListener m;
   private MyMouseMotionListener mm;
   private MyKeyListener k;
   private AI deepZ;
   private AI deepJ;
   
   public GameControl(int rows, int columns, int numToWin, int mode) {
      this.rows = rows;
      this.columns = columns;
      this.numToWin = numToWin;
      this.mode = mode;
      this.turn = 1;
      this.inGame = true;
      this.callComMove = true;
      this.numPieces = 0;
      this.pieceIndex = -1;
      this.players = 2;
      this.deepZColor = 0;
      this.deepJColor = 0;
      this.g = new Grid(rows, columns, numToWin);
      if (mode == 2) {
         //Selects a random color for the computer
         deepZColor = (int) (Math.random() * 2) + 1;
         this.deepZ = new AI(g, deepZColor);
      } else if (mode == 3) {
         this.players = 3;
      } else if (mode == 4) {
         this.players = 4;
      } else if (mode == 5) {
         this.deepZ = new AI(g, 2);
         deepZColor = 1;
         this.deepJ = new AI(g, 1);
         deepJColor = 2;
      }
      this.b = new Board(rows, columns, numToWin);
      this.m = new MyMouseListener(this, rows, columns);
      this.mm = new MyMouseMotionListener(this, rows, columns);
      b.getFrame().addMouseListener(m);
      b.getFrame().addMouseMotionListener(mm);
      this.k = new MyKeyListener(this);
      b.getFrame().addKeyListener(k);
   }
   
   //Plays a move, editing both the Grid and the Board
   public void play(int row, int column) {
      if (inGame && row < rows && column < columns && g.getState()[row][column].getColor() == 0) {
         g.edit(row, column, turn);
         b.move(row, column, turn);
         if (turn == players) {
            turn = 1;
         } else {
            turn++;
         }
         callComMove = true;
         numPieces++;
         pieceIndex++;
      }
   }
   
   //Called whenever mouse is moved. Organizes mouse hover actions.
   public void mouseResponse (int x, int y) {
      b.nonButton();
      if (b.getBackButton().contains(x, y)) {
         b.onBack();
      } else if (b.getForwardButton().contains(x, y)) {
         b.onForward();
      } else if (b.getResetButton().contains(x, y)) {
         b.onReset();
      } else if (pieceIndex == numPieces - 1 && turn != deepZColor && turn != deepJColor) {
         pretend(y / 40, x / 40);
      }
   }
   
   //Organizes graphical depiction of "hypothetical" piece
   public void pretend(int row, int column) {
      b.unpretend();
      if (inGame && row < rows && column < columns && g.getState()[row][column].getColor() == 0) {
         b.pretend(row, column, turn);
      }
   }
   
   //Undoes the last move
   public void undo() {
      if (pieceIndex > -1) {
         b.remove(pieceIndex);
         pieceIndex--;
      }
   }
   
   //Redoes(?) the last move
   public void redo() {
      if (pieceIndex < numPieces - 1) {
         b.add(pieceIndex + 1);
         pieceIndex++;
      }
   }
   
   //Resets the board to its most advanced state. If the board is already in its most advanced
   //state, the board is cleared.
   public void reset() {
      if (pieceIndex < numPieces - 1) {
         b.fill(pieceIndex);
         pieceIndex = numPieces - 1;
      } else {
         b.empty();
         pieceIndex = -1;
      }
   }
   
   //Organizes AI moves, when the turn is appropriate.
   public void comMove() {
      callComMove = false;
      if (turn == deepZColor) {
         Intersection nextMove = deepZ.move();
         play(nextMove.getRow(), nextMove.getColumn());
      } else if (turn == deepJColor) {
         Intersection nextMove = deepJ.move();
         play(nextMove.getRow(), nextMove.getColumn());
      }
   }
   
   public int getTurn() {
      return turn;
   }
   
   public Grid getGrid() {
      return g;
   }
   
   public Board getBoard() {
      return b;
   }
   
   public int getDeepZColor() {
      return deepZColor;
   }
   
   public int getDeepJColor() {
      return deepJColor;
   }
   
   //Ends the game.
   public void endGame() {
      inGame = false;
      int victory = g.detectVictory();
      if (mode == 2) {
         if (victory == deepZColor) {
            System.out.print("deepZ wins!");
            JOptionPane.showMessageDialog(b.getFrame(), "deepZ wins!");
         } else if (victory != -1) {
            System.out.print("You win!");
            JOptionPane.showMessageDialog(b.getFrame(), "You win!");
         } else {
            System.out.print("It's a draw!");
            JOptionPane.showMessageDialog(b.getFrame(), "It's a draw!");
         }
      } else {
         if (victory == 1) {
            System.out.print("Black wins!");
            JOptionPane.showMessageDialog(b.getFrame(), "Black wins!");
         } else if (victory == 2) {
            System.out.print("White wins!");
            JOptionPane.showMessageDialog(b.getFrame(), "White wins!");
         } else if (victory == 3) {
            System.out.print("Red wins!");
            JOptionPane.showMessageDialog(b.getFrame(), "Red wins!");
         } else if (victory == 4) {
            System.out.print("Blue wins!");
            JOptionPane.showMessageDialog(b.getFrame(), "Blue wins!");
         } else {
            System.out.print("It's a draw!");
            JOptionPane.showMessageDialog(b.getFrame(), "It's a draw!");
         }
      }
      System.out.println();
   }
   
   public boolean inGame() {
      return inGame;
   }
   
   public boolean callComMove() {
      return callComMove;
   }
   
   public int getNumPieces() {
      return numPieces;
   }
   
   public int getPieceIndex() {
      return pieceIndex;
   }
}