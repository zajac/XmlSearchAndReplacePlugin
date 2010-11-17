package org.jetbrains.plugins.xml.searchandreplace.tests;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class SimpleButBigTest extends XmlSearchTestCase {

  private static Pattern createPattern() {
    Node n1 = new Node(tag("TAG"), false);
    Node n2 = new Node(tag("TAG5"), false);
    Node n3 = new Node(tag("TAG3"), true);
    Node n4 = new Node(tag("TAG5"), false);
    Node n5 = new Node(tag("TAG4"), false);
    Node n6 = new Node(tag("someTag"), false);
    Node n7 = new Node(tag("someChild"), false);
    Node n8 = new Node(tag("BAZ"), false);
    n1.setChildren(l(n2, n6, n8));
    n2.setChildren(l(n3, n4));
    n4.setChildren(l(n5));
    n6.setChildren(l(n7));
    return new Pattern(l(n1, n2, n3, n4, n5, n6, n7, n8));
  }

  public SimpleButBigTest() {
    super(createPattern());
  }

  public void testThis() throws Throwable {
    assertTrue(match("<TAG2>\n" +
            "    <TAG>\n" +
            "        someTag:\n" +
            "        <someTag>\n" +
            "            Hey you!\n" +
            "            <someChild>onceMore text</someChild>\n" +
            "        </someTag>\n" +
            "        someTag:\n" +
            "        <someTag>\n" +
            "            Hey you!\n" +
            "            <someChild>onceMore text</someChild>\n" +
            "        </someTag>\n" +
            "        <BAZ/>\n" +
            "        <TAG3/>\n" +
            "        <TAG3>\n" +
            "            <TAG5/>\n" +
            "        </TAG3>\n" +
            "        <TAG>\n" +
            "            someTag:\n" +
            "            <someTag>\n" +
            "                Hey you!\n" +
            "                <someChild attr=\"abacaba\">onceMore text</someChild>\n" +
            "            </someTag>\n" +
            "            someTag:\n" +
            "            <someTag>\n" +
            "                Hey you!\n" +
            "                <someChild>onceMore text</someChild>\n" +
            "            </someTag>\n" +
            "            <BAZ/>\n" +
            "            <TAG4>\n" +
            "                <TAG3>\n" +
            "                    <TAG5>\n" +
            "                        <TAG>\n" +
            "                            someTag:\n" +
            "                            <someTag>\n" +
            "                                Hey you!\n" +
            "                                <someChild>onceMore text</someChild>\n" +
            "                            </someTag>\n" +
            "                            someTag:\n" +
            "                            <someTag>\n" +
            "                                Hey you!\n" +
            "                                <someChild>onceMore text</someChild>\n" +
            "                            </someTag>\n" +
            "                            <BAZ/>\n" +
            "                            <TAG5>\n" +
            "                                <TAG3>\n" +
            "                                    <TAG5>\n" +
            "                                        <TAG4>\n" +
            "\n" +
            "                                        </TAG4>\n" +
            "                                    </TAG5>\n" +
            "                                </TAG3>\n" +
            "                            </TAG5>\n" +
            "                        </TAG>\n" +
            "                    </TAG5>\n" +
            "                </TAG3>\n" +
            "                <TAG5/>\n" +
            "                <TAG>\n" +
            "                    someTag:\n" +
            "                    <someTag>\n" +
            "                        Hey you!\n" +
            "                        <someChild>onceMore text</someChild>\n" +
            "                    </someTag>\n" +
            "                    someTag:\n" +
            "                    <someTag>\n" +
            "                        Hey you!\n" +
            "                        <someChild>onceMore text</someChild>\n" +
            "                    </someTag>\n" +
            "                    <BAZ/>\n" +
            "                    <TAG3>\n" +
            "                        <TAG5/>\n" +
            "                    </TAG3>\n" +
            "                    <TAG4>\n" +
            "                    </TAG4>\n" +
            "\n" +
            "                </TAG>\n" +
            "\n" +
            "            </TAG4>\n" +
            "\n" +
            "        </TAG>\n" +
            "\n" +
            "    </TAG>\n" +
            "</TAG2>\n" +
            "\n" +
            "            \n" +
            "         ").results.size() == 1);
  }

}
