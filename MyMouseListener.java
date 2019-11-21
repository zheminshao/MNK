/*
A MyMouseListener responds to mouse clicks.
It is used for playing pieces, as well as the buttons.
*/

import java.awt.event.*;

public class MyMouseListener extends MouseAdapter {
   private final GameControl control;
   private final int rows;
   private final int columns;
   
   public MyMouseListener(GameControl control, int rows, int columns) {
      this.control = control;
      this.rows = rows;
      this.columns = columns;
   }
   
   //Called when the mouse is clicked.
   @Override
   public void mouseClicked(MouseEvent e) {
      int x = e.getX() - 8;
      int y = e.getY() - 31;
      if (control.getBoard().getBackButton().contains(x, y)) {
         control.undo();
      } else if (control.getBoard().getForwardButton().contains(x, y)) {
         control.redo();
      } else if (control.getBoard().getResetButton().contains(x, y)) {
         control.reset();
      } else if (control.getPieceIndex() == control.getNumPieces() - 1 
         && control.getTurn() != control.getDeepZColor() 
         && control.getTurn() != control.getDeepJColor()) {
         control.play(y / 40, x / 40);
      }
   }
}