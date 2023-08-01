package thowl.model;

public class Systeminfo {

  public static String javaVersion() {
    return System.getProperty("java.version");
  }

  public static String javafxVersion() {
    return System.getProperty("javafx.version");
  }
}
