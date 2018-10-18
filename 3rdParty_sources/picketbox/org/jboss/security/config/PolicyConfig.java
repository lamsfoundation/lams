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
package org.jboss.security.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//$Id$

/** The root object for the login-config.xml descriptor as defined by the
 * security-config_5_0.xsd.
 * 
 * @author Scott.Stark@jboss.org
 * @author Anil.Saldhana@jboss.org
 * @version $Revision$
 */
public class PolicyConfig
{
   Map<String,ApplicationPolicy> config = Collections.synchronizedMap(new HashMap<String,ApplicationPolicy>());  
   
   public void add(ApplicationPolicy ai)
   {
      config.put(ai.getName(), ai);
      ai.setPolicyConfig(this);
   }  
   
   public ApplicationPolicy get(String name)
   {
      return config.get(name);
   }
   
   public ApplicationPolicy remove(String name)
   {
      return config.remove(name);
   }
   
   public void clear()
   {
      config.clear();
   }
   public Set<String> getConfigNames()
   {
      return config.keySet();
   }
   public int size()
   {
      return config.size();
   }
   public boolean containsKey(String name)
   {
      return config.containsKey(name);
   }
   public void copy(PolicyConfig pc)
   {
      config.putAll(pc.config);
   }
   
   public Collection<ApplicationPolicy> getPolicies()
   {
      return Collections.unmodifiableCollection(this.config.values());
   }
}
