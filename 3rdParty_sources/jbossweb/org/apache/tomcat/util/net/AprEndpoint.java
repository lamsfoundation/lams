/*
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
 */

package org.apache.tomcat.util.net;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;

import org.apache.tomcat.jni.Address;
import org.apache.tomcat.jni.Error;
import org.apache.tomcat.jni.File;
import org.apache.tomcat.jni.Library;
import org.apache.tomcat.jni.OS;
import org.apache.tomcat.jni.Poll;
import org.apache.tomcat.jni.Pool;
import org.apache.tomcat.jni.SSL;
import org.apache.tomcat.jni.SSLContext;
import org.apache.tomcat.jni.SSLSocket;
import org.apache.tomcat.jni.Socket;
import org.apache.tomcat.jni.Status;
import org.apache.tomcat.util.res.StringManager;
import org.jboss.logging.Logger;

/**
 * APR tailored thread pool, providing the following services:
 * <ul>
 * <li>Socket acceptor thread</li>
 * <li>Socket poller thread</li>
 * <li>Sendfile thread</li>
 * <li>Worker threads pool</li>
 * </ul>
 *
 * When switching to Java 5, there's an opportunity to use the virtual
 * machine's thread pool.
 *
 * @author Mladen Turk
 * @author Remy Maucherat
 */
public class AprEndpoint {


    // -------------------------------------------------------------- Constants

    protected static Logger log = Logger.getLogger(AprEndpoint.class);

    protected static StringManager sm =
        StringManager.getManager("org.apache.tomcat.util.net.res");


    /**
     * The Request attribute key for the cipher suite.
     */
    public static final String CIPHER_SUITE_KEY = "javax.servlet.request.cipher_suite";

    /**
     * The Request attribute key for the key size.
     */
    public static final String KEY_SIZE_KEY = "javax.servlet.request.key_size";

    /**
     * The Request attribute key for the client certificate chain.
     */
    public static final String CERTIFICATE_KEY = "javax.servlet.request.X509Certificate";

    /**
     * The Request attribute key for the session id.
     * This one is a Tomcat extension to the Servlet spec.
     */
    public static final String SESSION_ID_KEY = "javax.servlet.request.ssl_session";


    // ----------------------------------------------------------------- Fields


    /**
     * Available workers.
     */
    protected WorkerStack workers = null;


    /**
     * Running state of the endpoint.
     */
    protected volatile boolean running = false;


    /**
     * Will be set to true whenever the endpoint is paused.
     */
    protected volatile boolean paused = false;


    /**
     * Track the initialization state of the endpoint.
     */
    protected boolean initialized = false;


    /**
     * Current worker threads busy count.
     */
    protected int curThreadsBusy = 0;


    /**
     * Current worker threads count.
     */
    protected int curThreads = 0;


    /**
     * Sequence number used to generate thread names.
     */
    protected int sequence = 0;


    /**
     * Root APR memory pool.
     */
    protected long rootPool = 0;


    /**
     * Server socket "pointer".
     */
    protected long serverSock = 0;


    /**
     * APR memory pool for the server socket.
     */
    protected long serverSockPool = 0;


    /**
     * SSL context.
     */
    protected long sslContext = 0;

    
    // ------------------------------------------------------------- Properties


    /**
     * External Executor based thread pool.
     */
    protected Executor executor = null;
    public void setExecutor(Executor executor) { this.executor = executor; }
    public Executor getExecutor() { return executor; }


    /**
     * Maximum amount of worker threads.
     */
    protected int maxThreads = 200;
    public void setMaxThreads(int maxThreads) { this.maxThreads = maxThreads; }
    public int getMaxThreads() { return maxThreads; }


    /**
     * Priority of the acceptor and poller threads.
     */
    protected int threadPriority = Thread.NORM_PRIORITY;
    public void setThreadPriority(int threadPriority) { this.threadPriority = threadPriority; }
    public int getThreadPriority() { return threadPriority; }


    /**
     * Size of the socket poller.
     */
    protected int pollerSize = 8 * 1024;
    public void setPollerSize(int pollerSize) { this.pollerSize = pollerSize; }
    public int getPollerSize() { return pollerSize; }


    /**
     * Size of the sendfile (= concurrent files which can be served).
     */
    protected int sendfileSize = 1 * 1024;
    public void setSendfileSize(int sendfileSize) { this.sendfileSize = sendfileSize; }
    public int getSendfileSize() { return sendfileSize; }


    /**
     * Server socket port.
     */
    protected int port;
    public int getPort() { return port; }
    public void setPort(int port ) { this.port=port; }


    /**
     * Address for the server socket.
     */
    protected InetAddress address;
    public InetAddress getAddress() { return address; }
    public void setAddress(InetAddress address) { this.address = address; }


    /**
     * Handling of accepted sockets.
     */
    protected Handler handler = null;
    public void setHandler(Handler handler ) { this.handler = handler; }
    public Handler getHandler() { return handler; }


    /**
     * Allows the server developer to specify the backlog that
     * should be used for server sockets. By default, this value
     * is 100.
     */
    protected int backlog = 100;
    public void setBacklog(int backlog) { if (backlog > 0) this.backlog = backlog; }
    public int getBacklog() { return backlog; }


    /**
     * Socket TCP no delay.
     */
    protected boolean tcpNoDelay = false;
    public boolean getTcpNoDelay() { return tcpNoDelay; }
    public void setTcpNoDelay(boolean tcpNoDelay) { this.tcpNoDelay = tcpNoDelay; }


    /**
     * Socket linger.
     */
    protected int soLinger = 100;
    public int getSoLinger() { return soLinger; }
    public void setSoLinger(int soLinger) { this.soLinger = soLinger; }


    /**
     * Socket timeout.
     */
    protected int soTimeout = -1;
    public int getSoTimeout() { return soTimeout; }
    public void setSoTimeout(int soTimeout) { this.soTimeout = soTimeout; }


    /**
     * Defer accept.
     */
    protected boolean deferAccept = true;
    public void setDeferAccept(boolean deferAccept) { this.deferAccept = deferAccept; }
    public boolean getDeferAccept() { return deferAccept; }


    /**
     * Keep-Alive timeout.
     */
    protected int keepAliveTimeout = -1;
    public int getKeepAliveTimeout() { return keepAliveTimeout; }
    public void setKeepAliveTimeout(int keepAliveTimeout) { this.keepAliveTimeout = keepAliveTimeout; }


    /**
     * Poll interval, in microseconds. The smaller the value, the more CPU the poller
     * will use, but the more responsive to activity it will be.
     */
    protected int pollTime = 2000;
    public int getPollTime() { return pollTime; }
    public void setPollTime(int pollTime) { if (pollTime > 0) { this.pollTime = pollTime; } }


    /**
     * The default is true - the created threads will be
     *  in daemon mode. If set to false, the control thread
     *  will not be daemon - and will keep the process alive.
     */
    protected boolean daemon = true;
    public void setDaemon(boolean b) { daemon = b; }
    public boolean getDaemon() { return daemon; }


