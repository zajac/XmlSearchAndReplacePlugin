package org.jetbrains.plugins.xml.searchandreplace.replace;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.Capture;

import java.awt.*;

public class CapturePresentation {

  private String identifier;

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

  private Capture capture;

  public Capture getCapture() {
    return capture;
  }

  public void setCapture(Capture capture) {
    this.capture = capture;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }
}
