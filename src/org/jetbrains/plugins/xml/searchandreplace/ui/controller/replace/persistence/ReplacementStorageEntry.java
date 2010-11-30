package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.persistence;

public class ReplacementStorageEntry {
  private ReplacementEntry recent;

  public ReplacementEntry getRecent() {
    return recent;
  }

  public void setRecent(ReplacementEntry recent) {
    this.recent = recent;
  }
}
