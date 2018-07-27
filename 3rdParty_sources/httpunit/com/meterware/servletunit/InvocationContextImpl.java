package com.meterware.servletunit;
/********************************************************************************************************************

*
* Copyright (c) 2001-2004, Russell Gold
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
import com.meterware.httpunit.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Stack;

import javax.servlet.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This class represents the context in which a specific servlet request is being made.
 * It contains the objects needed to unit test the methods of a servlet.
 **/
class InvocationContextImpl implements InvocationContext {

    private Stack             _contextStack = new Stack();
    private URL               _effectiveURL;

    private ServletUnitClient _client;
    private WebApplication    _application;
    private FrameSelector       _frame;

    private WebResponse       _webResponse;


    /**
     * Returns the request to be processed by the servlet or filter.
     **/
    public HttpServletRequest getRequest() {
        return getContext().getRequest();
    }


    /**
     * Returns the response which the servlet or filter should modify during its operation.
     **/
    public HttpServletResponse getResponse() {
        return getContext().getResponse();
    }


    /**
     * Invokes the current servlet or filter.
     */
    public void service() throws ServletException, IOException {
        if (isFilterActive()) {
            getFilter().doFilter( getRequest(), getResponse(), getFilterChain() );
        } else {
            getServlet().service( getRequest(), getResponse() );
        }
    }


    /**
     * Returns the selected servlet, initialized to provide access to sessions
     * and servlet context information.
     **/
    public Servlet getServlet() throws ServletException {
        return getContext().getServlet();
    }


    /**
     * Returns the final response from the servlet. Note that this method should
     * only be invoked after all processing has been done to the servlet response.
     **/
    public WebResponse getServletResponse() throws IOException {
        if (_contextStack.size() != 1) throw new IllegalStateException( "Have not returned from all request dispatchers" );
        if (_webResponse == null) {
            HttpSession session = getRequest().getSession( /* create */ false );
            if (session != null && session.isNew()) {
                Cookie cookie = new Cookie( ServletUnitHttpSession.SESSION_COOKIE_NAME, session.getId() );
                cookie.setPath( _application.getContextPath() );
                getResponse().addCookie( cookie );
            }
            _webResponse = new ServletUnitWebResponse( _client, _frame, _effectiveURL, getResponse(), _client.getExceptionsThrownOnErrorStatus() );
        }
        return _webResponse;
    }


    public FrameSelector getFrame() {
        return _frame;
    }


    public void pushIncludeRequest( RequestDispatcher rd, HttpServletRequest request, HttpServletResponse response ) throws ServletException {
        if (isFilterActive()) throw new IllegalStateException( "May not push an include request when no servlet is active" );
        _contextStack.push( new ExecutionContext( DispatchedRequestWrapper.createIncludeRequestWrapper( request, rd ),
                            response, ((RequestDispatcherImpl) rd).getServletMetaData() ) );
    }


    public void pushForwardRequest( RequestDispatcher rd, HttpServletRequest request, HttpServletResponse response ) throws ServletException {
        if (isFilterActive()) throw new IllegalStateException( "May not push a forward request when no servlet is active" );
        _contextStack.push( new ExecutionContext( DispatchedRequestWrapper.createForwardRequestWrapper( request, rd ),
                            response, ((RequestDispatcherImpl) rd).getServletMetaData() ) );
    }


    public void popRequest() {
        if (getContext().mayPopFilter()) {
            getContext().popFilter();
        } else if (_contextStack.size() == 1) {
            throw new IllegalStateException( "May not pop the initial request" );
        } else {
            _contextStack.pop();
        }
    }


    public boolean isFilterActive() {
        return getContext().isFilterActive();
    }


    public Filter getFilter() throws ServletException {
        return getContext().getFilter();
    }


    public FilterChain getFilterChain() {
        return new FilterChain() {
            public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse ) throws IOException, ServletException {
                pushFilter( servletRequest, servletResponse );
                service();
                popRequest();
            }
        };
    }


    public void pushFilter( ServletRequest request, ServletResponse response ) {
        getContext().pushFilter( request, response );
    }


    class AccessDeniedException extends HttpException {
        public AccessDeniedException( URL baseURL ) {
            super( 403, "Access Denied", baseURL );
        }
    }


