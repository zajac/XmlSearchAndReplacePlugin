package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;

import java.util.Arrays;

public abstract class ReplacementProvider {
    public abstract XmlElement getReplacementFor(XmlElement element);

    protected boolean isValid(XmlElement element) {
        return element.getParent() != null &&
                element.getParent().getChildren() != null &&
                Arrays.asList(element.getParent().getChildren()).indexOf(element) != -1;
     }
}
