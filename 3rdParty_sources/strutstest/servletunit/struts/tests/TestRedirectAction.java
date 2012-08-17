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
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit.struts.tests;

import junit.framework.AssertionFailedError;
import servletunit.struts.MockStrutsTestCase;

public class TestRedirectAction extends MockStrutsTestCase {

    public TestRedirectAction(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    public void testVerifyRedirect() {
        setRequestPathInfo("test","/testRedirect");
        actionPerform();
        verifyForward("redirect");
        verifyForwardPath("/test/main/success.jsp");
        verifyNoActionErrors();
    }

    /**
     * Confirms verifyForward works correctly when the redirect path
     * is a relative (not absolute) URL
     */
    public void testRelativeRedirect() {
        setRequestPathInfo("test","/testRelativeRedirect");
        actionPerform();
        verifyForward("redirect");
        verifyForwardPath("/test/main/success.jsp");
        verifyNoActionErrors();
    }

    //todo: this test is for a future enhancement.
//    public void testVerifiesRedirectedExternalURL() {
//        setRequestPathInfo("test","/testRedirectToExternalURL");
//        actionPerform();
//        verifyForwardPath("http://www.yahoo.com");
//        verifyForward("redirect");
//    }

    public void testVerifyRedirectFail() {
        try {
            setRequestPathInfo("test","/testRedirect");
            actionPerform();
            verifyForward("login");
            verifyNoActionErrors();
        } catch (AssertionFailedError e) {
            return;
        }
        fail("We are apparently getting the same redirects, when they should be different.");
    }


}
