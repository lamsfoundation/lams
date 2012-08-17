package servletunit;

// ServletUnit Library v1.2 - A java-based testing framework for servlets
// Copyright (C) June 1, 2001 Somik Raha
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// For any questions or suggestions, you can write to me at :
// Email : somik@kizna.com
//
// Postal Address :
// Somik Raha
// R&D Team
// Kizna Corporation
// 2-1-17-6F, Sakamoto Bldg., Moto Azabu, Minato ku, Tokyo, 106 0046, JAPAN
//
// Additions by:
//
// Dane S. Foster
// Equity Technology Group, Inc
// http://www.equitytg.com.
// 954.360.9800
// dfoster@equitytg.com
//
// Additions by:
// Sean Pritchard
// smpritchard@yahoo.com
//
import junit.framework.AssertionFailedError;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;


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

public class HttpServletResponseSimulator implements HttpServletResponse
{
    private OutputStream servOStream;       // The non-default javax.servlet.ServletOutputStream

    private boolean calledGetWriter, calledGetOutputStream;
    private StringWriter stringWriter=null;
    private PrintWriter printWriter=null;
    private Locale locale = null;
    private int contentLength;
    private String contentType = null;
    private int status = 200;
    private String message = null;
    private HashMap headers = new HashMap();
    private HashMap cookies = new HashMap();

    String charEncoding;

    private boolean isCommitted = false;

    public static final int SC_CONTINUE = 100;


    public static final int SC_SWITCHING_PROTOCOLS = 101;

    public static final int SC_OK = 200;

    public static final int SC_CREATED = 201;

    public static final int SC_ACCEPTED = 202;

    public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;

    public static final int SC_NO_CONTENT = 204;

    public static final int SC_RESET_CONTENT = 205;

    public static final int SC_PARTIAL_CONTENT = 206;

    public static final int SC_MULTIPLE_CHOICES = 300;

    public static final int SC_MOVED_PERMANENTLY = 301;

    public static final int SC_MOVED_TEMPORARILY = 302;

    public static final int SC_SEE_OTHER = 303;

    public static final int SC_NOT_MODIFIED = 304;

    public static final int SC_USE_PROXY = 305;

    public static final int SC_BAD_REQUEST = 400;

    public static final int SC_UNAUTHORIZED = 401;

    public static final int SC_PAYMENT_REQUIRED = 402;

    public static final int SC_FORBIDDEN = 403;

    public static final int SC_NOT_FOUND = 404;

    public static final int SC_METHOD_NOT_ALLOWED = 405;

    public static final int SC_NOT_ACCEPTABLE = 406;

    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;

    public static final int SC_REQUEST_TIMEOUT = 408;

    public static final int SC_CONFLICT = 409;

    public static final int SC_GONE = 410;

    public static final int SC_LENGTH_REQUIRED = 411;

    public static final int SC_PRECONDITION_FAILED = 412;

    public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;

    public static final int SC_REQUEST_URI_TOO_LONG = 414;

    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;

    public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    public static final int SC_EXPECTATION_FAILED = 417;

    public static final int SC_INTERNAL_SERVER_ERROR = 500;

    public static final int SC_NOT_IMPLEMENTED = 501;

    public static final int SC_BAD_GATEWAY = 502;

    public static final int SC_SERVICE_UNAVAILABLE = 503;

    public static final int SC_GATEWAY_TIMEOUT = 504;

    public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * Add a cookie to this response, which will then be stored in the browser.
     */
     public void addCookie(Cookie cookie)
    {
    cookies.put( cookie.getName(), cookie );
    }

    /**
     * Returns a cookie with a given, or null if this cookie has
     * not been added to the repsonse.
     */
    public Cookie findCookie( String name ) {
    return (Cookie) cookies.get( name );
    }

