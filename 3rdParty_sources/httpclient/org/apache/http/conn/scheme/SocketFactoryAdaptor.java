/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 * @deprecated (4.1) do not use
 */
@Deprecated
class SocketFactoryAdaptor implements SocketFactory {

    private final SchemeSocketFactory factory;

    SocketFactoryAdaptor(final SchemeSocketFactory factory) {
        super();
        this.factory = factory;
    }

    public Socket createSocket() throws IOException {
        HttpParams params = new BasicHttpParams();
        return this.factory.createSocket(params);
    }

    public Socket connectSocket(
            final Socket socket,
            final String host, int port,
            final InetAddress localAddress, int localPort,
            final HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        InetSocketAddress local = null;
        if (localAddress != null || localPort > 0) {
            // we need to bind explicitly
            if (localPort < 0) {
                localPort = 0; // indicates "any"
            }
            local = new InetSocketAddress(localAddress, localPort);
        }
        InetAddress remoteAddress = InetAddress.getByName(host);
        InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
        return this.factory.connectSocket(socket, remote, local, params);
    }

    public boolean isSecure(final Socket socket) throws IllegalArgumentException {
        return this.factory.isSecure(socket);
    }

    public SchemeSocketFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof SocketFactoryAdaptor) {
            return this.factory.equals(((SocketFactoryAdaptor)obj).factory);
        } else {
            return this.factory.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return this.factory.hashCode();
    }

}
