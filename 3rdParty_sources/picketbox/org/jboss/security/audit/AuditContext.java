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
package org.jboss.security.audit;

import java.util.ArrayList;
import java.util.List;

//$Id$

/**
 *  Context for Audit Purposes that manages a set of providers
 *  @see AuditProvider
 *  @author Anil.Saldhana@redhat.com
 *  @since  May 13, 2007 
 *  @version $Revision$
 */
public abstract class AuditContext
{
   protected String securityDomain = null;
   
   protected List<AuditProvider> providerList = new ArrayList<AuditProvider>();
   
   public void audit(AuditEvent ae)
   {
      int len = this.providerList.size();
      
      for(int i = 0; i < len; i++)
      {
         AuditProvider ap = (AuditProvider)this.providerList.get(i);
         ap.audit(ae);
      } 
   }
   
   public void addProvider(AuditProvider ap)
   {
      providerList.add(ap);
   }
   
   public void addProviders(List<AuditProvider> list)
   {
      providerList.addAll(list);
   }
   
   public List<AuditProvider> getProviders()
   {
      return providerList;
   }
   
   public void replaceProviders(List<AuditProvider> list)
   {
      providerList.clear();
      providerList = list;
   }   
}