package com.meterware.pseudoserver;
/********************************************************************************************************************

*
* Copyright (c) 2000-2003, Russell Gold
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
import java.io.*;

import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;

import com.meterware.httpunit.HttpUnitUtils;

/**
 * A basic simulated web-server for testing user agents without a web server.
 **/
public class PseudoServer {

    private static final int DEFAULT_SOCKET_TIMEOUT = 1000;

    private static final int INPUT_POLL_INTERVAL = 10;

    /** Time in msec to wait for an outstanding server socket to be released before creating a new one. **/
    private static int _socketReleaseWaitTime = 50;

    /** Number of outstanding server sockets that must be present before trying to wait for one to be released. **/
    private static int _waitThreshhold = 10;

    private static int _numServers = 0;

    private int _serverNum = 0;

    private int _connectionNum = 0;


    private ArrayList _classpathDirs = new ArrayList();

    private String _maxProtocolLevel = "1.1";


    private final int _socketTimeout;


    /**
     * Returns the amount of time the pseudo server will wait for a server socket to be released (in msec)
     * before allocating a new one. See also {@link #getWaitThreshhold getWaitThreshhold}.
     */
    public static int getSocketReleaseWaitTime() {
        return _socketReleaseWaitTime;
    }


    /**
     * Returns the amount of time the pseudo server will wait for a server socket to be released (in msec)
     * before allocating a new one. See also {@link #getWaitThreshhold getWaitThreshhold}.
     */
    public static void setSocketReleaseWaitTime( int socketReleaseWaitTime ) {
        _socketReleaseWaitTime = socketReleaseWaitTime;
    }


    /**
     * Returns the number of server sockets that must have been allocated and not returned before waiting for one
     * to be returned.
     */
    public static int getWaitThreshhold() {
        return _waitThreshhold;
    }


    /**
     * Specifies the number of server sockets that must have been allocated and not returned before waiting for one
     * to be returned.
     */
    public static void setWaitThreshhold( int waitThreshhold ) {
        _waitThreshhold = waitThreshhold;
    }


    public PseudoServer() {
        this( DEFAULT_SOCKET_TIMEOUT );
    }


    public PseudoServer( int socketTimeout ) {
        _socketTimeout = socketTimeout;
        _serverNum = ++_numServers;
        Thread t = new Thread( "PseudoServer " + _serverNum ) {
            public void run() {
                while (_active) {
                    try {
                        handleNewConnection( getServerSocket().accept() );
                        Thread.sleep( 20 );
                    } catch (InterruptedIOException e) {
                    } catch (IOException e) {
                        System.out.println( "Error in pseudo server: " + e );
                        HttpUnitUtils.handleException(e);
                    } catch (InterruptedException e) {
                        System.out.println( "Interrupted. Shutting down" );
                        _active = false;
                    }
                }
        		try {
                    if (_serverSocket != null) ServerSocketFactory.release( _serverSocket );
                    _serverSocket = null;
                } catch (IOException e) {
                }
                debug( "Pseudoserver shutting down" );
            }
        };
        debug( "Starting pseudoserver" );
        t.start();
    }


    public void shutDown() {
        debug( "Requested shutdown of pseudoserver" );
        _active = false;
    }


    private void debug( String message ) {
        if (!_debug) return;
        message = replaceDebugToken( message, "thread", "thread (" + Thread.currentThread().getName() + ")" );
        message = replaceDebugToken( message, "server", "server " + _serverNum );
        System.out.println( "** " + message );
    }


    private static String replaceDebugToken( String message, String token, String replacement ) {
        return message.indexOf( token ) < 0 ? message : message.replaceFirst( token, replacement );
    }


    public void setMaxProtocolLevel( int majorLevel, int minorLevel ) {
        _maxProtocolLevel = majorLevel + "." + minorLevel;
    }


    /**
     * Returns the port on which this server is listening.
     **/
    public int getConnectedPort() throws IOException {
        return getServerSocket().getLocalPort();
    }


    /**
     * Defines the contents of an expected resource.
     **/
    public void setResource( String name, String value ) {
        setResource( name, value, "text/html" );
    }


    /**
     * Defines the contents of an expected resource.
     **/
    public void setResource( String name, PseudoServlet servlet ) {
        _resources.put( asResourceName( name ), servlet );
    }