//------------------------------ package methods ---------------------------------------


    /**
     * Constructs a servlet invocation context for a specified servlet container,
     * request, and cookie headers.
     **/
    InvocationContextImpl( ServletUnitClient client, ServletRunner runner, FrameSelector frame, WebRequest request, Dictionary clientHeaders, byte[] messageBody ) throws IOException, MalformedURLException {
        _client      = client;
        _application = runner.getApplication();
        _frame       = frame;

        URL requestURL  = request.getURL();
        final ServletUnitHttpRequest suhr = new ServletUnitHttpRequest( _application.getServletRequest( requestURL ), request,
                                                                        runner.getContext(), clientHeaders, messageBody );
        if (_application.usesBasicAuthentication()) suhr.readBasicAuthentication();
        else if (_application.usesFormAuthentication()) suhr.readFormAuthentication();

        HttpSession session = suhr.getSession( /* create */ false );
        if (session != null) ((ServletUnitHttpSession) session).access();

        _effectiveURL = computeEffectiveUrl( suhr, requestURL );
        _contextStack.push( new ExecutionContext( suhr, new ServletUnitHttpResponse(),
                                                  _application.getServletRequest( _effectiveURL ) ) );
    }


    private URL computeEffectiveUrl( HttpServletRequest request, URL requestURL ) {
        if (!_application.requiresAuthorization( requestURL ) || userIsAuthorized( request, requestURL ) ) {
            return requestURL;
        } else if (request.getRemoteUser() != null) {
            throw new AccessDeniedException( requestURL );
        } else if (_application.usesBasicAuthentication()) {
            throw AuthorizationRequiredException.createBasicAuthenticationRequiredException( _application.getAuthenticationRealm() );
        } else if (!_application.usesFormAuthentication()) {
            throw new IllegalStateException( "Authorization required but no authentication method defined" );
        } else {
            ((ServletUnitHttpSession) request.getSession()).setOriginalURL( requestURL );
            return _application.getLoginURL();
        }
    }


    private boolean userIsAuthorized( HttpServletRequest request, URL requestURL ) {
        String[] roles = _application.getPermittedRoles( requestURL );
        for (int i = 0; i < roles.length; i++) {
            if (request.isUserInRole( roles[i] )) return true;
        }
        return false;
    }



    static class ExecutionContext {

        private HttpServletResponse _response;
        private HttpServletRequest  _request;
        private ServletMetaData     _metaData;

        private Stack               _filterStack = new Stack();


        ExecutionContext( HttpServletRequest request, HttpServletResponse response, ServletMetaData metaData ) {
            _request = request;
            _response = response;
            _metaData = metaData;
        }


        boolean isFilterActive() {
            return getFilterIndex() < _metaData.getFilters().length;
        }


        Servlet getServlet() throws ServletException {
            if (isFilterActive()) throw new IllegalStateException( "Current context is a filter - may not request servlet.");
            return _metaData.getServlet();
        }


        HttpServletResponse getResponse() {
            return _filterStack.isEmpty() ? _response : ((FilterContext) _filterStack.lastElement()).getResponse();
        }


        HttpServletRequest getRequest() {
            return _filterStack.isEmpty() ? _request : ((FilterContext) _filterStack.lastElement()).getRequest();
        }


        public Filter getFilter() throws ServletException {
            if (!isFilterActive()) throw new IllegalStateException( "Current context is a servlet - may not request filter.");
            return _metaData.getFilters()[ getFilterIndex() ].getFilter();
        }


        public void pushFilter( ServletRequest request, ServletResponse response ) {
            if (!isFilterActive()) throw new IllegalStateException( "Current context is a servlet - may not push filter.");
            if (!(request  instanceof HttpServletRequest))  throw new IllegalArgumentException( "HttpUnit does not support non-HTTP request wrappers" );
            if (!(response instanceof HttpServletResponse)) throw new IllegalArgumentException( "HttpUnit does not support non-HTTP response wrappers" );

            _filterStack.push( new FilterContext( (HttpServletRequest) request, (HttpServletResponse) response ) );
        }


        public boolean mayPopFilter() {
            return getFilterIndex() > 0;
        }


        public void popFilter() {
            _filterStack.pop();
        }


        private int getFilterIndex() {
            return _filterStack.size();
        }
    }


    static class FilterContext {
        HttpServletRequest  _request;
        HttpServletResponse _response;


        public FilterContext( HttpServletRequest request, HttpServletResponse response ) {
            _request = request;
            _response = response;
        }


        public HttpServletResponse getResponse() {
            return _response;
        }


        public HttpServletRequest getRequest() {
            return _request;
        }
    }

    private ExecutionContext getContext() {
        return (ExecutionContext) _contextStack.lastElement();
    }
}