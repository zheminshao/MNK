/*
A CircularButton is a button that can be pressed. In this project, it is used for the back, 
forward, and reset buttons.
*/

import java.awt.*;
import javax.swing.*;

public class CircularButton extends JPanel {
   private final int x;
   private final int y;
   private final int radius;
   private int transparency;
   
   public CircularButton(int x, int y, int radius) {
      this.x = x;
      this.y = y;
      this.radius = radius;
      this.transparency = 0;
   }
   
   public CircularButton(int x, int y, int radius, int transparency) {
      this.x = x;
      this.y = y;
      this.radius = radius;
      this.transparency = transparency;
   }
   
   //Draws the button
   @Override
   public void paintComponent(Graphics g) {
      g.setColor(new Color(100, 100, 100, transparency));
      g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
   }
   
   //Returns whether or not the specified mouse coordinates are located within the button
   public boolean contains(int mouseX, int mouseY) {
      return (Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2)) <= radius);
   }
}