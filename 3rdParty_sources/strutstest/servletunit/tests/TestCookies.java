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

import javax.servlet.http.Cookie;

public class TestCookies extends TestCase {

    HttpServletRequestSimulator request;

    public TestCookies(String testName) {
        super(testName);
    }

    public void setUp() {
    this.request = new HttpServletRequestSimulator(null);
    }

    public void testNoCookies() {
    assertNull(request.getCookies());
    }

    public void testAddCookie() {
    request.addCookie(new Cookie("test","testValue"));
    Cookie[] cookies = request.getCookies();
    boolean assertion = false;
    for (int i = 0; i < cookies.length; i++) {
        if ((cookies[i].getName().equals("test")) && (cookies[i].getValue().equals("testValue")))
        assertion = true;
    }
    assertTrue(assertion);
    }

    public void testSetCookies() {
    Cookie[] cookies = new Cookie[2];
    cookies[0] = new Cookie("test","testValue");
    cookies[1] = new Cookie("test2","testValue2");
    boolean assert1 = false;
    boolean assert2 = false;
    request.setCookies(cookies);
    Cookie[] resultCookies = request.getCookies();
    for (int i = 0; i < resultCookies.length; i++) {
        if ((resultCookies[i].getName().equals("test")) && (resultCookies[i].getValue().equals("testValue")))
        assert1 = true;
        if ((resultCookies[i].getName().equals("test2")) && (resultCookies[i].getValue().equals("testValue2")))
        assert2 = true;

    }
    assertTrue(assert1 && assert2);
    }

    public void testCheckForWrongCookie() {
    request.addCookie(new Cookie("test","testValue"));
    Cookie[] cookies = request.getCookies();
    boolean assertion = true;
    for (int i = 0; i < cookies.length; i++) {
        if ((cookies[i].getName().equals("badValue")) && (cookies[i].getValue().equals("dummyValue")))
        assertion = false;
    }
    assertTrue(assertion);
    }




}