    /**
     * Defines the contents of an expected resource.
     **/
    public void setResource( String name, String value, String contentType ) {
        _resources.put( asResourceName( name ), new WebResource( value, contentType ) );
    }


    /**
     * Defines the contents of an expected resource.
     **/
    public void setResource( String name, byte[] value, String contentType ) {
        _resources.put( asResourceName( name ), new WebResource( value, contentType ) );
    }


    /**
     * Defines a resource which will result in an error message.
     **/
    public void setErrorResource( String name, int errorCode, String errorMessage ) {
        _resources.put( asResourceName( name ), new WebResource( errorMessage, errorCode ) );
    }


    /**
     * Enables the sending of the character set in the content-type header.
     **/
    public void setSendCharacterSet( String name, boolean enabled ) {
        WebResource resource = (WebResource) _resources.get( asResourceName( name ) );
        if (resource == null) throw new IllegalArgumentException( "No defined resource " + name );
        resource.setSendCharacterSet( enabled );
    }


    /**
     * Specifies the character set encoding for a resource.
     **/
    public void setCharacterSet( String name, String characterSet ) {
        WebResource resource = (WebResource) _resources.get( asResourceName( name ) );
        if (resource == null) {
            resource = new WebResource( "" );
            _resources.put( asResourceName( name ), resource );
        }
        resource.setCharacterSet( characterSet );
    }


    /**
     * Adds a header to a defined resource.
     **/
    public void addResourceHeader( String name, String header ) {
        WebResource resource = (WebResource) _resources.get( asResourceName( name ) );
        if (resource == null) {
            resource = new WebResource( "" );
            _resources.put( asResourceName( name ), resource );
        }
        resource.addHeader( header );
    }


    public void mapToClasspath( String directory ) {
        _classpathDirs.add( directory );
    }


    public void setDebug( boolean debug ) {
        _debug = debug;
    }


//------------------------------------- private members ---------------------------------------

    private Hashtable _resources = new Hashtable();

    private boolean _active = true;

    private boolean _debug;


    private String asResourceName( String rawName ) {
        if (rawName.startsWith( "http:" ) || rawName.startsWith( "/" )) {
            return escape( rawName );
        } else {
            return escape( "/" + rawName );
        }
    }


    private static String escape( String urlString ) {
        if (urlString.indexOf( ' ' ) < 0) return urlString;
        StringBuffer sb = new StringBuffer();

        int start = 0;
        do {
            int index = urlString.indexOf( ' ', start );
            if (index < 0) {
                sb.append( urlString.substring( start ) );
                break;
            } else {
                sb.append( urlString.substring( start, index ) ).append( "%20" );
                start = index+1;
            }
        } while (true);
        return sb.toString();
    }


    private void handleNewConnection( final Socket socket ) {
        Thread t = new Thread( "PseudoServer " + _serverNum + " connection " + (++_connectionNum) ) {
            public void run() {
                try {
                    serveRequests( socket );
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                }
            }
        };
        t.start();
    }


    private void serveRequests( Socket socket ) throws IOException {
        socket.setSoTimeout( _socketTimeout );
        socket.setTcpNoDelay( true );

        debug( "Created server thread " + socket.getInetAddress() + ':' + socket.getPort() );
        final BufferedInputStream inputStream = new BufferedInputStream( socket.getInputStream() );
        final HttpResponseStream outputStream = new HttpResponseStream( socket.getOutputStream() );

        try {
            while (_active) {
                HttpRequest request = new HttpRequest( inputStream );
                boolean keepAlive = respondToRequest( request, outputStream );
                if (!keepAlive) break;
                while (_active && 0 == inputStream.available()) {
                    try { Thread.sleep( INPUT_POLL_INTERVAL ); } catch (InterruptedException e) {}
                }
            }
        } catch (IOException e) {
            outputStream.restart();
            outputStream.setProtocol( "HTTP/1.0" );
            outputStream.setResponse( HttpURLConnection.HTTP_BAD_REQUEST, e.toString() );
        }
        debug( "Closing server thread" );
        outputStream.close();
        socket.close();
        debug( "Server thread closed" );
    }


