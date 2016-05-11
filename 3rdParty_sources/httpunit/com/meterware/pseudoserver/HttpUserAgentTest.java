package com.meterware.pseudoserver;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2004, Russell Gold
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
import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.IOException;

import junit.framework.TestCase;


/**
 * A base class for test cases that use the pseudo server.
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class HttpUserAgentTest extends TestCase {

    private String _hostPath;
    private PseudoServer _server;


    public HttpUserAgentTest( String name ) {
        super( name );
    }


    public void setUp() throws Exception {
        _server = new PseudoServer();
        _hostPath = "http://localhost:" + _server.getConnectedPort();
    }


    public void tearDown() throws Exception {
        if (_server != null) _server.shutDown();
    }


    protected void defineResource( String resourceName, PseudoServlet servlet ) {
        _server.setResource( resourceName, servlet );
    }


    protected void defineResource( String resourceName, String value ) {
        _server.setResource( resourceName, value );
    }


    protected void defineResource( String resourceName, byte[] value, String contentType ) {
        _server.setResource( resourceName, value, contentType );
    }


    protected void defineResource( String resourceName, String value, int statusCode ) {
        _server.setErrorResource( resourceName, statusCode, value );
    }


    protected void defineResource( String resourceName, String value, String contentType ) {
        _server.setResource( resourceName, value, contentType );
    }


    protected void addResourceHeader( String resourceName, String header ) {
        _server.addResourceHeader( resourceName, header );
    }


    protected void setResourceCharSet( String resourceName, String setName, boolean reportCharSet ) {
        _server.setCharacterSet( resourceName, setName );
        _server.setSendCharacterSet( resourceName, reportCharSet );
    }

    /**
     * define a Web Page with the given page name and boy adding the html and body tags with pageName as the title of the page
     * use the given xml names space if it is not null 
     * @param xmlns
     * @param pageName
     * @param body
     */
    protected void defineWebPage( String xmlns,String pageName, String body ) {
    	String preamble="";
    	if (xmlns==null)
    		xmlns="";
    	else {
    		preamble ="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n";
      	preamble+="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
    		xmlns=" xmlns=\""+xmlns+"\"";
    	}	
      defineResource( pageName + ".html", preamble+"<html"+xmlns+">\n<head><title>" + pageName + "</title></head>\n" +
                                          "<body>\n" + body + "\n</body>\n</html>" );
  }

    /**
     * define a Web Page with the given page name and boy adding the html and body tags with pageName as the title of the page
     * @param pageName
     * @param body
     */
    protected void defineWebPage( String pageName, String body ) {
    	defineWebPage(null,pageName,body);
    }


    protected void mapToClasspath( String directory ) {
        _server.mapToClasspath( directory );
    }


    protected PseudoServer getServer() {
        return _server;
    }


    protected void setServerDebug( boolean enabled ) {
        _server.setDebug( enabled );
    }


    protected String getHostPath() {
        return _hostPath;
    }


    protected int getHostPort() throws IOException {
        return _server.getConnectedPort();
    }


    protected void assertEqualQueries( String query1, String query2 ) {
        assertEquals( new QuerySpec( query1 ), new QuerySpec( query2 ) );
    }



    protected void assertEquals( String comment, Object[] expected, Object[] found ) {
        if (!equals( expected, found )) {
            fail( comment + " expected: " + asText( expected ) + " but found " + asText( found ) );
        }
    }


    private boolean equals( Object[] first, Object[] second ) {
        if (first.length != second.length) return false;
        for (int i = 0; i < first.length; i++) {
            if (!first[ i ].equals( second[ i ] )) return false;
        }
        return true;
    }


    protected void assertImplement( String comment, Object[] objects, Class expectedClass ) {
        if (objects.length == 0) fail( "No " + comment + " found." );
        for (int i = 0; i < objects.length; i++) {
            assertImplements( comment, objects[i], expectedClass );
        }
    }


    protected void assertImplements( String comment, Object object, Class expectedClass ) {
        if (object == null) {
            fail( comment + " should be of class " + expectedClass.getName() + " but is null" );
        } else if (!expectedClass.isInstance( object )) {
            fail( comment + " should be of class " + expectedClass.getName() + " but is " + object.getClass().getName() );
        }
    }


    protected void assertMatchingSet( String comment, Object[] expected, Enumeration found ) {
        Vector foundItems = new Vector();
        while (found.hasMoreElements()) foundItems.addElement( found.nextElement() );

        assertMatchingSet( comment, expected, foundItems );
    }


    private void assertMatchingSet( String comment, Object[] expected, Vector foundItems ) {
        Vector expectedItems = new Vector();
        for (int i = 0; i < expected.length; i++) expectedItems.addElement( expected[ i ] );
        for (int i = 0; i < expected.length; i++) {
            if (!foundItems.contains( expected[ i ] )) {
                fail( comment + ": expected " + asText( expected ) + " but missing " + expected[ i ] );
            } else {
                foundItems.removeElement( expected[ i ] );
            }
        }

        if (!foundItems.isEmpty()) fail( comment + ": expected " + asText( expected ) + " but found superfluous" + foundItems.firstElement() );
    }


    public static void assertMatchingSet( String comment, Object[] expected, Object[] found ) {
        Vector foundItems = new Vector();
        for (int i = 0; i < found.length; i++) foundItems.addElement( found[ i ] );

        Vector expectedItems = new Vector();

        for (int i = 0; i < expected.length; i++) expectedItems.addElement( expected[ i ] );

        for (int i = 0; i < expected.length; i++) {
            if (!foundItems.contains( expected[ i ] )) {
                fail( comment + ": expected " + asText( expected ) + " but found " + asText( found ) );
            } else {
                foundItems.removeElement( expected[ i ] );
            }
        }

        for (int i = 0; i < found.length; i++) {
            if (!expectedItems.contains( found[ i ] )) {
                fail( comment + ": expected " + asText( expected ) + " but found " + asText( found ) );
            } else {
                expectedItems.removeElement( found[ i ] );
            }
        }

        if (!foundItems.isEmpty()) fail( comment + ": expected " + asText( expected ) + " but found " + asText( found ) );
    }


    public static String asText( Object[] args ) {
        StringBuffer sb = new StringBuffer( "{" );
        for (int i = 0; i < args.length; i++) {
            if (i != 0) sb.append( "," );
            sb.append( '"' ).append( args[ i ] ).append( '"' );
        }
        sb.append( "}" );
        return sb.toString();
    }


    protected String asBytes( String s ) {
        StringBuffer sb = new StringBuffer();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append( Integer.toHexString( chars[i] ) ).append( " " );
        }
        return sb.toString();
    }


    protected void assertEquals( String comment, byte[] expected, byte[] actual ) {
        if (!equals( expected, actual )) {
            fail( comment + " expected:\n" + toString( expected ) + ", but was:\n" + toString( actual ) );
        }
    }


    private boolean equals( byte[] first, byte[] second ) {
        if (first.length != second.length) return false;
        for (int i = 0; i < first.length; i++) {
            if (first[ i ] != second[ i ]) return false;
        }
        return true;
    }


    private String toString( byte[] message ) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < message.length; i++) {
            if (i != 0 && (i % 4) == 0) sb.append( ' ' );
            if (message[ i ] >= 0 && message[ i ] < 16) sb.append( '0' );
            sb.append( Integer.toHexString( 0xff & (int) message[ i ] ) );
        }
        return sb.toString();
    }


    static class QuerySpec {
        QuerySpec( String urlString ) {
            if (urlString.indexOf( '?' ) < 0) {
                _path = urlString;
            } else {
                _path = urlString.substring( 0, urlString.indexOf( '?' ) );
            }
            _fullString = urlString;

            StringTokenizer st = new StringTokenizer( urlString.substring( urlString.indexOf( '?' )+ 1 ), "&" );
            while (st.hasMoreTokens()) _parameters.addElement( st.nextToken() );
        }

        public String toString() {
            return _fullString;
        }

        public boolean equals( Object o ) {
            return getClass().equals( o.getClass() ) && equals( (QuerySpec) o );
        }


        public int hashCode() {
            return _path.hashCode() ^ _parameters.size();
        }


        private String _path;
        private String _fullString;
        private Vector _parameters = new Vector();

        private boolean equals( QuerySpec o ) {
            if (!_path.equals( o._path )) {
                return false;
            } else if (_parameters.size() != o._parameters.size() ) {
                return false;
            } else {
                for (Enumeration e = o._parameters.elements(); e.hasMoreElements();) {
                    if (!_parameters.contains( e.nextElement() )) return false;
                }
                return true;
            }
        }
    }
}


