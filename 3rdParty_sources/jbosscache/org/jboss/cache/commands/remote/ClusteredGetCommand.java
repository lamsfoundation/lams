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
import org.jboss.cache.DataContainer;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.commands.DataCommand;
import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.read.ExistsCommand;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.jboss.cache.commands.read.GetDataMapCommand;
import org.jboss.cache.interceptors.InterceptorChain;
import org.jboss.cache.loader.CacheLoaderManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Issues a clustered get call, for use primarily by the {@link org.jboss.cache.loader.ClusteredCacheLoader}.  This is
 * not a {@link org.jboss.cache.commands.VisitableCommand} and hence
 * not passed up the {@link org.jboss.cache.interceptors.base.CommandInterceptor} chain.
 * <p/>
 *
 * @author Mircea.Markus@jboss.com
 * @since 2.2.0
 */
public class ClusteredGetCommand implements ReplicableCommand
{
   public static final int METHOD_ID = 22;

   private DataCommand dataCommand;
   private boolean searchBackupSubtrees;
   private DataContainer dataContainer;
   private InterceptorChain interceptorChain;
   private CacheLoaderManager clm;

   private static final Log log = LogFactory.getLog(ClusteredGetCommand.class);
   private static final boolean trace = log.isTraceEnabled();

   public ClusteredGetCommand(boolean searchBackupSubtrees, DataCommand dataCommand)
   {
      this.searchBackupSubtrees = searchBackupSubtrees;
      this.dataCommand = dataCommand;
   }

   public ClusteredGetCommand()
   {
   }

   public void initialize(DataContainer dataContainer, InterceptorChain interceptorChain)
   {
      this.dataContainer = dataContainer;
      this.interceptorChain = interceptorChain;
   }

   /**
    * Invokes a {@link org.jboss.cache.commands.DataCommand} on a remote cache and returns results.
    *
    * @param context invocation context, ignored.
    * @return a List containing 2 elements: a boolean, (true or false) and a value (Object) which is the result of invoking a remote get specified by {@link #getDataCommand()}.  If buddy replication is used one further element is added - an Fqn of the backup subtree in which this node may be found.
    */
   public Object perform(InvocationContext context) throws Throwable
   {
      if (trace)
         log.trace("Clustered Get called with params: " + dataCommand + ", " + searchBackupSubtrees);

      Object callResults = null;
      try
      {
         if (trace) log.trace("Clustered get: invoking call with Fqn " + dataCommand.getFqn());
         InvocationContext ctx = interceptorChain.getInvocationContext();
         ctx.setOriginLocal(false);
         ctx.setBypassUnmarshalling(true);
         callResults = interceptorChain.invoke(ctx, dataCommand);
         Set mapCallRes;
         if (dataCommand instanceof GetChildrenNamesCommand && (mapCallRes = (Set) callResults) != null && mapCallRes.isEmpty())
            callResults = null;
         boolean found = validResult(callResults);
         if (trace) log.trace("Got result " + callResults + ", found=" + found);
         if (found && callResults == null) callResults = createEmptyResults();
      }
      catch (Exception e)
      {
         log.warn("Problems processing clusteredGet call", e);
      }

      List<Object> results = new ArrayList<Object>(2);
      if (callResults != null)
      {
         results.add(true);
         results.add(callResults);
      }
      else
      {
         results.add(false);
         results.add(null);
      }
      return results;
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   /**
    * Returns true if the call results returned a valid result.
    */
   private boolean validResult(Object callResults)
   {
      if (dataCommand instanceof GetDataMapCommand || dataCommand instanceof GetChildrenNamesCommand)
      {
         return callResults != null && dataContainer.exists(dataCommand.getFqn());
      }
      return dataCommand instanceof ExistsCommand && (Boolean) callResults;
   }

   /**
    * Creates an empty Collection class based on the return type of the method called.
    */
   private Object createEmptyResults()
   {
      if (dataCommand instanceof GetDataMapCommand || dataCommand instanceof GetChildrenNamesCommand)
      {
         return Collections.emptyMap();
      }
      return null;
   }

   public Boolean getSearchBackupSubtrees()
   {
      return searchBackupSubtrees;
   }

   public DataCommand getDataCommand()
   {
      return dataCommand;
   }

   public Object[] getParameters()
   {
      return new Object[]{dataCommand, searchBackupSubtrees};  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setParameters(int commandId, Object[] args)
   {
      dataCommand = (DataCommand) args[0];
      searchBackupSubtrees = (Boolean) args[1];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ClusteredGetCommand that = (ClusteredGetCommand) o;

      if (dataCommand != null ? !dataCommand.equals(that.dataCommand) : that.dataCommand != null)
         return false;
      return searchBackupSubtrees == that.searchBackupSubtrees;
   }

   @Override
   public int hashCode()
   {
      int result;
      result = (dataCommand != null ? dataCommand.hashCode() : 0);
      result = 31 * result + (searchBackupSubtrees ? 1 : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "ClusteredGetCommand{" +
            "dataCommand=" + dataCommand +
            ", searchBackupSubtrees=" + searchBackupSubtrees +
            '}';
   }
}
