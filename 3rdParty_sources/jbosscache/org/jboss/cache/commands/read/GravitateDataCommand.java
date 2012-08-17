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
package org.jboss.cache.commands.read;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;
import org.jboss.cache.Node;
import org.jboss.cache.NodeSPI;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.buddyreplication.BuddyManager;
import org.jboss.cache.buddyreplication.GravitateResult;
import org.jboss.cache.commands.Visitor;
import org.jboss.cache.marshall.NodeData;
import org.jgroups.Address;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Used with buddy replication's {@link org.jboss.cache.interceptors.DataGravitatorInterceptor}.
 * <p/>
 * This is the equivalent of the old MethodCallDefinitions.dataGravitationMethod method call from 2.1.x.
 * <p/>
 *
 * @author Manik Surtani
 * @since 2.2.0
 */
public class GravitateDataCommand extends AbstractDataCommand
{
   public static final int METHOD_ID = 35;

   /* dependencies */
   private CacheSPI spi;

   /* parametres */
   protected boolean searchSubtrees;
   private Address localAddress;

   private static final Log log = LogFactory.getLog(GravitateDataCommand.class);
   private static final boolean trace = log.isTraceEnabled();
   private BuddyFqnTransformer buddyFqnTransformer;

   public GravitateDataCommand(Fqn fqn, boolean searchSubtrees, Address localAddress)
   {
      this.fqn = fqn;
      this.searchSubtrees = searchSubtrees;
      this.localAddress = localAddress;
   }

   public GravitateDataCommand(Address localAddress)
   {
      this.localAddress = localAddress;
   }

   public void initialize(DataContainer dataContainer, CacheSPI spi, BuddyFqnTransformer transformer)
   {
      this.dataContainer = dataContainer;
      this.spi = spi;
      buddyFqnTransformer = transformer;
   }

   /**
    * Searches for data to gravitate given an Fqn and whether buddy backup subtrees are to be searched as well.  Note that
    * data stored under the Fqn, along with all children, are retrieved.
    *
    * @param ctx invocation context
    * @return a {@link org.jboss.cache.buddyreplication.GravitateResult} containing node data, as well as information on whether this was found in a primary or backup tree.
    */
   @SuppressWarnings("unchecked")
   public Object perform(InvocationContext ctx)
   {
      // TODO: Test this with MVCC.

      // for now, perform a very simple series of getData calls.
      if (trace) log.trace("Caller is asking for " + fqn);
      try
      {
         ctx.setOriginLocal(false);
         // use a get() call into the cache to make sure cache loading takes place.
         // no need to cache the original skipDataGravitation setting here - it will always be false of we got here!!
         //todo 2.2  use dataContainer for peek and load the data in the CLInterceptor rather than using the SPI for than!!!
         ctx.getOptionOverrides().setSkipDataGravitation(true);
         Node actualNode = spi.getNode(fqn);
         ctx.getOptionOverrides().setSkipDataGravitation(false);

         if (trace) log.trace("In local tree, this is " + actualNode);

         Fqn backupNodeFqn = null;
         if (actualNode == null && searchSubtrees)
         {
            log.trace("Looking at backup trees.");

            // need to loop through backupSubtree's children
            Set allGroupNames = getBackupRoots();
            if (allGroupNames != null)
            {
               for (Object groupName : allGroupNames)
               {
                  // groupName is the name of a buddy group since all child names in this
                  // collection are direct children of BUDDY_BACKUP_SUBTREE_FQN
                  Fqn backupRoot = Fqn.fromRelativeElements(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN, (String) groupName);
                  if (buddyFqnTransformer.isDeadBackupRoot(backupRoot))
                  {
                     Set<Integer> deadChildNames = new TreeSet<Integer>(spi.getChildrenNames(backupRoot));
                     Integer[] elems = deadChildNames.toArray(new Integer[deadChildNames.size()]);

                     // these are integers.  we need to start with the highest/most recent.
                     for (int i = elems.length - 1; i > -1; i--)
                     {
                        Integer versionOfDefunctData = elems[i];
                        backupNodeFqn = Fqn.fromRelativeFqn(Fqn.fromRelativeElements(backupRoot, versionOfDefunctData), fqn);

                        // use a get() call into the cache to make sure cache loading takes place.
                        ctx.getOptionOverrides().setSkipDataGravitation(true);
                        actualNode = spi.peek(backupNodeFqn, false);
                        ctx.getOptionOverrides().setSkipDataGravitation(false);

                        // break out of the inner loop searching through the dead node's various backups
                        if (actualNode != null) break;
                     }
                  }
                  else
                  {
                     backupNodeFqn = Fqn.fromRelativeFqn(backupRoot, fqn);
                     // use a get() call into the cache to make sure cache loading takes place.
                     ctx.getOptionOverrides().setSkipDataGravitation(true);
                     actualNode = spi.getNode(backupNodeFqn);
                     ctx.getOptionOverrides().setSkipDataGravitation(false);
                  }

                  if (trace)
                     log.trace("Looking for " + backupNodeFqn + ". Search result: " + actualNode);

                  // break out of outer loop searching through all available backups.
                  if (actualNode != null) break;
               }
            }

         }

         if (actualNode == null)
         {
            return GravitateResult.noDataFound();
         }
         else
         {
            // make sure we LOAD data for this node!!
            actualNode.getData();
            // and children!
            actualNode.getChildrenNames();
         }

         if (backupNodeFqn == null && searchSubtrees)
         {
            backupNodeFqn = buddyFqnTransformer.getBackupFqn(buddyFqnTransformer.getGroupNameFromAddress(localAddress), fqn);
         }

         List<NodeData> list = dataContainer.buildNodeData(new LinkedList<NodeData>(), (NodeSPI) actualNode, false);

         return GravitateResult.subtreeResult(list, backupNodeFqn);
      }
      catch (RuntimeException re)
      {
         if (trace) log.trace("Caught throwable", re);
         throw re;
      }
      finally
      {
         ctx.setOriginLocal(true);
      }
   }

   /**
    * @return a Set of child node names that hang directly off the backup tree root, or null if the backup tree root doesn't exist.
    */
   protected Set<Object> getBackupRoots()
   {
      InternalNode backupSubtree = dataContainer.peekInternalNode(BuddyManager.BUDDY_BACKUP_SUBTREE_FQN, false);
      if (backupSubtree == null) return null;
      return backupSubtree.getChildrenNames();
   }

   public Object acceptVisitor(InvocationContext ctx, Visitor visitor) throws Throwable
   {
      return visitor.visitGravitateDataCommand(ctx, this);
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   public boolean isSearchSubtrees()
   {
      return searchSubtrees;
   }

   @Override
   public Object[] getParameters()
   {
      return new Object[]{fqn, searchSubtrees};
   }

   @Override
   public void setParameters(int commandId, Object[] args)
   {
      fqn = (Fqn) args[0];
      searchSubtrees = (Boolean) args[1];
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      GravitateDataCommand that = (GravitateDataCommand) o;

      if (searchSubtrees != that.searchSubtrees) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (searchSubtrees ? 1 : 0);
      return result;
   }

   @Override
   public String toString()
   {
      return "GravitateDataCommand{" +
            "fqn=" + fqn +
            ", searchSubtrees=" + searchSubtrees +
            '}';
   }
}
