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

package servletunit.tests;

import junit.framework.TestCase;
import servletunit.ServletContextSimulator;

import java.util.Enumeration;
import java.io.File;

public class TestServletContext extends TestCase {

    String contextDirectory = "src/examples";
    ServletContextSimulator context;

    public TestServletContext(String testName) {
        super(testName);
    }

    public void setUp() {
        context = new ServletContextSimulator();
    }

    public void testSetAttribute() {
        context.setAttribute("test","testValue");
        assertEquals("testValue",context.getAttribute("test"));
    }

    public void testNoAttribute() {
        assertNull(context.getAttribute("badValue"));
    }

    public void testGetAttributeNames() {
        context.setAttribute("test","testValue");
        context.setAttribute("another","anotherValue");
        assertEquals("testValue",context.getAttribute("test"));
        assertEquals("anotherValue",context.getAttribute("another"));
        Enumeration names = context.getAttributeNames();
        boolean fail = true;
        while (names.hasMoreElements()) {
            fail = true;
            String name = (String) names.nextElement();
            if ((name.equals("test")) || (name.equals("another")))
                fail = false;
        }
        if (fail)
            fail();
    }

    public void testGetRealPath() {
        File file = new File(System.getProperty("basedir"));
        context.setContextDirectory(file);
        assertEquals(new File(file,"test.html").getAbsolutePath(),context.getRealPath("/test.html"));
    }

    public void testGetRealPathNotSet() {
        context.setContextDirectory(null);
        assertNull(context.getRealPath("/test.html"));
    }

    /**
     * verifies that web.xml can be loaded
     * using the classpath
     */
    public void testGetResourceAsStreamFromClasspath(){
        assertNotNull("resource was not found", context.getResourceAsStream("/WEB-INF/web.xml"));
    }

    /**
     * verified that web.xml can be loaded as a URL using the classpath
     * @throws Exception
     */
    public void testGetResourceFromClasspath() throws Exception{
        assertNotNull("resource was not found", context.getResource("/WEB-INF/web.xml"));
    }

    /**
     * verifies that web.xml can be loaded
     * using the filesystem.  Assumes test is being run from
     * the strutstestcase project root directory
     * and that WEB-INF is in src/examples
     */
    public void testGetResourceAsStreamFromFileSystem(){
        context.setContextDirectory(new File(contextDirectory));
        assertNotNull("resource was not found", context.getResourceAsStream("/WEB-INF/web.xml"));
    }

    /**
     * verified that web.xml can be loaded as a URL using the classpath
     * @throws Exception
     */
    public void testGetResourceFromFileSystem() throws Exception{
        context.setContextDirectory(new File(contextDirectory));
        assertNotNull("resource was not found", context.getResource("/WEB-INF/web.xml"));
    }

    /**
     * confirms that calls to getResource will
     * adjust to a path missing the leading "/"
     * @throws Exception
     */
    public void testGetResourceFromFileSystemWithPathCorrection() throws Exception{
        context.setContextDirectory(new File(contextDirectory));
        assertNotNull("resource was not found", context.getResource("WEB-INF/web.xml"));
    }

    /**
     * confirms that calls to getResource will
     * adjust to a path missing the leading "/"
     * @throws Exception
     */
    public void testGetResourceFromClasspathWithPathCorrection() throws Exception{
        assertNotNull("resource was not found", context.getResource("WEB-INF/web.xml"));
    }

    /**
     * confirms that calls to getResource will
     * adjust to a path missing the leading "/"
     * @throws Exception
     */
    public void testGetResourceAsStreamFromFileSystemWithPathCorrection() throws Exception{
        context.setContextDirectory(new File(contextDirectory));
        assertNotNull("resource was not found", context.getResourceAsStream("WEB-INF/web.xml"));
    }

    /**
     * confirms that calls to getResource will
     * adjust to a path missing the leading "/"
     * @throws Exception
     */
    public void testGetResourceAsStreamFromClasspathWithPathCorrection() throws Exception{
        assertNotNull("resource was not found", context.getResourceAsStream("WEB-INF/web.xml"));
    }

    /**
     * verifies that web.xml can be loaded
     * using the filesystem.  Assumes test is being run from
     * the strutstestcase project root directory
     * and that WEB-INF is in src/examples
     * this is necessary because the other tests could be "fooled"
     * by a file that was actually loaded by the classloader.
     */
    public void testGetResourceAsFileFromFileSystem(){
        context.setContextDirectory(new File(contextDirectory));
        File file = context.getResourceAsFile("/WEB-INF/web.xml");
        assertNotNull("resource was not found", file);
        assertTrue("resource was not found", file.exists());
    }

    /**
     * verifies that web.xml can be loaded
     * using the filesystem.  Assumes test is being run from
     * the strutstestcase project root directory
     * and that WEB-INF is in src/examples
     * this is necessary because the other tests could be "fooled"
     * by a file that was actually loaded by the classloader.
     */
    public void testGetResourceAsFileFromFileSystemWithRelativePath(){
        File file = context.getResourceAsFile("/src/examples/WEB-INF/web.xml");
        assertNotNull("resource was not found", file);
        assertTrue("resource was not found", file.exists());
    }






}