    private boolean respondToRequest( HttpRequest request, HttpResponseStream response ) {
        debug( "Server thread handling request: " + request );
        boolean keepAlive = isKeepAlive( request );
        WebResource resource = null;
        try {
            response.restart();
            response.setProtocol( getResponseProtocol( request ) );
            resource = getResource( request );
            if (resource == null) {
                response.setResponse( HttpURLConnection.HTTP_NOT_FOUND, "unable to find " + request.getURI() );
            } else {
                if (resource.closesConnection()) keepAlive = false;
                if (resource.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    response.setResponse( resource.getResponseCode(), "" );
                }
                String[] headers = resource.getHeaders();
                for (int i = 0; i < headers.length; i++) {
                    debug( "Server thread sending header: " + headers[i] );
                    response.addHeader( headers[i] );
                }
            }
        } catch (UnknownMethodException e) {
            response.setResponse( HttpURLConnection.HTTP_BAD_METHOD, "unsupported method: " + e.getMethod() );
        } catch (Throwable t) {
            t.printStackTrace();
            response.setResponse( HttpURLConnection.HTTP_INTERNAL_ERROR, t.toString() );
        }
        try { response.write( resource ); } catch (IOException e) { System.out.println( "*** Failed to send reply: " + e ); }
        return keepAlive;
    }


    private boolean isKeepAlive( HttpRequest request ) {
        return request.wantsKeepAlive() && _maxProtocolLevel.equals( "1.1" );
    }


    private String getResponseProtocol( HttpRequest request ) {
        return _maxProtocolLevel.equalsIgnoreCase( "1.1" ) ? request.getProtocol() : "HTTP/1.0";
    }