    /**
     * Name of the thread pool, which will be used for naming child threads.
     */
    protected String name = "TP";
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }


    /**
     * Use sendfile for sending static files.
     */
    protected boolean useSendfile = Library.APR_HAS_SENDFILE;
    public void setUseSendfile(boolean useSendfile) { this.useSendfile = useSendfile; }
    public boolean getUseSendfile() { return useSendfile; }


    /**
     * Allow comet request handling.
     */
    protected boolean useComet = true;
    public void setUseComet(boolean useComet) { this.useComet = useComet; }
    public boolean getUseComet() { return useComet; }


    /**
     * The socket poller.
     */
    protected Poller poller = null;
    public Poller getPoller() {
        return poller;
    }


    /**
     * The socket poller used for Comet support.
     */
    protected Poller cometPoller = null;
    public Poller getCometPoller() {
        return cometPoller;
    }


    /**
     * The static file sender.
     */
    protected Sendfile sendfile = null;
    public Sendfile getSendfile() {
        return sendfile;
    }


    /**
     * Dummy maxSpareThreads property.
     */
    public int getMaxSpareThreads() { return 0; }


    /**
     * Dummy minSpareThreads property.
     */
    public int getMinSpareThreads() { return 0; }


    /**
     * The server address.
     */
    protected long serverAddress = 0;
    
    
    /**
     * The server address family.
     */
    protected int serverAddressFamily = 0;
    
    
    /**
     * Reverse connection. In this proxied mode, the endpoint will not use a server
     * socket, but will connect itself to the front end server.
     */
    protected boolean reverseConnection = false;
    public boolean isReverseConnection() { return reverseConnection; }
    public void setReverseConnection(boolean reverseConnection) { this.reverseConnection = reverseConnection; }
    /**
     * List of socket counters.
     */
    class ListSock {
       int count;
       int port;
    }
    protected ListSock[] listsock = null;


    /**
     * SSL engine.
     */
    protected boolean SSLEnabled = false;
    public boolean isSSLEnabled() { return SSLEnabled; }
    public void setSSLEnabled(boolean SSLEnabled) { this.SSLEnabled = SSLEnabled; }


    /**
     * SSL protocols.
     */
    protected String SSLProtocol = "all";
    public String getSSLProtocol() { return SSLProtocol; }
    public void setSSLProtocol(String SSLProtocol) { this.SSLProtocol = SSLProtocol; }


    /**
     * SSL password (if a cert is encrypted, and no password has been provided, a callback
     * will ask for a password).
     */
    protected String SSLPassword = null;
    public String getSSLPassword() { return SSLPassword; }
    public void setSSLPassword(String SSLPassword) { this.SSLPassword = SSLPassword; }


    /**
     * SSL cipher suite.
     */
    protected String SSLCipherSuite = "ALL";
    public String getSSLCipherSuite() { return SSLCipherSuite; }
    public void setSSLCipherSuite(String SSLCipherSuite) { this.SSLCipherSuite = SSLCipherSuite; }


    /**
     * SSL certificate file.
     */
    protected String SSLCertificateFile = null;
    public String getSSLCertificateFile() { return SSLCertificateFile; }
    public void setSSLCertificateFile(String SSLCertificateFile) { this.SSLCertificateFile = SSLCertificateFile; }


    /**
     * SSL certificate key file.
     */
    protected String SSLCertificateKeyFile = null;
    public String getSSLCertificateKeyFile() { return SSLCertificateKeyFile; }
    public void setSSLCertificateKeyFile(String SSLCertificateKeyFile) { this.SSLCertificateKeyFile = SSLCertificateKeyFile; }


    /**
     * SSL certificate chain file.
     */
    protected String SSLCertificateChainFile = null;
    public String getSSLCertificateChainFile() { return SSLCertificateChainFile; }
    public void setSSLCertificateChainFile(String SSLCertificateChainFile) { this.SSLCertificateChainFile = SSLCertificateChainFile; }


    /**
     * SSL CA certificate path.
     */
    protected String SSLCACertificatePath = null;
    public String getSSLCACertificatePath() { return SSLCACertificatePath; }
    public void setSSLCACertificatePath(String SSLCACertificatePath) { this.SSLCACertificatePath = SSLCACertificatePath; }


    /**
     * SSL CA certificate file.
     */
    protected String SSLCACertificateFile = null;
    public String getSSLCACertificateFile() { return SSLCACertificateFile; }
    public void setSSLCACertificateFile(String SSLCACertificateFile) { this.SSLCACertificateFile = SSLCACertificateFile; }


    /**
     * SSL CA revocation path.
     */
    protected String SSLCARevocationPath = null;
    public String getSSLCARevocationPath() { return SSLCARevocationPath; }
    public void setSSLCARevocationPath(String SSLCARevocationPath) { this.SSLCARevocationPath = SSLCARevocationPath; }


    /**
     * SSL CA revocation file.
     */
    protected String SSLCARevocationFile = null;
    public String getSSLCARevocationFile() { return SSLCARevocationFile; }
    public void setSSLCARevocationFile(String SSLCARevocationFile) { this.SSLCARevocationFile = SSLCARevocationFile; }


    /**
     * SSL verify client.
     */
    protected String SSLVerifyClient = "none";
    public String getSSLVerifyClient() { return SSLVerifyClient; }
    public void setSSLVerifyClient(String SSLVerifyClient) { this.SSLVerifyClient = SSLVerifyClient; }


    /**
     * SSL verify depth.
     */
    protected int SSLVerifyDepth = 10;
    public int getSSLVerifyDepth() { return SSLVerifyDepth; }
    public void setSSLVerifyDepth(int SSLVerifyDepth) { this.SSLVerifyDepth = SSLVerifyDepth; }


    // --------------------------------------------------------- Public Methods


    /**
     * Number of keepalive sockets.
     */
    public int getKeepAliveCount() {
        if (poller == null) {
            return 0;
        } else {
            return poller.getConnectionCount();
        }
    }


    /**
     * Number of sendfile sockets.
     */
    public int getSendfileCount() {
        if (sendfile == null) {
            return 0;
        } else {
            return sendfile.getSendfileCount();
        }
    }


    /**
     * Return the amount of threads that are managed by the pool.
     *
     * @return the amount of threads that are managed by the pool
     */
    public int getCurrentThreadCount() {
        return curThreads;
    }


    /**
     * Return the amount of threads currently busy.
     *
     * @return the amount of threads currently busy
     */
    public int getCurrentThreadsBusy() {
        return curThreadsBusy;
    }


    /**
     * Return the state of the endpoint.
     *
     * @return true if the endpoint is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }


    /**
     * Return the state of the endpoint.
     *
     * @return true if the endpoint is paused, false otherwise
     */
    public boolean isPaused() {
        return paused;
    }


    // ----------------------------------------------- Public Lifecycle Methods


    /**
     * Initialize the endpoint.
     */
    public void init()
        throws Exception {

        if (initialized)
            return;
        
        // Create the root APR memory pool
        rootPool = Pool.create(0);
        // Create the pool for the server socket
        serverSockPool = Pool.create(rootPool);
        // Create the APR address that will be bound
        String addressStr = null;
        if (address == null) {
            addressStr = null;
        } else {
            addressStr = address.getHostAddress();
        }
        int family = Socket.APR_INET;
        if (Library.APR_HAVE_IPV6) {
            if (addressStr == null) {
                if (!OS.IS_BSD && !OS.IS_WIN32 && !OS.IS_WIN64)
                    family = Socket.APR_UNSPEC;
            } else if (addressStr.indexOf(':') >= 0) {
                family = Socket.APR_UNSPEC;
            }
         }

        // Sendfile usage on systems which don't support it cause major problems
        if (useSendfile && !Library.APR_HAS_SENDFILE) {
            useSendfile = false;
        }

        long inetAddress = Address.info(addressStr, family,
                port, 0, rootPool);
        
        if (!reverseConnection) {
            // Create the APR server socket
            serverSock = Socket.create(Address.getInfo(inetAddress).family, Socket.SOCK_STREAM,
                    Socket.APR_PROTO_TCP, rootPool);
            if (OS.IS_UNIX) {
                Socket.optSet(serverSock, Socket.APR_SO_REUSEADDR, 1);
            }
            // Deal with the firewalls that tend to drop the inactive sockets
            Socket.optSet(serverSock, Socket.APR_SO_KEEPALIVE, 1);
            // Bind the server socket
            int ret = Socket.bind(serverSock, inetAddress);
            if (ret != 0) {
                throw new Exception(sm.getString("endpoint.init.bind", "" + ret, Error.strerror(ret)));
            }
            // Start listening on the server socket
            ret = Socket.listen(serverSock, backlog);
            if (ret != 0) {
                throw new Exception(sm.getString("endpoint.init.listen", "" + ret, Error.strerror(ret)));
            }
            if (OS.IS_WIN32 || OS.IS_WIN64) {
                // On Windows set the reuseaddr flag after the bind/listen
                Socket.optSet(serverSock, Socket.APR_SO_REUSEADDR, 1);
            }

            // Delay accepting of new connections until data is available
            // Only Linux kernels 2.4 + have that implemented
            // on other platforms this call is noop and will return APR_ENOTIMPL.
            if (deferAccept && (Socket.optSet(serverSock, Socket.APR_TCP_DEFER_ACCEPT, 1) == Status.APR_ENOTIMPL)) {
                deferAccept = false;
            }
        } else {
            /* Initialize the SockList */
            listsock = new ListSock[10];
            for (int i=0; i<listsock.length; i++) {
                listsock[i] = new ListSock();
                listsock[i].port = port + i;
                listsock[i].count = 0;
            }
            serverAddress = inetAddress;
            serverAddressFamily = family;
            deferAccept = false;
        }

        // Initialize SSL if needed
        if (SSLEnabled) {

            // SSL protocol
            int value = SSL.SSL_PROTOCOL_ALL;
            if ("SSLv2".equalsIgnoreCase(SSLProtocol)) {
                value = SSL.SSL_PROTOCOL_SSLV2;
            } else if ("SSLv3".equalsIgnoreCase(SSLProtocol)) {
                value = SSL.SSL_PROTOCOL_SSLV3;
            } else if ("TLSv1".equalsIgnoreCase(SSLProtocol)) {
                value = SSL.SSL_PROTOCOL_TLSV1;
            } else if ("SSLv2+SSLv3".equalsIgnoreCase(SSLProtocol)) {
                value = SSL.SSL_PROTOCOL_SSLV2 | SSL.SSL_PROTOCOL_SSLV3;
            }
            // Create SSL Context
            sslContext = SSLContext.make(rootPool, value, (reverseConnection) ? SSL.SSL_MODE_CLIENT : SSL.SSL_MODE_SERVER);
            // List the ciphers that the client is permitted to negotiate
            SSLContext.setCipherSuite(sslContext, SSLCipherSuite);
            // Load Server key and certificate
            SSLContext.setCertificate(sslContext, SSLCertificateFile, SSLCertificateKeyFile, SSLPassword, SSL.SSL_AIDX_RSA);
            // Set certificate chain file
            SSLContext.setCertificateChainFile(sslContext, SSLCertificateChainFile, false);
            // Support Client Certificates
            SSLContext.setCACertificate(sslContext, SSLCACertificateFile, SSLCACertificatePath);
            // Set revocation
            SSLContext.setCARevocation(sslContext, SSLCARevocationFile, SSLCARevocationPath);
            // Client certificate verification
            value = SSL.SSL_CVERIFY_NONE;
            if ("optional".equalsIgnoreCase(SSLVerifyClient)) {
                value = SSL.SSL_CVERIFY_OPTIONAL;
            } else if ("require".equalsIgnoreCase(SSLVerifyClient)) {
                value = SSL.SSL_CVERIFY_REQUIRE;
            } else if ("optionalNoCA".equalsIgnoreCase(SSLVerifyClient)) {
                value = SSL.SSL_CVERIFY_OPTIONAL_NO_CA;
            }
            SSLContext.setVerify(sslContext, value, SSLVerifyDepth);
            // For now, sendfile is not supported with SSL
            useSendfile = false;

        }
        
        initialized = true;

    }


    /**
     * Start the APR endpoint, creating acceptor, poller and sendfile threads.
     */
    public void start()
        throws Exception {
        // Initialize socket if not done before
        if (!initialized) {
            init();
        }
        if (!running) {
            running = true;
            paused = false;

            // Create worker collection
            if (executor == null) {
                workers = new WorkerStack(maxThreads);
            }

            // Start poller thread
            poller = new Poller(false);
            poller.init();
            Thread pollerThread = new Thread(poller, getName() + "-Poller");
            pollerThread.setPriority(threadPriority);
            pollerThread.setDaemon(true);
            pollerThread.start();

            // Start comet poller thread
            cometPoller = new Poller(true);
            cometPoller.init();
            Thread cometPollerThread = new Thread(cometPoller, getName() + "-CometPoller");
            cometPollerThread.setPriority(threadPriority);
            cometPollerThread.setDaemon(true);
            cometPollerThread.start();

            // Start sendfile thread
            if (useSendfile) {
                sendfile = new Sendfile();
                sendfile.init();
                Thread sendfileThread = new Thread(sendfile, getName() + "-Sendfile");
                sendfileThread.setPriority(threadPriority);
                sendfileThread.setDaemon(true);
                sendfileThread.start();
            }
            
            // Start acceptor thread
            Thread acceptorThread = new Thread(new Acceptor(), getName() + "-Acceptor");
            acceptorThread.setPriority(threadPriority);
            acceptorThread.setDaemon(daemon);
            acceptorThread.start();

        }
    }


    /**
     * Pause the endpoint, which will make it stop accepting new sockets.
     */
    public void pause() {
        if (running && !paused) {
            paused = true;
            unlockAccept();
        }
    }


    /**
     * Resume the endpoint, which will make it start accepting new sockets
     * again.
     */
    public void resume() {
        if (running) {
            paused = false;
        }
    }


    /**
     * Stop the endpoint. This will cause all processing threads to stop.
     */
    public void stop() {
        if (running) {
            running = false;
            unlockAccept();
            poller.destroy();
            poller = null;
            cometPoller.destroy();
            cometPoller = null;
            if (useSendfile) {
                sendfile.destroy();
                sendfile = null;
            }
        }
    }


    /**
     * Deallocate APR memory pools, and close server socket.
     */
    public void destroy() throws Exception {
        if (running) {
            stop();
        }
        Pool.destroy(serverSockPool);
        serverSockPool = 0;
        // Close server socket
        Socket.close(serverSock);
        serverSock = 0;
        sslContext = 0;
        // Close all APR memory pools and resources
        Pool.destroy(rootPool);
        rootPool = 0;
        initialized = false;
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Get a sequence number used for thread naming.
     */
    protected int getSequence() {
        return sequence++;
    }


    /**
     * Unlock the server socket accept using a bogus connection.
     */
    protected void unlockAccept() {
        java.net.Socket s = null;
        try {
            // Need to create a connection to unlock the accept();
            if (address == null) {
                s = new java.net.Socket("localhost", port);
            } else {
                s = new java.net.Socket(address, port);
                // setting soLinger to a small value will help shutdown the
                // connection quicker
                s.setSoLinger(true, 0);
            }
            // If deferAccept is enabled, send at least one byte
            if (deferAccept) {
                s.getOutputStream().write(" ".getBytes());
            }
        } catch (Exception e) {
            // Ignore
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }


    /**
     * Process the specified connection.
     */
    protected boolean setSocketOptions(long socket) {
        // Process the connection
        int step = 1;
        try {

            // 1: Set socket options: timeout, linger, etc
            if (soLinger >= 0)
                Socket.optSet(socket, Socket.APR_SO_LINGER, soLinger);
            if (tcpNoDelay)
                Socket.optSet(socket, Socket.APR_TCP_NODELAY, (tcpNoDelay ? 1 : 0));
            if (soTimeout > 0)
                Socket.timeoutSet(socket, soTimeout * 1000);

            // 2: SSL handshake
            step = 2;
            if (sslContext != 0) {
                SSLSocket.attach(sslContext, socket);
                if (SSLSocket.handshake(socket) != 0) {
                    if (log.isDebugEnabled()) {
                        log.debug(sm.getString("endpoint.err.handshake") + ": " + SSL.getLastError());
                    }
                    return false;
                }
            }

        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                if (step == 2) {
                    log.debug(sm.getString("endpoint.err.handshake"), t);
                } else {
                    log.debug(sm.getString("endpoint.err.unexpected"), t);
                }
            }
            // Tell to close the socket
            return false;
        }
        return true;
    }


    /**
     * Create (or allocate) and return an available processor for use in
     * processing a specific HTTP request, if possible.  If the maximum
     * allowed processors have already been created and are in use, return
     * <code>null</code> instead.
     */
    protected Worker createWorkerThread() {

        synchronized (workers) {
            if (workers.size() > 0) {
                curThreadsBusy++;
                return (workers.pop());
            }
            if ((maxThreads > 0) && (curThreads < maxThreads)) {
                curThreadsBusy++;
                if (curThreadsBusy == maxThreads) {
                    log.info(sm.getString("endpoint.info.maxThreads",
                            Integer.toString(maxThreads), address,
                            Integer.toString(port)));
                }
                return (newWorkerThread());
            } else {
                if (maxThreads < 0) {
                    curThreadsBusy++;
                    return (newWorkerThread());
                } else {
                    return (null);
                }
            }
        }

    }


    /**
     * Create and return a new processor suitable for processing HTTP
     * requests and returning the corresponding responses.
     */
    protected Worker newWorkerThread() {

        Worker workerThread = new Worker();
        workerThread.start();
        return (workerThread);

    }


    /**
     * Return a new worker thread, and block while to worker is available.
     */
    protected Worker getWorkerThread() {
        // Allocate a new worker thread
        Worker workerThread = createWorkerThread();
        while (workerThread == null) {
            try {
                synchronized (workers) {
                    workers.wait();
                }
            } catch (InterruptedException e) {
                // Ignore
            }
            workerThread = createWorkerThread();
        }
        return workerThread;
    }


    /**
     * Recycle the specified Processor so that it can be used again.
     *
     * @param workerThread The processor to be recycled
     */
    protected void recycleWorkerThread(Worker workerThread) {
        synchronized (workers) {
            workers.push(workerThread);
            curThreadsBusy--;
            workers.notify();
        }
    }

    
    /**
     * Allocate a new poller of the specified size.
     */
    protected long allocatePoller(int size, long pool, int timeout) {
        try {
            return Poll.create(size, pool, 0, (timeout > 0) ? (timeout * 1000) : -1);
        } catch (Error e) {
            if (Status.APR_STATUS_IS_EINVAL(e.getError())) {
                log.info(sm.getString("endpoint.poll.limitedpollsize", "" + size));
                return 0;
            } else {
                log.error(sm.getString("endpoint.poll.initfail"), e);
                return -1;
            }
        }
    }

    
    /**
     * Process given socket.
     */
    protected boolean processSocketWithOptions(long socket) {
        try {
            if (executor == null) {
                getWorkerThread().assignWithOptions(socket);
            } else {
                executor.execute(new SocketWithOptionsProcessor(socket));
            }
        } catch (Throwable t) {
            // This means we got an OOM or similar creating a thread, or that
            // the pool and its queue are full
            log.error(sm.getString("endpoint.process.fail"), t);
            return false;
        }
        return true;
    }
    

    /**
     * Process given socket.
     */
    protected boolean processSocket(long socket) {
        try {
            if (executor == null) {
                getWorkerThread().assign(socket);
            } else {
                executor.execute(new SocketProcessor(socket));
            }
        } catch (Throwable t) {
            // This means we got an OOM or similar creating a thread, or that
            // the pool and its queue are full
            log.error(sm.getString("endpoint.process.fail"), t);
            return false;
        }
        return true;
    }
    

    /**
     * Process given socket for an event.
     */
    protected boolean processSocket(long socket, SocketStatus status) {
        try {
            if (executor == null) {
                getWorkerThread().assign(socket, status);
            } else {
                executor.execute(new SocketEventProcessor(socket, status));
            }
        } catch (Throwable t) {
            // This means we got an OOM or similar creating a thread, or that
            // the pool and its queue are full
            log.error(sm.getString("endpoint.process.fail"), t);
            return false;
        }
        return true;
    }
    

    // --------------------------------------------------- Acceptor Inner Class


    /**
     * Server socket acceptor thread.
     */
    protected class Acceptor implements Runnable {


        /**
         * The background thread that listens for incoming TCP/IP connections and
         * hands them off to an appropriate processor.
         */
        public void run() {

            // Loop until we receive a shutdown command
            while (running) {

                // Loop if endpoint is paused
                while (paused) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }

                if (reverseConnection) {
                    if (poller.getConnectionCount() < (maxThreads / 5)) {
                        /* Sort the ListSock (by count) */
                        boolean go = true;
                        while (go) {
                            go = false;
                            for (int i=0; i<listsock.length -1; i++) {
                                ListSock current;
                                if (listsock[i+1].count<listsock[i].count) {
                                    go = false;
                                    current = listsock[i+1];
                                    listsock[i+1] = listsock[i];
                                    listsock[i] = current;
                                }
                            }
                        }

                        // Create maxThreads / 5 sockets
                        int nsock = maxThreads / 5;
                        if (nsock>listsock.length)
                            nsock = listsock.length;
                        String addressStr = address.getHostAddress();
                        for (int i=0; i < nsock; i++) {
                            try {
                                long socket = Socket.create(serverAddressFamily, Socket.SOCK_STREAM,
                                        Socket.APR_PROTO_TCP, rootPool);
                                long inetAddress = Address.info(addressStr, serverAddressFamily,
                                        listsock[i].port, 0, rootPool);
                                if (Socket.connect(socket, inetAddress) != 0) {
                                    Socket.destroy(socket);
                                    continue; // try next one.
                                }
                                // Hand this socket off to an appropriate processor
                                if (!processSocketWithOptions(socket)) {
                                    // Close socket and pool right away
                                    Socket.destroy(socket);
                                } else {
                                    listsock[i].count++;
                                }
                                // Don't immediately create another socket
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    // Ignore
                                }
                            } catch (Throwable t) {
                                log.error(sm.getString("endpoint.accept.fail"), t);
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                } else {
                
                    try {
                        // Accept the next incoming connection from the server socket
                        long socket = Socket.accept(serverSock);
                        // Hand this socket off to an appropriate processor
                        if (!processSocketWithOptions(socket)) {
                            // Close socket and pool right away
                            Socket.destroy(socket);
                        }
                    } catch (Throwable t) {
                        log.error(sm.getString("endpoint.accept.fail"), t);
                    }

                }

                // The processor will recycle itself when it finishes

            }

        }
        
    }


    // ------------------------------------------------- SocketInfo Inner Class


    /**
     * Socket list class, used to avoid using a possibly large amount of objects
     * with very little actual use.
     */
    public static class SocketInfo {
        public static final int READ = 1;
        public static final int WRITE = 2;
        public static final int RESUME = 4;
        public static final int WAKEUP = 8;
        public long socket;
        public int timeout;
        public int flags;
        public boolean read() {
            return (flags & READ) == READ;
        }
        public boolean write() {
            return (flags & WRITE) == WRITE;
        }
        public boolean resume() {
            return (flags & RESUME) == RESUME;
        }
        public boolean wakeup() {
            return (flags & WAKEUP) == WAKEUP;
        }
        public static int merge(int flag1, int flag2) {
            return ((flag1 & READ) | (flag2 & READ))
                | ((flag1 & WRITE) | (flag2 & WRITE)) 
                | ((flag1 & RESUME) | (flag2 & RESUME)) 
                | ((flag1 & WAKEUP) & (flag2 & WAKEUP));
        }
    }
    
    
    // --------------------------------------------- SocketTimeouts Inner Class


    /**
     * Socket list class, used to avoid using a possibly large amount of objects
     * with very little actual use.
     */
    public class SocketTimeouts {
        protected int size;

        protected long[] sockets;
        protected long[] timeouts;
        protected int pos = 0;

        public SocketTimeouts(int size) {
            this.size = 0;
            sockets = new long[size];
            timeouts = new long[size];
        }

        public void add(long socket, long timeout) {
            sockets[size] = socket;
            timeouts[size] = timeout;
            size++;
        }
        
        public boolean remove(long socket) {
            for (int i = 0; i < size; i++) {
                if (sockets[i] == socket) {
                    sockets[i] = sockets[size - 1];
                    timeouts[i] = timeouts[size - 1];
                    size--;
                    return true;
                }
            }
            return false;
        }
        
        public long check(long date) {
            while (pos < size) {
                if (date >= timeouts[pos]) {
                    long result = sockets[pos];
                    sockets[pos] = sockets[size - 1];
                    timeouts[pos] = timeouts[size - 1];
                    size--;
                    return result;
                }
                pos++;
            }
            pos = 0;
            return 0;
        }
        
    }
    
    
    // ------------------------------------------------- SocketList Inner Class


    /**
     * Socket list class, used to avoid using a possibly large amount of objects
     * with very little actual use.
     */
    public class SocketList {
        protected int size;
        protected int pos;

        protected long[] sockets;
        protected int[] timeouts;
        protected int[] flags;
        
        protected SocketInfo info = new SocketInfo();
        
        public SocketList(int size) {
            this.size = 0;
            pos = 0;
            sockets = new long[size];
            timeouts = new int[size];
            flags = new int[size];
        }
        
        public int size() {
            return this.size;
        }
        
        public SocketInfo get() {
            if (pos == size) {
                return null;
            } else {
                info.socket = sockets[pos];
                info.timeout = timeouts[pos];
                info.flags = flags[pos];
                pos++;
                return info;
            }
        }
        
        public void clear() {
            size = 0;
            pos = 0;
        }
        
        public boolean add(long socket, int timeout, int flag) {
            if (size == sockets.length) {
                return false;
            } else {
                for (int i = 0; i < size; i++) {
                    if (sockets[i] == socket) {
                        flags[i] = SocketInfo.merge(flags[i], flag);
                        return true;
                    }
                }
                sockets[size] = socket;
                timeouts[size] = timeout;
                flags[size] = flag;
                size++;
                return true;
            }
        }
        
        public void duplicate(SocketList copy) {
            copy.size = size;
            copy.pos = pos;
            System.arraycopy(sockets, 0, copy.sockets, 0, size);
            System.arraycopy(timeouts, 0, copy.timeouts, 0, size);
            System.arraycopy(flags, 0, copy.flags, 0, size);
        }
        
    }
    
    
    // ----------------------------------------------------- Poller Inner Class


    /**
     * Poller class.
     */
    public class Poller implements Runnable {

        /**
         * Pointers to the pollers.
         */
        protected long[] pollers = null;
        
        /**
         * Actual poller size.
         */
        protected int actualPollerSize = 0;
        
        /**
         * Amount of spots left in the poller.
         */
        protected int[] pollerSpace = null;
        
        /**
         * Amount of low level pollers in use by this poller.
         */
        protected int pollerCount;
        
        /**
         * Timeout value for the poll call.
         */
        protected int pollerTime;
        
        /**
         * Root pool.
         */
        protected long pool = 0;
        
        /**
         * Socket descriptors.
         */
        protected long[] desc;

        /**
         * List of sockets to be added to the poller.
         */
        protected SocketList addList = null;

        /**
         * List of sockets to be added to the poller.
         */
        protected SocketList localAddList = null;

        /**
         * Comet mode flag.
         */
        protected boolean comet = true;

        /**
         * Structure used for storing timeouts.
         */
        protected SocketTimeouts timeouts = null;
        
        
        /**
         * Last run of maintain. Maintain will run usually every 5s.
         */
        protected long lastMaintain = System.currentTimeMillis();
        
        
        /**
         * Amount of connections inside this poller.
         */
        protected int connectionCount = 0;
        public int getConnectionCount() { return connectionCount; }

        public Poller(boolean comet) {
            this.comet = comet;
        }
        
        /**
         * Create the poller. With some versions of APR, the maximum poller size will
         * be 62 (recompiling APR is necessary to remove this limitation).
         */
        protected void init() {

            timeouts = new SocketTimeouts(pollerSize);
            
            pool = Pool.create(serverSockPool);
            actualPollerSize = pollerSize;
            if ((OS.IS_WIN32 || OS.IS_WIN64) && (actualPollerSize > 1024)) {
                // The maximum per poller to get reasonable performance is 1024
                // Adjust poller size so that it won't reach the limit
                actualPollerSize = 1024;
            }
            int timeout = keepAliveTimeout;
            if (timeout < 0) {
                timeout = soTimeout;
            }
            
            // At the moment, setting the timeout is useless, but it could get used
            // again as the normal poller could be faster using maintain. It might not
            // be worth bothering though.
            long pollset = allocatePoller(actualPollerSize, pool, -1);
            if (pollset == 0 && actualPollerSize > 1024) {
                actualPollerSize = 1024;
                pollset = allocatePoller(actualPollerSize, pool, -1);
            }
            if (pollset == 0) {
                actualPollerSize = 62;
                pollset = allocatePoller(actualPollerSize, pool, -1);
            }
            
            pollerCount = pollerSize / actualPollerSize;
            pollerTime = pollTime / pollerCount;
            
            pollers = new long[pollerCount];
            pollers[0] = pollset;
            for (int i = 1; i < pollerCount; i++) {
                pollers[i] = allocatePoller(actualPollerSize, pool, -1);
            }
            
            pollerSpace = new int[pollerCount];
            for (int i = 0; i < pollerCount; i++) {
                pollerSpace[i] = actualPollerSize;
            }

            desc = new long[actualPollerSize * 2];
            connectionCount = 0;
            addList = new SocketList(pollerSize);
            localAddList = new SocketList(pollerSize);

        }

        /**
         * Destroy the poller.
         */
        protected void destroy() {
            // Wait for pollerTime before doing anything, so that the poller threads
            // exit, otherwise parallel destruction of sockets which are still
            // in the poller can cause problems
            try {
                synchronized (this) {
                    this.wait(pollTime / 1000);
                }
            } catch (InterruptedException e) {
                // Ignore
            }
            // Close all sockets in the add queue
            SocketInfo info = addList.get();
            while (info != null) {
                if (!comet || (comet && !processSocket(info.socket, SocketStatus.STOP))) {
                    Socket.destroy(info.socket);
                }
                info = addList.get();
            }
            addList.clear();
            // Close all sockets still in the poller
            for (int i = 0; i < pollerCount; i++) {
                int rv = Poll.pollset(pollers[i], desc);
                if (rv > 0) {
                    for (int n = 0; n < rv; n++) {
                        if (!comet || (comet && !processSocket(desc[n*2+1], SocketStatus.STOP))) {
                            Socket.destroy(desc[n*2+1]);
                        }
                    }
                }
            }
            Pool.destroy(pool);
            connectionCount = 0;
        }

        /**
         * Add specified socket and associated pool to the poller. The socket will
         * be added to a temporary array, and polled first after a maximum amount
         * of time equal to pollTime (in most cases, latency will be much lower,
         * however).
         *
         * @param socket to add to the poller
         */
        public void add(long socket) {
            int timeout = keepAliveTimeout;
            if (timeout < 0) {
                timeout = soTimeout;
            }
            boolean ok = false;
            synchronized (this) {
                // Add socket to the list. Newly added sockets will wait
                // at most for pollTime before being polled
                if (addList.add(socket, timeout, SocketInfo.READ)) {
                    ok = true;
                    this.notify();
                }
            }
            if (!ok) {
                // Can't do anything: close the socket right away
                if (!comet || (comet && !processSocket(socket, SocketStatus.ERROR))) {
                    Socket.destroy(socket);
                }
            }
        }

        /**
         * Add specified socket and associated pool to the poller. The socket will
         * be added to a temporary array, and polled first after a maximum amount
         * of time equal to pollTime (in most cases, latency will be much lower,
         * however). Note: If both read and write are false, the socket will only
         * be checked for timeout; if the socket was already present in the poller, 
         * a callback event will be generated and the socket will be removed from the
         * poller.
         *
         * @param socket to add to the poller
         * @param timeout to use for this connection
         * @param read to do read polling
         * @param write to do write polling
         * @param resume to send a callback event
         */
        public void add(long socket, int timeout, boolean read, boolean write, boolean resume, boolean wakeup) {
            if (timeout < 0) {
                timeout = soTimeout;
            }
            boolean ok = false;
            synchronized (this) {
                // Add socket to the list. Newly added sockets will wait
                // at most for pollTime before being polled
                if (addList.add(socket, timeout, 
                        (read ? SocketInfo.READ : 0) | (write ? SocketInfo.WRITE : 0) | (resume ? SocketInfo.RESUME : 0)
                        | (wakeup ? SocketInfo.WAKEUP : 0))) {
                    ok = true;
                    this.notify();
                }
            }
            if (!ok) {
                // Can't do anything: close the socket right away
                if (!comet || (comet && !processSocket(socket, SocketStatus.ERROR))) {
                    Socket.destroy(socket);
                }
            }
        }

        /**
         * Add specified socket to one of the pollers. 
         */
        protected boolean addToPoller(long socket, int events) {
            int rv = -1;
            for (int i = 0; i < pollers.length; i++) {
                if (pollerSpace[i] > 0) {
                    rv = Poll.add(pollers[i], socket, events);
                    if (rv == Status.APR_SUCCESS) {
                        pollerSpace[i]--;
                        connectionCount++;
                        return true;
                    }
                }
            }
            return false;
        }
        
        /**
         * Remove specified socket from the pollers. 
         */
        protected boolean removeFromPoller(long socket) {
            int rv = -1;
            for (int i = 0; i < pollers.length; i++) {
                if (pollerSpace[i] < actualPollerSize) {
                    rv = Poll.remove(pollers[i], socket);
                    if (rv != Status.APR_NOTFOUND) {
                        pollerSpace[i]++;
                        connectionCount--;
                        break;
                    }
                }
            }
            return (rv == Status.APR_SUCCESS);
        }
        
        /**
         * Timeout checks.
         */
        protected void maintain() {

            long date = System.currentTimeMillis();
            // Maintain runs at most once every 5s, although it will likely get called more
            if ((date - lastMaintain) < 5000L) {
                return;
            } else {
                lastMaintain = date;
            }
            long socket = timeouts.check(date);
            while (socket != 0) {
                removeFromPoller(socket);
                if (!comet || (comet && !processSocket(socket, SocketStatus.TIMEOUT))) {
                    Socket.destroy(socket);
                }
                socket = timeouts.check(date);
            }

        }
        
        /**
         * Displays the list of sockets in the pollers.
         */
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("Poller comet=[").append(comet).append("]");
            long[] res = new long[actualPollerSize * 2];
            for (int i = 0; i < pollers.length; i++) {
                int count = Poll.pollset(pollers[i], res);
                buf.append(" [ ");
                for (int j = 0; j < count; j++) {
                    buf.append(desc[2*j+1]).append(" ");
                }
                buf.append("]");
            }
            return buf.toString();
        }
        
        /**
         * The background thread that listens for incoming TCP/IP connections and
         * hands them off to an appropriate processor.
         */
        public void run() {

            int maintain = 0;
            // Loop until we receive a shutdown command
            while (running) {

                // Loop if endpoint is paused
                while (paused) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
                // Check timeouts for suspended connections if the poller is empty
                while (connectionCount < 1 && addList.size() < 1) {
                    // Reset maintain time.
                    try {
                        if (soTimeout > 0 && running) {
                            maintain();
                        }
                        synchronized (this) {
                            this.wait(10000);
                        }
                    } catch (InterruptedException e) {
                        // Ignore
                    } catch (Throwable t) {
                        log.error(sm.getString("endpoint.maintain.error"), t);
                    }
                }

                try {
                    // Add sockets which are waiting to the poller
                    if (addList.size() > 0) {
                        synchronized (this) {
                            // Duplicate to another list, so that the syncing is minimal
                            addList.duplicate(localAddList);
                            addList.clear();
                        }
                        SocketInfo info = localAddList.get();
                        while (info != null) {
                            if (info.wakeup()) {
                                // Resume event if socket is present in the poller
                                if (timeouts.remove(info.socket)) {
                                    removeFromPoller(info.socket);
                                    if (info.resume()) {
                                        if (!processSocket(info.socket, SocketStatus.OPEN_CALLBACK)) {
                                            Socket.destroy(info.socket);
                                        }
                                    } else {
                                        int events = ((info.read()) ? Poll.APR_POLLIN : 0)
                                            | ((info.write()) ? Poll.APR_POLLOUT : 0);
                                        if (!addToPoller(info.socket, events)) {
                                            // Can't do anything: close the socket right away
                                            if (!comet || (comet && !processSocket(info.socket, SocketStatus.ERROR))) {
                                                Socket.destroy(info.socket);
                                            }
                                        } else {
                                            timeouts.add(info.socket, System.currentTimeMillis() + info.timeout);
                                        }
                                    }
                                }
                            } else if (info.read() || info.write()) {
                                if (info.resume()) {
                                    // Resume event
                                    timeouts.remove(info.socket);
                                    removeFromPoller(info.socket);
                                    if (!processSocket(info.socket, SocketStatus.OPEN_CALLBACK)) {
                                        Socket.destroy(info.socket);
                                    }
                                } else {
                                    // Store timeout
                                    if (comet) {
                                        removeFromPoller(info.socket);
                                    }
                                    int events = ((info.read()) ? Poll.APR_POLLIN : 0)
                                        | ((info.write()) ? Poll.APR_POLLOUT : 0);
                                    if (!addToPoller(info.socket, events)) {
                                        // Can't do anything: close the socket right away
                                        if (!comet || (comet && !processSocket(info.socket, SocketStatus.ERROR))) {
                                            Socket.destroy(info.socket);
                                        }
                                    } else {
                                        timeouts.add(info.socket, System.currentTimeMillis() + info.timeout);
                                    }
                                }
                            } else {
                                // This is either a resume or a suspend.
                                if (comet) {
                                    if (info.resume()) {
                                        // Resume event
                                        timeouts.remove(info.socket);
                                        removeFromPoller(info.socket);
                                        if (!processSocket(info.socket, SocketStatus.OPEN_CALLBACK)) {
                                            Socket.destroy(info.socket);
                                        }
                                    } else {
                                        // Suspend
                                        timeouts.add(info.socket, System.currentTimeMillis() + info.timeout);
                                    }
                                } else {
                                    // Should never happen, if not Comet, the socket is always put in
                                    // the list with the read flag.
                                    timeouts.remove(info.socket);
                                    Socket.destroy(info.socket);
                                    log.error(sm.getString("endpoint.poll.error"));
                                }
                            }
                            info = localAddList.get();
                        }
                    }

                    // Poll for the specified interval
                    for (int i = 0; i < pollers.length; i++) {
                        
                        // Flags to ask to reallocate the pool
                        boolean reset = false;
                        ArrayList<Long> skip = null;
                        
                        int rv = 0;
                        // Iterate on each pollers, but no need to poll empty pollers
                        if (pollerSpace[i] < actualPollerSize) {
                            rv = Poll.poll(pollers[i], pollerTime, desc, true);
                        }
                        if (rv > 0) {
                            pollerSpace[i] += rv;
                            connectionCount -= rv;
                            for (int n = 0; n < rv; n++) {
                                timeouts.remove(desc[n*2+1]);
                                // Check for failed sockets and hand this socket off to a worker
                                if (comet) {
                                    // Comet processes either a read or a write depending on what the poller returns
                                    if (((desc[n*2] & Poll.APR_POLLHUP) == Poll.APR_POLLHUP)
                                            || ((desc[n*2] & Poll.APR_POLLERR) == Poll.APR_POLLERR)) {
                                        if (!processSocket(desc[n*2+1], SocketStatus.ERROR)) {
                                            // Close socket and clear pool
                                            Socket.destroy(desc[n*2+1]);
                                        }
                                        // FIXME: decide vs destroy
                                        /*
                                        if ((desc[n*2] & Poll.APR_POLLHUP) == Poll.APR_POLLHUP) {
                                            // Destroy and reallocate the poller
                                            reset = true;
                                            if (skip == null) {
                                                skip = new ArrayList<Long>();
                                            }
                                            skip.add(desc[n*2+1]);
                                        }*/
                                    } else if ((desc[n*2] & Poll.APR_POLLIN) == Poll.APR_POLLIN) {
                                        if (!processSocket(desc[n*2+1], SocketStatus.OPEN_READ)) {
                                            // Close socket and clear pool
                                            Socket.destroy(desc[n*2+1]);
                                        }
                                    } else if ((desc[n*2] & Poll.APR_POLLOUT) == Poll.APR_POLLOUT) {
                                        if (!processSocket(desc[n*2+1], SocketStatus.OPEN_WRITE)) {
                                            // Close socket and clear pool
                                            Socket.destroy(desc[n*2+1]);
                                        }
                                    }
                                } else if (((desc[n*2] & Poll.APR_POLLHUP) == Poll.APR_POLLHUP)
                                        || ((desc[n*2] & Poll.APR_POLLERR) == Poll.APR_POLLERR)) {
                                    // Close socket and clear pool
                                    Socket.destroy(desc[n*2+1]);
                                    // FIXME: decide vs destroy
                                    /*
                                    if ((desc[n*2] & Poll.APR_POLLHUP) == Poll.APR_POLLHUP) {
                                        // Destroy and reallocate the poller
                                        reset = true;
                                        if (skip == null) {
                                            skip = new ArrayList<Long>();
                                        }
                                        skip.add(desc[n*2+1]);
                                    }*/
                                } else if (!processSocket(desc[n*2+1])) {
                                    // Close socket and clear pool
                                    Socket.destroy(desc[n*2+1]);
                                }
                            }
                        } else if (rv < 0) {
                            int errn = -rv;
                            // Any non timeup or interrupted error is critical
                            if ((errn != Status.TIMEUP) && (errn != Status.EINTR)) {
                                if (errn >  Status.APR_OS_START_USERERR) {
                                    errn -=  Status.APR_OS_START_USERERR;
                                }
                                log.error(sm.getString("endpoint.poll.fail", "" + errn, Error.strerror(errn)));
                                // Destroy and reallocate the poller
                                reset = true;
                            }
                        }
                        
                        if (reset) {
                            // Reallocate the current poller
                            int count = Poll.pollset(pollers[i], desc);
                            long newPoller = allocatePoller(actualPollerSize, pool, -1);
                            // FIXME: don't restore connections for now, since I have not tested it
                            pollerSpace[i] = actualPollerSize;
                            connectionCount -= count;
                            /*
                            for (int j = 0; j < count; j++) {
                                int events = (int) desc[2*j];
                                long socket = desc[2*j+1];
                                Poll.remove(pollers[i], socket);
                                if (skip != null && skip.contains(socket)) {
                                    continue;
                                }
                                if (Poll.add(newPoller, socket, events) != Status.APR_SUCCESS) {
                                    // Skip
                                }
                            }*/
                            Poll.destroy(pollers[i]);
                            pollers[i] = newPoller;
                        }
                        
                    }
                    
                    // Process socket timeouts
                    if (soTimeout > 0 && maintain++ > 1000 && running) {
                        // This works and uses only one timeout mechanism for everything, but the
                        // non Comet poller might be a bit faster by using the old maintain.
                        maintain = 0;
                        maintain();
                    }

                } catch (Throwable t) {
                    if (maintain == 0) {
                        log.error(sm.getString("endpoint.maintain.error"), t);
                    } else {
                        log.error(sm.getString("endpoint.poll.error"), t);
                    }
                }

            }

            synchronized (this) {
                this.notifyAll();
            }

        }
        
    }


    // ----------------------------------------------------- Worker Inner Class


    /**
     * Server processor class.
     */
    protected class Worker implements Runnable {


        protected Thread thread = null;
        protected boolean available = false;
        protected long socket = 0;
        protected SocketStatus status = null;
        protected boolean options = false;


        /**
         * Process an incoming TCP/IP connection on the specified socket.  Any
         * exception that occurs during processing must be logged and swallowed.
         * <b>NOTE</b>:  This method is called from our Connector's thread.  We
         * must assign it to our own thread so that multiple simultaneous
         * requests can be handled.
         *
         * @param socket TCP socket to process
         */
        protected synchronized void assignWithOptions(long socket) {

            // Wait for the Processor to get the previous Socket
            while (available) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            // Store the newly available Socket and notify our thread
            this.socket = socket;
            status = null;
            options = true;
            available = true;
            notifyAll();

        }


        /**
         * Process an incoming TCP/IP connection on the specified socket.  Any
         * exception that occurs during processing must be logged and swallowed.
         * <b>NOTE</b>:  This method is called from our Connector's thread.  We
         * must assign it to our own thread so that multiple simultaneous
         * requests can be handled.
         *
         * @param socket TCP socket to process
         */
        protected synchronized void assign(long socket) {

            // Wait for the Processor to get the previous Socket
            while (available) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            // Store the newly available Socket and notify our thread
            this.socket = socket;
            status = null;
            options = false;
            available = true;
            notifyAll();

        }


        protected synchronized void assign(long socket, SocketStatus status) {

            // Wait for the Processor to get the previous Socket
            while (available) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            // Store the newly available Socket and notify our thread
            this.socket = socket;
            this.status = status;
            options = false;
            available = true;
            notifyAll();

        }


        /**
         * Await a newly assigned Socket from our Connector, or <code>null</code>
         * if we are supposed to shut down.
         */
        protected synchronized long await() {

            // Wait for the Connector to provide a new Socket
            while (!available) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            // Notify the Connector that we have received this Socket
            long socket = this.socket;
            available = false;
            notifyAll();

            return (socket);

        }


        /**
         * The background thread that listens for incoming TCP/IP connections and
         * hands them off to an appropriate processor.
         */
        public void run() {

            // Process requests until we receive a shutdown signal
            while (running) {

                // Wait for the next socket to be assigned
                long socket = await();
                if (socket == 0)
                    continue;

                if (!deferAccept && options) {
                    if (setSocketOptions(socket)) {
                        getPoller().add(socket);
                    } else {
                        // Close socket and pool only if it wasn't closed
                        // already by the parent pool
                        if (serverSockPool != 0) {
                            Socket.destroy(socket);
                        }
                    }
                } else {
                    // Process the request from this socket
                    if ((status != null) && (handler.event(socket, status) == Handler.SocketState.CLOSED)) {
                        // Close socket and pool only if it wasn't closed
                        // already by the parent pool
                        if (serverSockPool != 0) {
                            Socket.destroy(socket);
                        }
                    } else if ((status == null) && ((options && !setSocketOptions(socket)) 
                            || handler.process(socket) == Handler.SocketState.CLOSED)) {
                        // Close socket and pool only if it wasn't closed
                        // already by the parent pool
                        if (serverSockPool != 0) {
                            Socket.destroy(socket);
                        }
                    }
                }

                // Finish up this request
                recycleWorkerThread(this);

            }

        }


        /**
         * Start the background processing thread.
         */
        public void start() {
            thread = new Thread(this);
            thread.setName(getName() + "-" + (++curThreads));
            thread.setDaemon(true);
            thread.start();
        }


    }


    // ----------------------------------------------- SendfileData Inner Class


    /**
     * SendfileData class.
     */
    public static class SendfileData {
        // File
        public String fileName;
        public long fd;
        public long fdpool;
        // Range information
        public long start;
        public long end;
        // Socket and socket pool
        public long socket;
        // Position
        public long pos;
        // KeepAlive flag
        public boolean keepAlive;
    }


    // --------------------------------------------------- Sendfile Inner Class


    /**
     * Sendfile class.
     */
    public class Sendfile implements Runnable {

        protected long sendfilePollset = 0;
        protected long pool = 0;
        protected long[] desc;
        protected HashMap<Long, SendfileData> sendfileData;
        
        protected int sendfileCount;
        public int getSendfileCount() { return sendfileCount; }

        protected ArrayList<SendfileData> addS;

        /**
         * Create the sendfile poller. With some versions of APR, the maximum poller size will
         * be 62 (recompiling APR is necessary to remove this limitation).
         */
        protected void init() {
            pool = Pool.create(serverSockPool);
            int size = sendfileSize;
            sendfilePollset = allocatePoller(size, pool, soTimeout);
            if (sendfilePollset == 0 && size > 1024) {
                size = 1024;
                sendfilePollset = allocatePoller(size, pool, soTimeout);
            }
            if (sendfilePollset == 0) {
                size = 62;
                sendfilePollset = allocatePoller(size, pool, soTimeout);
            }
            desc = new long[size * 2];
            sendfileData = new HashMap<Long, SendfileData>(size);
            addS = new ArrayList<SendfileData>();
        }

        /**
         * Destroy the poller.
         */
        protected void destroy() {
            // Wait for polltime before doing anything, so that the poller threads
            // exit, otherwise parallel destruction of sockets which are still
            // in the poller can cause problems
            try {
                synchronized (this) {
                    this.wait(pollTime / 1000);
                }
            } catch (InterruptedException e) {
                // Ignore
            }
            // Close any socket remaining in the add queue
            for (int i = (addS.size() - 1); i >= 0; i--) {
                SendfileData data = addS.get(i);
                Socket.destroy(data.socket);
            }
            // Close all sockets still in the poller
            int rv = Poll.pollset(sendfilePollset, desc);
            if (rv > 0) {
                for (int n = 0; n < rv; n++) {
                    Socket.destroy(desc[n*2+1]);
                }
            }
            Pool.destroy(pool);
            sendfileData.clear();
        }

        /**
         * Add the sendfile data to the sendfile poller. Note that in most cases,
         * the initial non blocking calls to sendfile will return right away, and
         * will be handled asynchronously inside the kernel. As a result,
         * the poller will never be used.
         *
         * @param data containing the reference to the data which should be snet
         * @return true if all the data has been sent right away, and false
         *              otherwise
         */
        public boolean add(SendfileData data) {
            // Initialize fd from data given
            try {
                data.fdpool = Socket.pool(data.socket);
                data.fd = File.open
                    (data.fileName, File.APR_FOPEN_READ
                     | File.APR_FOPEN_SENDFILE_ENABLED | File.APR_FOPEN_BINARY,
                     0, data.fdpool);
                data.pos = data.start;
                // Set the socket to nonblocking mode
                Socket.timeoutSet(data.socket, 0);
                while (true) {
                    long nw = Socket.sendfilen(data.socket, data.fd,
                                               data.pos, data.end - data.pos, 0);
                    if (nw < 0) {
                        if (!(-nw == Status.EAGAIN)) {
                            Socket.destroy(data.socket);
                            data.socket = 0;
                            return false;
                        } else {
                            // Break the loop and add the socket to poller.
                            break;
                        }
                    } else {
                        data.pos = data.pos + nw;
                        if (data.pos >= data.end) {
                            // Entire file has been sent
                            Pool.destroy(data.fdpool);
                            // Set back socket to blocking mode
                            Socket.timeoutSet(data.socket, soTimeout * 1000);
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                log.error(sm.getString("endpoint.sendfile.error"), e);
                return false;
            }
            // Add socket to the list. Newly added sockets will wait
            // at most for pollTime before being polled
            synchronized (this) {
                addS.add(data);
                this.notify();
            }
            return false;
        }

        /**
         * Remove socket from the poller.
         *
         * @param data the sendfile data which should be removed
         */
        protected void remove(SendfileData data) {
            int rv = Poll.remove(sendfilePollset, data.socket);
            if (rv == Status.APR_SUCCESS) {
                sendfileCount--;
            }
            sendfileData.remove(new Long(data.socket));
        }

        /**
         * The background thread that listens for incoming TCP/IP connections and
         * hands them off to an appropriate processor.
         */
        public void run() {

            long maintainTime = 0;
            // Loop until we receive a shutdown command
            while (running) {

                // Loop if endpoint is paused
                while (paused) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
                // Loop if poller is empty
                while (sendfileCount < 1 && addS.size() < 1) {
                    // Reset maintain time.
                    maintainTime = 0;
                    try {
                        synchronized (this) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }

                try {
                    // Add socket to the poller
                    if (addS.size() > 0) {
                        synchronized (this) {
                            for (int i = (addS.size() - 1); i >= 0; i--) {
                                SendfileData data = addS.get(i);
                                int rv = Poll.add(sendfilePollset, data.socket, Poll.APR_POLLOUT);
                                if (rv == Status.APR_SUCCESS) {
                                    sendfileData.put(new Long(data.socket), data);
                                    sendfileCount++;
                                } else {
                                    log.warn(sm.getString("endpoint.sendfile.addfail", "" + rv, Error.strerror(rv)));
                                    // Can't do anything: close the socket right away
                                    Socket.destroy(data.socket);
                                }
                            }
                            addS.clear();
                        }
                    }

                    maintainTime += pollTime;
                    // Pool for the specified interval
                    int rv = Poll.poll(sendfilePollset, pollTime, desc, false);
                    if (rv > 0) {
                        for (int n = 0; n < rv; n++) {
                            // Get the sendfile state
                            SendfileData state =
                                sendfileData.get(new Long(desc[n*2+1]));
                            // Problem events
                            if (((desc[n*2] & Poll.APR_POLLHUP) == Poll.APR_POLLHUP)
                                    || ((desc[n*2] & Poll.APR_POLLERR) == Poll.APR_POLLERR)) {
                                // Close socket and clear pool
                                remove(state);
                                // Destroy file descriptor pool, which should close the file
                                // Close the socket, as the response would be incomplete
                                Socket.destroy(state.socket);
                                continue;
                            }
                            // Write some data using sendfile
                            long nw = Socket.sendfilen(state.socket, state.fd,
                                                       state.pos,
                                                       state.end - state.pos, 0);
                            if (nw < 0) {
                                // Close socket and clear pool
                                remove(state);
                                // Close the socket, as the response would be incomplete
                                // This will close the file too.
                                Socket.destroy(state.socket);
                                continue;
                            }

                            state.pos = state.pos + nw;
                            if (state.pos >= state.end) {
                                remove(state);
                                if (state.keepAlive) {
                                    // Destroy file descriptor pool, which should close the file
                                    Pool.destroy(state.fdpool);
                                    Socket.timeoutSet(state.socket, soTimeout * 1000);
                                    // If all done put the socket back in the poller for
                                    // processing of further requests
                                    getPoller().add(state.socket);
                                } else {
                                    // Close the socket since this is
                                    // the end of not keep-alive request.
                                    Socket.destroy(state.socket);
                                }
                            }
                        }
                    } else if (rv < 0) {
                        int errn = -rv;
                        /* Any non timeup or interrupted error is critical */
                        if ((errn != Status.TIMEUP) && (errn != Status.EINTR)) {
                            if (errn >  Status.APR_OS_START_USERERR) {
                                errn -=  Status.APR_OS_START_USERERR;
                            }
                            log.error(sm.getString("endpoint.poll.fail", "" + errn, Error.strerror(errn)));
                            // Handle poll critical failure
                            synchronized (this) {
                                destroy();
                                init();
                            }
                            continue;
                        }
                    }
                    // Call maintain for the sendfile poller
                    if (soTimeout > 0 && maintainTime > 1000000L && running) {
                        rv = Poll.maintain(sendfilePollset, desc, false);
                        maintainTime = 0;
                        if (rv > 0) {
                            for (int n = 0; n < rv; n++) {
                                // Get the sendfile state
                                SendfileData state = sendfileData.get(new Long(desc[n]));
                                // Close socket and clear pool
                                remove(state);
                                // Destroy file descriptor pool, which should close the file
                                // Close the socket, as the response would be incomplete
                                Socket.destroy(state.socket);
                            }
                        }
                    }
                } catch (Throwable t) {
                    log.error(sm.getString("endpoint.poll.error"), t);
                }
            }

            synchronized (this) {
                this.notifyAll();
            }

        }

    }


    // ------------------------------------------------ Handler Inner Interface


    /**
     * Bare bones interface used for socket processing. Per thread data is to be
     * stored in the ThreadWithAttributes extra folders, or alternately in
     * thread local fields.
     */
    public interface Handler {
        public enum SocketState {
            OPEN, CLOSED, LONG
        }
        public SocketState process(long socket);
        public SocketState event(long socket, SocketStatus status);
    }


    // ------------------------------------------------- WorkerStack Inner Class


    public class WorkerStack {
        
        protected Worker[] workers = null;
        protected int end = 0;
        
        public WorkerStack(int size) {
            workers = new Worker[size];
        }
        
        /** 
         * Put the object into the queue.
         * 
         * @param   object      the object to be appended to the queue (first element). 
         */
        public void push(Worker worker) {
            workers[end++] = worker;
        }
        
        /**
         * Get the first object out of the queue. Return null if the queue
         * is empty. 
         */
        public Worker pop() {
            if (end > 0) {
                return workers[--end];
            }
            return null;
        }
        
        /**
         * Get the first object out of the queue, Return null if the queue
         * is empty.
         */
        public Worker peek() {
            return workers[end];
        }
        
        /**
         * Is the queue empty?
         */
        public boolean isEmpty() {
            return (end == 0);
        }
        
        /**
         * How many elements are there in this queue?
         */
        public int size() {
            return (end);
        }
    }


    // ---------------------------------------------- SocketProcessor Inner Class


    /**
     * This class is the equivalent of the Worker, but will simply use in an
     * external Executor thread pool. This will also set the socket options
     * and do the handshake.
     */
    protected class SocketWithOptionsProcessor implements Runnable {
        
        protected long socket = 0;
        
        public SocketWithOptionsProcessor(long socket) {
            this.socket = socket;
        }

        public void run() {

            if (!deferAccept) {
                if (setSocketOptions(socket)) {
                    getPoller().add(socket);
                } else {
                    // Close socket and pool
                    Socket.destroy(socket);
                }
            } else {
                // Process the request from this socket
                if (!setSocketOptions(socket) 
                        || handler.process(socket) == Handler.SocketState.CLOSED) {
                    // Close socket and pool
                    Socket.destroy(socket);
                }
            }
            socket = 0;

        }
        
    }
    
    
    // ---------------------------------------------- SocketProcessor Inner Class


    /**
     * This class is the equivalent of the Worker, but will simply use in an
     * external Executor thread pool.
     */
    protected class SocketProcessor implements Runnable {
        
        protected long socket = 0;
        
        public SocketProcessor(long socket) {
            this.socket = socket;
        }

        public void run() {

            // Process the request from this socket
            if (handler.process(socket) == Handler.SocketState.CLOSED) {
                // Close socket and pool
                Socket.destroy(socket);
            }
            socket = 0;

        }
        
    }
    
    
    // --------------------------------------- SocketEventProcessor Inner Class


    /**
     * This class is the equivalent of the Worker, but will simply use in an
     * external Executor thread pool.
     */
    protected class SocketEventProcessor implements Runnable {
        
        protected long socket = 0;
        protected SocketStatus status = null; 
        
        public SocketEventProcessor(long socket, SocketStatus status) {
            this.socket = socket;
            this.status = status;
        }

        public void run() {

            // Process the request from this socket
            if (handler.event(socket, status) == Handler.SocketState.CLOSED) {
                // Close socket and pool
                Socket.destroy(socket);
            }
            socket = 0;

        }
        
    }
    
    
}
