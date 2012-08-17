/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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
package org.jboss.cache.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.commands.WriteCommand;
import org.jboss.cache.commands.tx.PrepareCommand;
import org.jboss.cache.marshall.Marshaller;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Logs transactions and writes for Non-Blocking State Transfer
 *
 * @author Jason T. Greene
 */
public class TransactionLog
{
   private final Map<GlobalTransaction, PrepareCommand> pendingPrepares = new ConcurrentHashMap<GlobalTransaction, PrepareCommand>();
   private final BlockingQueue<LogEntry> entries = new LinkedBlockingQueue<LogEntry>();
   private AtomicBoolean active = new AtomicBoolean();

   public static class LogEntry
   {
      private final GlobalTransaction transaction;
      private final List<WriteCommand> modifications;

      public LogEntry(GlobalTransaction transaction, List<WriteCommand> modifications)
      {
         this.transaction = transaction;
         this.modifications = modifications;
      }

      public GlobalTransaction getTransaction()
      {
         return transaction;
      }

      public List<WriteCommand> getModifications()
      {
         return modifications;
      }
   }

   private static Log log = LogFactory.getLog(TransactionLog.class);

   public void logPrepare(PrepareCommand command)
   {
      pendingPrepares.put(command.getGlobalTransaction(), command);
   }

   public void logCommit(GlobalTransaction gtx)
   {
      PrepareCommand command = pendingPrepares.remove(gtx);
      // it is perfectly normal for a prepare not to be logged for this gtx, for example if a transaction did not
      // modify anything, then beforeCompletion() is not invoked and logPrepare() will not be called to register the
      // prepare.
      if (command != null && isActive()) addEntry(gtx, command.getModifications());
   }

   private void addEntry(GlobalTransaction gtx, WriteCommand command)
   {      
      addEntry(gtx, Collections.singletonList(command));
   }

   private void addEntry(GlobalTransaction gtx, List<WriteCommand> commands)
   {
      LogEntry entry = new LogEntry(gtx, commands);

      for (;;)
      {
         try
         {
            if (log.isTraceEnabled())
               log.trace("Added commit entry to tx log" + entry);

            entries.put(entry);
            break;
         }
         catch (InterruptedException e)
         {
            Thread.currentThread().interrupt();
         }
      }
   }

   public void logOnePhaseCommit(GlobalTransaction gtx, List<WriteCommand> modifications)
   {
      // Just in case...
      if (gtx != null) pendingPrepares.remove(gtx);
      if (isActive() && !modifications.isEmpty()) addEntry(gtx, modifications);
   }

   public void logNoTxWrite(WriteCommand write)
   {
      if (isActive()) addEntry(null, write);
   }

   public void rollback(GlobalTransaction gtx)
   {
      pendingPrepares.remove(gtx);
   }

   public final boolean isActive()
   {
      return active.get();
   }

   public boolean activate()
   {
      return active.compareAndSet(false, true);
   }

   public void deactivate()
   {
      active.set(false);
      if (entries.size() > 0)
         log.error("Unprocessed Transaction Log Entries! = " + entries.size());
      entries.clear();
   }

   public int size()
   {
      return entries.size();
   }

   public void writeCommitLog(Marshaller marshaller, ObjectOutputStream out) throws Exception
   {
     List<LogEntry> buffer = new ArrayList<LogEntry>(10);

     while (entries.drainTo(buffer, 10) > 0)
     {
        for (LogEntry entry : buffer)
           marshaller.objectToObjectStream(entry, out);

        buffer.clear();
     }
   }

   public void writePendingPrepares(Marshaller marshaller, ObjectOutputStream out) throws Exception
   {
      for (PrepareCommand entry : pendingPrepares.values())
         marshaller.objectToObjectStream(entry, out);
   }

   public final boolean hasPendingPrepare(PrepareCommand command)
   {
      return pendingPrepares.containsKey(command.getGlobalTransaction());
   }
}
