package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence;

import java.util.HashMap;
import java.util.Map;

public class GlobalPatternsStorageState {

  private Map<String, PatternStorageEntry> savedPatterns = new HashMap<String, PatternStorageEntry>();

  public Map<String, PatternStorageEntry> getSavedPatterns() {
    return savedPatterns;
  }

  public void setSavedPatterns(Map<String, PatternStorageEntry> savedPatterns) {
    this.savedPatterns = savedPatterns;
  }
}
