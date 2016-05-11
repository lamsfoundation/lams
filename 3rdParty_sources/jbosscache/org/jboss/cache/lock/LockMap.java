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
package org.jboss.cache.lock;

import org.jboss.cache.util.concurrent.ConcurrentHashSet;

import java.util.Collection;

/**
 * Provide lock ownership mapping.
 *
 * @author Ben Wang
 *
 */
public class LockMap
{
   public static final int OWNER_ANY = 0;
   public static final int OWNER_READ = 1;
   public static final int OWNER_WRITE = 2;

   private Object writeOwner_ = null;


   // This is more efficient (lower CPU utilisation and better concurrency) than a CopyOnWriteArraySet or ConcurrentHashSet.
   // for some reason this barfs with concurrent mod exceptions.  Need to see why.
   private final Collection<Object> readOwners;
//   private final Set<Object> readOwners = new ConcurrentHashSet<Object>();


   public LockMap()
   {
      this(new ConcurrentHashSet<Object>(4));
   }

   /**
    * This constructor is made available for testing with different collection types for the readOwners collection.
    *
    * @param readOwners
    */
   public LockMap(Collection<Object> readOwners)
   {
      this.readOwners = readOwners;
   }

   /**
    * Check whether this owner has reader or writer ownership.
    *
    * @param caller    the potential owner.  Cannot be <code>null</code>.
    * @param ownership Either <code>OWNER_ANY</code>, <code>OWNER_READ</code>,
    *                  or <code>OWNER_WRITE</code>.
    * @return true if the caller is the owner of the current lock
    * @throws NullPointerException if <code>caller</code> is <code>null</code>.
    */
   public boolean isOwner(Object caller, int ownership)
   {
      /* This method doesn't need to be synchronized; the thread is doing a simple read access (writer, readers)
         and only the current thread can *change* the writer or readers, so this cannot happen while we read.
      */

      switch (ownership)
      {
         case OWNER_ANY:
            return ((writeOwner_ != null && caller.equals(writeOwner_)) || readOwners.contains(caller));
         case OWNER_READ:
            return (readOwners.contains(caller));
         case OWNER_WRITE:
            return (writeOwner_ != null && caller.equals(writeOwner_));
         default:
            return false;
      }
   }


   /**
    * Adding a reader owner.
    *
    * @param owner
    */
   public void addReader(Object owner)
   {
      readOwners.add(owner);
   }

   /**
    * Adding a writer owner.
    *
    * @param owner
    */
   public void setWriterIfNotNull(Object owner)
   {
      synchronized (this)
      {
         if (writeOwner_ != null)
            throw new IllegalStateException("there is already a writer holding the lock: " + writeOwner_);
         writeOwner_ = owner;
      }
   }

   private Object setWriter(Object owner)
   {
      Object old;
      synchronized (this)
      {
         old = writeOwner_;
         writeOwner_ = owner;
      }
      return old;
   }


   /**
    * Upgrading current reader ownership to writer one.
    *
    * @param owner
    * @return True if successful.
    */
   public boolean upgrade(Object owner) throws OwnerNotExistedException
   {
      boolean old_value = readOwners.remove(owner);
      if (!old_value) // didn't exist in the list
         throw new OwnerNotExistedException("Can't upgrade lock. Read lock owner did not exist");
      setWriter(owner);
      return true;
   }

   /**
    * Returns an unmodifiable set of reader owner objects.
    */
   public Collection<Object> readerOwners()
   {
      // not necessary if the collection is a CHS.  Saves on overhead.
//      return Collections.unmodifiableCollection(readOwners);
      return readOwners;
   }

   public void releaseReaderOwners(LockStrategy lock)
   {
      int size = readOwners.size();
      for (int i = 0; i < size; i++)
         lock.readLock().unlock();
   }

   /**
    * @return Writer owner object. Null if none.
    */
   public Object writerOwner()
   {
      return writeOwner_;
   }

   /**
    * Remove reader ownership.
    */
   public void removeReader(Object owner)
   {
      readOwners.remove(owner);
   }

   /**
    * Remove writer ownership.
    */
   public void removeWriter()
   {
      synchronized (this)
      {
         writeOwner_ = null;
      }
   }

   /**
    * Remove all ownership.
    */
   public void removeAll()
   {
      removeWriter();
      readOwners.clear();
   }

   /**
    * Debugging information.
    *
    * @return lock information
    */
   public String printInfo()
   {
      StringBuilder buf = new StringBuilder(64);
      buf.append("Read lock owners: ").append(readOwners).append('\n');
      buf.append("Write lock owner: ").append(writeOwner_).append('\n');
      return buf.toString();
   }

   public boolean isReadLocked()
   {
      return !readOwners.isEmpty();
   }
}
