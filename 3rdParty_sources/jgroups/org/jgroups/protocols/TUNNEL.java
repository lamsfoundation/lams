

package org.jgroups.protocols;

import org.jgroups.Address;
import org.jgroups.Event;
import org.jgroups.Message;
import org.jgroups.annotations.GuardedBy;
import org.jgroups.stack.RouterStub;
import org.jgroups.stack.IpAddress;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Replacement for UDP. Instead of sending packets via UDP, a TCP connection is
 * opened to a Router (using the RouterStub client-side stub), the IP
 * address/port of which was given using channel properties
 * <code>router_host</code> and <code>router_port</code>. All outgoing
 * traffic is sent via this TCP socket to the Router which distributes it to all
 * connected TUNNELs in this group. Incoming traffic received from Router will
 * simply be passed up the stack.
 * 
 * <p>
 * A TUNNEL layer can be used to penetrate a firewall, most firewalls allow
 * creating TCP connections to the outside world, however, they do not permit
 * outside hosts to initiate a TCP connection to a host inside the firewall.
 * Therefore, the connection created by the inside host is reused by Router to
 * send traffic from an outside host to a host inside the firewall.
 * 
 * @author Bela Ban
 */
public class TUNNEL extends TP {
    private String router_host = null;

    private int router_port = 0;

    private RouterStub stub;

    long reconnect_interval = 5000;
    
    /*
     * flag indicating if tunnel was destroyed intentionally (disconnect, channel destroy etc)
     */
    private volatile boolean intentionallyTornDown = false; 

    /** time to wait in ms between reconnect attempts */

    @GuardedBy("reconnectorLock")
    private Future<?> reconnectorFuture = null;

    private final Lock reconnectorLock = new ReentrantLock();

    public TUNNEL(){}

    public String toString() {
        return "Protocol TUNNEL(local_addr=" + local_addr + ')';
    }

    public String getRouterHost() {
        return router_host;
    }

    public void setRouterHost(String router_host) {
        this.router_host=router_host;
    }

    public int getRouterPort() {
        return router_port;
    }

    public void setRouterPort(int router_port) {
        this.router_port=router_port;
    }

    public long getReconnectInterval() {
        return reconnect_interval;
    }

    public void setReconnectInterval(long reconnect_interval) {
        this.reconnect_interval=reconnect_interval;
    }

    /*------------------------------ Protocol interface ------------------------------ */

    public String getName() {
        return "TUNNEL";
    }

    public void init() throws Exception {        
        super.init();
        if(timer == null)
            throw new Exception("TUNNEL.init(): timer cannot be retrieved from protocol stack");
    }

    public void start() throws Exception {
        // loopback turned on is mandatory
        loopback = true;
        intentionallyTornDown = false;

        stub = new RouterStub(router_host, router_port, bind_addr);
        stub.setConnectionListener(new StubConnectionListener());
        local_addr = stub.getLocalAddress();
        if(additional_data != null && local_addr instanceof IpAddress)
            ((IpAddress) local_addr).setAdditionalData(additional_data);
        super.start();
    }

    public void stop() {        
        teardownTunnel();
        super.stop();        
    }

