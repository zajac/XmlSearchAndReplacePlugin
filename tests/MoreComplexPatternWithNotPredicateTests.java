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

    n1.setChildren(l(n2, n3));
    n2.setChildren(l(n));
    n3.setChildren(l(n));
    n.setChildren(l(n4, n6));
    n4.setChildren(l(n5));
    return new Pattern(l(n, n1, n2, n3, n4, n5, n6));
  }

  public MoreComplexPatternWithNotPredicateTests() {
    super(createPattern());
  }

}
