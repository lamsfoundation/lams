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

import org.apache.struts.action.Action;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.Globals;
import servletunit.struts.CactusStrutsTestCase;

public class TestTokenAction extends CactusStrutsTestCase {

    public TestTokenAction(String testName) {
        super(testName);
    }

    public void testTransactionToken() {
	addRequestParameter(Constants.TOKEN_KEY, "test_token");
	getSession().setAttribute(Globals.TRANSACTION_TOKEN_KEY, "test_token");
        setRequestPathInfo("test","/testToken");
        actionPerform();
        verifyNoActionErrors();
    }


}
