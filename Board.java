/*
A Board is a graphical representation of a Grid.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Board extends JPanel {
   private final int rows;
   private final int columns;
   private final int numToWin;
   private final JFrame frame;
   private CircularButton backButton;
   private CircularButton forwardButton;
   private CircularButton resetButton;
   private CircularButton back;
   private CircularButton forward;
   private CircularButton reset;
   private final ArrayList<Piece> pieces;
   private Piece pretendPiece;
   private final Piece placeholderPiece;
   
   public Board(int rows, int columns, int numToWin) {
      this.rows = rows;
      this.columns = columns;
      this.numToWin = numToWin;
      this.pieces = new ArrayList<Piece>();
      this.pretendPiece = new Piece(0, 0, 0, 0);
      this.placeholderPiece = new Piece(0, 0, 0, 0);
      frame = new JFrame(columns + "," + rows + "," + numToWin +"-game");
      frame.setBackground(new Color(255, 255, 255));
      this.setPreferredSize(new Dimension(columns * 40, (rows + 1) * 40));
      frame.add(this);
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.backButton = new CircularButton(columns * 20 - 45, rows * 40 + 20, 15);
      this.forwardButton = new CircularButton(columns * 20 + 45, rows * 40 + 20, 15);
      this.resetButton = new CircularButton(columns * 20, rows * 40 + 20, 15);
      this.back = new CircularButton(columns * 20 - 45, rows * 40 + 20, 15, 102);
      this.forward = new CircularButton(columns * 20 + 45, rows * 40 + 20, 15, 102);
      this.reset = new CircularButton(columns * 20, rows * 40 + 20, 15, 102);
      frame.add(backButton);
      frame.add(forwardButton);
      frame.add(resetButton);
      frame.setVisible(true);
   }
   
   //Draws board graphics
   @Override
   public void paintComponent(Graphics g) {
      //Row lines
      for (int i = 0; i < rows; i++) {
         //Row numbers
         //g.drawString("" + i, 5, 18 + 40 * i);
         g.drawLine(0, 20 + 40 * i, columns * 40, 20 + 40 * i);
      }
      //Column lines
      for (int j = 0; j < columns; j++) {
         //Column numbers
         //g.drawString("" + j, 23 + 40 * j, 16);
         g.drawLine(20 + 40 * j, 0, 20 + 40 * j, rows * 40);
      }      
      //Intersection dots
      for(int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            g.fillOval(17 + 40 * j, 17 + 40 * i, 6, 6);
         }
      }
      
      //Back arrow graphics
      g.drawLine(columns * 20 - 35, rows * 40 + 20, columns * 20 - 55, rows * 40 + 20);
      g.drawLine(columns * 20 - 55, rows * 40 + 20, columns * 20 - 45, rows * 40 + 10);
      g.drawLine(columns * 20 - 55, rows * 40 + 20, columns * 20 - 45, rows * 40 + 30);
      
      //Forward arrow graphics
      g.drawLine(columns * 20 + 35, rows * 40 + 20, columns * 20 + 55, rows * 40 + 20);
      g.drawLine(columns * 20 + 55, rows * 40 + 20, columns * 20 + 45, rows * 40 + 10);
      g.drawLine(columns * 20 + 55, rows * 40 + 20, columns * 20 + 45, rows * 40 + 30);
      
      //Reset button circle
      g.drawOval(columns * 20 - 10, rows * 40 + 10, 20, 20);
   }
   
   //Graphical representation of a move (placing a piece)
   public void move(int row, int column, int color) {
      frame.remove(pretendPiece);
      Piece lastPiece = new Piece(row, column, color);
      pieces.add(lastPiece);
      frame.add(lastPiece);
      refresh();
   }
   
   //Graphical representation of a faint "hypothetical" piece (used when mouse is hovering over
   //an intersection)
   public void pretend(int row, int column, int color) {
      pretendPiece = new Piece(row, column, color, 0.4);
      frame.add(pretendPiece);
      refresh();
   }
   
   //Removes the current "hypothetical" piece before creating a new "hypothetical" piece
   public void unpretend() {
      frame.remove(pretendPiece);
      frame.add(placeholderPiece);
      refresh();
   }
   
   //Creates gray background on back button when mouse hovered
   public void onBack() {
      frame.add(back);
      refresh();
   }
   
   //Creates gray background on forward button when mouse hovered
   public void onForward() {
      frame.add(forward);
      refresh();
   }
   
   //Creates gray background on reset button when mouse hovered
   public void onReset() {
      frame.add(reset);
      refresh();
   }
   
   //Removes gray background on all buttons
   public void nonButton() {
      frame.remove(back);
      frame.remove(forward);
      frame.remove(reset);
      refresh();
   }
   
   //Removes the piece at a particular index
   public void remove(int index) {
      frame.remove(pieces.get(index));
      frame.remove(pretendPiece);
      refresh();
   }
   
   //Replaces the piece at a particular index
   public void add(int index) {
      frame.add(pieces.get(index));
      refresh();
   }
   
   //Fills the board to its most advanced state
   public void fill(int index) {
      for (int i = index + 1; i < pieces.size(); i++) {
         frame.add(pieces.get(i));
      }
      refresh();
   }
   
   //Removes all pieces
   public void empty() {
      for (Piece p: pieces) {
         frame.remove(p);
      }
      frame.remove(pretendPiece);
      refresh();
   }
   
   //Refreshes the layout manager so that graphics are displayed properly
   public void refresh() {
      frame.revalidate();
      frame.repaint();
   }
   
   public JFrame getFrame() {
      return frame;
   }
   
   public CircularButton getBackButton() {
      return backButton;
   }
   
   public CircularButton getForwardButton() {
      return forwardButton;
   }
   
   public CircularButton getResetButton() {
      return resetButton;
   }
   
   //Closes the JFrame
   public void close() {
      frame.setVisible(false);
   }
}