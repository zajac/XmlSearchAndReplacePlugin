package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.False;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.HashSet;
import java.util.Set;

public class Node extends UserDataHolderBase {

  private XmlElementPredicate predicate;
  private Set<Node> children = new HashSet<Node>();
  private Set<Node> parents = new HashSet<Node>();
  private boolean theOne;


  public static class NeverSuccessfull extends Node {

    private Node node;

    public NeverSuccessfull(Node node) {
      super(new False());
      this.node = node;
    }

    public Node getUnderlyingNode() {
      return node;
    }

  }

  public XmlElementPredicate getPredicate() {
    return predicate;
  }

  public Set<Node> getParents() {
    return parents;
  }

  public Set<Node> getChildren() {
    return children;
  }

  public boolean isTheOne() {
    return theOne;
  }

  public Node(XmlElementPredicate predicate, boolean isTheOne) {
    this.predicate = predicate;
    theOne = isTheOne;
  }

  public Node(XmlElementPredicate predicate) {
    this(predicate, false);
  }

  public void setChildren(Set<Node> children) {
    this.children = children;
  }

  public boolean isNot() {
    return predicate instanceof Not;
  }

  @Override
  public String toString() {
    return "\nNode{" +
                   "\npredicate=" + predicate +
                   ",\n children=" + children +
                   ",\n theOne=" + theOne +
                   "\n}";
  }

  public boolean apply(XmlElement tag) {
    return predicate.apply(tag);
  }

  public void setPredicate(XmlElementPredicate predicate) {
    this.predicate = predicate;
  }

  void gatherParents(Set<Node> nodeParents) {
    nodeParents.addAll(getParents());
    for (Node p : getParents()) {
      p.gatherParents(nodeParents);
    }
  }

}
