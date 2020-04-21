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
package org.jboss.security.javaee;

//$Id: SecurityRoleRef.java 68749 2008-01-09 20:25:39Z anil.saldhana@jboss.com $

/**
 *  Represents a Security Role Ref element in the deployment descriptor
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @since  Jul 21, 2006 
 *  @version $Revision: 68749 $
 */
public class SecurityRoleRef
{
   private String name;
   private String link;
   private String description;
   
   public SecurityRoleRef()
   {   
   }
   
   public SecurityRoleRef(String name, String link)
   {
      this.name = name;
      this.link = link; 
   }
   
   public SecurityRoleRef(String name, String link, String description)
   {
      this.name = name;
      this.link = link;
      this.description = description;
   }

   /**
    * Get the description.
    * 
    * @return the description.
    */
   public String getDescription()
   {
      return description;
   }
   
   /**
    * Set the description. 
    */
   public void setDescription(String desc)
   {
      this.description = desc;
   }

   /**
    * Get the link.
    * @return link
    */
   public String getLink()
   {
      return this.link;
   }


   /**
    * Set the link.
    */
   public void setLink(String l)
   {
      this.link = l;
   }


   /**
    * Get the name.
    * 
    * @return the name.
    */
   public String getName()
   {
      return this.name;
   }  

   
   /**
    * Set the name.
    * 
    * @return the name.
    */
   public void setName(String n)
   {
      this.name = n;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder(super.toString());
      builder.append("[");
      builder.append(this.name).append(";").append(this.link);
      builder.append("]"); 
      return builder.toString();
   }
}