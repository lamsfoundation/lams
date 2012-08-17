/*
 * User: dxseale
 * Date: Nov 8, 2002
 */
package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;

public class TestDispatchAction extends CactusStrutsTestCase {

    public TestDispatchAction(String testName) {
        super(testName);
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
