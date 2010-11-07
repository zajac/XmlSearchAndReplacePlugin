import org.jetbrains.plugins.xml.searchandreplace.search.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: zajac
 * Date: Nov 7, 2010
 * Time: 4:51:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelfContainsPatternTest extends XmlSearchTestCase {

  private static Pattern createPattern() {
    Pattern.Node n = new Pattern.Node(tag("div"), true);
    Pattern.Node child = new Pattern.Node(tag("div"), false);
    n.setChildren(l(child));
    return new Pattern(l(n, child));
  }

  public SelfContainsPatternTest() {
    super(createPattern());
  }

  public void testThisSlyCase() throws Throwable {
    assertTrue(match("<div attr=\"fuckenAttribute\"><div></div></div>").results.size() == 1);
  }
}
