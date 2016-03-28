/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.ws.soap.client.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import net.jcip.annotations.ThreadSafe;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/** An SSL/TLS socket factory that uses KeyStoreFactory's to get its key and trust material. */
@ThreadSafe
public class TLSProtocolSocketFactory implements SecureProtocolSocketFactory {

    /** Managers used to retrieve client-cert authentication keys for a given host. */
    private X509KeyManager[] keyManagers;

    /** Managers used to validate the X.509 credentials of a given host. */
    private X509TrustManager[] trustManagers;
    
    /** The randomness generator to use when creating SSL sockets.*/
    private SecureRandom secureRandom;
    
    /** Hostname verifier used to validate the peer's certificate against the hostname. */
    private HostnameVerifier hostnameVerifier;
    
    /** Currently active SSL context. */
    private SSLContext sslContext;
    
    /**
     * Constructor.
     * 
     * @param keyMgr manager used to retrieve client-cert authentication keys for a given host
     * @param trustMgr manager used to validate the X.509 credentials of a given host. May be null, in which case
     *          the JSSE default trust manager lookup mechanism is used.
     * 
     * @throws IllegalArgumentException thrown if the given key or trust manager can not be used to create the
     *             {@link SSLContext} used to create new sockets
     */
    public TLSProtocolSocketFactory(X509KeyManager keyMgr, X509TrustManager trustMgr) throws IllegalArgumentException {
        this(keyMgr, trustMgr, null);
    }
    
    /**
     * Constructor.
     * 
     * @param keyMgr manager used to retrieve client-cert authentication keys for a given host.
     * @param trustMgr manager used to validate the X.509 credentials of a given host. May be null, in which case
     *          the JSSE default trust manager lookup mechanism is used.
     * @param verifier the hostname verifier used to verify the SSL/TLS's peer's hostname. May be null, in which case
     *          no hostname verification is performed.
     * 
     * @throws IllegalArgumentException thrown if the given key or trust manager can not be used to create the
     *             {@link SSLContext} used to create new sockets
     */
    public TLSProtocolSocketFactory(X509KeyManager keyMgr, X509TrustManager trustMgr, HostnameVerifier verifier) 
            throws IllegalArgumentException {
        
        keyManagers = new X509KeyManager[] { keyMgr };
                
        // Note: There is a huge difference with SSLContext.init between:
        //    1) passing a null for TrustManager[]
        //    2) passing a TrustManager[] that contains 1 null member.
        //
        // The former causes the default trust manager set to be used. That's what we want 
        // if we TLS peer authN to happen (in the default way).
        // The latter effectively disables trust processing entirely (but not in the way we'd probably want).
        // So we need to make sure we don't do the latter.
        if (trustMgr != null) {
            trustManagers = new X509TrustManager[] { trustMgr };
        } else {
            trustManagers = null;
        }
        
        hostnameVerifier = verifier;
        
        secureRandom = null;
        
        init();
    }
    
    /**
     * Constructor.
     * 
     * @param keyMgrs managers used to retrieve client-cert authentication keys for a given host. 
     *          May be null, in which case the JSSE default key manager lookup mechanism is used.
     * @param trustMgrs manager used to validate the X.509 credentials of a given host.
     *          May be null, in which case the JSSE default trust manager lookup mechanism is used.
     * @param verifier the hostname verifier used to verify the SSL/TLS's peer's hostname. 
     *          May be null, in which case no hostname verification is performed.
     * @param random the secure random instance used to create SSL sessions.
     *          May be null, in which case the JSSE default secure random impl is used.
     * 
     * @throws IllegalArgumentException thrown if the given key or trust manager can not be used to create the
     *             {@link SSLContext} used to create new sockets
     */
    public TLSProtocolSocketFactory(X509KeyManager[] keyMgrs, X509TrustManager[] trustMgrs, HostnameVerifier verifier, 
            SecureRandom random) throws IllegalArgumentException {
        
        keyManagers = keyMgrs;
        trustManagers = trustMgrs;
        hostnameVerifier = verifier;
        secureRandom = random;
        
        init();
    }
    
