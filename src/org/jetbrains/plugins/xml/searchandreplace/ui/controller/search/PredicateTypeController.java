package org.jetbrains.plugins.xml.searchandreplace.ui.controller.search;

import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import javax.swing.*;

public abstract class PredicateTypeController {


    public enum Params {NOT}

    Params p = null;

    public PredicateTypeController() {
    }

    public PredicateTypeController(Params p) {
        this.p = p;
    }

    public abstract JPanel getView();

    protected final XmlElementPredicate decorateWithNotIfNeccessary(XmlElementPredicate predicate) {
        return p == Params.NOT ? new Not(predicate) : predicate;
    }

    public abstract XmlElementPredicate buildPredicate();
    public boolean canBeParentOf(XmlElementPredicate predicate) {return !(predicate instanceof Not);}
    public boolean canBeChildOf(XmlElementPredicate predicate) {return true;}
}
