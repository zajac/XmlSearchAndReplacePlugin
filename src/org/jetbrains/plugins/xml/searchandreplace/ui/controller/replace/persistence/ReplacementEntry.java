package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.persistence;

import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.ReplacementControllerState;

public class ReplacementEntry {

  private String replacementName = "";

  public void setReplacementControllerIndex(int replacementControllerIndex) {
    this.replacementControllerIndex = replacementControllerIndex;
  }

  private int replacementControllerIndex;

  public ReplacementControllerState getReplacementControllerState() {
    return replacementControllerState;
  }

  private ReplacementControllerState replacementControllerState;

  public String getReplacementName() {
    return replacementName;
  }

  public void setReplacementName(String replacementName) {
    this.replacementName = replacementName;
  }

  public void setReplacementControllerState(ReplacementControllerState replacementControllerState) {
    this.replacementControllerState = replacementControllerState;
  }

  public int getReplacementControllerIndex() {
    return replacementControllerIndex;
  }
}
