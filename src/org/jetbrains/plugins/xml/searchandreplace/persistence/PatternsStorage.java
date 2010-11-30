package org.jetbrains.plugins.xml.searchandreplace.persistence;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PatternController;

@State(
  name="XmlSearchAndReplace.PatternsStorage",
  storages= {
    @Storage(
      id="other",
      file = "$APP_CONFIG$/other.xml"
    )}
)
public class PatternsStorage implements PersistentStateComponent<PatternsStorageEntry> {

  private PatternController recent;

  public PatternController getRecent() {
    return recent;
  }

  public void setRecent(PatternController recent) {
    this.recent = recent;
  }

  @Override
  public PatternsStorageEntry getState() {
    if (recent == null) return null;
    PatternsStorageEntry state = new PatternsStorageEntry();
    state.setRecent(recent.getState());
    return state;
  }

  @Override
  public void loadState(PatternsStorageEntry state) {
    recent = new PatternController();

    recent.loadState(state.getRecent());
  }
}
