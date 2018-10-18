/*
  * JBoss, Home of Professional Open Source
  * Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.security.config;

import org.jboss.security.PicketBoxMessages;

/**
 *  Control Flag for module entries
 *  @author Anil.Saldhana@redhat.com
 *  @since  Aug 2, 2007 
 *  @version $Revision$
 */
public class ControlFlag
{
   private String flag;
   public static final ControlFlag REQUIRED = new ControlFlag("REQUIRED");
   public static final ControlFlag REQUISITE = new ControlFlag("REQUISITE");
   public static final ControlFlag SUFFICIENT = new ControlFlag("SUFFICIENT");
   public static final ControlFlag OPTIONAL = new ControlFlag("OPTIONAL");
   
   public ControlFlag(String flag)
   { 
      this.flag = flag;
   }  
   
   /**
    * Returns the string represented
    * "required", "requisite" etc
    */
   public String toString()
   {
      return flag;
   }

   @Override
   public boolean equals(Object obj)
   {
      if(obj instanceof ControlFlag == false)
         return false;
      ControlFlag objControlFlag = (ControlFlag) obj;
      return flag.equals(objControlFlag.flag);
   }

   @Override
   public int hashCode()
   { 
      return flag.hashCode();
   } 
   
   /**
    * Method that returns the correct
    * Control flag that is associated with the
    * argument flag, which can be (REQUIRED,
    * REQUISITE, SUFFICIENT and OPTIONAL)
    * @param flag
    * @return
    * @throws IllegalArgumentException when flag is 
    * different from the four above
    */
   public static ControlFlag valueOf(String flag)
   {
      if("REQUIRED".equalsIgnoreCase(flag))
         return REQUIRED;
      if("REQUISITE".equalsIgnoreCase(flag))
         return REQUISITE;
      if("SUFFICIENT".equalsIgnoreCase(flag))
         return SUFFICIENT;
      if("OPTIONAL".equalsIgnoreCase(flag))
         return OPTIONAL;

      throw PicketBoxMessages.MESSAGES.invalidControlFlag(flag);
   }
}