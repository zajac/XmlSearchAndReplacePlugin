package org.jetbrains.plugins.xml.searchandreplace.tests;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class MergePatternsBugTestCase extends XmlSearchTestCase {

  private static Pattern createPattern() {
    Node n1 = new Node(tag("TAG1"), false);
    Node n2 = new Node(tag("t"), true);
    Node n3 = new Node(tag("TAG"), false);
    n1.setChildren(l(n2, n3));
    return new Pattern(l(n1, n2, n3));
  }

  public MergePatternsBugTestCase() {
    super(createPattern());
  }

  public void testMergeBug() throws Throwable {
    assertTrue(match("<TAG1>\n" +
            "    <t/>\n" +
            "    <TAG>\n" +
            "        <t/>\n" +
            "    </TAG>\n" +
            "</TAG1>").results.size() == 2);
  }
}

