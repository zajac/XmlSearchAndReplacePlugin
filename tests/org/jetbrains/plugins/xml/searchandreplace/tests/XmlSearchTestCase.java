package org.jetbrains.plugins.xml.searchandreplace.tests;

import com.intellij.codeInsight.CodeInsightTestCase;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.TagSearchObserver;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.Not;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.TagPredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.XmlElementPredicate;

import java.util.Arrays;
import java.util.HashSet;

public abstract class XmlSearchTestCase extends CodeInsightTestCase {

  protected static class TestSearchResults implements TagSearchObserver {
    HashSet<XmlElement> results = new HashSet<XmlElement>();

    public void elementFound(XmlElement tag) {
      results.add(tag);
    }
  }

  private Pattern pattern;

  protected XmlSearchTestCase(Pattern pattern) {
    this.pattern = pattern;
  }

  protected static HashSet<Node> l(Node... nodes) {
    return new HashSet<Node>(Arrays.asList(nodes));
  }

  protected static XmlElementPredicate notTag(String tag) {
    return new Not(tag(tag));
  }

  protected static XmlElementPredicate tag(final String tagName) {
    return new TagPredicate() {

      public String toString() {
        return tagName;
      }

      @Override
      public boolean applyToTag(XmlTag tag) {
        return tag.getName().equals(tagName);
      }
    };
  }

  protected TestSearchResults match(String xml) throws Throwable {
    PsiFile testFile = configureByText(XmlFileType.INSTANCE, xml);
    assertTrue(testFile instanceof XmlFile);
    XmlFile xmlFile = (XmlFile)testFile;
    TestSearchResults results = new TestSearchResults();
    pattern.match(xmlFile.getRootTag(), results);
    return results;
  }
}
