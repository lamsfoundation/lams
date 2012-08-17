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
package org.jboss.cache.marshall;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.RPCManager;
import org.jboss.cache.RPCManagerImpl.FlushTracker;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.remote.AnnounceBuddyPoolNameCommand;
import org.jboss.cache.commands.remote.AssignToBuddyGroupCommand;
import org.jboss.cache.commands.remote.RemoveFromBuddyGroupCommand;
import org.jboss.cache.commands.remote.StateTransferControlCommand;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.factories.ComponentRegistry;
import org.jboss.cache.interceptors.InterceptorChain;
import org.jboss.cache.invocation.InvocationContextContainer;
import org.jboss.cache.util.concurrent.BoundedExecutors;
import org.jboss.cache.util.concurrent.WithinThreadExecutor;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.MembershipListener;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.blocks.GroupRequest;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.blocks.RspFilter;
import org.jgroups.util.Buffer;
import org.jgroups.util.Rsp;
import org.jgroups.util.RspList;

import java.io.NotSerializableException;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A JGroups RPC dispatcher that knows how to deal with {@link org.jboss.cache.commands.ReplicableCommand}s.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
public class CommandAwareRpcDispatcher extends RpcDispatcher
{
   protected InvocationContextContainer invocationContextContainer;
   protected InterceptorChain interceptorChain;
   protected ComponentRegistry componentRegistry;
   protected boolean trace;
   private ExecutorService replicationProcessor;
   private AtomicInteger replicationProcessorCount;
   private boolean asyncSerial;
   private Configuration configuration;
   private RPCManager rpcManager;
   private ReplicationObserver replicationObserver;

   public CommandAwareRpcDispatcher() {}

   public CommandAwareRpcDispatcher(Channel channel, MessageListener l, MembershipListener l2,
                                    Object serverObj, InvocationContextContainer container, InterceptorChain interceptorChain,
                                    ComponentRegistry componentRegistry, RPCManager manager)
   {
      super(channel, l, l2, serverObj);
      this.invocationContextContainer = container;
      this.componentRegistry = componentRegistry;
      this.interceptorChain = interceptorChain;
      this.rpcManager = manager;

      trace = log.isTraceEnabled();

      // what sort of a repl processor do we need?
      Configuration c = componentRegistry.getComponent(Configuration.class);
      this.configuration = c;
      replicationProcessor = c.getRuntimeConfig().getAsyncSerializationExecutor();
      if (c.getCacheMode().isSynchronous() ||
            (replicationProcessor == null && c.getSerializationExecutorPoolSize() < 1) || requireSyncMarshalling(c)) // if an executor has not been injected and the pool size is set
      {
         // in-process thread.  Not async.
         replicationProcessor = new WithinThreadExecutor();
         asyncSerial = false;
      }
      else
      {
         asyncSerial = true;
         if (replicationProcessor == null)
         {
            replicationProcessorCount = new AtomicInteger(0);

            replicationProcessor = BoundedExecutors.newFixedThreadPool(c.isUseReplQueue() ? 1 : c.getSerializationExecutorPoolSize(),
                  new ThreadFactory()
                  {
                     public Thread newThread(Runnable r)
                     {
                        return new Thread(r, "AsyncReplicationProcessor-" + replicationProcessorCount.incrementAndGet());
                     }
                  }, c.getSerializationExecutorQueueSize()
            );
         }
      }
   }

   public ReplicationObserver setReplicationObserver(ReplicationObserver replicationObserver)
   {
      ReplicationObserver result = this.replicationObserver;
      this.replicationObserver = replicationObserver;
      return result;
   }

   /**
    * Serial(sync) marshalling should be enabled for async optimistic caches. That is because optimistic async is a 2PC,
    * which might cause the Commit command to be send before the Prepare command, so replication will fail. This is not
    * the same for async <b>pessimistic/mvcc</b> replication, as this uses a 1PC.
    */
   private boolean requireSyncMarshalling(Configuration c)
   {
      boolean enforceSerialMarshalling =
            c.getNodeLockingScheme().equals(Configuration.NodeLockingScheme.OPTIMISTIC) && !c.getCacheMode().isInvalidation();

      if (enforceSerialMarshalling)
      {
         if (c.getSerializationExecutorPoolSize() > 1 && log.isWarnEnabled())
         {
            log.warn("Async optimistic caches do not support serialization pools.");
         }
         if (trace) log.trace("Disbaling serial marshalling for opt async cache");
      }
      return enforceSerialMarshalling;
   }

