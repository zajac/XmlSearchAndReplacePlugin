package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.PredicateTypeController;
import org.jetbrains.plugins.xml.searchandreplace.ui.controller.search.TagPredicateController;

public class RootPredicateType extends PredicateType {

  public PredicateTypeController createNewController() {
    return new TagPredicateController();
  }

  public void addNodeToPattern(Pattern p, Node node, Node parent) {
    p.getUnmatchedNodes().add(node);
  }

}
