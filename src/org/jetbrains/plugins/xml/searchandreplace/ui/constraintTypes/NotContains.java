package org.jetbrains.plugins.xml.searchandreplace.ui.constraintTypes;


import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class NotContains extends Contains {
  public NotContains() {
    super(Params.NOT);
  }


  @Override
  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    return super.addNodeToPattern(p, node, parent);
  }
}
