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

import servletunit.struts.*;
import java.io.File;

/**
 * <p>Title: NullPointerActionTest</p>
 * <p>Description: Tests to confirm a NullPointerException
 * thrown from a misbehaving action reaches the test.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Sean Pritchard
 * @version 1.0
 */
public class TestNullPointerAction extends MockStrutsTestCase {

    public TestNullPointerAction() {
    }

    public void setUp() throws Exception {
        super.setUp();
        this.setContextDirectory(new File(System.getProperty("basedir") + "/src/examples"));
        setConfigFile("/WEB-INF/struts-config-test.xml");
    }

    public void testNullPointer() throws Exception{
        try{
            this.setRequestPathInfo("/testNullPointer");
            this.actionPerform();
            fail("Exception expected");
        }catch(ExceptionDuringTestError e){
            //uncomment this to see a sample stack trace
            //that users will see if their action throws an exception
//            throw e;
        }
    }

    public void testNullPointerFromForm() throws Exception{
        try{
            this.setRequestPathInfo("/testNullPointerForm");
            this.actionPerform();
            fail("Exception expected");
        }catch(ExceptionDuringTestError e){
            //uncomment this to see a sample stack trace
            //that users will see if their action throws an exception
//            throw e;
        }
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestNullPointerAction.class);
    }

}
