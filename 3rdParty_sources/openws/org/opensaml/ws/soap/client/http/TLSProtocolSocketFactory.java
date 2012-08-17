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

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import net.jcip.annotations.ThreadSafe;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/** An SSL/TLS socket factory that uses KeyStoreFactory's to get its key and trust material. */
@ThreadSafe
public class TLSProtocolSocketFactory implements SecureProtocolSocketFactory {

    /** Manager used to retrieve client-cert authentication keys for a given host. */
    private X509KeyManager keyManager;

    /** Manager used to validate the X.509 credentials of a given host. */
    private X509TrustManager trustManager;

    /** Currently active SSL context. */
    private SSLContext sslContext;

    /**
     * Constructor.
     * 
     * @param keyMgr manager used to retrieve client-cert authentication keys for a given host
     * @param trustMgr manager used to validate the X.509 credentials of a given host
     * 
     * @throws IllegalArgumentException thrown if the given key or trust manager can not be used to create the
     *             {@link SSLContext} used to create new sockets
     */
    public TLSProtocolSocketFactory(X509KeyManager keyMgr, X509TrustManager trustMgr) throws IllegalArgumentException {
        keyManager = keyMgr;
        trustManager = trustMgr;

        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(new KeyManager[] { keyManager }, new TrustManager[] { trustManager }, null);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Error create SSL context", e);
        }
    }

    /** {@inheritDoc} */
    public Socket createSocket(String host, int port) throws IOException {
        return sslContext.getSocketFactory().createSocket(host, port);
    }

    /** {@inheritDoc} */
    public Socket createSocket(String host, int port, InetAddress localHost, int clientPort) throws IOException {
        return sslContext.getSocketFactory().createSocket(host, port, localHost, clientPort);
    }

    /** {@inheritDoc} */
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
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
            return socketfactory.createSocket(host, port, localHost, localPort);
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localHost, localPort);
            SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
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
}