package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence;

import org.intellij.plugins.xpathView.search.SearchScope;

public class PatternsStorageState {
  private PatternStorageEntry recent;



  public void setScope(SearchScope scope) {
    this.scope = scope;
  }

  private SearchScope scope;

  public PatternStorageEntry getRecent() {
    return recent;
  }

  public void setRecent(PatternStorageEntry recent) {
    this.recent = recent;
  }

  public SearchScope getScope() {
    return scope;
  }

}
