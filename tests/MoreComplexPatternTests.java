import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class MoreComplexPatternTests extends XmlSearchTestCase {

  static Pattern createPattern() {
    Pattern.Node n1 = new Pattern.Node(tag("TAG1"), false);
    Pattern.Node n2 = new Pattern.Node(tag("TAG2"), false);
    Pattern.Node n3 = new Pattern.Node(tag("TAG3"), false);
    Pattern.Node n4 = new Pattern.Node(tag("TAG4"), false);
    Pattern.Node n5 = new Pattern.Node(tag("TAG5"), false);
    Pattern.Node n0 = new Pattern.Node(tag("TAG0"), false);
    Pattern.Node n = new Pattern.Node(tag("TAG"), true);
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



}
