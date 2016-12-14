/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.conn.DnsResolver;

/**
 * Manages a pool of {@link OperatedClientConnection client connections} and
 * is able to service connection requests from multiple execution threads.
 * Connections are pooled on a per route basis. A request for a route which
 * already the manager has persistent connections for available in the pool
 * will be services by leasing a connection from the pool rather than
 * creating a brand new connection.
 * <p>
 * PoolingConnectionManager maintains a maximum limit of connection on
 * a per route basis and in total. Per default this implementation will
 * create no more than than 2 concurrent connections per given route
 * and no more 20 connections in total. For many real-world applications
 * these limits may prove too constraining, especially if they use HTTP
 * as a transport protocol for their services. Connection limits, however,
 * can be adjusted using HTTP parameters.
 *
 * @since 4.2
 */
@ThreadSafe
public class PoolingClientConnectionManager implements ClientConnectionManager, ConnPoolControl<HttpRoute> {

    private final Log log = LogFactory.getLog(getClass());

    private final SchemeRegistry schemeRegistry;

    private final HttpConnPool pool;

    private final ClientConnectionOperator operator;

    /** the custom-configured DNS lookup mechanism. */
    private final DnsResolver dnsResolver;

    public PoolingClientConnectionManager(final SchemeRegistry schreg) {
        this(schreg, -1, TimeUnit.MILLISECONDS);
    }

    public PoolingClientConnectionManager(final SchemeRegistry schreg,final DnsResolver dnsResolver) {
        this(schreg, -1, TimeUnit.MILLISECONDS,dnsResolver);
    }

    public PoolingClientConnectionManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    public PoolingClientConnectionManager(
            final SchemeRegistry schemeRegistry,
            final long timeToLive, final TimeUnit tunit) {
        this(schemeRegistry, timeToLive, tunit, new SystemDefaultDnsResolver());
    }

