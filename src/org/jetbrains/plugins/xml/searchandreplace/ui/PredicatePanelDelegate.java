package org.jetbrains.plugins.xml.searchandreplace.ui;

import java.util.List;

public interface PredicatePanelDelegate {
    void addChild(PredicatePanel panel);
    List<PredicateType> getPredicateTypes(PredicatePanel panel);

    void predicateTypeSelected(PredicatePanel panel, PredicateType selection);
}
