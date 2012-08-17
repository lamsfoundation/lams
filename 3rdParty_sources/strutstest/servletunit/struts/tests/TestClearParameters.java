package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;

/**
 * Created by IntelliJ IDEA.
 * User: deryl
 * Date: Apr 12, 2003
 * Time: 8:53:11 PM
 * To change this template use Options | File Templates.
 */
public class TestClearParameters extends MockStrutsTestCase {

    public TestClearParameters(String testName) {
        super(testName);
    }

    /**
     * Sets up the test fixture for this test.  This method creates
     * an instance of the ActionServlet, initializes it to validate
     * forms and turn off debugging, and creates a mock HttpServletRequest
     * and HttpServletResponse object to use in this test.
     */
    protected void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
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

    public void testClearsRedirectHeaderWhenRequested() {
        setRequestPathInfo("test","/testRedirect");
        actionPerform();
        String forward = getActualForward();
        assertEquals("/test/main/success.jsp",forward);
        clearRequestParameters();
        addRequestParameter("username","deryl");
        addRequestParameter("password","express");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("login");
    }
}
