package org.jetbrains.plugins.xml.searchandreplace.ui;

import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: Oct 26, 2010
 * Time: 5:05:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PredicateType {

    PredicateTypeController createNewController();

    void addNodeInPattern(Pattern p, Pattern.Node node, Pattern.Node parent);
}
