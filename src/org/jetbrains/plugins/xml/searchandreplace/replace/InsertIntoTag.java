package org.jetbrains.plugins.xml.searchandreplace.replace;

import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

public class InsertIntoTag extends ReplacementProvider {

    private ReplacementProvider replacementProvider;
    private Anchor anchor;

    public enum Anchor {BEGIN, END}

    public InsertIntoTag(@NotNull ReplacementProvider replacementProvider, @NotNull Anchor anchor) {
        this.replacementProvider = replacementProvider;
        this.anchor = anchor;
    }

    public XmlElement getReplacementFor(XmlElement element) {
        if (isValid(element) && element instanceof XmlTag) {
            XmlTag tag = (XmlTag)element;
            XmlElement toInsert = replacementProvider.getReplacementFor(element);
            if (toInsert != null) {
                Utils.insertElementIntoTag(toInsert, tag, anchor == Anchor.BEGIN);
            }
        }
        return element;
    }
}
