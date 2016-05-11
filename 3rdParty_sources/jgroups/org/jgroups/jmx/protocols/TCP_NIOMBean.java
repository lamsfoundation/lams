package org.jgroups.jmx.protocols;

/**
 * @author Scott Marlow
 *
 */
public interface TCP_NIOMBean extends TCPMBean {

   int getReaderThreads();
   int getWriterThreads();
   int getProcessorThreads();
   int getProcessorMinThreads();
   int getProcessorMaxThreads();
   int getProcessorQueueSize();
   long getProcessorKeepAliveTime();
}
