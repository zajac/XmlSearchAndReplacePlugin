import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class MoreComplexPatternWithNotPredicateTests extends XmlSearchTestCase {

  static Pattern createPattern() {
    Pattern.Node n = new Pattern.Node(tag("TAG"), true);
    Pattern.Node n1 = new Pattern.Node(notTag("TAG1"), false);
    Pattern.Node n2 = new Pattern.Node(tag("TAG2"), false);
    Pattern.Node n3 = new Pattern.Node(tag("TAG3"), false);
    Pattern.Node n4 = new Pattern.Node(tag("TAG4"), false);
    Pattern.Node n5 = new Pattern.Node(tag("TAG5"), false);
    Pattern.Node n6 = new Pattern.Node(tag("TAG6"), false);
    Pattern.Node n7 = new Pattern.Node(notTag("TAG7"), false);

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
