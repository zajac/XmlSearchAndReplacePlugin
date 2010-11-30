package org.jetbrains.plugins.xml.searchandreplace.persistence;

public class PatternsStorageEntry {
  private PatternStorageEntry recent;

  public PatternStorageEntry getRecent() {
    return recent;
  }

  public void setRecent(PatternStorageEntry recent) {
    this.recent = recent;
  }
}
