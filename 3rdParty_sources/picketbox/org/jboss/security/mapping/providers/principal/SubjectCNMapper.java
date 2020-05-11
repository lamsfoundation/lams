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
package org.jboss.security.mapping.providers.principal;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.certs.SubjectCNMapping;
import org.jboss.security.mapping.MappingResult;
 

/**
 *  A X500 Principal Mapper from a X509 Certificate that uses the client cert
 *  SubjectDN CN='...' element as the principal.
 *  
 *  @see org.jboss.security.auth.certs.SubjectCNMapping
 *  @author Anil.Saldhana@redhat.com
 *  @since  Oct 5, 2007 
 *  @version $Revision$
 */
public class SubjectCNMapper extends AbstractPrincipalMappingProvider
{ 
   private MappingResult<Principal> result;

   public void init(Map<String,Object> opt)
   {
   }

   public void setMappingResult(MappingResult<Principal> res)
   { 
      result = res;
   }
   
   public void performMapping(Map<String,Object> contextMap, Principal principal)
   {
      if(principal instanceof X500Principal == false)
         return;
      if(contextMap == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");

      X509Certificate[] certs = (X509Certificate[]) contextMap.get("X509");
      if(certs != null)
      {
        SubjectCNMapping sdn = new SubjectCNMapping();
        principal = sdn.toPrinicipal(certs);
        PicketBoxLogger.LOGGER.traceMappedX500Principal(principal);
      }
      
      result.setMappedObject(principal);
   } 
}