    /** Setup the Protocol instance acording to the configuration string */
    public boolean setProperties(Properties props) {
        String str;

        super.setProperties(props);
        str = props.getProperty("router_host");
        if(str != null){
            router_host = str;
            props.remove("router_host");
        }

        str = props.getProperty("router_port");
        if(str != null){
            router_port = Integer.parseInt(str);
            props.remove("router_port");
        }

        if(log.isDebugEnabled()){
            log.debug("router_host=" + router_host + ";router_port=" + router_port);
        }

        if(router_host == null || router_port == 0){
            if(log.isErrorEnabled()){
                log.error("both router_host and router_port have to be set !");
                return false;
            }
        }

        str = props.getProperty("reconnect_interval");
        if(str != null){
            reconnect_interval = Long.parseLong(str);
            props.remove("reconnect_interval");
        }

        if(!props.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Enumeration e = props.propertyNames();e.hasMoreElements();){
                sb.append(e.nextElement().toString());
                if(e.hasMoreElements()){
                    sb.append(", ");
                }
            }
            if(log.isErrorEnabled())
                log.error("The following properties are not recognized: " + sb);
            return false;
        }
        return true;
    }

    /** Tears the TCP connection to the router down */
    void teardownTunnel() {
        intentionallyTornDown = true;
        stopReconnecting();
        stub.disconnect();
    }

    public Object handleDownEvent(Event evt) {
        Object retEvent = super.handleDownEvent(evt);
        switch(evt.getType()){
        case Event.CONNECT:
        case Event.CONNECT_WITH_STATE_TRANSFER:
            try{
                stub.connect(channel_name);
            }catch(Exception e){
                if(log.isErrorEnabled())
                    log.error("failed connecting to GossipRouter at " + router_host
                              + ":"
                              + router_port);
                startReconnecting();
            }
            break;

        case Event.DISCONNECT:
            teardownTunnel();
            break;
        }
        return retEvent;
    }

    private void startReconnecting() {
        reconnectorLock.lock();
        try{
            if(reconnectorFuture == null || reconnectorFuture.isDone()){
                final Runnable reconnector = new Runnable() {
                    public void run() {
                        try{
                            if(!intentionallyTornDown){
                                if(log.isDebugEnabled()){
                                    log.debug("Reconnecting " + getLocalAddress()
                                              + " to router at "
                                              + router_host
                                              + ":"
                                              + router_port);
                                }
                                stub.connect(channel_name);
                            }
                        }catch(Exception ex){
                            if(log.isTraceEnabled())
                                log.trace("failed reconnecting", ex);
                        }
                    }
                };
                reconnectorFuture = timer.scheduleWithFixedDelay(reconnector,
                                                                 0,
                                                                 reconnect_interval,
                                                                 TimeUnit.MILLISECONDS);
            }
        }finally{
            reconnectorLock.unlock();
        }
    }

    private void stopReconnecting() {
        reconnectorLock.lock();
        try{
            if(reconnectorFuture != null){
                reconnectorFuture.cancel(true);
                reconnectorFuture = null;
            }
        }finally{
            reconnectorLock.unlock();
        }
    }

    private class StubConnectionListener implements RouterStub.ConnectionListener {

        private volatile int currentState = RouterStub.STATUS_DISCONNECTED;

        public void connectionStatusChange(int newState) {            
            if(newState == RouterStub.STATUS_DISCONNECTED){
                startReconnecting();
            }else if(currentState != RouterStub.STATUS_CONNECTED && newState == RouterStub.STATUS_CONNECTED){
                stopReconnecting();
                Thread t = global_thread_factory.newThread(new TunnelReceiver(), "TUNNEL receiver");
                t.setDaemon(true);
                t.start();
            }
            currentState = newState;
        }
    }

    private class TunnelReceiver implements Runnable {
        public void run() {
            while(stub.isConnected()){
                Address dest = null;                
                int len;
                byte[] data = null;
                DataInputStream input = null;
                try{
                    input = stub.getInputStream();
                    dest = Util.readAddress(input);                    
                    len = input.readInt();
                    if(len > 0){
                        data = new byte[len];
                        input.readFully(data, 0, len);                        
                        receive(dest, null/*src will be read from data*/, data, 0, len);
                    }
                }catch(SocketException se){
                    // if(log.isWarnEnabled()) log.warn("failure in TUNNEL
                    // receiver thread", se);
                }catch(IOException ioe){
                    // if(log.isWarnEnabled()) log.warn("failure in TUNNEL
                    // receiver thread", ioe);
                }catch(Exception e){
                    if(log.isWarnEnabled())
                        log.warn("failure in TUNNEL receiver thread", e);
                }
            }
        }
    }

    public void sendToAllMembers(byte[] data, int offset, int length) throws Exception {
        stub.sendToAllMembers(data, offset, length);
    }

    public void sendToSingleMember(Address dest, byte[] data, int offset, int length) throws Exception {
        stub.sendToSingleMember(dest, data, offset, length);
    }

    public String getInfo() {
        if(stub != null)
            return stub.toString();
        else
            return "RouterStub not yet initialized";
    }

    public void postUnmarshalling(Message msg, Address dest, Address src, boolean multicast) {
        msg.setDest(dest);
    }

    public void postUnmarshallingList(Message msg, Address dest, boolean multicast) {
        msg.setDest(dest);
    }
}
