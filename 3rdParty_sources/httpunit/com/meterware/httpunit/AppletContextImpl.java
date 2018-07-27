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
 *******************************************************************************************************************/import java.util.*;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.applet.Applet;
import java.net.URL;
import java.awt.*;
import java.io.InputStream;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
class AppletContextImpl implements AppletContext {

    private WebApplet _webApplet;


    AppletContextImpl( WebApplet webApplet ) {
        _webApplet = webApplet;
    }


    /**
     * Creates an audio clip.
     *
     * @param   url   an absolute URL giving the location of the audio clip.
     * @return  the audio clip at the specified URL.
     */
    public AudioClip getAudioClip( URL url ) {
        return null;
    }


    /**
     * Returns an <code>Image</code> object that can then be painted on
     * the screen. The <code>url</code> argument<code> </code>that is
     * passed as an argument must specify an absolute URL.
     * <p>
     * This method always returns immediately, whether or not the image
     * exists. When the applet attempts to draw the image on the screen,
     * the data will be loaded. The graphics primitives that draw the
     * image will incrementally paint on the screen.
     *
     * @param   url   an absolute URL giving the location of the image.
     * @return  the image at the specified URL.
     * @see     Image
     */
    public Image getImage( URL url ) {
        return null;
    }


    /**
     * Finds and returns the applet in the document represented by this
     * applet context with the given name. The name can be set in the
     * HTML tag by setting the <code>name</code> attribute.
     *
     * @param   name   an applet name.
     * @return  the applet with the given name, or <code>null</code> if
     *          not found.
     */
    public Applet getApplet( String name ) {
        try {
            WebApplet[] webApplets = _webApplet.getAppletsInPage();
            for (int i = 0; i < webApplets.length; i++) {
                if (webApplets[i].getName().equals( name )) return webApplets[i].getApplet();
            }
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * Finds all the applets in the document represented by this applet
     * context.
     *
     * @return  an enumeration of all applets in the document represented by
     *          this applet context.
     */
    public Enumeration getApplets() {
        WebApplet[] webApplets = _webApplet.getAppletsInPage();
        Vector v = new Vector();
        try {
            for (int i = 0; i < webApplets.length; i++) {
                v.add( webApplets[i].getApplet() );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException( e.toString() );
        }
        return v.elements();
    }


    /**
     * Replaces the Web page currently being viewed with the given URL.
     * This method may be ignored by applet contexts that are not
     * browsers.
     *
     * @param   url   an absolute URL giving the location of the document.
     */
    public void showDocument( URL url ) {
        showDocument( url, _webApplet.getBaseTarget() );
    }


    /**
     * Requests that the browser or applet viewer show the Web page
     * indicated by the <code>url</code> argument. The
     * <code>target</code> argument indicates in which HTML frame the
     * document is to be displayed.
     * The target argument is interpreted as follows:
     * <p>
     * <center><table border="3">
     * <tr><td><code>"_self"</code>  <td>Show in the window and frame that
     *                                   contain the applet.</tr>
     * <tr><td><code>"_parent"</code><td>Show in the applet's parent frame. If
     *                                   the applet's frame has no parent frame,
     *                                   acts the same as "_self".</tr>
     * <tr><td><code>"_top"</code>   <td>Show in the top-level frame of the applet's
     *                                   window. If the applet's frame is the
     *                                   top-level frame, acts the same as "_self".</tr>
     * <tr><td><code>"_blank"</code> <td>Show in a new, unnamed
     *                                   top-level window.</tr>
     * <tr><td><i>name</i><td>Show in the frame or window named <i>name</i>. If
     *                        a target named <i>name</i> does not already exist, a
     *                        new top-level window with the specified name is created,
     *                        and the document is shown there.</tr>
     * </table> </center>
     * <p>
     * An applet viewer or browser is free to ignore <code>showDocument</code>.
     *
     * @param   url   an absolute URL giving the location of the document.
     * @param   target   a <code>String</code> indicating where to display
     *                   the page.
     */
    public void showDocument( URL url, String target ) {
        _webApplet.sendRequest( url, target );
    }


    /**
     * Requests that the argument string be displayed in the
     * "status window". Many browsers and applet viewers
     * provide such a window, where the application can inform users of
     * its current state.
     *
     * @param   status   a string to display in the status window.
     */
    public void showStatus( String status ) {
    }


    /**
     * Returns the stream to which specified key is associated within this
     * applet context. Returns <tt>null</tt> if the applet context contains
     * no stream for this key.
     * <p>
     * For security reasons, mapping of streams and keys exists for each
     * codebase. In other words, applet from one codebase cannot access
     * the streams created by an applet from a different codebase
     * <p>
     * @return the stream to which this applet context maps the key
     * @param key key whose associated stream is to be returned.
     * @since JDK1.4
     */
    public InputStream getStream( String key ) {
        return null;
    }


    /**
     * Finds all the keys of the streams in this applet context.
     * <p>
     * For security reasons, mapping of streams and keys exists for each
     * codebase. In other words, applet from one codebase cannot access
     * the streams created by an applet from a different codebase
     * <p>
     * @return  an Iterator of all the names of the streams in this applet
     *          context.
     * @since JDK1.4
     */
    public Iterator getStreamKeys() {
        return null;
    }


    /**
     * Associates the specified stream with the specified key in this
     * applet context. If the applet context previously contained a mapping
     * for this key, the old value is replaced.
     * <p>
     * For security reasons, mapping of streams and keys exists for each
     * codebase. In other words, applet from one codebase cannot access
     * the streams created by an applet from a different codebase
     * <p>
     * @param key key with which the specified value is to be associated.
     * @param stream stream to be associated with the specified key. If this
     *               parameter is <code>null<code>, the specified key is removed
     *               in this applet context.
     * @throws <code>IOException</code> if the stream size exceeds a certain
     *         size limit. Size limit is decided by the implementor of this
     *         interface.
     * @since JDK1.4
     */
    public void setStream( String key, InputStream stream ) throws IOException {
    }
}
