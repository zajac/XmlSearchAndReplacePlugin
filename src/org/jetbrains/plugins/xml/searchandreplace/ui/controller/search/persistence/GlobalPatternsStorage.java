package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.persistence;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PatternController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@State(
  name="XmlSearchAndReplace.GlobalPatternsStorage",
  storages= {
    @Storage(
      id="other",
      file = "$APP_CONFIG$/other.xml"
    )}
)
public class GlobalPatternsStorage implements PersistentStateComponent<GlobalPatternsStorageState> {

  private Map<String, PatternStorageEntry> savedPatterns = new HashMap<String, PatternStorageEntry>();

  @Override
  public GlobalPatternsStorageState getState() {
    GlobalPatternsStorageState result = new GlobalPatternsStorageState();
    result.setSavedPatterns(savedPatterns);
    return result;
  }

  @Override
  public void loadState(GlobalPatternsStorageState state) {
    savedPatterns = state.getSavedPatterns();
  }

  public Set<String> getSavedPatternsNames() {
    return savedPatterns.keySet();
  }

  public PatternController load(String name, Project project) {
    if (!savedPatterns.containsKey(name)) {
      return null;
    }
    PatternController patternController = new PatternController(project);
    patternController.loadState(savedPatterns.get(name));
    return patternController;
  }

  public void saveAs(PatternController patternController, String name) {
    if (name == null || savedPatterns.containsKey(name) || patternController == null) return;
    PatternStorageEntry state = patternController.getState();
    if (state != null) {
      savedPatterns.put(name, state);
    }
  }

  public void delete(String name) {
    savedPatterns.remove(name);
  }
}
