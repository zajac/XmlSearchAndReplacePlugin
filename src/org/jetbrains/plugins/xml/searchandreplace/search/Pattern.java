package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.containers.FilteringIterator;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.*;

public class Pattern implements Cloneable {

    public static class Node {
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

        public String toString() {
            return predicate.toString();
        }
    }

    private HashSet<Node> roots = new HashSet<Node>();
    private HashMap<Node, Integer> parentsNum = new HashMap<Node, Integer>();
    private Node theOne;
    private XmlElement candidate;
    private HashSet<Node> allNodes = new HashSet<Node>();

    public Pattern(HashSet<Node> nodes) {
        allNodes = nodes;
        nodesChanged();
        for (Node n : allNodes) {
            if (n.isTheOne()) {
                theOne = n;
                break;
            }
        }
    }

    private Iterable<Node> childrenOfNode(final Node n) {
        return new Iterable<Node>() {
            public Iterator<Node> iterator() {
                return new FilteringIterator<Node, Node>(n.children.iterator(), new Condition<Node>() {
                    public boolean value(Node node) {
                        return allNodes.contains(node);
                    }
                });
            }
        };
    }

    private int getParentsNum(Node n) {
        Integer num = parentsNum.get(n);
        return num == null ? 0 : num;
    }

    private void setPrentsNum(Node n, int num) {
        parentsNum.put(n, num);
    }

    private void nodesChanged() {
        roots = (HashSet<Node>) allNodes.clone();
        parentsNum = new HashMap<Node, Integer>();
        for (Node n : allNodes) {
            for (Node c : childrenOfNode(n)) {
                roots.remove(c);
                setPrentsNum(c, getParentsNum(c) + 1);
            }
        }
    }

    public final Pattern clone() {
        Pattern result = null;
        try {
            result = (Pattern) super.clone();
            result.candidate = candidate;
            result.theOne = theOne;
            result.roots = (HashSet<Node>) roots.clone();
            result.parentsNum = (HashMap<Node, Integer>) parentsNum.clone();
            result.allNodes = (HashSet<Node>) allNodes.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void rm(Pattern other) {
        assert candidate == null || other.candidate == null || other.candidate == candidate : "FUCKEN FUCK!!";
        boolean changed, onceChanged = false;
        do {
            changed = false;
            for (Node n : allNodes) {
                if (!other.allNodes.contains(n)) {
                    allNodes.remove(n);
                    changed = true;
                    onceChanged = true;
                    break;
                }
            }
        } while (changed);
        if (onceChanged) {
            nodesChanged();
        }
    }

    private void removeRoot(Node root) {
        roots.remove(root);
        for (Node child : childrenOfNode(root)) {
            setPrentsNum(child, getParentsNum(child) - 1);
            if (getParentsNum(child) == 0) {
                roots.add(child);
            }
        }
        allNodes.remove(root);
    }

    private boolean repair(Node n) {
        boolean changed = false;
        for (Node c : childrenOfNode(n)) {
            if (!allNodes.contains(c)) {
                allNodes.add(c);
                repair(c);
                changed = true;
            }
        }
        return changed;
    }

    private Pattern repair() {
        Pattern result = clone();
        if (result.repair(theOne)) {
            result.nodesChanged();
        }
        return result;
    }

    private Pattern reduce(XmlElement tag) {
        Pattern reduced = this.clone();
        for (Node root : roots) {
            if (root.getPredicate().apply(tag)) {
                if (!(root.getPredicate() instanceof Not)) {
                    reduced.removeRoot(root);
                } else {
                    assert false;
                }
                //TODO handle case of "Not" predicate instance
                if (root.isTheOne()) {
                    reduced.candidate = tag;
                }
            }
        }
        return reduced;
    }

    private static Map<XmlElement, Set<Pattern>> classifyByCandidate(Set<Pattern> afterChildrenMatching) {
        Map<XmlElement, Set<Pattern>> sort = new HashMap<XmlElement, Set<Pattern>>();
        for (Pattern p : afterChildrenMatching) {
            if (p.candidate != null) {
                Set<Pattern> s = sort.get(p.candidate);
                if (s == null) {
                    s = new HashSet<Pattern>();
                }
                s.add(p);
                sort.put(p.candidate, s);
            }
        }
        for (Pattern p : afterChildrenMatching) {
            if (p.candidate == null) {
                for (XmlElement key : sort.keySet()) {
                    sort.get(key).add(p);
                }
            }
        }
        return sort;
    }
    private static Set<Pattern> mergeClassifiedPatterns(TagSearchObserver observer, Map<XmlElement, Set<Pattern>> sort) {
        Set<Pattern> result = new HashSet<Pattern>();
        for (XmlElement aCandidate : sort.keySet()) {
            Set<Pattern> patterns = sort.get(aCandidate);
            Pattern newPattern = null;
            for (Pattern p : patterns) {
                if (newPattern == null) {
                    newPattern = p;
                } else {
                    newPattern.rm(p);
                }
            }
            if (newPattern != null) {
                if (newPattern.roots.isEmpty()) {
                    observer.elementFound(aCandidate);
                } else {
                    result.add(newPattern);
                }
            }
        }
        return result;
    }

    private static Set<Pattern> matchChildren(XmlElement element, TagSearchObserver observer, Set<Pattern> patternSet) {
        Set<Pattern> afterChildrenMatching = new HashSet<Pattern>();
        for (PsiElement c : element.getChildren()) {
            if (c instanceof XmlElement) {
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
        return mergeClassifiedPatterns(observer, classifyByCandidate(afterChildrenMatching));
    }

    public Set<Pattern> match(XmlElement element, TagSearchObserver observer) {
        Pattern reduced = this.reduce(element);
        Set<Pattern> forFurtherMatching = new HashSet<Pattern>();
        forFurtherMatching.add(reduced);
        if (reduced.candidate != element && getParentsNum(theOne) == 0 && theOne.predicate.apply(element)) {
            Pattern repaired = reduced.repair();
            repaired.candidate = element;
            forFurtherMatching.add(repaired);
        }
        return matchChildren(element, observer, forFurtherMatching);
    }
}