    /**
     * This method is not supported.
     */
    public void addDateHeader(String name, long date)
    {
        this.headers.put(name,new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").format(new Date(date)));
    }

    /**
     * Adds a response header with the given name and value.
     */
    public void addHeader(String name, String value)
    {
    this.setHeader(name,value);
    }

    /**
     * Returns a given header field, or null if this header
     * has not been set.
     */
    public String getHeader(String name) {
    if (headers.containsKey(name))
        return (String) headers.get(name);
    else
        return null;
    }

    /**
     * Adds a response header with the given name and integer value.
     */
    public void addIntHeader(String name, int value)
    {
    this.setIntHeader(name,value);
    }

    /**
     * returns true if a header with the given name
     * has already been set
     */
    public boolean containsHeader(String name)
    {
        return headers.containsKey(name);
    }

    /**
     * Returns the given URL unmodified
     */
    public String encodeRedirectUrl(String url)
    {
        return url;
    }


    /**
     * Returns the given URL unmodified.
     */
    public String encodeRedirectURL(String url)
    {
        return url;
    }

    /**
     * Returns the given URL unmodified.
     */
    public String encodeUrl(String url)
    {
        return url;
    }

    /**
     * Returns the given URL unmodified
     */
    public String encodeURL(String url)
    {
        return url;
    }

    /**
     * This method is not supported.
     */
    public void flushBuffer() throws IOException
    {
    throw new UnsupportedOperationException("flushBuffer operation is not supported!");
    }


    /**
     * This method is not supported.
     */
    public int getBufferSize()
    {
    throw new UnsupportedOperationException("getBufferSize operation is not supported!");
    }

    /**
     * This method is not supported.
     */
    public String getCharacterEncoding()
    {
        return charEncoding;
    }

    /**
     * Returns the locale assigned to the response.
     *
     *
     * @see 		#setLocale
     *
     */
    public Locale getLocale()
    {
    if (locale == null)
        return Locale.US;
    else
        return locale;
    }



    /**
     * Returns a {@link ServletOutputStream} suitable for writing binary
     * data in the response. The servlet container does not encode the
     * binary data.

     * <p> Calling flush() on the ServletOutputStream commits the response.

     * Either this method or {@link #getWriter} may
     * be called to write the body, not both.
     *
     * @return				a {@link ServletOutputStream} for writing binary data
     *
     * @exception IllegalStateException if the <code>getWriter</code> method
     * 					has been called on this response
     *
     * @exception IOException 		if an input or output exception occurred
     *
     * @see 				#getWriter
     *
     */
    public ServletOutputStream getOutputStream() throws IOException
    {
        if( this.calledGetWriter )
            throw new IllegalStateException( "The getWriter method has already been called" );

        ServletOutputStream oStream = null;
        if( null == this.servOStream )
            oStream = new ServletOutputStreamSimulator();
        else
            oStream = new ServletOutputStreamSimulator( this.servOStream );
        // resets the status of servOStream to prevent us from possible using a closed stream
        this.servOStream = null;
        this.calledGetOutputStream = true;
        return oStream;
    }


    /**
     * Returns a <code>PrintWriter</code> object that
     * can send character text to the client.
     * The character encoding used is the one specified
     * in the <code>charset=</code> property of the
     * {@link #setContentType} method, which must be called
     * <i>before</i> calling this method for the charset to take effect.
     *
     * <p>If necessary, the MIME type of the response is
     * modified to reflect the character encoding used.
     *
     * <p> Calling flush() on the PrintWriter commits the response.
     *
     * <p>Either this method or {@link #getOutputStream} may be called
     * to write the body, not both.
     *
     *
     * @return 				a <code>PrintWriter</code> object that
     *					can return character data to the client
     *
     * @exception UnsupportedEncodingException  if the charset specified in
     *						<code>setContentType</code> cannot be
     *						used
     *
     * @exception IllegalStateException    	if the <code>getOutputStream</code>
     * 						method has already been called for this
     *						response object
     *
     * @exception IOException   		if an input or output exception occurred
     *
     * @see 					#getOutputStream
     * @see 					#setContentType
     *
     */
    public PrintWriter getWriter() throws IOException
    {
        if( this.calledGetOutputStream )
            throw new IllegalStateException( "The getOutputStream method has already been called" );

        if( stringWriter == null )
            stringWriter = new StringWriter();
        if( printWriter == null )
            printWriter = new PrintWriter( stringWriter );

        this.calledGetWriter = true;
    return printWriter;
    }


    /**
     * Use this method to pick up the string buffer which will hold
     * the contents of the string buffer. You can then
     * write your test case to examine the contents of this
     * buffer and match it against an expected output.
     */
    public StringBuffer getWriterBuffer()
    {
    if (stringWriter==null) return null;
    return stringWriter.getBuffer();
    }

    //TODO: better documentation
    public boolean isCommitted()
    {
        return isCommitted;
    }

    public void setIsCommitted(boolean isCommitted) {
        this.isCommitted = isCommitted;
    }

    /**
     * Reinitializes all local variables.
     * Note, in most servlet containers, you may get an
     * IllegalStateException if you call this method
     * after committing the response.
     * That behavior is not replicated here.
     */
    public void reset()
    {
        this.calledGetOutputStream = false;
        this.calledGetWriter = false;
        this.contentLength = 0;
        this.contentType = null;
        this.stringWriter = null;
        this.printWriter = null;
        headers = new HashMap();
    }

    /**
     * This method is not supported.
     */
    public void resetBuffer()
    {
    throw new UnsupportedOperationException("resetBuffer operation is not supported.");
    }

    /**
     * Sends an error response to the client using the specified
     * status clearing the buffer.  This method always throws
     * an AssertionFailedError with the corresponding error
     * number.
     *
     * @param	sc	the error status code
     */
    public void sendError(int sc) throws IOException
    {
        setStatus(sc);
        throw new AssertionFailedError("received error: " + sc);
    }


    /**
     * Sends an error response to the client using the specified
     * status clearing the buffer.  This method always throws
     * an AssertionFailedError with the corresponding error
     * number and descriptive text.
     *
     * @param	sc	the error status code
     * @param	msg	the descriptive message
     */
    public void sendError(int sc, String msg) throws IOException
    {
        setStatus(sc,msg);
        throw new AssertionFailedError("received error " + sc + " : " + msg);
    }

    /**
     * Resets the response and sets the appropriate redirect headers.
     */
    public void sendRedirect(String location) throws IOException
    {
        reset();
        setStatus(SC_MOVED_TEMPORARILY);
        setHeader("Location", location);
    }

    /**
     * This method is not supported.
     */
    public void setBufferSize(int size)
    {
    throw new UnsupportedOperationException("setBufferSize operation not supported.");
    }


    /**
     * Sets the length of the content body in the response
     * In HTTP servlets, this method sets the HTTP Content-Length header.
     *
     *
     * @param len 	an integer specifying the length of the
     * 			content being returned to the client; sets
     *			the Content-Length header
     *
     */
    public void setContentLength(int len)
    {
    this.contentLength = len;
    }

    /**
     * returns the content length previously set in setContentLength()
     * @return the content length
     */
    public int getContentLength(){
        return this.contentLength;
    }

    /**
     * Sets the content type of the response being sent to
     * the client. The content type may include the type of character
     * encoding used, for example, <code>text/html; charset=ISO-8859-4</code>.
     *
     * <p>If obtaining a <code>PrintWriter</code>, this method should be
     * called first.
     *
     *
     * @param type 	a <code>String</code> specifying the MIME
     *			type of the content
     *
     * @see 		#getOutputStream
     * @see 		#getWriter
     *
     */
    public void setContentType(String type)
    {
    this.contentType = type;
    }

    /**
     * returns the content type previously set in setContentType()
     * @return the content type
     */
    public String getContentType(){
        return this.contentType;
    }


    /**
     * This method is not supported.
     */
    public void setDateHeader(String name, long date)
    {
    this.addDateHeader(name,date);
    }

    /**
     * adds the name/value pair to the headers
     */
    public void setHeader(String name, String value)
    {
        if (name.equalsIgnoreCase("content-type")) {
          setContentType(value);
          return;
        }
        else if (name.equalsIgnoreCase("content-length")) {
          this.setContentLength(Integer.parseInt(value));
          return;
        }

        headers.put(name, value);
    }

    /**
     * Removes a given header
     */
    public void removeHeader(String name) {
        if (headers.containsKey(name))
            headers.remove(name);
    }

    /**
     * Adds the given name/value pair to the headers collection.
     */
    public void setIntHeader(String name, int value)
    {
        setHeader(name, String.valueOf(value));
    }


    /**
     * Sets the locale of the response, setting the headers (including the
     * Content-Type's charset) as appropriate.  This method should be called
     * before a call to {@link #getWriter}.  By default, the response locale
     * is the default locale for the server.
     *
     * @param loc  the locale of the response
     *
     * @see 		#getLocale
     *
     */
    public void setLocale(Locale loc)
    {
    this.locale = loc;
    }

    /**
     * The default action of calling the <code>getOutputStream</code> method
     * is to return a <code>javax.servlet.ServletOutputStream</code> object
     * that sends the data to <code> System.out</code>.  If you don't want
     * the output sent to <code>System.out</code> you can use this method to
     * set where the output will go.  Please note, subsequent calls to
     * <code>getOutputStream</code> will reset the output path to
     * <code>System.out</code>. This prevents the OutputStream returned by
     * calling getOutputStream from writing to a closed stream
     *
     * @param   out     The <code>java.io.OutputStream</code> that represents
     * the real path of the output.
     */
    public void setOutputStream( OutputStream out )
    {
        this.servOStream = out;
    }

    /**
     * Sets the given status code.
     */
    public void setStatus(int sc)
    {
        setStatus(sc, null);
    }

    /**
     * Sets the given status and an associated message.
     */
    public void setStatus(int sc, String sm)
    {
        this.status = sc;
        this.message = sm;
    }

    /**
     * Returns the status code for this response, which is useful for testing expected errors.
     * @return the status code for this response.
     */
    public int getStatusCode() {
        return this.status;
    }

    public void setCharacterEncoding(String charEncoding) {
        this.charEncoding = charEncoding;
    }

    public String getMessage() {
        return message;
    }


}

