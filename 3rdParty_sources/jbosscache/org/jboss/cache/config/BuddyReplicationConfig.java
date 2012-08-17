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
package org.jboss.cache.config;

import org.jboss.cache.buddyreplication.NextMemberBuddyLocator;
import org.jboss.cache.util.Util;

import java.util.Properties;


public class BuddyReplicationConfig extends ConfigurationComponent
{
   private static final long serialVersionUID = -4826380823985089339L;

   /**
    * Test whether buddy replication is enabled.
    */
   private boolean enabled;

   /**
    * Name of the buddy pool for current instance.  May be null if buddy pooling is not used.
    */
   private String buddyPoolName;

   private boolean autoDataGravitation = true;
   private boolean dataGravitationRemoveOnFind = true;
   private boolean dataGravitationSearchBackupTrees = true;
   @Dynamic
   private int buddyCommunicationTimeout = 10000;
   private BuddyLocatorConfig buddyLocatorConfig;

   public boolean isAutoDataGravitation()
   {
      return autoDataGravitation;
   }

   public void setAutoDataGravitation(boolean autoDataGravitation)
   {
      testImmutability("autoDataGravitation");
      this.autoDataGravitation = autoDataGravitation;
   }

   public int getBuddyCommunicationTimeout()
   {
      return buddyCommunicationTimeout;
   }

   public void setBuddyCommunicationTimeout(int buddyCommunicationTimeout)
   {
      testImmutability("buddyCommunicationTimeout");
      this.buddyCommunicationTimeout = buddyCommunicationTimeout;
   }

   public String getBuddyPoolName()
   {
      return buddyPoolName;
   }

   public void setBuddyPoolName(String buddyPoolName)
   {
      testImmutability("buddyPoolName");
      this.buddyPoolName = buddyPoolName;
   }

   public boolean isDataGravitationRemoveOnFind()
   {
      return dataGravitationRemoveOnFind;
   }

   public void setDataGravitationRemoveOnFind(boolean dataGravitationRemoveOnFind)
   {
      testImmutability("dataGravitationRemoveOnFind");
      this.dataGravitationRemoveOnFind = dataGravitationRemoveOnFind;
   }

   public boolean isDataGravitationSearchBackupTrees()
   {
      return dataGravitationSearchBackupTrees;
   }

   public void setDataGravitationSearchBackupTrees(boolean dataGravitationSearchBackupTrees)
   {
      testImmutability("dataGravitationSearchBackupTrees");
      this.dataGravitationSearchBackupTrees = dataGravitationSearchBackupTrees;
   }

   public boolean isEnabled()
   {
      return enabled;
   }

   public void setEnabled(boolean enabled)
   {
      testImmutability("enabled");
      this.enabled = enabled;
   }

   public BuddyLocatorConfig getBuddyLocatorConfig()
   {
      return buddyLocatorConfig;
   }

   public void setBuddyLocatorConfig(BuddyLocatorConfig buddyLocatorConfig)
   {
      testImmutability("buddyLocatorConfig");
      replaceChildConfig(this.buddyLocatorConfig, buddyLocatorConfig);
      this.buddyLocatorConfig = buddyLocatorConfig;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;

      if (obj instanceof BuddyReplicationConfig)
      {
         BuddyReplicationConfig other = (BuddyReplicationConfig) obj;
         return (this.autoDataGravitation == other.autoDataGravitation)
               && (this.dataGravitationRemoveOnFind == other.dataGravitationRemoveOnFind)
               && (this.dataGravitationSearchBackupTrees == other.dataGravitationSearchBackupTrees)
               && (this.enabled == other.enabled)
               && (this.buddyCommunicationTimeout == other.buddyCommunicationTimeout)
               && Util.safeEquals(this.buddyPoolName, other.buddyPoolName)
               && Util.safeEquals(this.buddyLocatorConfig, other.buddyLocatorConfig);
      }

      return false;
   }

   @Override
   public int hashCode()
   {
      int result = 11;
      result = 29 * result + (autoDataGravitation ? 0 : 1);
      result = 29 * result + (dataGravitationRemoveOnFind ? 0 : 1);
      result = 29 * result + (dataGravitationSearchBackupTrees ? 0 : 1);
      result = 29 * result + (enabled ? 0 : 1);
      result = 29 * result + buddyCommunicationTimeout;
      result = 29 * result + (buddyPoolName == null ? 0 : buddyPoolName.hashCode());
      result = 29 * result + (buddyLocatorConfig == null ? 0 : buddyLocatorConfig.hashCode());
      return result;
   }

   @Override
   public BuddyReplicationConfig clone() throws CloneNotSupportedException
   {
      BuddyReplicationConfig clone = (BuddyReplicationConfig) super.clone();
      if (buddyLocatorConfig != null)
         clone.setBuddyLocatorConfig(buddyLocatorConfig.clone());
      return clone;
   }

   public static class BuddyLocatorConfig extends PluggableConfigurationComponent
   {
      private static final long serialVersionUID = -8003634097931826091L;

      public BuddyLocatorConfig()
      {
         // default
         className = NextMemberBuddyLocator.class.getName();
      }

      public String getBuddyLocatorClass()
      {
         return className;
      }

      public void setBuddyLocatorClass(String buddyLocatorClass)
      {
         setClassName(buddyLocatorClass);
      }

      public Properties getBuddyLocatorProperties()
      {
         return properties;
      }

      public void setBuddyLocatorProperties(Properties buddyLocatorProperties)
      {
         setProperties(buddyLocatorProperties);
      }

      public BuddyLocatorConfig clone() throws CloneNotSupportedException
      {
         return (BuddyLocatorConfig) super.clone();
      }
   }
}
