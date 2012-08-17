/*
 *
 *  Copyright(c) 2008 Red Hat Middleware, LLC,
 *  and individual contributors as indicated by the @authors tag.
 *  See the copyright.txt in the distribution for a
 *  full listing of individual contributors.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library in the file COPYING.LIB;
 *  if not, write to the Free Software Foundation, Inc.,
 *  59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
 *
 */

package org.jboss.web.cluster.advertise;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.jboss.web.cluster.ClusterListener;


/** AdvertiseListener
 * Listens for Advertise messages from mod_cluster
 *
 * @author Mladen Turk
 *
 */
public class AdvertiseListener
{
    /** Default port for listening Advertise messages.
     */
    public static int    DEFAULT_PORT      = 23364;
    /** Default Multicast group address for listening Advertise messages.
     */
    public static String DEFAULT_GROUP     = "224.0.1.105";

    private static String RFC_822_FMT      = "EEE, d MMM yyyy HH:mm:ss Z";
    private int              advertisePort = DEFAULT_PORT;
    private String           groupAddress  = DEFAULT_GROUP;
    private MulticastSocket  ms;
    private SimpleDateFormat df;
    private boolean          listening   = true;
    private boolean          initialized = false;
    private boolean          running     = false;
    private boolean          paused      = false;
    private boolean          daemon      = true;
    private byte []          secure      = new byte[16];
    private String           securityKey = null;
    private MessageDigest    md          = null;

    private     HashMap<String, AdvertisedServer> servers;

    private     ClusterListener listener;
    private     Thread          workerThread;


