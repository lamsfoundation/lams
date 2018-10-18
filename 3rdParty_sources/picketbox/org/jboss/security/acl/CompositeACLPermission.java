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

/**
 * <p>
 * This class represents a composite permission - a permission that contains one or more basic permissions. The bitmask
 * value of this permission is calculated by combining (logical or) the bitmask values of the basic permissions it
 * contains. Thus, a composite permission's bitmask can have more than one bit on, and each bit corresponds to one of
 * the basic permissions that are part of the composite permission.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class CompositeACLPermission implements BitMaskPermission
{
   private int mask = 0;

   /**
    * <p>
    * Builds an instance of {@code CompositeACLPermission} with the specified bitmask value.
    * </p>
    * 
    * @param mask an {@code int} representing the bitmask value of the permission being created.
    */
   public CompositeACLPermission(int mask)
   {
      this.mask = mask;
   }

   /**
    * <p>
    * Builds an instance of {@code CompositeACLPermission} with the given basic permissions.
    * </p>
    * 
    * @param permissions a comma-separated list of {@code BasicACLPermission}s.
    */
   public CompositeACLPermission(BasicACLPermission... permissions)
   {
      for (BasicACLPermission basicPermission : permissions)
      {
         this.mask |= basicPermission.getMaskValue();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.acl.MaskPermission#getMask()
    */
   public int getMaskValue()
   {
      return this.mask;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof BitMaskPermission)
         return this.mask == ((BitMaskPermission) obj).getMaskValue();
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      return this.mask;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      if (this.mask == 0)
         return "NO PERMISSION";
      StringBuffer buffer = new StringBuffer();
      for (BasicACLPermission permission : BasicACLPermission.values())
      {
         if((permission.getMaskValue() & this.mask) != 0)
            buffer.append(permission.toString() + ",");
      }
      return buffer.substring(0, buffer.lastIndexOf(","));
   }

   /**
    * <p>
    * Returns the binary representation of this permission.
    * </p>
    * 
    * @return a {@code String} containing this permission's binary representation.
    */
   public String toBinaryString()
   {
      return Integer.toBinaryString(this.mask);
   }
}