    private WebResource getResource( HttpRequest request ) throws IOException {
        Object resource = _resources.get( request.getURI() );
        if (resource == null) resource = _resources.get( withoutParameters( request.getURI() ) );

        if (request.getCommand().equals( "GET" ) && resource instanceof WebResource) {
            return (WebResource) resource;
        } else if (resource instanceof PseudoServlet) {
            return getResource( (PseudoServlet) resource, request );
        } else if (request.getURI().endsWith( ".class" )) {
            for (Iterator iterator = _classpathDirs.iterator(); iterator.hasNext();) {
                String directory = (String) iterator.next();
                if (request.getURI().startsWith( directory )) {
                    String resourceName = request.getURI().substring( directory.length()+1 );
                    return new WebResource( getClass().getClassLoader().getResourceAsStream( resourceName ), "application/class", 200 );
                }
            }
            return null;
        } else if (request.getURI().endsWith( ".zip" ) || request.getURI().endsWith( ".jar" )) {
            for (Iterator iterator = _classpathDirs.iterator(); iterator.hasNext();) {
                String directory = (String) iterator.next();
                if (request.getURI().startsWith( directory )) {
                    String resourceName = request.getURI().substring( directory.length()+1 );
                    String classPath = System.getProperty( "java.class.path" );
                    StringTokenizer st = new StringTokenizer( classPath, ":;," );
                    while (st.hasMoreTokens()) {
                        String file = st.nextToken();
                        if (file.endsWith( resourceName )) {
                            File f = new File( file );
                            return new WebResource( new FileInputStream( f ), "application/zip", 200 );
                        }
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }


    private String withoutParameters( String uri ) {
        return uri.indexOf( '?' ) < 0 ? uri : uri.substring( 0, uri.indexOf( '?' ) );
    }


    private WebResource getResource( PseudoServlet servlet, HttpRequest request ) throws IOException {
        servlet.init( request );
        return servlet.getResponse( request.getCommand() );
    }


    private ServerSocket getServerSocket() throws IOException {
        synchronized (this) {
            if (_serverSocket == null) _serverSocket = ServerSocketFactory.newServerSocket();
        }
        return _serverSocket;
    }


    private ServerSocket _serverSocket;

}




class HttpResponseStream {

    final private static String CRLF = "\r\n";

    void restart() {
        _headersWritten = false;
        _headers.clear();
        _responseCode = HttpURLConnection.HTTP_OK;
        _responseText = "OK";
    }


    void close() throws IOException {
        flushHeaders();
        _pw.close();
    }


    HttpResponseStream( OutputStream stream ) {
        _stream = stream;
        try {
            setCharacterSet( "us-ascii" );
        } catch (UnsupportedEncodingException e) {
            _pw = new PrintWriter( new OutputStreamWriter( _stream ) );
        }
    }


    void setProtocol( String protocol ) {
        _protocol = protocol;
    }


    void setResponse( int responseCode, String responseText ) {
        _responseCode = responseCode;
        _responseText = responseText;
    }


    void addHeader( String header ) {
        _headers.addElement( header );
    }


    void write( String contents, String charset ) throws IOException {
        flushHeaders();
        setCharacterSet( charset );
        sendText( contents );
    }


    void write( WebResource resource ) throws IOException {
        flushHeaders();
        if (resource != null) resource.writeTo( _stream );
        _stream.flush();
    }


    private void setCharacterSet( String characterSet ) throws UnsupportedEncodingException {
        if (_pw != null) _pw.flush();
        _pw = new PrintWriter( new OutputStreamWriter( _stream, characterSet ) );
    }


    private void flushHeaders() {
        if (!_headersWritten) {
            sendResponse( _responseCode, _responseText );
            for (Enumeration e = _headers.elements(); e.hasMoreElements();) {
                sendLine( (String) e.nextElement() );
            }
            sendText( CRLF );
            _headersWritten = true;
            _pw.flush();
        }
    }


    private void sendResponse( int responseCode, String responseText ) {
        sendLine( _protocol + ' ' + responseCode + ' ' + responseText );
    }


    private void sendLine( String text ) {
        sendText( text );
        sendText( CRLF );
    }


    private void sendText( String text ) {
        _pw.write( text );
    }


    private OutputStream _stream;
    private PrintWriter _pw;

    private Vector    _headers = new Vector();
    private String    _protocol = "HTTP/1.0";
    private int       _responseCode = HttpURLConnection.HTTP_OK;
    private String    _responseText = "OK";

    private boolean   _headersWritten;

}



class ServerSocketFactory {

    static private ArrayList _sockets = new ArrayList();

    static private int _outstandingSockets;

    static private Object _releaseSemaphore = new Object();

    static synchronized ServerSocket newServerSocket() throws IOException {
        if (_sockets.isEmpty() && _outstandingSockets > PseudoServer.getWaitThreshhold()) {
            try { synchronized( _releaseSemaphore) {_releaseSemaphore.wait( PseudoServer.getSocketReleaseWaitTime() ); } } catch (InterruptedException e) {};
        }
        _outstandingSockets++;
        if (!_sockets.isEmpty()) {
            return (ServerSocket) _sockets.remove(0);
        } else {
            ServerSocket serverSocket = new ServerSocket(0);
            serverSocket.setSoTimeout( 1000 );
            return serverSocket;
        }
    }

    static synchronized void release( ServerSocket serverSocket ) throws IOException {
        if (_sockets.size() >= 2 * PseudoServer.getWaitThreshhold()) {
            serverSocket.close();
        } else {
            _sockets.add( serverSocket );
            _outstandingSockets--;
            synchronized (_releaseSemaphore) { _releaseSemaphore.notify(); }
        }
    }
}


class RecordingOutputStream extends OutputStream {

    private OutputStream _nestedStream;
    private PrintStream _log;


    public RecordingOutputStream( OutputStream nestedStream, PrintStream log ) {
        _nestedStream = nestedStream;
        _log = log;
    }


    public void write( int b ) throws IOException {
        _nestedStream.write( b );
        _log.println( "sending " + Integer.toHexString( b ) );
    }


    public void write( byte b[], int offset, int len ) throws IOException {
        _nestedStream.write( b, offset, len );
        _log.print( "sending" );
        for (int i = offset; i < offset+len; i++) {
            _log.print( ' ' + Integer.toHexString( b[i] ) );
        }
        _log.println();
    }
}


class RecordingInputStream extends InputStream {

    private InputStream _nestedStream;
    private PrintStream _log;


    public RecordingInputStream( InputStream nestedStream, PrintStream log ) {
        _nestedStream = nestedStream;
        _log = log;
    }


    public int read() throws IOException {
        int value = _nestedStream.read();
        if (value != -1) _log.print( ' ' + Integer.toHexString( value ) );
        return value;
    }
}