    private static void digestString(MessageDigest md, String s)
    {
        int len   = s.length();
        byte [] b = new byte[len];
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c < 127)
                b[i] = (byte)c;
            else
                b[i] = '?';
        }
        md.update(b);
    }

    /** Create AdvertiseListener instance
     * @param eventHandler The event handler that will be used for
     * status and new server notifications.
     */
    public AdvertiseListener(ClusterListener listener)
    {
        df = new SimpleDateFormat(RFC_822_FMT, Locale.US);
        servers = new HashMap<String, AdvertisedServer>();
        this.listener = listener;
    }

    /**
     * The default is true - the control thread will be
     * in daemon mode. If set to false, the control thread
     * will not be daemon - and will keep the process alive.
     */
    public void setDaemon(boolean b)
    {
        daemon = b;
    }

    public boolean getDaemon()
    {
        return daemon;
    }

    /** Set Advertise security key
     * @param key The key to use.<br/>
     *      Security key must match the AdvertiseKey
     *      on the advertised server.
     */
    public void     setSecurityKey(String key)
        throws NoSuchAlgorithmException
    {
        securityKey = key;
        if (md == null)
            md = MessageDigest.getInstance("MD5");
    }

    /** Set Advertise port
     * @param port The UDP port to use.
     */
    public void     setAdvertisePort(int port)
    {
        advertisePort = port;
    }

    public int      getAdvertisePort()
    {
        return advertisePort;
    }

    /** Set Advertise Multicaset group address
     * @param address The IP or host address to use.
     */
    public void     setGroupAddress(String address)
    {
        groupAddress = address;
    }

    /** Get Advertise Multicaset group address
     */
    public String   getGroupAddress()
    {
        return groupAddress;
    }

    /** Get Collection of all AdvertisedServer instances.
     */
    public Collection<AdvertisedServer> getServers()
    {
        return servers.values();
    }

    /** Get AdvertiseServer server.
     * @param name Server name to get.
     */
    public AdvertisedServer getServer(String name)
    {
        return servers.get(name);
    }

    /** Remove the AdvertisedServer from the collection.
     * @param server Server to remove.
     */
    public void removeServer(AdvertisedServer server)
    {
        servers.remove(server);
    }

    private void init()
        throws IOException
    {
        ms = new MulticastSocket(advertisePort);
        ms.setTimeToLive(16);
        ms.joinGroup(InetAddress.getByName(groupAddress));
        initialized = true;
    }

    private void interruptDatagramReader()
    {
        if (!initialized)
            return;
        try {
            // Restrict to localhost.
            ms.setTimeToLive(0);
            DatagramPacket dp = new DatagramPacket(secure, secure.length,
                                            InetAddress.getByName(groupAddress),
                                            advertisePort);
            ms.send(dp);
        } catch (IOException e) {
            // Ignore
        }
    }

    /** Start the Listener, creating listener thread.
     */
    public void start()
        throws IOException
    {
        if (!initialized) {
            init();
        }
        if (!running) {
            SecureRandom random = new SecureRandom();
            random.nextBytes(secure);
            secure[0] = 0;
            running = true;
            paused  = false;
            listening = true;
            AdvertiseListenerWorker aw = new AdvertiseListenerWorker();
            workerThread = new Thread(aw);
            workerThread.setDaemon(daemon);
            workerThread.start();
        }
    }

    /**
     * Pause the listener, which will make it stop accepting new advertise
     * messages.
     */
    public void pause()
    {
        if (running && !paused) {
            paused = true;
            interruptDatagramReader();
        }
    }


    /**
     * Resume the listener, which will make it start accepting new advertise
     * messages again.
     */
    public void resume()
    {
        if (running && paused) {
            // Genererate new private secure
            SecureRandom random = new SecureRandom();
            random.nextBytes(secure);
            secure[0] = 0;
            paused = false;
        }
    }


    /**
     * Stop the endpoint. This will cause all processing threads to stop.
     */
    public void stop()
    {
        if (running) {
            running = false;
            interruptDatagramReader();
            workerThread = null;
        }
    }


    /**
     * Deallocate listener and close sockets.
     */
    public void destroy()
    throws IOException
    {
        if (running) {
            stop();
        }
        if (initialized) {
            ms.leaveGroup(InetAddress.getByName(groupAddress));
            ms.close();
            initialized = false;
            ms = null;
        }
    }

    private boolean verifyDigest(String digest, String server, String date)
    {
        if (md == null)
            return true;
        md.reset();
        digestString(md, securityKey);
        digestString(md, date);
        digestString(md, server);
        byte [] our = md.digest();
        byte [] dst = new byte[digest.length() * 2];
        for (int i = 0, j = 0; i < digest.length(); i++) {
            char ch = digest.charAt(i);
            dst[j++] = (byte)((ch >= 'A') ? ((ch & 0xdf) - 'A') + 10 : (ch - '0'));
        }
        return true;
    }

    /**
     * True if listener is accepting the advetise messages.<br/>
     * If false it means that listener is experiencing some
     * network problems if running.
     */
    public boolean isListening()
    {
        return listening;
    }


    // ------------------------------------ AdvertiseListenerWorker Inner Class
    private class AdvertiseListenerWorker implements Runnable
    {

        protected AdvertiseListenerWorker()
        {
            // Nothing
        }
        /**
         * The background thread that listens for incoming Advertise packets
         * and hands them off to an appropriate AdvertiseEvent handler.
         */
        public void run() {
            byte[] buffer = new byte[512];
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
                try {
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                    ms.receive(dp);
                    if (!running)
                        break;
                    byte [] data = dp.getData();
                    boolean intr = false;
                    if (dp.getLength() == secure.length) {
                        int i;
                        for (i = 0; i < secure.length; i++) {
                            if (data[i] != secure[i])
                                break;
                        }
                        if (i == secure.length)
                            intr = true;
                    }
                    if (intr)
                        continue;
                    String s = new String(data, 0, dp.getLength(), "8859_1");
                    if (!s.startsWith("HTTP/1."))
                        continue;

                    String [] headers =  s.split("\r\n");
                    String    date_str = null;
                    Date      date   = null;
                    int       status = 0;
                    String    status_desc = null;
                    String    digest      = null;
                    String    server_name = null;
                    AdvertisedServer server = null;
                    boolean added = false;
                    for (int i = 0; i < headers.length; i++) {
                        if (i == 0) {
                            String [] sline = headers[i].split(" ", 3);
                            if (sline == null || sline.length != 3)
                                break;
                             status = Integer.parseInt(sline[1]);
                             if (status < 100)
                                break;
                             status_desc = sline[2];
                        }
                        else {
                            String [] hdrv = headers[i].split(": ", 2);
                            if (hdrv == null || hdrv.length != 2)
                                break;
                            if (hdrv[0].equals("Date")) {
                                date_str = hdrv[1];
                                try {
                                    date = df.parse(date_str);
                                } catch (ParseException e) {
                                    date = new Date();
                                }
                            }
                            else if (hdrv[0].equals("Digest")) {
                                digest = hdrv[1];
                            }
                            else if (hdrv[0].equals("Server")) {
                                server_name = hdrv[1];
                                server = servers.get(server_name);
                                if (server == null) {
                                    server = new AdvertisedServer(server_name);
                                    added = true;
                                }
                            }
                            else if (server != null) {
                                server.setParameter(hdrv[0], hdrv[1]);
                            }
                        }
                    }
                    if (server != null && status > 0) {
                        if (md != null) {
                            /* We need a digest to match */
                            if (!verifyDigest(digest, server_name, date_str)) {
                                System.out.println("Digest mismatch");
                                continue;
                            }
                        }
                        server.setDate(date);
                        boolean rc = server.setStatus(status, status_desc);
                        if (added) {
                            servers.put(server_name, server);
                            // Call the new server callback
                            //eventHandler.onEvent(AdvertiseEventType.ON_NEW_SERVER, server);
                            String proxy = server.getParameter(AdvertisedServer.MANAGER_ADDRESS);
                            if (proxy != null) {
                                listener.addProxy(proxy);
                            }
                        }
                        else if (rc) {
                            // Call the status change callback
                            //eventHandler.onEvent(AdvertiseEventType.ON_STATUS_CHANGE, server);
                        }
                    }
                    listening = true;
                } catch (IOException e) {
                    // Do not blow the CPU in case of communication error
                    listening = false;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException x) {
                        // Ignore
                    }
                }
            }
        }
    }

}
