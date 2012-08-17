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
import junit.framework.AssertionFailedError;

public class TestTilesForward extends CactusStrutsTestCase {

    public TestTilesForward(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testTilesForward() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("tiles","/tilesForward");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/pageLayout.jsp");
        verifyTilesForward("success","page.library");
    }

    public void testTilesInputForward() {
        setRequestPathInfo("tiles","/tilesInputForward.do");
        actionPerform();
        verifyInputForward();
    }

    public void testTileForwardFail() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("tiles","/tilesForward.do");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/pageLayout.jsp");
        try {
        verifyTilesForward("success","foo.fail");
        } catch (AssertionFailedError afe) {
            return;
        }
        fail("Should have failed.");
    }

//    public void testTileForwardFailDefinitionExists() {
//        addRequestParameter("username","deryl");
//        addRequestParameter("password","radar");
//        setRequestPathInfo("tiles","/tilesForward.do");
//        actionPerform();
//        verifyForward("success");
//        verifyForwardPath("/layouts/pageLayout.jsp");
//        try {
//        verifyTilesForward("failure","another.page");
//        } catch (AssertionFailedError afe) {
//            return;
//        }
//        fail("Should have failed.");
//    }


}
