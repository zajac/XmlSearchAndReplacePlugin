import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

public class PrimaryPatternTest extends XmlSearchTestCase {

  static Pattern createPattern() {
    Pattern.Node n = new Pattern.Node(tag("TAG"), true);
    return new Pattern(l(n));
  }

  public PrimaryPatternTest() {
    super(createPattern());
  }

  public void testPrimaryPatternWorks() throws Throwable {
    String xml = "<TAG><tag/><t><TAG/><t2><TAG></TAG></t2></t></TAG>";
    TestSearchResults results = match(xml);
    assertTrue(results.results.size() == 3);
  }

}
