package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import javax.swing.*;

public abstract class PredicateTypeController {
    public abstract JPanel getView();
    public abstract XmlElementPredicate buildPredicate();
    public boolean canBeParentOf(XmlElementPredicate predicate) {return !(predicate instanceof Not);}
    public boolean canBeChildOf(XmlElementPredicate predicate) {return true;}
}
