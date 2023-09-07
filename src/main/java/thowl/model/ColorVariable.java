package thowl.model;

import javafx.scene.paint.Color;

/**
 * Class is unused because the allocation of Colors doesnt work as i like it to. If used it gets and
 * sets Colors to a variable used to change the GridColor.
 *
 * @autor Dylan Senger
 */
public class ColorVariable {

  private Color color1;
  private Color color2;

  public synchronized Color getColor1() {
    return color1;
  }

  public synchronized void setColor1(Color color1) {
    this.color1 = color1;
  }

  public synchronized Color getColor2() {
    return color2;
  }

  public synchronized void setColor2(Color color2) {
    this.color2 = color2;
  }
}
