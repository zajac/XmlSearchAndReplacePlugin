package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.util.containers.FilteringIterator;

import java.util.*;

public class Pattern implements Cloneable {

  private HashSet<Node> roots;
  private HashMap<Node, Integer> parentsNum;

  private Node theOne;

  private HashMap<Node, XmlElement> matchedNodes = new HashMap<Node, XmlElement>();

  private HashSet<Node> unmatchedNodes;

  public Pattern(HashSet<Node> nodes) {
    unmatchedNodes = nodes;
    validateNodes();
    for (Node n : unmatchedNodes) {
      if (n.isTheOne()) {
        theOne = n;
        break;
      }
    }
  }

  public Node getTheOne() {
    return theOne;
  }

  public Set<Node> getUnmatchedNodes() {
    return unmatchedNodes;
  }

  private Iterable<Node> unmatchedChildrenOfNode(final Node n) {
    return new Iterable<Node>() {
      public Iterator<Node> iterator() {
        return new FilteringIterator<Node, Node>(n.getChildren().iterator(), new Condition<Node>() {
          public boolean value(Node node) {
            return unmatchedNodes.contains(node);
          }
        });
      }
    };
  }

  private int getParentsNum(Node n) {
    Integer num = parentsNum.get(n);
    return num == null ? 0 : num;
  }

  private void setParentsNum(Node n, int num) {
    parentsNum.put(n, num);
  }

  public void validateNodes() {
    roots = (HashSet<Node>) unmatchedNodes.clone();
    parentsNum = new HashMap<Node, Integer>();
    for (Node n : unmatchedNodes) {
      matchedNodes.remove(n);
      if (n.isTheOne()) {
        theOne = n;
      }
      for (Node c : unmatchedChildrenOfNode(n)) {
        roots.remove(c);
        setParentsNum(c, getParentsNum(c) + 1);
      }
    }
  }

  @Override
  public String toString() {
    return "\nPattern{" +
                   "\nroots=" + roots +
                   ",\n parentsNum=" + parentsNum +
                   ",\n theOne=" + theOne +
                   ",\n candidate=" + getCandidate() +
                   ",\n unmatchedNodes=" + unmatchedNodes +
                   "}\n";
  }

