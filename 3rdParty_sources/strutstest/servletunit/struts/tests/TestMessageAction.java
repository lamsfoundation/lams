//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;
import junit.framework.AssertionFailedError;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMessage;

public class TestMessageAction extends MockStrutsTestCase {

    public TestMessageAction(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    public void testNoMessages() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        assertEquals("deryl",getSession().getAttribute("authentication"));
        verifyNoActionMessages();
    }

    public void testMessageExists() {
        setRequestPathInfo("test","/testActionMessages");
        actionPerform();
        verifyForward("success");
        verifyActionMessages(new String[] {"test.message"});
    }

     public void testMessageExistsExpectedNone() {
        setRequestPathInfo("test","/testActionMessages");
        actionPerform();
        verifyForward("success");
        try {
            verifyNoActionMessages();
        } catch (AssertionFailedError afe) {
            return;
        }
        fail("Expected an AssertionFailedError!");
    }

    public void testMessageMismatch() {
        setRequestPathInfo("test","/testActionMessages");
        actionPerform();
        verifyForward("success");
        try {
            verifyActionMessages(new String[] {"error.password.mismatch"});
        } catch (AssertionFailedError afe) {
            return;
        }
        fail("Expected an AssertionFailedError!");
    }

    public void testExpectedMessagesNoneExist() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        assertEquals("deryl",getSession().getAttribute("authentication"));
        try {
        verifyActionMessages(new String[] {"test.message"});
        } catch (AssertionFailedError afe) {
            return;
        }
        fail("Expected AssertionFailedError!");
    }

    public void testVerifiesComplexErrorMessageScenario() {
        ActionErrors errors = new ActionErrors();
        errors.add("error1",new ActionError("error1"));
        errors.add("error2",new ActionError("error2"));
        errors.add("error1",new ActionError("error1"));
        getRequest().setAttribute(Globals.ERROR_KEY,errors);
        try {
        verifyActionErrors(new String[] {"error1","error2","error2"});
        } catch (AssertionFailedError ex) {
            return;
        }
        fail("should not have passed!");
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestMessageAction.class);
    }


}