    /**
     * Do initialization that is common across constructors.
     * 
     * @throws IllegalArgumentException thrown if the given key or trust manager can not be used to create the
     *             {@link SSLContext} used to create new sockets
     */
    protected void init() throws IllegalArgumentException {
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(keyManagers, trustManagers, secureRandom);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Error create SSL context", e);
        }
    }

    /** {@inheritDoc} */
    public Socket createSocket(String host, int port) throws IOException {
        Socket socket = sslContext.getSocketFactory().createSocket(host, port);
        verifyHostname(socket, host);
        return socket;
    }

    /** {@inheritDoc} */
    public Socket createSocket(String host, int port, InetAddress localHost, int clientPort) throws IOException {
        Socket socket = sslContext.getSocketFactory().createSocket(host, port, localHost, clientPort);
        verifyHostname(socket, host);
        return socket;
    }

    /** {@inheritDoc} */
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        Socket newSocket = sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        verifyHostname(socket, host);
        return newSocket;
    }

    /** {@inheritDoc} */
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort,
            HttpConnectionParams connParams) throws IOException {
        if (connParams == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = connParams.getConnectionTimeout();
        SocketFactory socketfactory = sslContext.getSocketFactory();
        if (timeout == 0) {
            Socket socket = socketfactory.createSocket(host, port, localHost, localPort);
            verifyHostname(socket, host);
            return socket;
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localHost, localPort);
            SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
            verifyHostname(socket, host);
            return socket;
        }
    }

    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        return (obj != null) && obj.getClass().equals(getClass());
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return getClass().hashCode();
    }
    
    /**
     * Verifies the peer's hostname using the configured {@link HostnameVerifier}.
     * 
     * @param socket the socket connected to the peer whose hostname is to be verified.
     * 
     * @throws SSLException if the hostname does not verify against the peer's certificate, 
     *          or if there is an error in performing the evaluation
     *          
     * @deprecated Use instead {@link #verifyHostname(Socket, String)
     */
    protected void verifyHostname(Socket socket) throws SSLException {
        if (hostnameVerifier == null) {
            return;
        }
        
        if (!(socket instanceof SSLSocket)) {
            return;
        }
        
        SSLSocket sslSocket = (SSLSocket) socket;
        
        try {
            SSLSession sslSession = sslSocket.getSession();
            if (!sslSession.isValid()) {
                throw new SSLException("SSLSession was invalid: Likely implicit handshake failure: " 
                        + "Set system property javax.net.debug=all for details");
            }
            
            verifyHostname(sslSocket, sslSession.getPeerHost());
        } catch (SSLException e) {
            cleanUpFailedSocket(sslSocket);
            throw e;
        } catch (Throwable t) {
            // Make sure we close the socket on any kind of Exception, RuntimeException or Error.
            cleanUpFailedSocket(sslSocket);
            throw new SSLException("Error in deprecated verifyHostname(Socket)", t);
        }
    }
    
    /**
     * Verifies the peer's hostname using the configured {@link HostnameVerifier}.
     * 
     * @param socket the socket connected to the peer whose hostname is to be verified.
     * @param hostname the caller-supplied hostname to verify
     * 
     * @throws SSLException if the hostname does not verify against the peer's certificate, 
     *          or if there is an error in performing the evaluation
     */
    protected void verifyHostname(Socket socket, String hostname) throws SSLException {
        if (hostnameVerifier == null) {
            return;
        }
        
        if (hostname == null) {
            throw new SSLException("Supplied hostname was null, skipping hostname verification and terminating");
        }
        
        if (!(socket instanceof SSLSocket)) {
            return;
        }
        
        SSLSocket sslSocket = (SSLSocket) socket;
        
        try {
            SSLSession sslSession = sslSocket.getSession();
            if (!sslSession.isValid()) {
                throw new SSLException("SSLSession was invalid: Likely implicit handshake failure: " 
                        + "Set system property javax.net.debug=all for details");
            }
            
            if (!hostnameVerifier.verify(hostname, sslSession)) {
                throw new SSLPeerUnverifiedException("SSL peer failed hostname validation for name: " + hostname);
            }
        } catch (SSLException e) {
            cleanUpFailedSocket(sslSocket);
            throw e;
        } catch (Throwable t) {
            // Make sure we close the socket on any kind of Exception, RuntimeException or Error.
            cleanUpFailedSocket(sslSocket);
            throw new SSLException("Error in hostname verification", t);
        }
    }
    
    /**
     * Do any cleanup necessary due to socket creation failure (e.g. due to hostname validation failure).
     * 
     * @param sslSocket the {@link SSLSocket} to cleanup
     */
    protected void cleanUpFailedSocket(SSLSocket sslSocket) {
        try {
            sslSocket.close();
        } catch (IOException e) {
            // Do nothing. We haven't returned it yet, so can just ignore.
        }
    }
}