  public final Pattern clone() {
    Pattern result = null;
    try {
      result = (Pattern) super.clone();
      result.theOne = theOne;
      result.roots = (HashSet<Node>) roots.clone();
      result.parentsNum = (HashMap<Node, Integer>) parentsNum.clone();
      result.unmatchedNodes = (HashSet<Node>) unmatchedNodes.clone();
      result.matchedNodes = (HashMap<Node, XmlElement>) matchedNodes.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return result;
  }

  private void rm(Pattern other) {
    assert getCandidate() == null || other.getCandidate() == null || other.getCandidate() == getCandidate() : "FUCKEN FUCK!!";
    boolean changed, onceChanged = false;
    do {
      changed = false;
      for (Node n : unmatchedNodes) {
        if (!other.unmatchedNodes.contains(n)) {
          unmatchedNodes.remove(n);
          matchedNodes.put(n, other.matchedNodes.get(n));
          changed = true;
          onceChanged = true;
          break;
        }
      }
    } while (changed);
    if (onceChanged) {
      validateNodes();
    }
  }

  private void removeRoot(Node root) {
    roots.remove(root);
    for (Node child : unmatchedChildrenOfNode(root)) {
      setParentsNum(child, getParentsNum(child) - 1);
      if (getParentsNum(child) == 0) {
        roots.add(child);
      }
    }
    unmatchedNodes.remove(root);
  }

  private void repair(Node n, boolean needValidate) {
    unmatchedNodes.add(n);
    for (Node c : n.getChildren()) {
      repair(c, false);
    }
    if (needValidate) {
      validateNodes();
    }
  }

  private Pattern repaired() {
    Pattern result = clone();
    result.repair(theOne, true);
    return result;
  }

  private void replaceRoot(Node root, Node newRoot) {
    if (roots.contains(root)) {
      removeRoot(root);
      unmatchedNodes.add(newRoot);
      roots.add(newRoot);
      for (Node child : unmatchedChildrenOfNode(newRoot)) {
        if (roots.contains(child)) {
          roots.remove(child);
        }
        setParentsNum(child, getParentsNum(child) + 1);
      }
    }
  }

  private static boolean isEmpty(Iterable i) {
    for (Object o : i) {
      return false;
    }
    return true;
  }

  private boolean reduce(XmlElement tag, Node node) {
    if (node.apply(tag)) {
      matchedNodes.put(node, tag);
      if (node.isNot()) {
        Set<Node> successfullChildren = new HashSet<Node>();
        for (Node child : unmatchedChildrenOfNode(node)) {
          if (reduce(tag, child)) {
            successfullChildren.add(child);
          }
        }
        Node newRoot = node.mutableNotNodeInstanceByRemovingChildren(successfullChildren);
        if (isEmpty(unmatchedChildrenOfNode(newRoot))) {
          removeRoot(node);
          return true;
        } else {
          replaceRoot(node, newRoot);
        }

      } else {
        removeRoot(node);
        return true;
      }
    } else if (node.isNot()) {
      replaceRoot(node, node.neverSuccessfullNode());
    }
    return false;
  }

  private Pattern reduced(XmlElement tag) {
    Pattern reduced = this.clone();
    for (Node root : roots) {
      reduced.reduce(tag, root);
    }
    return reduced;
  }

  public HashMap<Node, XmlElement> getMatchedNodes() {
    return matchedNodes;
  }

  private interface Criterium<T> {
    boolean value(T t1, T t2);
  }

  private static <T> Collection<Set<T>> classify(Collection<T> collection, Criterium<T> f) {
    Map<T, Set<T>> result = new HashMap<T, Set<T>>();
    for (T element : collection) {
      boolean found = false;
      for (T otherElement : result.keySet()) {
        if (f.value(element, otherElement)) {
          result.get(otherElement).add(element);
          found = true;
        }
      }
      if (!found) {
        HashSet<T> value = new HashSet<T>();
        value.add(element);
        result.put(element, value);
      }
    }
    return result.values();
  }

  private static Collection<Set<Pattern>> classify(Set<Pattern> patterns) {
//    return classify(patterns, new Criterium<Pattern>() {
//      public boolean value(Pattern t1, Pattern t2) {
//        return t1.getCandidate() == t2.getCandidate() || t1.getCandidate() == null || t2.getCandidate() == null;
//      }
//    });

    Map<XmlElement, Set<Pattern>> sort = new HashMap<XmlElement, Set<Pattern>>();
    for (Pattern p : patterns) {
      Set<Pattern> s = sort.get(p.getCandidate());
      if (s == null) {
        s = new HashSet<Pattern>();
      }
      s.add(p);
      sort.put(p.getCandidate(), s);
    }
    for (Pattern p : patterns) {
      if (p.getCandidate() == null) {
        Set<XmlElement> keySet = sort.keySet();
        if (keySet.isEmpty()) {
          sort.put(null, new HashSet<Pattern>());
        }
        for (XmlElement key : sort.keySet()) {
          sort.get(key).add(p);
        }
      }
    }
    return sort.values();
  }

  private static Set<Pattern> mergePatterns(Set<Pattern> patterns) {
    Collection<Set<Pattern>> sort = classify(patterns);
    Set<Pattern> result = new HashSet<Pattern>();
    for (Set<Pattern> patternsClass : sort) {
      Pattern newPattern = null;
      for (Pattern p : patternsClass) {
        if (p.getCandidate() != null) {
          newPattern = p;
        }
      }
      for (Pattern p : patternsClass) {
        if (p == newPattern) {
          continue;
        }
        if (newPattern == null) {
          newPattern = p;
        } else {
          newPattern.rm(p);
        }
      }
      if (newPattern != null) {
        result.add(newPattern);
      }
    }
    return result;
  }

  private boolean isEmptyOrContainsOnlyNot() {
    for (Node n : unmatchedNodes) {
      if (!(n instanceof Node.NeverSuccessfullNode)) {
        return false;
      }
    }
    return true;
  }

  private static Set<Pattern> matchChildren(XmlElement element, TagSearchObserver observer, Set<Pattern> patternSet) {
    Set<Pattern> afterChildrenMatching = new HashSet<Pattern>();
    for (PsiElement c : element.getChildren()) {
      if (c instanceof XmlTag || c instanceof XmlText) {
        XmlElement child = (XmlElement) c;
        for (Pattern p : patternSet) {
          Set<Pattern> matchChildResult = p.match(child, observer);
          afterChildrenMatching.addAll(matchChildResult);
        }
      }
    }
    if (afterChildrenMatching.isEmpty()) {
      return patternSet;
    }
    return mergePatterns(afterChildrenMatching);
  }

  private boolean isReducableBy(XmlElement tag) {
    for (Node root : roots) {
      if (root.apply(tag)) {
        return true;
      }
      if (root.isNot()) {
        return true;
      }
    }
    return false;
  }

  public Set<Pattern> match(XmlElement element, TagSearchObserver observer) {
    Pattern reduced = this;
    if (isReducableBy(element)) {
      reduced = reduced(element);
    }
    Set<Pattern> forFurtherMatching = new HashSet<Pattern>();
    forFurtherMatching.add(reduced);
    if (reduced.matchedNodes.containsKey(theOne) && matchedNodes.containsKey(theOne)) {
      Pattern repaired = reduced.repaired();
      repaired.reduce(element, repaired.getTheOne());
      forFurtherMatching.add(repaired);
    }
    Set<Pattern> result = matchChildren(element, observer, forFurtherMatching);
    for (Pattern p : result) {
      if (p.isEmptyOrContainsOnlyNot()) {
        if (p.getCandidate() != null) {
          observer.elementFound(p, p.getCandidate());
        }
      }
    }
    return result;
  }

  private XmlElement getCandidate() {
    return matchedNodes.get(theOne);
  }
}
