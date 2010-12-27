package org.jetbrains.plugins.xml.searchandreplace.ui;

import com.intellij.ui.UserActivityWatcher;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;


public class MomentoUserActivityWatcher extends UserActivityWatcher {

  Set<Component> components = new HashSet<Component>();

  @Override
  protected void processComponent(Component parentComponent) {
    super.processComponent(parentComponent);
    components.add(parentComponent);
  }

  @Override
  protected void unprocessComponent(Component component) {
    components.remove(component);
    super.unprocessComponent(component);
  }

  public boolean isWatched(Component c) {
    return components.contains(c);
  }
}
