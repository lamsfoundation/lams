package com.meterware.httpunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002, Russell Gold
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
import java.applet.AppletStub;
import java.applet.AppletContext;
import java.net.URL;

class AppletStubImpl implements AppletStub {

    private WebApplet _webApplet;


    AppletStubImpl( WebApplet webApplet ) {
        _webApplet = webApplet;
    }


    /**
     * Determines if the applet is active. An applet is active just
     * before its <code>start</code> method is called. It becomes
     * inactive just before its <code>stop</code> method is called.
     *
     * @return  <code>true</code> if the applet is active;
     *          <code>false</code> otherwise.
     */
    public boolean isActive() {
        return false;
    }


    /**
     * Returns an absolute URL naming the directory of the document in which
     * the applet is embedded.  For example, suppose an applet is contained
     * within the document:
     * <blockquote><pre>
     *    http://java.sun.com/products/jdk/1.2/index.html
     * </pre></blockquote>
     * The document base is:
     * <blockquote><pre>
     *    http://java.sun.com/products/jdk/1.2/
     * </pre></blockquote>
     *
     * @return  the {@link URL} of the document that contains this
     *          applet.
     * @see     AppletStub#getCodeBase()
     */
    public URL getDocumentBase() {
        return null;
    }


    /**
     * Gets the base URL.
     *
     * @return  the <code>URL</code> of the applet.
     */
    public URL getCodeBase() {
        return null;
    }


    /**
     * Returns the value of the named parameter in the HTML tag. For
     * example, if an applet is specified as
     * <blockquote><pre>
     * &lt;applet code="Clock" width=50 height=50&gt;
     * &lt;param name=Color value="blue"&gt;
     * &lt;/applet&gt;
     * </pre></blockquote>
     * <p>
     * then a call to <code>getParameter("Color")</code> returns the
     * value <code>"blue"</code>.
     *
     * @param   name   a parameter name.
     * @return  the value of the named parameter,
     * or <tt>null</tt> if not set.
     */
    public String getParameter( String name ) {
        return _webApplet.getParameter( name );
    }


    /**
     * Gets a handler to the applet's context.
     *
     * @return  the applet's context.
     */
    public AppletContext getAppletContext() {
        return new AppletContextImpl( _webApplet );
    }


    /**
     * Called when the applet wants to be resized.
     *
     * @param   width    the new requested width for the applet.
     * @param   height   the new requested height for the applet.
     */
    public void appletResize( int width, int height ) {
    }
}
