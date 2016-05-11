package org.jgroups.jmx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.stack.ProtocolStack;
import org.jgroups.util.Util;

import javax.management.*;
import java.util.Vector;
import java.util.Set;
import java.util.Iterator;

/**
 * @author Bela Ban
 *
 */
public class JmxConfigurator {
    static final Log log=LogFactory.getLog(JmxConfigurator.class);

    /**
     * Registers an already created channel with the MBeanServer. Creates an org.jgroups.jmx.JChannel which
     * delegates to the org.jgroups.JChannel and registers it. Optionally, this method will also try to
     * create one MBean proxy for each protocol in the channel's protocol stack, and register it as well.
     * @param channel
     * @param server
     * @param domain Has to be a JMX ObjectName of the domain, e.g. DefaultDomain:name=JGroups
     * @param register_protocols
     * @return org.jgroups.jmx.JChannel for the specified org.jgroups.JChannel
     */
    public static org.jgroups.jmx.JChannel registerChannel(org.jgroups.JChannel channel,
                                                           MBeanServer server, String domain, String cluster_name,
                                                           boolean register_protocols) throws Exception {
        if(cluster_name == null)
            cluster_name=channel != null? channel.getClusterName() : null;
        if(cluster_name == null)
            cluster_name="null";
        if(register_protocols) {
            String tmp=domain + ":type=protocol,cluster=" +cluster_name;
            registerProtocols(server, channel, tmp);
        }
        return registerChannel(channel, server, domain + ":type=channel,cluster=" +cluster_name);
    }

    /**
     * Registers an already created channel with the MBeanServer. Creates an org.jgroups.jmx.JChannel which
     * delegates to the org.jgroups.JChannel and registers it.
     * @param channel
     * @param server
     * @param name The JMX ObjectName 
     * @return org.jgroups.jmx.JChannel for the specified org.jgroups.JChannel
     */
    public static org.jgroups.jmx.JChannel registerChannel(org.jgroups.JChannel channel,
                                                           MBeanServer server, String name) throws Exception {
        JChannel retval=new JChannel(channel);
        server.registerMBean(retval, new ObjectName(name));
        return retval;
    }
    
    
    
    public static void unregisterChannel(MBeanServer server, ObjectName name) throws Exception {
        if(server != null)
            server.unregisterMBean(name);
    }

    public static void unregisterChannel(MBeanServer server, String name) throws Exception {
        if(server != null)
            server.unregisterMBean(new ObjectName(name));
    }


    public static org.jgroups.jmx.JChannelFactory registerChannelFactory(org.jgroups.JChannelFactory factory,
                                                                         MBeanServer server, String name) throws Exception {
        JChannelFactory retval=new JChannelFactory(factory);
        server.registerMBean(retval, new ObjectName(name));
        return retval;
    }

    


    /**
     * Takes all protocols of an existing stack, creates corresponding MBean proxies and registers them with
     * the MBean server
     * @param channel
     * @param prefix
     */
    public static void registerProtocols(MBeanServer server, org.jgroups.JChannel channel, String prefix) throws Exception {
        ProtocolStack stack=channel.getProtocolStack();
        Vector protocols=stack.getProtocols();
        org.jgroups.stack.Protocol prot;
        org.jgroups.jmx.Protocol p=null;
        for(int i=0; i < protocols.size(); i++) {
            prot=(org.jgroups.stack.Protocol)protocols.get(i);
            try {
                p=findProtocol(prot);
            }
            catch(ClassNotFoundException e) {
                p=null;
            }
            catch(Throwable e) {
                log.error("failed creating a JMX wrapper instance for " + prot, e);
                p=null;
            }
            if(p == null)
                p=new org.jgroups.jmx.Protocol(prot);
            ObjectName prot_name=new ObjectName(prefix + ",protocol=" + prot.getName());
            server.registerMBean(p, prot_name);
        }
    }

    public static void unregisterProtocols(MBeanServer server, org.jgroups.JChannel channel, String channel_name) {
        ProtocolStack stack=channel.getProtocolStack();
        Vector protocols=stack.getProtocols();
        org.jgroups.stack.Protocol prot;
        ObjectName prot_name=null;
        for(int i=0; i < protocols.size(); i++) {
            prot=(org.jgroups.stack.Protocol)protocols.get(i);
            try {
                prot_name=new ObjectName(channel_name + ",protocol=" + prot.getName());
                server.unregisterMBean(prot_name);
            }
            catch(Throwable e) {
                log.error("failed to unregister " + prot_name, e);
            }
        }
    }

    /**
     * Unregisters object_name and everything under it
     * @param object_name
     */
    public static void unregister(MBeanServer server, String object_name) throws Exception {
        Set mbeans=server.queryNames(new ObjectName(object_name), null);
        if(mbeans != null) {
            ObjectName name;
            for(Iterator it=mbeans.iterator(); it.hasNext();) {
                name=(ObjectName)it.next();
                server.unregisterMBean(name);
            }
        }
    }


    protected static Protocol findProtocol(org.jgroups.stack.Protocol prot) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Protocol p;
        String prot_name=prot.getClass().getName();
        String clname=prot_name.replaceFirst("org.jgroups.", "org.jgroups.jmx.");
        Class cl=Util.loadClass(clname, JmxConfigurator.class);
        if(cl != null) {
            p=(Protocol)cl.newInstance();
            p.attachProtocol(prot);
            return p;
        }
        return null;
    }
}
