package org.jetbrains.plugins.xml.searchandreplace.tests;

import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.And;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.AnyTag;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.AttributePredicate;
import org.jetbrains.plugins.xml.searchandreplace.search.predicates.HasSpecificAttribute;

public class WhiteSpaceMatchingWithNotPredicateTest extends XmlSearchTestCase {

  private static Pattern createPattern() {
    Node n = new Node(tag("A"), true);
    Node n1 = new Node(new And(new AnyTag(), new HasSpecificAttribute(new AttributePredicate() {
      @Override
      public boolean applyToAttribute(XmlAttribute a) {
        return a.getName().equals("attr") && a.getValue().equals("val");
      }
    })));
    Node n2 = new Node(notTag("abacaba"));
    n.setChildren(l(n1, n2));
    return new Pattern(l(n, n1, n2));
  }

  public WhiteSpaceMatchingWithNotPredicateTest() {
    super(createPattern());
  }

  public void testThis() throws Throwable {
    assertTrue(match("<A>\n" +
            "    <B attr=\"val\"/>\n" +
            "</A>").results.size() == 1);
  }
}
