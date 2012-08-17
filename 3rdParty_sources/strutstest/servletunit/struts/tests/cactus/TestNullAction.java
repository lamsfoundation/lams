package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;

/**
 * Created by IntelliJ IDEA.
 * User: deryl
 * Date: Apr 13, 2003
 * Time: 12:07:32 PM
 * To change this template use Options | File Templates.
 */
public class TestNullAction extends CactusStrutsTestCase {

    public TestNullAction(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testNullAction() {
        setRequestPathInfo("test","/testNullAction.do");
        actionPerform();
        verifyForward(null);
        verifyForwardPath(null);
    }
}
