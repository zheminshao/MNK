/*
A Piece is a graphical representation of the state of an individual Intersection.
*/

import java.awt.*;
import javax.swing.*;

public class Piece extends JPanel {
   private final int row;
   private final int column;
   private final int color;
   private final int transparency;
   
   public Piece(int row, int column, int color) {
      this.row = row;
      this.column = column;
      this.color = color;
      this.transparency = 255;
   }
   
   public Piece(int row, int column, int color, double lighten) {
      this.row = row;
      this.column = column;
      this.color = color;
      this.transparency = (int) (255 * lighten);
   }
   
   //Draws the piece
   @Override
   public void paintComponent(Graphics g) {
      g.setColor(new Color(0, 0, 0, transparency));
      g.fillOval(5 + 40 * column, 5 + 40 * row, 30, 30);
      if (color == 2) {
         g.setColor(new Color(255, 255, 255, transparency));
      } else if (color == 3) {
         g.setColor(new Color(255, 0, 0, transparency));
      } else if (color == 4) {
         g.setColor(new Color(0, 0, 255, transparency));
      }
      g.fillOval(7 + 40 * column, 7 + 40 * row, 26, 26);
   }
}