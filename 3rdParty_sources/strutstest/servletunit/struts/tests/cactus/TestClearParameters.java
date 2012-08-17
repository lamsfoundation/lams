package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: deryl
 * Date: Apr 12, 2003
 * Time: 8:53:11 PM
 * To change this template use Options | File Templates.
 */
public class TestClearParameters extends CactusStrutsTestCase {

    public TestClearParameters(String testName) {
        super(testName);
    }

    public void testClearParameters() {
        addRequestParameter("foo","bar");
        addRequestParameter("hi", "there");
        assertEquals("bar",getRequest().getParameter("foo"));
        assertEquals("there",getRequest().getParameter("hi"));
        clearRequestParameters();
        assertNull(getRequest().getParameter("foo"));
        assertNull(getRequest().getParameter("hi"));
    }

    
}
