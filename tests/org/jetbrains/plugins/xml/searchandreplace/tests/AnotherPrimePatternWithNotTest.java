package org.jetbrains.plugins.xml.searchandreplace.tests;

import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class AnotherPrimePatternWithNotTest extends XmlSearchTestCase {
  private static Pattern createPattern() {
    Node n1 = new Node(tag("Tag"), true);
    Node n2 = new Node(notTag("Tag1"));
    n1.setChildren(l(n2));
    return new Pattern(l(n1, n2));
  }

  public AnotherPrimePatternWithNotTest() {
    super(createPattern());
  }

  public void testNotMatch() throws Throwable {
    assertTrue(match("<Tag>asdasd<Tag1/></Tag>").results.isEmpty());
  }

  public void testMatch() throws Throwable {
    assertTrue(match("<ROOT>    <Tag>abacaba</Tag>   </ROOT>").results.size() == 1);
  }
}
