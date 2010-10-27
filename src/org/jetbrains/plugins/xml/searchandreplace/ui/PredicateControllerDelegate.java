package org.jetbrains.plugins.xml.searchandreplace.ui;

import java.util.List;

public interface PredicateControllerDelegate {
    void addChild(PredicateController predicateController);
    List<PredicateType> getAllowedPredicateTypes(PredicateController predicateController);
}
