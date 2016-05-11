

package org.jgroups.protocols;


import org.jgroups.Address;
import org.jgroups.blocks.ConnectionTable;
import org.jgroups.stack.IpAddress;
import org.jgroups.util.PortsManager;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Properties;


/**
 * TCP based protocol. Creates a server socket, which gives us the local address of this group member. For
 * each accept() on the server socket, a new thread is created that listens on the socket.
 * For each outgoing message m, if m.dest is in the ougoing hashtable, the associated socket will be reused
 * to send message, otherwise a new socket is created and put in the hashtable.
 * When a socket connection breaks or a member is removed from the group, the corresponding items in the
 * incoming and outgoing hashtables will be removed as well.<br>
 * This functionality is in ConnectionTable, which isT used by TCP. TCP sends messages using ct.send() and
 * registers with the connection table to receive all incoming messages.
 * @author Bela Ban
 */
public class TCP extends BasicTCP implements ConnectionTable.Receiver { // , BasicConnectionTable.ConnectionListener {
    private ConnectionTable ct=null;



   public TCP() {
   }

    public String getName() {
        return "TCP";
    }


    public int getOpenConnections()      {return ct.getNumConnections();}
    public String printConnections()     {return ct.toString();}


    /** Setup the Protocol instance acording to the configuration string */
    public boolean setProperties(Properties props) {
        super.setProperties(props);
        if(!props.isEmpty()) {
            log.error("the following properties are not recognized: " + props);
            return false;
        }
        return true;
    }

    public void send(Address dest, byte[] data, int offset, int length) throws Exception {
        ct.send(dest, data, offset, length);
    }

    public void retainAll(Collection<Address> members) {
        ct.retainAll(members);
    }

    public void start() throws Exception {
        ct=getConnectionTable(reaper_interval,conn_expire_time,bind_addr,external_addr,start_port,end_port,pm);
        // ct.addConnectionListener(this);
        ct.setUseSendQueues(use_send_queues);
        ct.setSendQueueSize(send_queue_size);
        // ct.addConnectionListener(this);
        ct.setReceiveBufferSize(recv_buf_size);
        ct.setSendBufferSize(send_buf_size);
        ct.setSocketConnectionTimeout(sock_conn_timeout);
        ct.setPeerAddressReadTimeout(peer_addr_read_timeout);
        ct.setTcpNodelay(tcp_nodelay);
        ct.setLinger(linger);
        local_addr=ct.getLocalAddress();
        if(additional_data != null && local_addr instanceof IpAddress)
            ((IpAddress)local_addr).setAdditionalData(additional_data);
        
        //http://jira.jboss.com/jira/browse/JGRP-626
        //we first start threads in TP
        super.start();
        //and then we kick off acceptor thread for CT
        ct.start();
    }

    public void stop() {
        //and for stopping we do vice versa
        ct.stop();
        super.stop();
    }




   /**
    * @param reaperInterval
    * @param connExpireTime
    * @param bindAddress
    * @param startPort
    * @throws Exception
    * @return ConnectionTable
    * Sub classes overrides this method to initialize a different version of
    * ConnectionTable.
    */
   protected ConnectionTable getConnectionTable(long reaperInterval, long connExpireTime, InetAddress bindAddress,
                                                InetAddress externalAddress, int startPort, int endPort,
                                                PortsManager pm) throws Exception {
       ConnectionTable cTable;
       if(reaperInterval == 0 && connExpireTime == 0) {
           cTable=new ConnectionTable(this, bindAddress, externalAddress, startPort, endPort, pm);
       }
       else {
           if(reaperInterval == 0) {
               reaperInterval=5000;
               if(log.isWarnEnabled()) log.warn("reaper_interval was 0, set it to " + reaperInterval);
           }
           if(connExpireTime == 0) {
               connExpireTime=1000 * 60 * 5;
               if(log.isWarnEnabled()) log.warn("conn_expire_time was 0, set it to " + connExpireTime);
           }
           cTable=new ConnectionTable(this, bindAddress, externalAddress, startPort, endPort,
                                      reaperInterval, connExpireTime, pm);
       }       
       cTable.setThreadFactory(getThreadFactory());
       return cTable;
   }


//    public void connectionOpened(Address peer_addr) {
//    }
//
//    public void connectionClosed(Address peer_addr) {
//        if(log.isTraceEnabled())
//            log.trace("removing connection to " + peer_addr + " from connection table as peer closed connection");
//        ct.remove(peer_addr);
//    }
}
