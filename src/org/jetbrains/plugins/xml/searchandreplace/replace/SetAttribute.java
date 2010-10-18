package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;

public class SetAttribute extends ReplacementProvider {
    private String name;
    private String value;

    public SetAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public XmlElement getReplacementFor(XmlElement element) {
        if (isValid(element) && element instanceof XmlTag) {
            XmlTag tag = (XmlTag) element;
            tag.setAttribute(name, value);
        }
        return element;
    }
}
