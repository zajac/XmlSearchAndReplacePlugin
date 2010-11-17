package org.jetbrains.plugins.xml.searchandreplace.ui;

import java.awt.*;

public class CapturePresentation {
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Color getTextColor() {
    return textColor;
  }

  public void setTextColor(Color textColor) {
    this.textColor = textColor;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  private String name;
  private Color textColor;
  private Color backgroundColor;
}
