/* 
An Intersection is a point on which a piece may be played.
Intersections have a location (row and column) and the color of the piece it contains.
Color is represented as follows:
0: no piece
1: black piece
2: white piece
3: red piece (only in 3- or 4-player mode)
4: blue piece (only in 4-player mode)
*/

public class Intersection {
   private final int row;
   private final int column;
   private int color;
   
   //Constructs an Intersection with a specified location and no existing piece
   public Intersection(int row, int column) {
      this.row = row;
      this.column = column;
      this.color = 0;
   }
   
   //Constructs an Intersection with a specified location and an existing piece
   public Intersection(int row, int column, int color) {
      this.row = row;
      this.column = column;
      this.color = color;
   }
   
   //Returns the color of the current piece as a String. 
   //Used to create key Strings for the AI and for victory detection.
   @Override
   public String toString() {
      return "" + color;
   }
   
   //Returns the row.
   public int getRow() {
      return row;
   }
   
   //Returns the column.
   public int getColumn() {
      return column;
   }
   
   //Returns the color of the current piece.
   public int getColor() {
      return color;
   }
   
   //Sets the color of the occupying piece.
   public void setColor(int color) {
      this.color = color;
   }
}