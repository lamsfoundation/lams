package com.meterware.servletunit;
/********************************************************************************************************************
 *
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
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.FrameSelector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

import java.io.IOException;


/**
 * An interface which represents the invocation of a servlet.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public interface InvocationContext {


    /**
     * Returns the request to be processed by the servlet or filter.
     **/
    HttpServletRequest getRequest();


    /**
     * Returns the response which the servlet or filter should modify during its operation.
     **/
    HttpServletResponse getResponse();


    /**
     * Invokes the current servlet or filter.
     * @since 1.6
     */
    void service() throws ServletException, IOException;


    /**
     * Returns the selected servlet, initialized to provide access to sessions
     * and servlet context information.  Only valid to call if {@link #isFilterActive} returns false.
     **/
    Servlet getServlet() throws ServletException;


    /**
     * Returns the final response from the servlet. Note that this method should
     * only be invoked after all processing has been done to the servlet response.
     **/
    WebResponse getServletResponse() throws IOException;


    /**
     * Returns the target frame for the original request.
     * @since 1.6
     */
    FrameSelector getFrame();


    /**
     * Adds a request dispatcher to this context to simulate an include request.
     */
    void pushIncludeRequest( RequestDispatcher rd, HttpServletRequest request, HttpServletResponse response ) throws ServletException;


    /**
     * Adds a request dispatcher to this context to simulate a forward request.
     */
    void pushForwardRequest( RequestDispatcher rd, HttpServletRequest request, HttpServletResponse response ) throws ServletException;


    /**
     * Removes the top request dispatcher or filter from this context.
     */
    void popRequest();


    /**
     * Returns true if the current context is a filter, rather than a servlet.
     * @since 1.6
     */
    boolean isFilterActive();


    /**
     * Returns the current active filter object. Only valid to call if {@link #isFilterActive} returns true.
     * @since 1.6
     */
    Filter getFilter() throws ServletException;


    /**
     * Returns the current filter chain. Only valid to call if {@link #isFilterActive} returns true.
     * @since 1.6
     */
    FilterChain getFilterChain();


    /**
     * Pushes the current filter onto the execution stack and switches to the next filter or the selected servlet.
     * This can be used to simulate the effect of the {@link javax.servlet.FilterChain#doFilter doFilter} call.
     * <br><b>Note:</b> this method specifies {@link ServletRequest} and {@link ServletResponse} because those are the
     * types passed to {@link Filter#doFilter}; however, HttpUnit requires the objects to implement
     * {@link HttpServletRequest} and {@link HttpServletResponse} because they will eventually be passed to an
     * {@link javax.servlet.http.HttpServlet}.
     *
     * @param request the request to pass to the next filter. May be a wrapper.
     * @param response the response object to pass to the next filter. May be a wrapper.
     * @since 1.6
     */
    void pushFilter( ServletRequest request, ServletResponse response );

}
