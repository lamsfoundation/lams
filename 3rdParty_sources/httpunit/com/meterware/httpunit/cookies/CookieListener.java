package com.meterware.httpunit.cookies;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2003-2004, Russell Gold
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 *******************************************************************************************************************/

/**
 * An interface for classes which can listen for cookies being rejected and the reason.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public interface CookieListener {


    /** Indicates that the cookie was accepted. **/
    public final static int ACCEPTED = 0;

    /** Indicates that the domain attribute has only one dot. **/
    public final static int DOMAIN_ONE_DOT = 2;

    /** Indicates that the domain attribute is not a suffix of the source domain issuing the cookie. **/
    public final static int DOMAIN_NOT_SOURCE_SUFFIX = 3;

    /** Indicates that the source domain has an extra dot beyond those defined in the domain attribute. **/
    public final static int DOMAIN_TOO_MANY_LEVELS = 4;

    /** Indicates that the source path does not begin with the path attribute. **/
    public final static int PATH_NOT_PREFIX = 5;


    /**
     * Invoked when a cookie is rejected by HttpUnit.
     **/
    void cookieRejected( String cookieName, int reason, String attribute );


}
