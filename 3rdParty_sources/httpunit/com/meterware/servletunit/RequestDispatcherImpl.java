package com.meterware.servletunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2003, Russell Gold
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
import java.io.IOException;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
class RequestDispatcherImpl extends RequestContext implements RequestDispatcher {

    private ServletMetaData _servletMetaData;


    RequestDispatcherImpl( WebApplication application, URL url ) throws ServletException {
        super( url );
        _servletMetaData = application.getServletRequest( url );
    }


    public ServletMetaData getServletMetaData() {
        return _servletMetaData;
    }


    public void forward( ServletRequest request, ServletResponse response ) throws ServletException, IOException {
        response.reset();
        _servletMetaData.getServlet().service( DispatchedRequestWrapper.createForwardRequestWrapper( (HttpServletRequest) request, this ), response );
    }


    public void include( ServletRequest request, ServletResponse response ) throws ServletException, IOException {
        _servletMetaData.getServlet().service( DispatchedRequestWrapper.createIncludeRequestWrapper( (HttpServletRequest) request, this ), response );
    }
}
