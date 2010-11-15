import org.jetbrains.plugins.xml.searchandreplace.search.Node;
import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class PrimaryPatternWithNotPredicateTest extends XmlSearchTestCase {

  static Pattern createPattern() {
    Node n = new Node(tag("TAG"), true);
    Node n1 = new Node(notTag("TAG1"), false);
    n1.setChildren(l(n));
    return new Pattern(l(n, n1));
  }

  public PrimaryPatternWithNotPredicateTest() {
    super(createPattern());
  }

  public void testTagShouldBeFound() throws Throwable {
    assertTrue(match("<root><TAG/></root>").results.size() == 1);
  }

  public void testTagShouldNotBeFound() throws Throwable {
    assertTrue(match("<root><TAG1><TAG/></TAG1></root>").results.isEmpty());
  }

  public void testTagShouldBeFoundAgain() throws Throwable {
    assertTrue(match("<root><TAG1/><TAG/></root>").results.size() == 1);
  }
}
