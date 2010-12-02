package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.intellij.plugins.xpathView.search.SearchScope;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PatternController;

@State(
  name="XmlSearchAndReplace.PatternsStorage",
  storages= {
    @Storage(
      id="other",
      file = "$WORKSPACE_FILE$"
    )}
)
public class PatternsStorage implements PersistentStateComponent<PatternsStorageEntry> {

  private PatternController recent;
  private SearchScope recentScope;
  private Project project;

  public PatternsStorage(Project project) {
    this.project = project;
  }

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
    state.setScope(recentScope);
    return state;
  }

  @Override
  public void loadState(PatternsStorageEntry state) {
    recent = new PatternController(project);
    recentScope = state.getScope();
    recent.loadState(state.getRecent());
  }

  public static PatternsStorage getInstance(Project project) {
    return ServiceManager.getService(project, PatternsStorage.class);
  }

  public void setRecentScope(SearchScope recentScope) {
    this.recentScope = recentScope;
  }

  public SearchScope getRecentScope() {
    return recentScope;
  }
}
