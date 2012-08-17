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
package org.jboss.cache.buddyreplication;

import org.jboss.cache.Fqn;
import org.jboss.cache.marshall.NodeData;

import java.util.List;

/**
 * A class that encapsulates the result of a data gravitation call, a part of the Buddy Replication framwork.  A GravitateResult
 * contains 3 elements; a boolean indicating whether the gravitation request found any data at all, a List of {@link NodeData} objects
 * containing the data to be gravitated, and an {@link Fqn} of the buddy backup region being gravitated.
 *
 * @since 2.0.0
 */
public class GravitateResult
{
   private final boolean dataFound;

   private final List<NodeData> nodeData;

   private final Fqn buddyBackupFqn;

   /**
    * Factory method that creates a GravitateResult indicating that no data has been found.
    *
    * @return GravitateResult encapsulating the fact that no data was found
    */
   public static GravitateResult noDataFound()
   {
      return new GravitateResult(false, null, null);
   }

   /**
    * Factory method that creates a GravitateResult with the data found and the backup fqn it was found in.
    *
    * @param nodeData data found
    * @param fqn      backup fqn the data was found in
    * @return GravitateResult encapsulating the above
    */
   public static GravitateResult subtreeResult(List<NodeData> nodeData, Fqn fqn)
   {
      return new GravitateResult(true, nodeData, fqn);
   }

   private GravitateResult(boolean dataFound, List<NodeData> nodeData, Fqn buddyBackupRegion)
   {
      this.dataFound = dataFound;
      this.nodeData = nodeData;
      this.buddyBackupFqn = buddyBackupRegion;
   }

   /**
    * @return the buddyBackupFqn
    */
   public Fqn getBuddyBackupFqn()
   {
      return buddyBackupFqn;
   }

   /**
    * @return true if data was found
    */
   public boolean isDataFound()
   {
      return dataFound;
   }

   /**
    * @return the nodeData
    */
   public List<NodeData> getNodeData()
   {
      return nodeData;
   }

   @Override
   public String toString()
   {
      return "GravitateResult dataFound=" + dataFound +
            " nodeData=" + nodeData +
            " fqn=" + buddyBackupFqn;
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      GravitateResult that = (GravitateResult) o;

      if (dataFound != that.dataFound) return false;
      if (buddyBackupFqn != null ? !buddyBackupFqn.equals(that.buddyBackupFqn) : that.buddyBackupFqn != null)
         return false;
      if (nodeData != null ? !nodeData.equals(that.nodeData) : that.nodeData != null) return false;

      return true;
   }

   public int hashCode()
   {
      int result;
      result = (dataFound ? 1 : 0);
      result = 31 * result + (nodeData != null ? nodeData.hashCode() : 0);
      result = 31 * result + (buddyBackupFqn != null ? buddyBackupFqn.hashCode() : 0);
      return result;
   }
}
