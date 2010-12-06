package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.intellij.plugins.xpathView.search.SearchScope;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PatternController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@State(
  name="XmlSearchAndReplace.PatternsStorage",
  storages= {
    @Storage(
      id="other",
      file = "$WORKSPACE_FILE$"
    )}
)
public class PatternsStorage implements PersistentStateComponent<PatternsStorageState> {

  public interface PatternsStorageListener {
    void storageStateChanged();
  }

  private List<PatternsStorageListener> listeners = new ArrayList<PatternsStorageListener>();

  public void addListener(PatternsStorageListener patternsStorageListener) {
    listeners.add(patternsStorageListener);
  }

  public void removeListener(PatternsStorageListener patternsStorageListener) {
    listeners.remove(patternsStorageListener);
  }

  private void fireChange() {
    for (PatternsStorageListener listener : listeners) {
      listener.storageStateChanged();
    }
  }

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
  public PatternsStorageState getState() {
    if (recent == null) return null;
    PatternsStorageState state = new PatternsStorageState();
    state.setRecent(recent.getState());
    state.setScope(recentScope);
    return state;
  }

  @Override
  public void loadState(PatternsStorageState state) {
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

  private GlobalPatternsStorage getGlobalStorage() {
    return ServiceManager.getService(GlobalPatternsStorage.class);
  }

  public SearchScope getRecentScope() {
    return recentScope;
  }

   public Set<String> getSavedPatternsNames() {
     GlobalPatternsStorage globalStorage = getGlobalStorage();
     if (globalStorage == null) return null;
     return globalStorage.getSavedPatternsNames();
   }

  public PatternController load(String name) {
    GlobalPatternsStorage globalStorage = getGlobalStorage();
     if (globalStorage == null) return null;
    return globalStorage.load(name, project);
  }

  public void saveAs(PatternController patternController, String name) {
    GlobalPatternsStorage globalStorage = getGlobalStorage();
    if (globalStorage == null) return;
    globalStorage.saveAs(patternController, name);
    fireChange();
  }

  public void delete(String name) {
    GlobalPatternsStorage globalStorage = getGlobalStorage();
    if (globalStorage == null) return;
    globalStorage.delete(name);
    fireChange();
  }

}
