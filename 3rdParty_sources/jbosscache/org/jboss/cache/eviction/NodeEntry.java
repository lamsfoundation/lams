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
package org.jboss.cache.eviction;

import org.jboss.cache.Fqn;

/**
 * Value object used in queue
 *
 * @author Ben Wang 2-2004
 * @author Daniel Huang - dhuang@jboss.org
 */
public class NodeEntry
{
   private long modifiedTimeStamp;
   private long creationTimeStamp;
   private int numberOfNodeVisits;
   private int numberOfElements;
   private Fqn fqn;

   private long inUseTimeoutTimestamp;
   private boolean currentlyInUse = false;

   EvictionQueue queue;

   /**
    * Private constructor that automatically sets the creation time stamp of the node entry.
    */
   private NodeEntry()
   {
      this.creationTimeStamp = System.currentTimeMillis();
   }

   public NodeEntry(Fqn fqn)
   {
      this();
      setFqn(fqn);
   }

   public NodeEntry(String fqn)
   {
      this();
      setFqn(Fqn.fromString(fqn));
   }

   /**
    * Is the node currently in use.
    *
    * @return True/false if the node is currently marked as in use.
    */
   public boolean isCurrentlyInUse()
   {
      return currentlyInUse;
   }

   public void setCurrentlyInUse(boolean currentlyInUse, long inUseTimeout)
   {
      this.currentlyInUse = currentlyInUse;
      if (inUseTimeout > 0)
      {
         this.inUseTimeoutTimestamp = System.currentTimeMillis() + inUseTimeout;
      }
   }

   public long getInUseTimeoutTimestamp()
   {
      return this.inUseTimeoutTimestamp;
   }

   /**
    * Get modified time stamp. This stamp is created during the node is
    * processed so it has some fuzy tolerance in there.
    *
    * @return The last modified time stamp
    */
   public long getModifiedTimeStamp()
   {
      return modifiedTimeStamp;
   }

   public void setModifiedTimeStamp(long modifiedTimeStamp)
   {
//      log.error("Being modified to " + modifiedTimeStamp, new Throwable());
      this.modifiedTimeStamp = modifiedTimeStamp;
   }

   /**
    * Get the time stamp for when the node entry was created.
    *
    * @return The node entry creation time stamp
    */
   public long getCreationTimeStamp()
   {
      return creationTimeStamp;
   }

   public void setCreationTimeStamp(long creationTimeStamp)
   {
      this.creationTimeStamp = creationTimeStamp;
   }

   public int getNumberOfNodeVisits()
   {
      return numberOfNodeVisits;
   }

   public void setNumberOfNodeVisits(int numberOfNodeVisits)
   {
      this.numberOfNodeVisits = numberOfNodeVisits;
   }

   public int getNumberOfElements()
   {
      return numberOfElements;
   }

   public void setNumberOfElements(int numberOfElements)
   {
      if (queue != null)
      {
         int difference = numberOfElements - this.numberOfElements;
         queue.modifyElementCount(difference);
      }
      this.numberOfElements = numberOfElements;
   }

   public Fqn getFqn()
   {
      return fqn;
   }

   void setFqn(Fqn fqn)
   {
      this.fqn = fqn;
   }

   @Override
   public int hashCode()
   {
      return fqn.hashCode();
   }

   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof NodeEntry))
         return false;
      NodeEntry ne = (NodeEntry) o;
      return fqn.equals(ne.getFqn());
   }

   @Override
   public String toString()
   {
      StringBuilder output = new StringBuilder();
      output.append("Fqn: ");
      if (fqn != null)
      {
         output.append(fqn);
      }
      else
      {
         output.append(" null");
      }

      output.append(" CreateTime: ").append(this.getCreationTimeStamp());
      output.append(" NodeVisits: ").append(this.getNumberOfNodeVisits());
      output.append(" ModifiedTime: ").append(this.getModifiedTimeStamp());
      output.append(" NumberOfElements: ").append(this.getNumberOfElements());
      output.append(" CurrentlyInUse: ").append(this.isCurrentlyInUse());
      return output.toString();
   }

}
