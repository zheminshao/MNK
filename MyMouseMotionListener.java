/*
A MyMouseMotionListener responds to mouse movements.
Used for mouse hover actions over potential moves, as well as buttons.
*/

import java.awt.event.*;

public class MyMouseMotionListener extends MouseMotionAdapter {
   private final GameControl control;
   private final int rows;
   private final int columns;
   
   public MyMouseMotionListener(GameControl control, int rows, int columns) {
      this.control = control;
      this.rows = rows;
      this.columns = columns;
   }
   
   //Called when the mouse is moved.
   @Override
   public void mouseMoved(MouseEvent e) {
      int x = e.getX() - 8;
      int y = e.getY() - 31;
      control.mouseResponse(x, y);
   }
}