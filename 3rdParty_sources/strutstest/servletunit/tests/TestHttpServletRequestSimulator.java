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
import servletunit.HttpServletRequestSimulator;
import servletunit.ServletContextSimulator;

import javax.servlet.RequestDispatcher;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Date;

/**
 * A Junit based test of the HttpServletResponseSimulator class
 * @author Sean Pritchard
 * @version 1.0
 */

public class TestHttpServletRequestSimulator extends TestCase {

    HttpServletRequestSimulator request;
    ServletContextSimulator context;

    public TestHttpServletRequestSimulator(String testCase) {
        super(testCase);
    }

    public void setUp() {
        context = new ServletContextSimulator();
        request = new HttpServletRequestSimulator(context);
    }

    public void testAddParameterArray() {
        String[] values = { "value1", "value2" };
        request.addParameter("name1",values);
        String[] result = request.getParameterValues("name1");
        if (result.length != 2)
            fail();
        if (!((result[0].equals("value1")) && (result[1].equals("value2"))))
            fail();
    }

    public void testGetParameterValuesSingle() {
        request.addParameter("name1","value1");
        String[] result = request.getParameterValues("name1");
        if (result.length != 1)
            fail();
        if (!(result[0].equals("value1")))
            fail();
    }

    public void testGetParameterWithNullKey() {
        String result = request.getParameter(null);
        assertNull(result);

    }

     public void testGetParameterValuesWithNullKey() {
        String[] result = request.getParameterValues(null);
        assertNull(result);

    }

    public void testGetParameterWithArray() {
        String[] values = { "value1", "value2" };
        request.addParameter("name1",values);
        String result = request.getParameter("name1");
        if (!(result.equals("value1")))
            fail();
    }

    public void testSetAttributeNullValue() {
        request.setAttribute("test1","value1");
        assertEquals(request.getAttribute("test1"),"value1");
        request.setAttribute("test1",null);
        assertNull(request.getAttribute("test1"));
    }

    public void testSetAttribute() {
        request.setAttribute("test1","value1");
        assertEquals(request.getAttribute("test1"),"value1");
    }

    public void testIsUserInRole() {
        request.setUserRole("role1");
        assertTrue(request.isUserInRole("role1"));
    }

    public void testIsUserInRoleFalse() {
        request.setUserRole("role2");
        assertTrue(!request.isUserInRole("role1"));
    }

    public void testGetServerPort() {
        request.setServerPort(8080);
        assertEquals(request.getServerPort(),8080);
    }

    public void testGetRequestURLWithQueryString() {
        request.setRequestURL("https://server:8080/my/request/url?param=1");
        assertEquals("https://server:8080/my/request/url",request.getRequestURL().toString());
        assertEquals("param=1",request.getQueryString());
        assertEquals("https",request.getScheme());
        assertTrue(request.isSecure());
        assertEquals("/my/request/url",request.getRequestURI());
        assertEquals("server",request.getServerName());
        assertEquals(8080,request.getServerPort());
    }

    public void testGetRequestURLWithoutQueryStringOrPort() {
        request.setRequestURL("http://server/my/request/url");
        assertEquals("http://server/my/request/url",request.getRequestURL().toString());
        assertNull(request.getQueryString());
        assertEquals("http",request.getScheme());
        assertTrue(!request.isSecure());
        assertEquals("/my/request/url",request.getRequestURI());
        assertEquals("server",request.getServerName());
        assertEquals(80,request.getServerPort());
    }

    public void testGetSecurePort() {
        request.setRequestURL("https://server/my/request/url?param=1");
        assertTrue(request.isSecure());
        assertEquals(443,request.getServerPort());
    }

    public void testGetLocales() {
        Enumeration enum = request.getLocales();
        for (Object enumObject = enum.nextElement(); enum.hasMoreElements(); enumObject = enum.nextElement()) {
            Locale locale = (Locale) enumObject;
            if (!locale.equals(Locale.US))
                fail();
        }
    }

    public void testGetDateHeaderNoHeader() {
        assertEquals(-1,request.getDateHeader("foo"));
    }

    public void testGetDateHeaderBadHeader() {
        request.setHeader("DATE_HEADER","foofoofoo");
        try {
            request.getDateHeader("DATE_HEADER");
        } catch (IllegalArgumentException iae) {
            return;
        }
        fail();
    }

    public void testGetDateHeader() throws ParseException
    {
        String date = "05/23/73 8:05 PM, PST";
        long time = new SimpleDateFormat().parse(date).getTime();
        request.setDateHeader("DATE_HEADER",time);
        assertEquals(time,request.getDateHeader("DATE_HEADER"));
    }

    public void testGetRealPath() {
        File file = new File(System.getProperty("basedir"));
        context.setContextDirectory(file);
        assertEquals(new File(file,"test.html").getAbsolutePath(),request.getRealPath("/test.html"));
    }

    public void testGetRealPathNotSet() {
        assertNull(request.getRealPath("/test.html"));
    }

    public void testReturnsRequestDispatcher() {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/test/login.jsp");
        assertNotNull(dispatcher);
    }


}

