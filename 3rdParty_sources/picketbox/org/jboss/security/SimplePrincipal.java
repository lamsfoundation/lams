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
package org.jboss.security;

import java.io.Serializable;
import java.security.Principal;

/**
 * A simple String based implementation of Principal. Typically a SimplePrincipal is created given a userID which is
 * used as the Principal name.
 * 
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author Scott.Stark@jboss.org
 */
public class SimplePrincipal implements Principal, Serializable
{
   private static final long serialVersionUID = 7701951188631723261L;
   private final String name;
   private static final String OVERRIDE_EQUALS_BEHAVIOR = "org.jboss.security.simpleprincipal.equals.override";

   public SimplePrincipal(String name)
   {
      this.name = name;
   }

   /**
    * Compare this SimplePrincipal's name against another Principal. If system property
    * org.jboss.security.simpleprincipal.equals.override is set to true will only
    * compare instances of SimplePrincipals.
    * @return true if name equals another.getName();
    */ 
   @Override
   public boolean equals(Object another)
   {
      if (!(another instanceof Principal))
         return false;
      if ("true".equals(SecurityActions.getProperty(OVERRIDE_EQUALS_BEHAVIOR, "false")))
      {
         if (!(another instanceof SimplePrincipal))
            return false;
      }
      String anotherName = ((Principal) another).getName();
      boolean equals = false;
      if (name == null)
         equals = anotherName == null;
      else
         equals = name.equals(anotherName);
      return equals;
   }

   @Override
   public int hashCode()
   {
      return (name == null ? 0 : name.hashCode());
   }

   @Override
   public String toString()
   {
      return name;
   }

   public String getName()
   {
      return name;
   }
}