   @Override
   public void stop()
   {
      replicationProcessor.shutdownNow();
      try
      {
         replicationProcessor.awaitTermination(60, TimeUnit.SECONDS);
      }
      catch (InterruptedException e)
      {
         Thread.currentThread().interrupt();
      }
      super.stop();
   }

   protected boolean isValid(Message req)
   {
      if (server_obj == null)
      {
         log.error("no method handler is registered. Discarding request.");
         return false;
      }

      if (req == null || req.getLength() == 0)
      {
         log.error("message or message buffer is null");
         return false;
      }

      return true;
   }

   /**
    * Similar to {@link #callRemoteMethods(java.util.Vector, org.jgroups.blocks.MethodCall, int, long, boolean, boolean, org.jgroups.blocks.RspFilter)} except that this version
    * is aware of {@link org.jboss.cache.commands.ReplicableCommand} objects.
    */
   public RspList invokeRemoteCommands(Vector<Address> dests, ReplicableCommand command, int mode, long timeout,
                                       boolean anycasting, boolean oob, RspFilter filter) throws NotSerializableException, ExecutionException, InterruptedException
   {
      if (dests != null && dests.isEmpty())
      {
         // don't send if dest list is empty
         if (trace) log.trace("Destination list is empty: no need to send message");
         return new RspList();
      }

      if (trace)
         log.trace(new StringBuilder("dests=").append(dests).append(", command=").append(command).
               append(", mode=").append(mode).append(", timeout=").append(timeout));

      boolean supportReplay = configuration.isNonBlockingStateTransfer();
      ReplicationTask replicationTask = new ReplicationTask(command, oob, dests, mode, timeout, anycasting, supportReplay, filter);
      Future<RspList> response = replicationProcessor.submit(replicationTask);
      if (asyncSerial)
      {
         // don't care about the response.  return.
         return null;
      }
      else
      {
         RspList retval = response.get();
         if (retval.isEmpty() || containsOnlyNulls(retval))
            return null;
         else
            return retval;
      }
   }

   private boolean containsOnlyNulls(RspList l)
   {
      for (Rsp r : l.values())
      {
         if (r.getValue() != null || !r.wasReceived() || r.wasSuspected()) return false;
      }
      return true;
   }

   /**
    * Message contains a Command. Execute it against *this* object and return result.
    */
   @Override
   public Object handle(Message req)
   {
      if (isValid(req))
      {
         try
         {
            ReplicableCommand command = (ReplicableCommand) req_marshaller.objectFromByteBuffer(req.getBuffer(), req.getOffset(), req.getLength());
            Object execResult = executeCommand(command, req);
            if (log.isTraceEnabled()) log.trace("Command : " + command + " executed, result is: " + execResult);
            return execResult;
         }
         catch (Throwable x)
         {
            if (trace) log.trace("Problems invoking command.", x);
            return x;
         }
      }
      else
      {
         return null;
      }
   }

   protected Object executeCommand(ReplicableCommand cmd, Message req) throws Throwable
   {
      boolean unlock = false;
      FlushTracker flushTracker = rpcManager.getFlushTracker();

      try
      {
         if (cmd == null) throw new NullPointerException("Unable to execute a null command!  Message was " + req);
         if (trace) log.trace("Executing command: " + cmd + " [sender=" + req.getSrc() + "]");

         boolean replayIgnored = false;


         if (configuration.isNonBlockingStateTransfer() && !(cmd instanceof StateTransferControlCommand))
         {
            int flushCount  = flushTracker.getFlushCompletionCount();
            flushTracker.lockProcessingLock();
            unlock = true;

            flushTracker.waitForFlushCompletion(configuration.getStateRetrievalTimeout());

            // If this thread blocked during a NBST flush, then inform the sender
            // it needs to replay ignored messages
            replayIgnored = flushTracker.getFlushCompletionCount() != flushCount;
         }

         Object ret;

         if (cmd instanceof VisitableCommand)
         {
            InvocationContext ctx = invocationContextContainer.get();
            ctx.setOriginLocal(false);
            if (!componentRegistry.invocationsAllowed(false))
            {
               return new RequestIgnoredResponse();
            }
            ret = interceptorChain.invoke(ctx, (VisitableCommand) cmd);
         }
         else
         {
            if (trace) log.trace("This is a non-visitable command - so performing directly and not via the invoker.");

            // need to check cache status for all except buddy replication commands.
            if (requiresRunningCache(cmd) && !componentRegistry.invocationsAllowed(false))
            {
               return new RequestIgnoredResponse();
            }
            ret = cmd.perform(null);
         }

         if (replayIgnored)
         {
            ExtendedResponse extended = new ExtendedResponse(ret);
            extended.setReplayIgnoredRequests(true);
            ret = extended;
         }

         return ret;
      }
      finally
      {
         if (replicationObserver != null)
            replicationObserver.afterExecutingCommand(cmd);

         if (unlock)
            flushTracker.unlockProcessingLock();
      }
   }

