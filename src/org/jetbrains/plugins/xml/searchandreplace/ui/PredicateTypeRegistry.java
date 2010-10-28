package org.jetbrains.plugins.xml.searchandreplace.ui;

import java.util.ArrayList;
import java.util.List;


public class PredicateTypeRegistry {

    private static PredicateTypeRegistry ourInstance = new PredicateTypeRegistry();

    private List<PredicateType> predicateTypes = new ArrayList<PredicateType>();

    public List<PredicateType> getPredicateTypes() {
        return predicateTypes;
    }

    public static PredicateTypeRegistry getInstance() {
        return ourInstance;
    }

    private PredicateTypeRegistry() {
        registerPredicateType(new Inside());
        registerPredicateType(new NotInside());
        registerPredicateType(new Contains());
        registerPredicateType(new NotContains());
    }

    public void registerPredicateType(PredicateType predicateType) {
        predicateTypes.add(predicateType);
    }
}
