/*
 * User: dxseale
 * Date: Nov 8, 2002
 */
package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;

public class TestDispatchAction extends MockStrutsTestCase {

    public TestDispatchAction(String testName) {
        super(testName);
    }

     public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    public void testDispatchAction() {
        addRequestParameter("method","actionOne");
        setRequestPathInfo("test","/testDispatchAction");
        actionPerform();
        verifyNoActionErrors();
        verifyForward("action1");
        addRequestParameter("method","actionTwo");
        setRequestPathInfo("test","/testDispatchAction");
        actionPerform();
        verifyNoActionErrors();
        verifyForward("action2");

    }

}
