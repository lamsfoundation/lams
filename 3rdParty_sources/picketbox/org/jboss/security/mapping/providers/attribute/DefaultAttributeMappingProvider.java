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
package org.jboss.security.mapping.providers.attribute;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.security.SecurityConstants;
import org.jboss.security.identity.Attribute;
import org.jboss.security.identity.AttributeFactory;
import org.jboss.security.mapping.MappingProvider;
import org.jboss.security.mapping.MappingResult;
 

/**
 * Generates an attribute array from the passed options
 * @author Anil.Saldhana@redhat.com
 */
public class DefaultAttributeMappingProvider implements MappingProvider<List<Attribute<String>>>
{
   private MappingResult<List<Attribute<String>>> result = new MappingResult<List<Attribute<String>>>();
   
   private Map<String,Object> options = new HashMap<String,Object>();
   
   public void init(Map<String,Object> options)
   {
      this.options.putAll(options);
   }

   public void performMapping(Map<String, Object> map, List<Attribute<String>> mappedObject)
   {
      List<Attribute<String>> attList = new ArrayList<Attribute<String>>();
      
      //Get the Principal
      Principal principal = (Principal) map.get(SecurityConstants.PRINCIPAL_IDENTIFIER);
      
      if(principal != null)
      {
         String principalName = principal.getName(); 
         
         //Get the email address
         String emailAddress = (String) options.get(principalName + ".email"); 
         
         Attribute<String> att = AttributeFactory.createEmailAddress(emailAddress);
         attList.add(att);
      }
      mappedObject.addAll(attList);
      result.setMappedObject(mappedObject);
   }

   public void setMappingResult(MappingResult<List<Attribute<String>>> result)
   {    
      this.result = result;
   }

   public boolean supports(Class<?> clazz)
   {
      if(Attribute.class.isAssignableFrom(clazz)) 
        return true;
      return false;
   }
}