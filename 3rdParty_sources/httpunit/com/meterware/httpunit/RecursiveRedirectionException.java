package com.meterware.httpunit;

/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2008, Russell Gold
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
import java.net.URL;

/**
 * Class used to indicate when a request to a resource resulted in an HTTP
 * redirect response that lead to a recursive loop of redirections
 * 
 * @author <a href="mailto:james.abley@gmail.com">James Abley </a>
 */
public class RecursiveRedirectionException extends RuntimeException {

    private URL url;

    /**
     * Create a new <code>RecursiveRedirectionException</code> with the
     * specified URL and cause.
     * 
     * @param url
     *            the {@link URL}that caused the recursive loop to be detected
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()}method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     */
    public RecursiveRedirectionException(URL url, Throwable cause) {
        super(cause);
        this.url = url;
    }

    /**
     * Create a new <code>RecursiveRedirectionException</code> with the
     * specified URL and detail message.
     * 
     * @param url
     *            the <code>URL</code> that caused the recursive loop to be
     *            detected. The URL is saved for later retrieval by
     *            {@link #getURL()}
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by {@link #getMessage()}
     */
    public RecursiveRedirectionException(URL url, String message) {
        super(message);
        this.url = url;
    }

    /**
     * Create a new <code>RecursiveRedirectionException</code> with the
     * specified URL, detail message and cause.
     * 
     * @param url
     *            the <code>URL</code> that caused the recursive loop to be
     *            detected. The URL is saved for later retrieval by
     *            {@link #getURL()}
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by {@link #getMessage()}
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            {@link #getCause()}method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     */
    public RecursiveRedirectionException(URL url, String message,
            Throwable cause) {
        super(message, cause);
        this.url = url;
    }

    /**
     * Returns the URL that caused this exception to be thrown.
     * 
     * @return the <code>URL</code> that gave rise to this Exception
     */
    public URL getURL() {
        return url;
    }
}
