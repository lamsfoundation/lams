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
 * This enum defines the basic ACL permissions. Each permission defined here is represented by a unique
 * integer mask value that is a power of 2 (that is, each permission's bitmask has only one bit on).
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public enum BasicACLPermission implements BitMaskPermission {

   // basic create permission - value = 1
   CREATE(1 << 0),
   // basic read permission - value = 2
   READ(1 << 1),
   // basic updated permission - value = 4
   UPDATE(1 << 2),
   // basic delete permission - value = 8
   DELETE(1 << 3);

   private int mask;

   /**
    * <p>
    * Buils an instance of {@code BasicACLPermission}.
    * </p>
    * 
    * @param mask   an integer representing the permission's mask value.
    */
   private BasicACLPermission(int mask)
   {
      this.mask = mask;
   }

   /**
    * <p>
    * Obtains this permission's mask value.
    * </p>
    */
   public int getMaskValue()
   {
      return this.mask;
   }

   /**
    * <p>
    * Returns the binary representation of this permission.
    * </p>
    * 
    * @return   a {@code String} containing this permission's binary representation.
    */
   public String toBinaryString()
   {
      return Integer.toBinaryString(this.mask);
   }
}
