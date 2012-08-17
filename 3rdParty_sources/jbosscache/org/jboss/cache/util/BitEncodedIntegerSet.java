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
package org.jboss.cache.util;

import net.jcip.annotations.NotThreadSafe;

/**
 * A Set that encodes integers as bits in a long.  Does not implement java.util.Set since autoboxing is unnecessarily
 * expensive for the ints stored, but follows very similar semantics to Set: no nulls, no duplicates, and order not guaranteed,
 * and adds one more: this can only store ints from 0 to 63, inclusive.
 * <p/>
 * Integers in this set are packed into a single long, setting bit values accordingly and hence the strict range on allowable
 * integers.  The upshot is a guaranteed limit on how much memory is consumed, as well as very efficient operations on the set.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.0
 */
@NotThreadSafe
public class BitEncodedIntegerSet
{
   private long encoded = 0;

   /**
    * Adds an integer to the set.
    *
    * @param i integer to add
    */
   public void add(int i)
   {
      encoded |= ((long) 1 << i);
   }

   /**
    * Removes an integer from the set
    *
    * @param i integer to remove
    */
   public void remove(int i)
   {
      encoded &= ~((long) 1 << i);
   }

   /**
    * Tests whether the set contains an integer
    *
    * @param i integer to check for
    * @return true if contained; false otherwise
    */
   public boolean contains(int i)
   {
      return (encoded & ((long) 1 << i)) != 0;
   }

   @Override
   public boolean equals(Object o)
   {
      if (o == this) return true;
      if (o == null || getClass() != o.getClass()) return false;

      BitEncodedIntegerSet that = (BitEncodedIntegerSet) o;

      return encoded == that.encoded;
   }

   @Override
   public int hashCode()
   {
      return (int) (encoded ^ (encoded >>> 32));
   }

   /**
    * Clears the set
    */
   public void clear()
   {
      encoded = 0;
   }

   /**
    * Tests if the set is empty
    *
    * @return true if empty
    */
   public boolean isEmpty()
   {
      return encoded == 0;
   }

   @Override
   public String toString()
   {
      return "BitEncodedSet (encoded as: " + Long.toBinaryString(encoded) + ")";
   }

   /**
    * Adds all elements of another BitEncodedIntegerSet to the current set.
    *
    * @param otherSet other set to add
    */
   public void addAll(BitEncodedIntegerSet otherSet)
   {
      encoded |= otherSet.encoded;
   }
}
