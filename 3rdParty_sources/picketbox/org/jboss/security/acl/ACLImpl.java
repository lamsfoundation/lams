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
package org.jboss.security.acl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.Identity;

/**
 * <p>
 * Simple ACL implementation that keeps the entries in a Map whose keys are the identities of the entries, to provide
 * fast access.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
@Entity
@Table(name = "ACL")
public class ACLImpl implements ACL, Serializable
{
   private static final long serialVersionUID = -6390609071167528812L;

   @Id
   @GeneratedValue
   private long aclID;

   @Transient
   private Resource resource;

   @Column(name = "resource")
   private String resourceAsString;

   @Transient
   private Map<String, ACLEntry> entriesMap;

   @OneToMany(mappedBy = "acl", fetch = FetchType.EAGER, cascade =
   {CascadeType.REMOVE, CascadeType.PERSIST})
   @Cascade(
   {org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
   private Collection<ACLEntryImpl> entries;

   /**
    * <p>
    * Builds an instance of {@code ACLImpl}. This constructor is required by the JPA specification.
    * </p>
    */
   ACLImpl()
   {
   }

   /**
    * <p>
    * Builds an instance of {@code ACLImpl} for the specified resource.
    * </p>
    * 
    * @param resource a reference to the {@code Resource} associated with the ACL being constructed.
    */
   public ACLImpl(Resource resource)
   {
      this(resource, new ArrayList<ACLEntry>());
   }

   /**
    * <p>
    * Builds an instance of {@code ACLImpl} for the specified resource, and initialize it with the specified entries.
    * </p>
    * 
    * @param resource a reference to the {@code Resource} associated with the ACL being constructed.
    * @param entries a {@code Collection} containing the ACL's initial entries.
    */
   public ACLImpl(Resource resource, Collection<ACLEntry> entries)
   {
      this(Util.getResourceAsString(resource), entries);
      this.resource = resource;
   }

   public ACLImpl(String resourceString, Collection<ACLEntry> entries)
   {
      this.resourceAsString = resourceString;
      this.entries = new ArrayList<ACLEntryImpl>();
      if (entries != null)
      {
         for (ACLEntry entry : entries)
         {
            ACLEntryImpl entryImpl = (ACLEntryImpl) entry;
            entryImpl.setAcl(this);
            this.entries.add(entryImpl);
         }
      }
      this.initEntriesMap();
   }

   /**
    * <p>
    * Obtains the persistent id of this {@code ACLImpl}.
    * </p>
    * 
    * @return a {@code long} representing the persistent id this ACL.
    */
   public long getACLId()
   {
      return this.aclID;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACL#addEntry(org.jboss.security.acl.ACLEntry)
    */
   public boolean addEntry(ACLEntry entry)
   {
      if (this.entriesMap == null)
         this.initEntriesMap();

      // don't add a null entry or an entry that already existSELECT * FROM ACL_ENTRYs.
      if (entry == null || this.entriesMap.get(entry.getIdentityOrRole()) != null)
         return false;
      this.entries.add((ACLEntryImpl) entry);
      ((ACLEntryImpl) entry).setAcl(this);
      this.entriesMap.put(entry.getIdentityOrRole(), entry);
      return true;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACL#removeEntry(org.jboss.security.acl.ACLEntry)
    */
   public boolean removeEntry(ACLEntry entry)
   {
      if (this.entriesMap == null)
         this.initEntriesMap();
      this.entriesMap.remove(entry.getIdentityOrRole());
      return this.entries.remove(entry);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACL#getEntries()
    */
   public Collection<? extends ACLEntry> getEntries()
   {
      if (this.entriesMap == null)
         this.initEntriesMap();
      return Collections.unmodifiableCollection(this.entries);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACL#getEntry(org.jboss.security.identity.Identity)
    */
   public ACLEntry getEntry(Identity identity)
   {
      if (this.entriesMap == null)
         this.initEntriesMap();
      return this.entriesMap.get(identity.getName());
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACL#getEntry(java.lang.String)
    */
   public ACLEntry getEntry(String identityOrRole)
   {
      if (this.entriesMap == null)
         this.initEntriesMap();
      return this.entriesMap.get(identityOrRole);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACL#isGranted(org.jboss.security.acl.ACLPermission,
    *      org.jboss.security.identity.Identity)
    */
   public boolean isGranted(ACLPermission permission, Identity identity)
   {
      if (this.entriesMap == null)
         this.initEntriesMap();

      // lookup the entry corresponding to the specified identity.
      ACLEntry entry = this.entriesMap.get(identity.getName());
      if (entry != null)
      {
         // check the permission associated with the identity.
         return entry.checkPermission(permission);
      }
      return false;
   }

   /**
    * <p>
    * Obtains the stringfied representation of the resource associated with this {@code ACL}.
    * </p>
    * 
    * @return a {@code String} representation of the resource.
    */
   public String getResourceAsString()
   {
      return this.resourceAsString;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.ACL#getResource()
    */
   public Resource getResource()
   {
      return this.resource;
   }

   /**
    * <p>
    * Sets the resource associated with this {@code ACL}.
    * </p>
    * 
    * @param resource a reference to the {@code Resource} associated with this {@code ACL}.
    */
   public void setResource(Resource resource)
   {
      if (this.resource != null)
         throw PicketBoxMessages.MESSAGES.aclResourceAlreadySet();
      this.resource = resource;
   }

   /**
    * <p>
    * Initializes the entries map of this {@code ACL} instance.
    * </p>
    */
   private void initEntriesMap()
   {
      this.entriesMap = new HashMap<String, ACLEntry>();
      for (ACLEntry entry : this.entries)
         this.entriesMap.put(entry.getIdentityOrRole(), entry);
   }

}
