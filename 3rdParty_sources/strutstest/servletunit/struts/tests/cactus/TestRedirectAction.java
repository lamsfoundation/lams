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

package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;

public class TestRedirectAction extends CactusStrutsTestCase {

    public TestRedirectAction(String testName) {
        super(testName);
    }

    public void testRedirect() {
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
        // in a servlet engine, this will have the context prepended
        verifyForwardPath("/test/main/success.jsp");
        verifyNoActionErrors();
    }

}
