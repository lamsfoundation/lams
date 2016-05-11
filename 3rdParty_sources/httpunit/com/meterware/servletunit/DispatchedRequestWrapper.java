package com.meterware.servletunit;
/********************************************************************************************************************

*
* Copyright (c) 2003, Russell Gold
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
import java.util.Enumeration;
import java.util.Map;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.RequestDispatcher;

/**
 * This class represents a request dispatched via a RequestDispatcherImpl.
 **/
class DispatchedRequestWrapper extends HttpServletRequestWrapper {

    /** Request-specific information, including parameters and paths. **/
    private RequestContext _requestContext;

    /** The request being wrapped. **/
    private HttpServletRequest _baseRequest;


    static HttpServletRequest createIncludeRequestWrapper( HttpServletRequest request, RequestDispatcher dispatcher ) {
        return new IncludeRequestWrapper( request, dispatcher );
    }


    static HttpServletRequest createForwardRequestWrapper( HttpServletRequest request, RequestDispatcher dispatcher ) {
        return new ForwardRequestWrapper( request, dispatcher );
    }


    DispatchedRequestWrapper( HttpServletRequest baseRequest, RequestDispatcher dispatcher ) {
        super( baseRequest );
        _baseRequest = baseRequest;
        _requestContext = (RequestContext) dispatcher;
        _requestContext.setParentRequest( baseRequest );
    }


    HttpServletRequest getBaseRequest() {
        return _baseRequest;
    }


    public String getParameter( String s ) {
        return _requestContext.getParameter( s );
    }


    public Enumeration getParameterNames() {
        return _requestContext.getParameterNames();
    }


    public String[] getParameterValues( String s ) {
        return _requestContext.getParameterValues( s );
    }


    public Map getParameterMap() {
        return _requestContext.getParameterMap();
    }

}


class IncludeRequestWrapper extends DispatchedRequestWrapper {

    final static String REQUEST_URI  = "javax.servlet.include.request_uri";
    final static String CONTEXT_PATH = "javax.servlet.include.context_path";
    final static String SERVLET_PATH = "javax.servlet.include.servlet_path";
    final static String PATH_INFO    = "javax.servlet.include.path_info";
    final static String QUERY_STRING = "javax.servlet.include.query_string";

    private Hashtable _attributes = new Hashtable();


    IncludeRequestWrapper( HttpServletRequest request, RequestDispatcher dispatcher ) {
        super( request, dispatcher );
        _attributes.put( REQUEST_URI, ((RequestDispatcherImpl) dispatcher ).getRequestURI() );
        _attributes.put( CONTEXT_PATH, request.getContextPath() );
        _attributes.put( SERVLET_PATH, ((RequestDispatcherImpl) dispatcher ).getServletMetaData().getServletPath() );
        final String pathInfo = ((RequestDispatcherImpl) dispatcher ).getServletMetaData().getPathInfo();
        if (pathInfo != null) _attributes.put( PATH_INFO, pathInfo );
    }


    public Object getAttribute( String s ) {
        Object result = _attributes.get( s );
        return (result != null) ? result : super.getAttribute( s );
    }

}


class ForwardRequestWrapper extends DispatchedRequestWrapper {

    private RequestDispatcherImpl _requestContext;

    ForwardRequestWrapper( HttpServletRequest request, RequestDispatcher dispatcher ) {
        super( request, dispatcher );
        _requestContext = (RequestDispatcherImpl) dispatcher;
    }


    public String getRequestURI() {
        return _requestContext.getRequestURI();
    }


    public String getQueryString() {
        return super.getQueryString();
    }


    public String getServletPath() {
        return _requestContext.getServletMetaData().getServletPath();
    }


    public String getPathInfo() {
        return _requestContext.getServletMetaData().getPathInfo();
    }
}




