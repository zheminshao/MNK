/*
A MyKeyListener responds to key presses.
LEFT: back button
RIGHT: forward button
ENTER: reset button
*/

import java.awt.event.*;

public class MyKeyListener extends KeyAdapter {
   private final GameControl control;
   
   public MyKeyListener(GameControl control) {
      this.control = control;
   }
   
   //Called when a key is pressed.
   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 37) {
         control.undo();
      } else if (e.getKeyCode() == 39) {
         control.redo();
      } else if (e.getKeyCode() == 10) {
         control.reset();
      }
   }
}