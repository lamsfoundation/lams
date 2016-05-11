package org.jgroups.jmx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Bela Ban
 *
 */
public class JChannel implements JChannelMBean {
    /** Ref to the original JGroups channel */
    org.jgroups.JChannel channel;
    String props=null;
    String group_name="TestGroup";
    String object_name=null;
    Log    log=LogFactory.getLog(getClass());

    /*flag to indicate whether to receive blocks, if this is set to true, receive_views is set to true*/
    private boolean receive_blocks=false;
    /*flag to indicate whether to receive local messages
     *if this is set to false, the JChannel will not receive messages sent by itself*/
    private boolean receive_local_msgs=true;
    /*flag to indicate whether the channel will reconnect (reopen) when the exit message is received*/
    private boolean auto_reconnect=false;
    /*flag t indicate whether the state is supposed to be retrieved after the channel is reconnected
     *setting this to true, automatically forces auto_reconnect to true*/
    private boolean auto_getstate=false;

    private String mbean_server_name=null;


    public JChannel() {
    }

    public JChannel(org.jgroups.JChannel channel) {
        this.channel=channel;
        setValues();
    }


    protected final void setValues() {
        if(channel != null) {
            props=getProperties();
            group_name=channel.getClusterName();
            receive_blocks=getReceiveBlockEvents();
            receive_local_msgs=getReceiveLocalMessages();
            auto_reconnect=getAutoReconnect();
            auto_getstate=getAutoGetState();
        }
    }

    //void jbossInternalLifecycle(String method) throws Exception;
    public org.jgroups.JChannel getChannel() {
        return channel;
    }

    public String getVersion() {
        return Version.printDescription();
    }

    public String getMBeanServerName() {
        return mbean_server_name;
    }

    public void setMBeanServerName(String n) {
        this.mbean_server_name=n;
    }

    public String getProperties() {
        props=channel.getProperties();
        return props;
    }

    public void setProperties(String props) {
        this.props=props;
    }

    public String getObjectName() {
        return object_name;
    }

    public void setObjectName(String name) {
        object_name=name;
    }

    public int getNumberOfTasksInTimer() {
        return channel.getNumberOfTasksInTimer();
    }

    public String dumpTimerQueue() {
        return channel.dumpTimerQueue();
    }

    public void setClusterConfig(Element config) {
        StringBuilder buffer=new StringBuilder();
        NodeList stack=config.getChildNodes();
        int length=stack.getLength();

        for(int s=0; s < length; s++) {
            org.w3c.dom.Node node=stack.item(s);
            if(node.getNodeType() != org.w3c.dom.Node.ELEMENT_NODE)
                continue;

            Element tag=(Element)node;
            String protocol=tag.getTagName();
            buffer.append(protocol);
            NamedNodeMap attrs=tag.getAttributes();
            int attrLength=attrs.getLength();
            if(attrLength > 0)
                buffer.append('(');
            for(int a=0; a < attrLength; a++) {
                Attr attr=(Attr)attrs.item(a);
                String name=attr.getName();
                String value=attr.getValue();
                buffer.append(name);
                buffer.append('=');
                buffer.append(value);
                if(a < attrLength - 1)
                    buffer.append(';');
            }
            if(attrLength > 0)
                buffer.append(')');
            buffer.append(':');
        }
        // Remove the trailing ':'
        buffer.setLength(buffer.length() - 1);
        setProperties(buffer.toString());
        if(log.isDebugEnabled())
            log.debug("setting cluster properties from xml to: " + props);
    }

    public String getGroupName() {
        if(channel != null)
            group_name=channel.getClusterName();
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name=group_name;
    }

    public String getClusterName() {
        return getGroupName();
    }

    public void setClusterName(String cluster_name) {
        setGroupName(cluster_name);
    }

    public boolean getReceiveBlockEvents() {
        if(channel != null)
            receive_blocks=((Boolean)channel.getOpt(Channel.BLOCK)).booleanValue();
        return receive_blocks;
    }

    public void setReceiveBlockEvents(boolean flag) {
        this.receive_blocks=flag;
        if(channel != null)
            channel.setOpt(Channel.BLOCK, Boolean.valueOf(flag));
    }

    public boolean getReceiveLocalMessages() {
        if(channel != null)
            receive_local_msgs=((Boolean)channel.getOpt(Channel.LOCAL)).booleanValue();
        return receive_local_msgs;
    }

    public void setReceiveLocalMessages(boolean flag) {
        this.receive_local_msgs=flag;
        if(channel != null)
            channel.setOpt(Channel.LOCAL, Boolean.valueOf(flag));
    }

    public boolean getAutoReconnect() {
        if(channel != null)
            auto_reconnect=((Boolean)channel.getOpt(Channel.AUTO_RECONNECT)).booleanValue();
        return auto_reconnect;
    }

    public void setAutoReconnect(boolean flag) {
        this.auto_reconnect=flag;
        if(channel != null) {
            channel.setOpt(Channel.AUTO_RECONNECT, Boolean.valueOf(flag));
        }
    }

    public boolean getAutoGetState() {
        if(channel != null)
            auto_getstate=((Boolean)channel.getOpt(Channel.AUTO_GETSTATE)).booleanValue();
        return auto_getstate;
    }

