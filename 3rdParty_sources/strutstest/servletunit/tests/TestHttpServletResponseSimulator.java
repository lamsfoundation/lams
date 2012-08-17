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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import servletunit.HttpServletResponseSimulator;

import java.util.Date;

/**
 * A Junit based test of the HttpServletResponseSimulator class
 * @author Sean Pritchard
 * @version 1.0
 */

public class TestHttpServletResponseSimulator extends TestCase {

    public TestHttpServletResponseSimulator(String testCase) {
        super(testCase);
    }

    /**
     * Ensures the value returned by encodeURL contains the
     * original url.
     */
    public void testEncodeURL(){
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        String url = "http://sourceforge.net";
        assertTrue(response.encodeURL(url).indexOf(url)!=-1);
    }

    /**
     * Ensures the value returned by encodeUrl contains the
     * original url.
     */
    public void testEncodeUrl(){
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        String url = "http://sourceforge.net";
        assertTrue(response.encodeUrl(url).indexOf(url)!=-1);
    }

    /**
     * Ensures the value returned by encodeRedirectURL contains the
     * original url.
     */
    public void testEncodeRedirectURL(){
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        String url = "http://sourceforge.net";
        assertTrue(response.encodeRedirectURL(url).indexOf(url)!=-1);
    }

    /**
     * Ensures the value returned by encodeRedirectUrl contains the
     * original url.
     */
    public void testEncodeRedirectUrl(){
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        String url = "http://sourceforge.net";
        assertTrue(response.encodeRedirectUrl(url).indexOf(url)!=-1);
    }

    /**
     * tests the setHeader() and containsHeader() methods
     */
    public void testSetHeader() {
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.setHeader("TestName", "testValue");
        assertTrue(response.containsHeader("TestName"));
    }

    /**
     * tests the setIntHeader() and containsHeader() methods.
     */
    public void testSetIntHeader() {
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.setIntHeader("TestName", 5);
        assertTrue(response.containsHeader("TestName"));
    }


    public static Test suite() {
        return new TestSuite(TestHttpServletResponseSimulator.class);
    }

    /**
     * tests the send redirect method
     * @throws Exception
     */
    public void testSendRedirect() throws Exception{
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.sendRedirect("http://sourceforge.net");
        response.containsHeader("Location");
    }

    /**
     * tests that getContentType() returns the value previously set
     * by calling setContentType() or setHeader() with a "Content-type"
     * parameter
     */
    public void testContentType(){
        String type = "image/gif";
        String type2 = "text/xml";
        String type3 = "video/mpeg";
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.setContentType(type);
        assertEquals(type, response.getContentType());
        response.setHeader("Content-type", type2);
        assertEquals(type2, response.getContentType());
        response.addHeader("Content-type", type3);
        assertEquals(type3, response.getContentType());
    }

    /**
     * tests that getContentLength() returns the value previously set
     * by calling setContentLength() or setIntHeader() with a "Content-length"
     * parameter
     */
    public void testContentLength(){
        int len1 = 25;
        int len2 = 156;
        int len3 = 42;
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.setContentLength(len1);
        assertEquals(len1, response.getContentLength());
        response.setIntHeader("Content-length", len2);
        assertEquals(len2, response.getContentLength());
        response.addIntHeader("Content-length", len3);
        assertEquals(len3, response.getContentLength());
    }


    public void testAddDateHeader() {
        Date date = new Date(2004,5,23);
        HttpServletResponseSimulator response = new HttpServletResponseSimulator();
        response.addDateHeader("date",date.getTime());
        assertEquals("unexpected header value","Thu, 23 Jun 3904 00:00:00 PDT",response.getHeader("date"));
    }
}