    public PoolingClientConnectionManager(final SchemeRegistry schemeRegistry,
                final long timeToLive, final TimeUnit tunit,
                final DnsResolver dnsResolver) {
        super();
        if (schemeRegistry == null) {
            throw new IllegalArgumentException("Scheme registry may not be null");
        }
        if (dnsResolver == null) {
            throw new IllegalArgumentException("DNS resolver may not be null");
        }
        this.schemeRegistry = schemeRegistry;
        this.dnsResolver  = dnsResolver;
        this.operator = createConnectionOperator(schemeRegistry);
        this.pool = new HttpConnPool(this.log, 2, 20, timeToLive, tunit);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    /**
     * Hook for creating the connection operator.
     * It is called by the constructor.
     * Derived classes can override this method to change the
     * instantiation of the operator.
     * The default implementation here instantiates
     * {@link DefaultClientConnectionOperator DefaultClientConnectionOperator}.
     *
     * @param schreg    the scheme registry.
     *
     * @return  the connection operator to use
     */
    protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
            return new DefaultClientConnectionOperator(schreg, this.dnsResolver);
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    private String format(final HttpRoute route, final Object state) {
        StringBuilder buf = new StringBuilder();
        buf.append("[route: ").append(route).append("]");
        if (state != null) {
            buf.append("[state: ").append(state).append("]");
        }
        return buf.toString();
    }

    private String formatStats(final HttpRoute route) {
        StringBuilder buf = new StringBuilder();
        PoolStats totals = this.pool.getTotalStats();
        PoolStats stats = this.pool.getStats(route);
        buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
        buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
        buf.append(" of ").append(stats.getMax()).append("; ");
        buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
        buf.append(" of ").append(totals.getMax()).append("]");
        return buf.toString();
    }

    private String format(final HttpPoolEntry entry) {
        StringBuilder buf = new StringBuilder();
        buf.append("[id: ").append(entry.getId()).append("]");
        buf.append("[route: ").append(entry.getRoute()).append("]");
        Object state = entry.getState();
        if (state != null) {
            buf.append("[state: ").append(state).append("]");
        }
        return buf.toString();
    }

    public ClientConnectionRequest requestConnection(
            final HttpRoute route,
            final Object state) {
        if (route == null) {
            throw new IllegalArgumentException("HTTP route may not be null");
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Connection request: " + format(route, state) + formatStats(route));
        }
        final Future<HttpPoolEntry> future = this.pool.lease(route, state);

        return new ClientConnectionRequest() {

            public void abortRequest() {
                future.cancel(true);
            }

            public ManagedClientConnection getConnection(
                    final long timeout,
                    final TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                return leaseConnection(future, timeout, tunit);
            }

        };

    }

    ManagedClientConnection leaseConnection(
            final Future<HttpPoolEntry> future,
            final long timeout,
            final TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
        HttpPoolEntry entry;
        try {
            entry = future.get(timeout, tunit);
            if (entry == null || future.isCancelled()) {
                throw new InterruptedException();
            }
            if (entry.getConnection() == null) {
                throw new IllegalStateException("Pool entry with no connection");
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connection leased: " + format(entry) + formatStats(entry.getRoute()));
            }
            return new ManagedClientConnectionImpl(this, this.operator, entry);
        } catch (ExecutionException ex) {
            Throwable cause = ex.getCause();
            if (cause == null) {
                cause = ex;
            }
            this.log.error("Unexpected exception leasing connection from pool", cause);
            // Should never happen
            throw new InterruptedException();
        } catch (TimeoutException ex) {
            throw new ConnectionPoolTimeoutException("Timeout waiting for connection");
        }
    }

    public void releaseConnection(
            final ManagedClientConnection conn, final long keepalive, final TimeUnit tunit) {

        if (!(conn instanceof ManagedClientConnectionImpl)) {
            throw new IllegalArgumentException
                ("Connection class mismatch, " +
                 "connection not obtained from this manager.");
        }
        ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl) conn;
        if (managedConn.getManager() != this) {
            throw new IllegalStateException("Connection not obtained from this manager.");
        }

        synchronized (managedConn) {
            HttpPoolEntry entry = managedConn.detach();
            if (entry == null) {
                return;
            }
            try {
                if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
                    try {
                        managedConn.shutdown();
                    } catch (IOException iox) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("I/O exception shutting down released connection", iox);
                        }
                    }
                }
                entry.updateExpiry(keepalive, tunit != null ? tunit : TimeUnit.MILLISECONDS);
                if (this.log.isDebugEnabled()) {
                    String s;
                    if (keepalive > 0) {
                        s = "for " + keepalive + " " + tunit;
                    } else {
                        s = "indefinitely";
                    }
                    this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
                }
            } finally {
                this.pool.release(entry, managedConn.isMarkedReusable());
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connection released: " + format(entry) + formatStats(entry.getRoute()));
            }
        }
    }

    public void shutdown() {
        this.log.debug("Connection manager is shutting down");
        try {
            this.pool.shutdown();
        } catch (IOException ex) {
            this.log.debug("I/O exception shutting down connection manager", ex);
        }
        this.log.debug("Connection manager shut down");
    }

    public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
        }
        this.pool.closeIdle(idleTimeout, tunit);
    }

    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.pool.closeExpired();
    }

    public int getMaxTotal() {
        return this.pool.getMaxTotal();
    }

    public void setMaxTotal(int max) {
        this.pool.setMaxTotal(max);
    }

    public int getDefaultMaxPerRoute() {
        return this.pool.getDefaultMaxPerRoute();
    }

    public void setDefaultMaxPerRoute(int max) {
        this.pool.setDefaultMaxPerRoute(max);
    }

    public int getMaxPerRoute(final HttpRoute route) {
        return this.pool.getMaxPerRoute(route);
    }

    public void setMaxPerRoute(final HttpRoute route, int max) {
        this.pool.setMaxPerRoute(route, max);
    }

    public PoolStats getTotalStats() {
        return this.pool.getTotalStats();
    }

    public PoolStats getStats(final HttpRoute route) {
        return this.pool.getStats(route);
    }

}

