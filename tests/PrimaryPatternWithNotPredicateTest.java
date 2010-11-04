import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class PrimaryPatternWithNotPredicateTest extends XmlSearchTestCase {

  static Pattern createPattern() {
    Pattern.Node n = new Pattern.Node(tag("TAG"), true);
    Pattern.Node n1 = new Pattern.Node(notTag("TAG1"), false);
    n1.setChildren(l(n));
    return new Pattern(l(n, n1));
  }

  public PrimaryPatternWithNotPredicateTest() {
    super(createPattern());
  }
}
