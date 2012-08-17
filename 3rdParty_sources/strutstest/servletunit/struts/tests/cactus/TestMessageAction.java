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
//                                                                             test
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;
import junit.framework.AssertionFailedError;

public class TestMessageAction extends CactusStrutsTestCase {

    public TestMessageAction(String testName) {
        super(testName);
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

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestMessageAction.class);
    }


}

