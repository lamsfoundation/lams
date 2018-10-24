/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.security.identity;

import java.io.Serializable;

/**
 * Represents an attribute of the identity
 * @author Anil.Saldhana@redhat.com
 */
public interface Attribute<T> extends Serializable
{
   /**
    * Get the name of the attribute
    * @return
    */
   String getName();
   
   /**
    * The value of the attribute
    * @return
    */
   T getValue();
   
   public enum TYPE
   {
      COUNTRY("country"),
      EMAIL_ADDRESS("email"),EMPLOYEE_TYPE("employeeType"),EMPLOYEE_NUMBER("employeeNumber"),
      GIVEN_NAME("givenName"), 
      PREFERRED_LANGUAGE("preferredLanguage"), PO_BOX("postOfficeBox"), POSTAL_CODE("postalCode"),
      POSTAL_ADDRESS("postalAddress"),
      SURNAME("surname"), STREET("street"), 
      TITLE("title"), TELEPHONE("telephoneNumber");
  
      private String type;

      TYPE(String type)
      {
         this.type = type; 
      }
      
      public String get()
      {
         return this.type;
      }
   }
}