package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;

import java.util.List;

public abstract class PredicateType {

  public abstract PredicateTypeController createNewController();

  public abstract void addNodeToPattern(Pattern p, Node node, Node parent);

  public List<PredicateType> getAllowedChildrenTypes() {
    return PredicateTypeRegistry.getInstance().getPredicateTypes();
  }
}