    public void setAutoGetState(boolean flag) {
        this.auto_getstate=flag;
        if(channel != null)
            channel.setOpt(Channel.AUTO_GETSTATE, Boolean.valueOf(flag));
    }

    public boolean getStatsEnabled() {
        return channel.statsEnabled();
    }

    public void setStatsEnabled(boolean flag) {
        channel.enableStats(flag);
    }

    public Map dumpStats() {
        return channel.dumpStats();
    }

    public void resetStats() {
        channel.resetStats();
    }

    public long getSentMessages() {return channel.getSentMessages();}
    public long getSentBytes() {return channel.getSentBytes();}
    public long getReceivedMessages() {return channel.getReceivedMessages();}
    public long getReceivedBytes() {return channel.getReceivedBytes();}


    public int getTimerThreads() {
        return channel.getTimerThreads();
    }

    public void create() throws Exception {
        if(channel != null)
            channel.close();
        channel=new org.jgroups.JChannel(props);
        setOptions();
        setValues();
        MBeanServer server=(MBeanServer)MBeanServerFactory.findMBeanServer(mbean_server_name).get(0);
        JmxConfigurator.registerProtocols(server, channel, getObjectName());
    }

    public void start() throws Exception {
        channel.connect(group_name);
    }

    public void stop() {
        if(channel != null)
            channel.disconnect();
    }

    public void destroy() {
        MBeanServer server=(MBeanServer)MBeanServerFactory.findMBeanServer(mbean_server_name).get(0);
        JmxConfigurator.unregisterProtocols(server, channel, getObjectName());
        if(channel != null) {
            channel.close();
            channel=null;
        }
    }

//    public void jbossInternalLifecycle(String method) throws Exception {
//        System.out.println("method: " + method);
//        if (method == null)
//            throw new IllegalArgumentException("Null method name");
//
//        if (method.equals("create"))
//            create();
//        else if (method.equals("start"))
//            start();
//        else if (method.equals("stop"))
//            stop();
//        else if (method.equals("destroy"))
//            destroy();
//        else
//            throw new IllegalArgumentException("Unknown lifecyle method " + method);
//    }


    public View getView() {
        return channel.getView();
    }

    public String getViewAsString() {
        View v=channel.getView();
        return v != null ? v.toString() : "n/a";
    }

    public Address getLocalAddress() {
        return channel.getLocalAddress();
    }

    public String getLocalAddressAsString() {
        Address addr=getLocalAddress();
        return addr != null? addr.toString() : "n/a";
    }

    /** @deprecated Use addChannelListener() instead */
    public void setChannelListener(ChannelListener channel_listener) {
        if(channel != null)
            channel.addChannelListener(channel_listener);
    }

    public void addChannelListener(ChannelListener listener) {
        if(channel != null)
            channel.addChannelListener(listener);
    }

    public void removeChannelListener(ChannelListener l) {
        if(channel != null)
            channel.removeChannelListener(l);
    }

    public boolean isOpen() {
        return channel.isOpen();
    }

    public boolean isConnected() {
        return channel.isConnected();
    }

    public int getNumMessages() {
        return channel.getNumMessages();
    }

    public String dumpQueue() {
        return channel.dumpQueue();
    }

    public String printProtocolSpec(boolean include_properties) {
        return channel.printProtocolSpec(include_properties);
    }

    public String toString(boolean print_details) {
        return channel.toString(print_details);
    }

    public void connect(String channel_name) throws ChannelException {
        channel.connect(channel_name);
    }

    public void disconnect() {
        channel.disconnect();
    }

    public void close() {
        channel.close();
    }

    public void shutdown() {
        channel.shutdown();
    }

    public void send(Message msg) throws ChannelNotConnectedException, ChannelClosedException {
        channel.send(msg);
    }

    public void send(Address dst, Address src, Serializable obj) throws ChannelNotConnectedException, ChannelClosedException {
        channel.send(dst, src, obj);
    }

    public void sendToAll(String msg) throws ChannelNotConnectedException, ChannelClosedException {
        send(null, null, msg);
    }

    public void down(Event evt) {
        channel.down(evt);
    }

    public Object receive(long timeout) throws ChannelNotConnectedException, ChannelClosedException, TimeoutException {
        return channel.receive(timeout);
    }

    public Object peek(long timeout) throws ChannelNotConnectedException, ChannelClosedException, TimeoutException {
        return channel.peek(timeout);
    }

    public void blockOk() {
        channel.blockOk();
    }

    public boolean getState(Address target, long timeout) throws ChannelNotConnectedException, ChannelClosedException {
        return channel.getState(target, timeout);
    }

    public void returnState(byte[] state) {
        channel.returnState(state);
    }

    public void returnState(byte[] state, String state_id) {
        channel.returnState(state, state_id);
    }

    private void setOptions() {
        channel.setOpt(Channel.BLOCK, Boolean.valueOf(this.receive_blocks));
        channel.setOpt(Channel.LOCAL, Boolean.valueOf(this.receive_local_msgs));
        channel.setOpt(Channel.AUTO_RECONNECT, Boolean.valueOf(this.auto_reconnect));
        channel.setOpt(Channel.AUTO_GETSTATE, Boolean.valueOf(this.auto_getstate));
    }

}
