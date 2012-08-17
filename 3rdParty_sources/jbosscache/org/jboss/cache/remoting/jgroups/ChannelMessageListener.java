/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cache.remoting.jgroups;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.io.ExposedByteArrayOutputStream;
import org.jboss.cache.statetransfer.DefaultStateTransferManager;
import org.jboss.cache.statetransfer.StateTransferManager;
import org.jboss.util.stream.MarshalledValueInputStream;
import org.jboss.util.stream.MarshalledValueOutputStream;
import org.jgroups.ExtendedMessageListener;
import org.jgroups.Message;
import org.jgroups.util.Util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * JGroups MessageListener
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.0
 */
@NonVolatile
public class ChannelMessageListener implements ExtendedMessageListener
{
   /**
    * Reference to an exception that was raised during
    * state installation on this node.
    */
   protected volatile Exception setStateException;
   private final Object stateLock = new Object();
   private static final Log log = LogFactory.getLog(ChannelMessageListener.class);
   private static final boolean trace = log.isTraceEnabled();
   private StateTransferManager stateTransferManager;
   private Configuration configuration;
   /**
    * True if state was initialized during start-up.
    */
   private volatile boolean isStateSet = false;


   @Inject
   void injectDependencies(StateTransferManager stateTransferManager, Configuration configuration)
   {
      this.stateTransferManager = stateTransferManager;
      this.configuration = configuration;
   }

   public boolean isStateSet()
   {
      return isStateSet;
   }

   public void setStateSet(boolean stateSet)
   {
      isStateSet = stateSet;
   }

   public void waitForState() throws Exception
   {
      synchronized (stateLock)
      {
         while (!isStateSet)
         {
            if (setStateException != null)
            {
               throw setStateException;
            }

            try
            {
               stateLock.wait();
            }
            catch (InterruptedException iex)
            {
            }
         }
      }
   }

   protected void stateReceivedSuccess()
   {
      isStateSet = true;
      setStateException = null;
   }

   protected void stateReceivingFailed(Throwable t)
   {
      if (t instanceof CacheException)
      {
         log.debug("Caught exception integrating state!", t);
      }
      else
      {
         log.error("failed setting state", t);
      }
      if (t instanceof Exception)
      {
         setStateException = (Exception) t;
      }
      else
      {
         setStateException = new Exception(t);
      }
   }

   protected void stateProducingFailed(Throwable t)
   {
      if (t instanceof CacheException)
      {
         log.debug("Caught exception generating state!", t);
      }
      else
      {
         log.error("Caught " + t.getClass().getName()
               + " while responding to state transfer request", t);
      }
   }

   /**
    * Callback, does nothing.
    */
   public void receive(Message msg)
   {
   }

   public byte[] getState()
   {
      MarshalledValueOutputStream out = null;
      byte[] result;
      ExposedByteArrayOutputStream baos = new ExposedByteArrayOutputStream(16 * 1024);
      try
      {
         out = new MarshalledValueOutputStream(baos);

         stateTransferManager.getState(out, Fqn.ROOT, configuration.getStateRetrievalTimeout(), true, true);
      }
      catch (Throwable t)
      {
         stateProducingFailed(t);
      }
      finally
      {
         result = baos.getRawBuffer();
         Util.close(out);
      }
      return result;
   }

   public void setState(byte[] new_state)
   {
      if (new_state == null)
      {
         log.debug("transferred state is null (may be first member in cluster)");
         return;
      }
      if (trace) log.trace("setState() called with byte array of size " + new_state.length);
      ByteArrayInputStream bais = new ByteArrayInputStream(new_state);
      MarshalledValueInputStream in = null;
      try
      {
         in = new MarshalledValueInputStream(bais);
         stateTransferManager.setState(in, Fqn.ROOT);
         stateReceivedSuccess();
      }
      catch (Throwable t)
      {
         stateReceivingFailed(t);
      }
      finally
      {
         Util.close(in);
         synchronized (stateLock)
         {
            // Notify wait that state has been set.
            stateLock.notifyAll();
         }
      }
   }

   public byte[] getState(String state_id)
   {
      if (trace) log.trace("Getting state for state id " + state_id);
      MarshalledValueOutputStream out = null;
      String sourceRoot = state_id;
      byte[] result;

      boolean hasDifferentSourceAndIntegrationRoots = state_id.indexOf(DefaultStateTransferManager.PARTIAL_STATE_DELIMITER) > 0;
      if (hasDifferentSourceAndIntegrationRoots)
      {
         sourceRoot = state_id.split(DefaultStateTransferManager.PARTIAL_STATE_DELIMITER)[0];
      }

      ExposedByteArrayOutputStream baos = new ExposedByteArrayOutputStream(16 * 1024);
      try
      {
         out = new MarshalledValueOutputStream(baos);

         stateTransferManager.getState(out, Fqn.fromString(sourceRoot),
               configuration.getStateRetrievalTimeout(), true, true);
      }
      catch (Throwable t)
      {
         stateProducingFailed(t);
      }
      finally
      {
         result = baos.getRawBuffer();
         Util.close(out);
      }
      return result;
   }

