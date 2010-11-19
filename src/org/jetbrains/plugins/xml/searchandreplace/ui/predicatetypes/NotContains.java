package org.jetbrains.plugins.xml.searchandreplace.ui.predicatetypes;


import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.ui.PredicateType;

import java.util.ArrayList;
import java.util.List;

public class NotContains extends Contains {
  public NotContains() {
    super(Params.NOT);
  }

  @Override
  public List<PredicateType> getAllowedChildrenTypes() {
    return new ArrayList<PredicateType>();
  }

  @Override
  public Node addNodeToPattern(Pattern p, Node node, Node parent) {
    return super.addNodeToPattern(p, node, parent);
//    Node n = new Node(new XmlElementPredicate() {
//      @Override
//      public boolean apply(XmlElement element) {
//        return (element instanceof XmlTag) && ((XmlTag) element).getSubTags().length == 0;
//      }
//    }, false);
//    node.getChildren().add(n);
//    p.getUnmatchedNodes().add(n);
  }
}
