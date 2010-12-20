package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence;


public class PatternsStorageState {
  private PatternStorageEntry recent;

  public PatternStorageEntry getRecent() {
    return recent;
  }

  public void setRecent(PatternStorageEntry recent) {
    this.recent = recent;
  }

}