   public void getState(OutputStream ostream)
   {
      MarshalledValueOutputStream out = null;
      try
      {
         out = new MarshalledValueOutputStream(ostream);
         stateTransferManager.getState(out, Fqn.ROOT, configuration.getStateRetrievalTimeout(), true, true);
      }
      catch (Throwable t)
      {
         stateProducingFailed(t);
      }
      finally
      {
         Util.close(out);
      }
   }

   public void getState(String state_id, OutputStream ostream)
   {
      if (trace) log.trace("Getting state for state id " + state_id);
      String sourceRoot = state_id;
      MarshalledValueOutputStream out = null;
      boolean hasDifferentSourceAndIntegrationRoots = state_id.indexOf(DefaultStateTransferManager.PARTIAL_STATE_DELIMITER) > 0;
      if (hasDifferentSourceAndIntegrationRoots)
      {
         sourceRoot = state_id.split(DefaultStateTransferManager.PARTIAL_STATE_DELIMITER)[0];
      }
      try
      {
         out = new MarshalledValueOutputStream(ostream);
         stateTransferManager.getState(out, Fqn.fromString(sourceRoot), configuration.getStateRetrievalTimeout(), true, true);
      }
      catch (Throwable t)
      {
         stateProducingFailed(t);
      }
      finally
      {
         Util.close(out);
      }
   }

   public void setState(InputStream istream)
   {
      if (istream == null)
      {
         log.debug("stream is null (may be first member in cluster)");
         return;
      }
      if (trace) log.trace("setState() called with input stream");
      MarshalledValueInputStream in = null;
      try
      {
         in = new MarshalledValueInputStream(istream);
         stateTransferManager.setState(in, Fqn.ROOT);
         stateReceivedSuccess();
      }
      catch (Throwable t)
      {
         stateReceivingFailed(t);
      }
      finally
      {
         Util.close(in);
         synchronized (stateLock)
         {
            // Notify wait that state has been set.
            stateLock.notifyAll();
         }
      }
   }

   public void setState(String state_id, byte[] state)
   {
      if (state == null)
      {
         log.debug("partial transferred state for id "+state_id+ " is null");
         return;
      }
      if (trace) log.trace("Receiving state byte array of length " +state.length+" for " + state_id);

      MarshalledValueInputStream in = null;
      String targetRoot = state_id;
      boolean hasDifferentSourceAndIntegrationRoots = state_id.indexOf(DefaultStateTransferManager.PARTIAL_STATE_DELIMITER) > 0;
      if (hasDifferentSourceAndIntegrationRoots)
      {
         targetRoot = state_id.split(DefaultStateTransferManager.PARTIAL_STATE_DELIMITER)[1];
      }
      try
      {
         log.debug("Setting received partial state for subroot " + state_id);
         Fqn subroot = Fqn.fromString(targetRoot);
//            Region region = regionManager.getRegion(subroot, false);
//            ClassLoader cl = null;
//            if (region != null)
//            {
//               // If a classloader is registered for the node's region, use it
//               cl = region.getClassLoader();
//            }
         ByteArrayInputStream bais = new ByteArrayInputStream(state);
         in = new MarshalledValueInputStream(bais);
         //getStateTransferManager().setState(in, subroot, cl);
         stateTransferManager.setState(in, subroot);
         stateReceivedSuccess();
      }
      catch (Throwable t)
      {
         stateReceivingFailed(t);
      }
      finally
      {
         Util.close(in);
         synchronized (stateLock)
         {
            // Notify wait that state has been set.
            stateLock.notifyAll();
         }
      }
   }

   public void setState(String stateId, InputStream istream)
   {
      if (trace) log.trace("Receiving state on stream for " + stateId);
      String targetRoot = stateId;
      MarshalledValueInputStream in = null;
      boolean hasDifferentSourceAndIntegrationRoots = stateId.indexOf(DefaultStateTransferManager.PARTIAL_STATE_DELIMITER) > 0;
      if (hasDifferentSourceAndIntegrationRoots)
      {
         targetRoot = stateId.split(DefaultStateTransferManager.PARTIAL_STATE_DELIMITER)[1];
      }
      if (istream == null)
      {
         log.debug("stream is null (may be first member in cluster). State is not set");
         return;
      }

      try
      {
         log.debug("Setting received partial state for subroot " + stateId);
         in = new MarshalledValueInputStream(istream);
         Fqn subroot = Fqn.fromString(targetRoot);
//            Region region = regionManager.getRegion(subroot, false);
//            ClassLoader cl = null;
//            if (region != null)
//            {
//               // If a classloader is registered for the node's region, use it
//               cl = region.getClassLoader();
//            }
         //getStateTransferManager().setState(in, subroot, cl);
         stateTransferManager.setState(in, subroot);
         stateReceivedSuccess();
      }
      catch (Throwable t)
      {
         if (log.isTraceEnabled()) log.trace("Unknown error while integrating state", t);
         stateReceivingFailed(t);
      }
      finally
      {
         Util.close(in);
         synchronized (stateLock)
         {
            // Notify wait that state has been set.
            stateLock.notifyAll();
         }
      }
   }
}
