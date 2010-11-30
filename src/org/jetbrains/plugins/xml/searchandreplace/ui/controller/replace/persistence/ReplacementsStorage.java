package org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.persistence;

import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.replace.ReplaceController;


@State(
  name="XmlSearchAndReplace.ReplacementsStorage",
  storages= {
    @Storage(
      id="other",
      file = "$WORKSPACE_FILE$"
    )}
)
public class ReplacementsStorage implements PersistentStateComponent <ReplacementStorageEntry> {

  private ReplaceController recent;
  private Project project;

  public Project getProject() {
    return project;
  }

  public ReplaceController getRecent() {
    return recent;
  }

  public static ReplacementsStorage getInstance(Project project) {
    return ServiceManager.getService(project, ReplacementsStorage.class);
  }

  public ReplacementsStorage(Project project) {
    this.project = project;
  }

  @Override
  public ReplacementStorageEntry getState() {
    if (recent == null) {
      return null;
    }
    ReplacementStorageEntry state = new ReplacementStorageEntry();
    state.setRecent(recent.getState());
    return state;
  }

  @Override
  public void loadState(ReplacementStorageEntry state) {
    if (state.getRecent() == null) return;
    recent = new ReplaceController(project, XMLLanguage.INSTANCE);
    recent.loadState(state.getRecent());
  }

  public void setRecent(ReplaceController recent) {
    this.recent = recent;
  }
}
