import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class MoreComplexPatternTests extends XmlSearchTestCase {

  static Pattern createPattern() {
    Node n1 = new Node(tag("TAG1"), false);
    Node n2 = new Node(tag("TAG2"), false);
    Node n3 = new Node(tag("TAG3"), false);
    Node n4 = new Node(tag("TAG4"), false);
    Node n5 = new Node(tag("TAG5"), false);
    Node n0 = new Node(tag("TAG0"), false);
    Node n = new Node(tag("TAG"), true);
    n.setChildren(l(n3, n4));
    n1.setChildren(l(n));
    n2.setChildren(l(n));
    n3.setChildren(l(n5));
    n0.setChildren(l(n2));
    return new Pattern(l(n0, n, n1, n2, n3, n4, n5));
  }

  public MoreComplexPatternTests() {
    super(createPattern());
  }

  public void testTagShouldBeFound() throws Throwable {
    assertTrue(match("<root><TAG0><TAG2><TAG1><TAG><TAG3><TAG5/></TAG3><TAG4/></TAG></TAG1></TAG2></TAG0></root>").results.size() == 1);
  }

  public void testTagShouldBeFoundAgain() throws Throwable {
    assertTrue(match("<root><TAG0><TAG2><TAG1><TAG><TAG3><TAG5/><TAG4/></TAG3></TAG></TAG1></TAG2></TAG0></root>").results.size() == 1);
  }

  public void testTagShouldNotBeFound() throws Throwable {
    assertTrue(match("<root><TAG0><TAG2><TAG1><TAG><TAG5/><TAG4/></TAG></TAG1></TAG2></TAG0></root>").results.isEmpty());
  }

  public void testTagShouldNotBeFoundAgain() throws Throwable {
    assertTrue(match("<root><TAG0><TAG2><TAG1><TAG></TAG><TAG3><TAG5/><TAG4/></TAG3></TAG1></TAG2></TAG0></root>").results.isEmpty());
  }

  public void testTagShouldNotBeFoundAgainAgain() throws Throwable {
    assertTrue(match("<root><TAG2><TAG1><TAG></TAG><TAG3><TAG5/><TAG4/></TAG3></TAG1></TAG2></root>").results.isEmpty());
  }

  public void testTagShouldBeFoundOnlyOnce() throws Throwable {
    assertTrue(match("<root><TAG0><TAG2><TAG1><TAG><TAG3><TAG5/><TAG4/></TAG3></TAG></TAG1></TAG2></TAG0>" +
            "<TAG0><TAG2><TAG1><TAG><TAG5/><TAG4/></TAG></TAG1></TAG2></TAG0></root>").results.size() == 1);
  }

  public void testTagShouldBeFoundExactlyTwice() throws Throwable {
    assertTrue(match("<root><TAG0><TAG2><TAG1><TAG><TAG3><TAG5/></TAG3><TAG4/></TAG></TAG1></TAG2></TAG0>" +
            "<TAG0><TAG2><TAG1><TAG><TAG3><TAG5/><TAG4/></TAG3></TAG></TAG1></TAG2></TAG0></root>").results.size() == 2);
  }
}