   private boolean requiresRunningCache(ReplicableCommand cmd)
   {
      return !(cmd instanceof AnnounceBuddyPoolNameCommand ||
                  cmd instanceof AssignToBuddyGroupCommand ||
                  cmd instanceof RemoveFromBuddyGroupCommand ||
                  cmd instanceof StateTransferControlCommand);
   }

   @Override
   public String toString()
   {
      return getClass().getSimpleName() + "[Outgoing marshaller: " + req_marshaller + "; incoming marshaller: " + rsp_marshaller + "]";
   }

   private class ReplicationTask implements Callable<RspList>
   {
      private ReplicableCommand command;
      private boolean oob;
      private Vector<Address> dests;
      private int mode;
      private long timeout;
      private boolean anycasting;
      private boolean supportReplay;
      private RspFilter filter;

      private ReplicationTask(ReplicableCommand command, boolean oob, Vector<Address> dests, int mode, long timeout, boolean anycasting, boolean supportReplay, RspFilter filter)
      {
         this.command = command;
         this.oob = oob;
         this.dests = dests;
         this.mode = mode;
         this.timeout = timeout;
         this.anycasting = anycasting;
         this.supportReplay = supportReplay;
         this.filter = filter;
      }

      public RspList call() throws Exception
      {
         Buffer buf;
         try
         {
            buf = req_marshaller.objectToBuffer(command);
         }
         catch (Exception e)
         {
            if (log.isErrorEnabled()) log.error(e);
            throw new RuntimeException("Failure to marshal argument(s)", e);
         }

         Message msg = new Message();
         msg.setBuffer(buf);
         if (oob) msg.setFlag(Message.OOB);

         // Replay capability requires responses from all members!
         int mode = supportReplay ? GroupRequest.GET_ALL : this.mode;
         RspList retval = castMessage(dests, msg, mode, timeout, anycasting, filter);
         if (trace) log.trace("responses: " + retval);

         // a null response is 99% likely to be due to a marshalling problem - we throw a NSE, this needs to be changed when
         // JGroups supports http://jira.jboss.com/jira/browse/JGRP-193
         // the serialization problem could be on the remote end and this is why we cannot catch this above, when marshalling.

         if (retval == null)
            throw new NotSerializableException("RpcDispatcher returned a null.  This is most often caused by args for " + command.getClass().getSimpleName() + " not being serializable.");

         if (supportReplay)
         {
            boolean replay = false;
            Vector<Address> ignorers = new Vector<Address>();
            for (Map.Entry<Address, Rsp> entry : retval.entrySet())
            {
               Object value = entry.getValue().getValue();
               if (value instanceof RequestIgnoredResponse)
               {
                  ignorers.add(entry.getKey());
               }
               else if (value instanceof ExtendedResponse)
               {
                  ExtendedResponse extended = (ExtendedResponse) value;
                  replay |= extended.isReplayIgnoredRequests();
                  entry.getValue().setValue(extended.getResponse());
               }
            }

            if (replay && ignorers.size() > 0)
            {
               if (trace)
                  log.trace("Replaying message to ignoring senders: " + ignorers);
               RspList responses = castMessage(ignorers, msg, GroupRequest.GET_ALL, timeout, anycasting, filter);
               if (responses != null)
                  retval.putAll(responses);
            }
         }

         return retval;
      }
   }
}
