import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class MoreComplexPatternWithNotPredicateTests extends XmlSearchTestCase {

  static Pattern createPattern() {
    Node n = new Node(tag("TAG"), true);
    Node n1 = new Node(notTag("TAG1"), false);
    Node n2 = new Node(tag("TAG2"), false);
    Node n3 = new Node(tag("TAG3"), false);
    Node n4 = new Node(tag("TAG4"), false);
    Node n5 = new Node(tag("TAG5"), false);
    Node n6 = new Node(tag("TAG6"), false);
    Node n7 = new Node(notTag("TAG7"), false);

    n1.setChildren(l(n2, n3));
    n2.setChildren(l(n));
    n3.setChildren(l(n));
    n.setChildren(l(n4, n6));
    n4.setChildren(l(n5));
    n7.setChildren(l(n4));
    return new Pattern(l(n, n1, n2, n3, n4, n5, n6, n7));
  }

  public MoreComplexPatternWithNotPredicateTests() {
    super(createPattern());
  }

  public void testShouldMatchVerySimpleXml() throws Throwable {
    String xml = "<TAG2><TAG3><TAG><TAG4><TAG5/></TAG4><TAG6/></TAG></TAG3></TAG2>";
    assertTrue(match(xml).results.size() == 1);
  }

  public void testShouldNotMatchSimpleXml() throws Throwable {
    String xml = "<TAG2><TAG1><TAG3><TAG><TAG4><TAG5/></TAG4><TAG6/></TAG></TAG3></TAG1></TAG2>";
    assertTrue(match(xml).results.isEmpty());
  }

  public void testShouldMatchSimpleXml() throws Throwable {
    String xml = "<TAG2><TAG3><TAG1/><TAG><TAG4><TAG5/></TAG4><TAG6/></TAG></TAG3></TAG2>";
    assertTrue(match(xml).results.size() == 1);
  }

  public void testShouldNotMatchAgainXml() throws Throwable {
    String xml = "<TAG2><TAG3><TAG><TAG7>" +
            "<TAG4><TAG5/></TAG4>" +
            "</TAG7><TAG6/></TAG>" +
            "</TAG3></TAG2>";
    assertTrue(match(xml).results.isEmpty());
  }

  public void testShouldMatchXml() throws Throwable {
    String xml = "<TAG2><TAG3><TAG><TAG7/><TAG4><TAG5/></TAG4><TAG6/></TAG></TAG3></TAG2>";
    assertTrue(match(xml).results.size() == 1);
  }

  public void testShouldMatchXmlAgain() throws Throwable {
    String xml = "<TAG2><TAG3><TAG><TAG4><TAG5/></TAG4><TAG6/></TAG></TAG3><TAG3><TAG><TAG7><TAG4><TAG5/></TAG4></TAG7><TAG6/></TAG></TAG3></TAG2>";
    assertTrue(match(xml).results.size() == 1);
  }

  public void testShouldMatchXmlTwice() throws Throwable {
    String xml = "<TAG2><TAG3><TAG><TAG4><TAG5/></TAG4><TAG6/></TAG></TAG3><TAG3><TAG><TAG7/><TAG4><TAG5/></TAG4><TAG6/></TAG></TAG3></TAG2>";
    assertTrue(match(xml).results.size() == 2);
  }

}
