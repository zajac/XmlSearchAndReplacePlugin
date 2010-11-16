package org.jetbrains.plugins.xml.searchandreplace.tests;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class OneComplicatedTest extends XmlSearchTestCase {
  private static Pattern createPattern() {
    Node n0 = new Node(tag("TAG"), false);
    Node n1 = new Node(tag("TAG3"), false);
    Node n2 = new Node(tag("TAG4"), false);
    Node n = new Node(tag("BAZ"), true);
    n0.setChildren(l(n1, n, n2));
    return new Pattern(l(n0, n1, n2, n));
  }

  public OneComplicatedTest() {
    super(createPattern());
  }

  public void testBazShouldBeFound() throws Throwable {
    assertTrue(match("<TAG>\n" +
            "    someTag:\n" +
            "    <someTag>\n" +
            "        Hey you!\n" +
            "        <someChild>onceMore text</someChild>\n" +
            "    </someTag>\n" +
            "    someTag:\n" +
            "    <someTag>\n" +
            "        Hey you!\n" +
            "        <someChild>onceMore text</someChild>\n" +
            "    </someTag>\n" +
            "    <BAZ/>\n" +
            "    <TAG3>\n" +
            "        <TAG5/>\n" +
            "    </TAG3>\n" +
            "    <TAG4>\n" +
            "    </TAG4>\n" +
            "\n" +
            "</TAG>").results.size() == 1);
  }
}
