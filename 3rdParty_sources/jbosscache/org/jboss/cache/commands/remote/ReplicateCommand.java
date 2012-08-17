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
package org.jboss.cache.commands.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.VisitableCommand;
import org.jboss.cache.commands.read.GravitateDataCommand;
import org.jboss.cache.commands.write.PutForExternalReadCommand;
import org.jboss.cache.interceptors.InterceptorChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Command that implements cluster replication logic.  Essentially mimics the replicate() and replicateAll() methods
 * in 2.1.x, we may need to revisit the usefulness of such a command.
 * <p/>
 * This is not a {@link org.jboss.cache.commands.VisitableCommand} and hence
 * not passed up the {@link org.jboss.cache.interceptors.base.CommandInterceptor} chain.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2
 */
public class ReplicateCommand implements ReplicableCommand
{
   public static final int SINGLE_METHOD_ID = 13;
   public static final int MULTIPLE_METHOD_ID = 14;

   private InterceptorChain invoker;

   private static final Log log = LogFactory.getLog(ReplicateCommand.class);
   private static final boolean trace = log.isTraceEnabled();

   /**
    * optimisation - rather than constructing a new list each for scenarios where a single modification needs
    * to be replicated rather use this instance.
    */
   private ReplicableCommand singleModification;
   private List<ReplicableCommand> modifications;

   public ReplicateCommand(List<ReplicableCommand> modifications)
   {
      if (modifications != null && modifications.size() == 1)
      {
         singleModification = modifications.get(0);
      }
      else
      {
         this.modifications = modifications;
      }
   }

   public ReplicateCommand(ReplicableCommand command)
   {
      this.singleModification = command;
   }

   public ReplicateCommand()
   {
   }

   public void initialize(InterceptorChain interceptorChain)
   {
      this.invoker = interceptorChain;
   }

   public void setSingleModification(ReplicableCommand singleModification)
   {
      this.singleModification = singleModification;
   }

   public void setModifications(List<ReplicableCommand> modifications)
   {
      if (modifications != null && modifications.size() == 1)
         singleModification = modifications.get(0);
      else
         this.modifications = modifications;
   }

   /**
    * Executes commands replicated to the current cache instance by other cache instances.
    *
    * @param ctx invocation context, ignored.
    * @return if this is a single command being processed <b>and</b> it is a {@link org.jboss.cache.commands.read.GravitateDataCommand}, the result of processing this command is returned.  Otherwise, null is returned.
    * @throws Throwable
    */
   public Object perform(InvocationContext ctx) throws Throwable
   {
      if (isSingleCommand())
      {
         Object retVal = processSingleCommand(singleModification);

         // only send back the result of the execution if it is a data gravitation command.
         // all other commands don't need to send back return values, there will be an unnecessary overhead of
         // marshalling results that won't ever be used.
         if (singleModification instanceof GravitateDataCommand)
            return retVal;
         else
            return null;
      }
      for (ReplicableCommand command : modifications) processSingleCommand(command);
      return null;
   }

   private Object processSingleCommand(ReplicableCommand cacheCommand)
         throws Throwable
   {
      Object result;
      try
      {
         if (trace) log.trace("Invoking command " + cacheCommand + ", with originLocal flag set to false.");

         if (cacheCommand instanceof VisitableCommand)
         {
            Object retVal = invoker.invokeRemote((VisitableCommand) cacheCommand);
            // we only need to return values for a set of remote calls; not every call.
            if (returnValueForRemoteCall(cacheCommand))
            {
               result = retVal;
            }
            else
            {
               result = null;
            }
         }
         else
         {
            result = cacheCommand.perform(null);
         }
      }
      catch (Throwable ex)
      {
         if (!(cacheCommand instanceof PutForExternalReadCommand))
         {
            throw ex;
         }
         else
         {
            if (trace)
               log.trace("Caught an exception, but since this is a putForExternalRead() call, suppressing the exception.  Exception is:", ex);
            result = null;
         }
      }
      return result;
   }

   private boolean returnValueForRemoteCall(ReplicableCommand cacheCommand)
   {
      return cacheCommand instanceof GravitateDataCommand || cacheCommand instanceof ClusteredGetCommand;
   }

   public int getCommandId()
   {
      return isSingleCommand() ? SINGLE_METHOD_ID : MULTIPLE_METHOD_ID;
   }

   public List<ReplicableCommand> getModifications()
   {
      return modifications;
   }

   public ReplicableCommand getSingleModification()
   {
      return singleModification;
   }

   public Object[] getParameters()
   {
      if (isSingleCommand())
         return new Object[]{singleModification};
      else
         return new Object[]{modifications};
   }

   @SuppressWarnings("unchecked")
   public void setParameters(int commandId, Object[] args)
   {
      if (commandId == SINGLE_METHOD_ID)
      {
         singleModification = (ReplicableCommand) args[0];
      }
      else
      {
         modifications = (List<ReplicableCommand>) args[0];
      }
   }

   public boolean isSingleCommand()
   {
      return singleModification != null;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ReplicateCommand that = (ReplicateCommand) o;

      if (modifications != null ? !modifications.equals(that.modifications) : that.modifications != null) return false;
      if (singleModification != null ? !singleModification.equals(that.singleModification) : that.singleModification != null)
         return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (singleModification != null ? singleModification.hashCode() : 0);
      result = 31 * result + (modifications != null ? modifications.hashCode() : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "ReplicateCommand{" +
            "cmds=" + (isSingleCommand() ? singleModification : modifications) +
            '}';
   }

   /**
    * Creates a copy of this command, amking a deep copy of any collections but everything else copied shallow.
    *
    * @return a copy
    */
   public ReplicateCommand copy()
   {
      ReplicateCommand clone;
      clone = new ReplicateCommand();
      clone.invoker = invoker;
      clone.modifications = modifications == null ? null : new ArrayList<ReplicableCommand>(modifications);
      clone.singleModification = singleModification;
      return clone;
   }

   public boolean containsCommandType(Class<? extends ReplicableCommand> aClass)
   {
      if (isSingleCommand())
      {

         return isCommandWithType(getSingleModification(), aClass);
      }
      else
      {
         for (ReplicableCommand command : getModifications())
         {
            if (isCommandWithType(command, aClass)) return true;
         }
      }
      return false;
   }

   private boolean isCommandWithType(ReplicableCommand command, Class<? extends ReplicableCommand> aClass)
   {
      if (command.getClass().equals(aClass)) return true;
      if (command instanceof ReplicateCommand)
      {
         return ((ReplicateCommand) command).containsCommandType(aClass);
      }
      else
      {
         return false;
      }
   }

   public boolean removeCommands(List<Class<? extends ReplicableCommand>> whereFrom)
   {
      boolean foundMods = false;
      List<ReplicableCommand> modifications = isSingleCommand() ? Collections.singletonList(this.singleModification) : this.modifications;
      for (ReplicableCommand command : modifications)
      {
         if (command instanceof ReplicateCommand)
         {
            foundMods = foundMods | ((ReplicateCommand)command).removeCommands(whereFrom);
         }
         else 
         {
            foundMods = foundMods | whereFrom.remove(command.getClass());
         }
      }
      return foundMods;
   }
}