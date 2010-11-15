package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.xml.XmlElement;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.HashSet;
import java.util.Set;

/**
* Created by IntelliJ IDEA.
* User: zajac
* Date: Nov 15, 2010
* Time: 4:04:39 PM
* To change this template use File | Settings | File Templates.
*/
public class Node {
  private XmlElementPredicate predicate;
  private Set<Node> children = new HashSet<Node>();
  private boolean theOne;

  public XmlElementPredicate getPredicate() {
    return predicate;
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

  public void setChildren(Set<Node> children) {
    this.children = children;
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

  public boolean isNot() {
    return predicate instanceof Not;
  }

  public Node mutableNotNodeInstanceByRemovingChildren(Set<Node> successfulChildren) {
    if (successfulChildren.isEmpty()) return this;
    class NotNode extends Node {
      public NotNode(XmlElementPredicate predicate, boolean isTheOne) {
        super(predicate, isTheOne);
      }

      @Override
      public Node mutableNotNodeInstanceByRemovingChildren(Set<Node> successfulChildren) {
        return this;
      }
    }
    NotNode n = new NotNode(predicate, isTheOne());
    Set<Node> newChildrenSet = new HashSet<Node>();
    newChildrenSet.addAll(children);
    newChildrenSet.removeAll(successfulChildren);
    n.setChildren(newChildrenSet);
    return n;
  }

  public void setPredicate(XmlElementPredicate predicate) {
    this.predicate = predicate;
  }

  static class NeverSuccessfullNode extends Node {
    public NeverSuccessfullNode(boolean isTheOne) {
      super(new XmlElementPredicate() {

        @Override
        public boolean apply(XmlElement element) {
          return false;
        }

      }, isTheOne);
    }
  }

  public Node neverSuccessfullNode() {
    NeverSuccessfullNode result = new NeverSuccessfullNode(isTheOne());
    result.setChildren(children);
    return result;
  }
}
