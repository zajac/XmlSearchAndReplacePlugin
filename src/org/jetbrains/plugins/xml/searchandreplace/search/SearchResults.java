package org.jetbrains.plugins.xml.searchandreplace.search;

import com.intellij.psi.xml.XmlElement;

import java.util.Collection;
import java.util.Iterator;


public class SearchResults<T extends Collection<XmlElement>> implements Collection<XmlElement>, TagSearchObserver {

    private T found;

    public SearchResults(T found) {
        this.found = found;
    }

    public void elementFound(Search search, XmlElement tag) {
        if (found != null) {
            found.add(tag);
        }
    }

    public T getFound() {
        return found;
    }

    public int size() {
        return found.size();
    }

    public boolean isEmpty() {
        return found.isEmpty();
    }

    public boolean contains(Object o) {
        return found.contains(o);
    }

    public Iterator<XmlElement> iterator() {
        return found.iterator();
    }

    public Object[] toArray() {
        return found.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return found.toArray(a);
    }

    public boolean add(XmlElement xmlElement) {
        return found.add(xmlElement);
    }

    public boolean remove(Object o) {
        return found.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return found.containsAll(c);
    }

    public boolean addAll(Collection<? extends XmlElement> c) {
        return found.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return found.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return found.retainAll(c);
    }

    public void clear() {
        found.clear();
    }

    public boolean equals(Object o) {
        return found.equals(o);
    }

    public int hashCode() {
        return found.hashCode();
    }
}
