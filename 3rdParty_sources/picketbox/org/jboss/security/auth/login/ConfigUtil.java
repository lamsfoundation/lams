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
package org.jboss.security.auth.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.security.auth.login.AppConfigurationEntry;

import org.jboss.security.util.xml.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Utility methods for parsing the XMlLoginConfig elements into
 * AuthenticationInfo instances.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
@SuppressWarnings("rawtypes")
public class ConfigUtil
{
   /** Parse the application-policy/authentication element
    @param policy , the application-policy/authentication element
    @return the AuthenticationInfo object for the xml policy fragment
    */
   @SuppressWarnings("unchecked")
   static public AuthenticationInfo parseAuthentication(Element policy)
      throws Exception
   {
      // Parse the permissions
      NodeList authentication = policy.getElementsByTagName("authentication");
      if (authentication.getLength() == 0)
      {
         return null;
      }

      Element auth = (Element) authentication.item(0);
      NodeList modules = auth.getElementsByTagName("login-module");
      ArrayList tmp = new ArrayList();
      for (int n = 0; n < modules.getLength(); n++)
      {
         Element module = (Element) modules.item(n);
         parseModule(module, tmp);
      }
      AppConfigurationEntry[] entries = new AppConfigurationEntry[tmp.size()];
      tmp.toArray(entries);
      AuthenticationInfo info = new AuthenticationInfo();
      info.setAppConfigurationEntry(entries);
      return info;
   }

   @SuppressWarnings("unchecked")
   static void parseModule(Element module, ArrayList entries)
      throws Exception
   {
      AppConfigurationEntry.LoginModuleControlFlag controlFlag = AppConfigurationEntry.LoginModuleControlFlag.REQUIRED;
      String className = DOMUtils.getAttributeValue(module, "code");
      String flag = DOMUtils.getAttributeValue(module, "flag");
      if (flag != null)
      {
         // Lower case is what is used by the jdk1.4.1 implementation
         flag = flag.toLowerCase(Locale.ENGLISH);
         if (AppConfigurationEntry.LoginModuleControlFlag.REQUIRED.toString().indexOf(flag) > 0)
            controlFlag = AppConfigurationEntry.LoginModuleControlFlag.REQUIRED;
         else if (AppConfigurationEntry.LoginModuleControlFlag.REQUISITE.toString().indexOf(flag) > 0)
            controlFlag = AppConfigurationEntry.LoginModuleControlFlag.REQUISITE;
         else if (AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT.toString().indexOf(flag) > 0)
            controlFlag = AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT;
         else if (AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL.toString().indexOf(flag) > 0)
            controlFlag = AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL;
      }
      NodeList opts = module.getElementsByTagName("module-option");
      HashMap options = new HashMap();
      for (int n = 0; n < opts.getLength(); n++)
      {
         Element opt = (Element) opts.item(n);
         String name = opt.getAttribute("name");
         String value = DOMUtils.getTextContent(opt);
         if( value == null )
            value = "";
         options.put(name, value);
      }
      AppConfigurationEntry entry = new AppConfigurationEntry(className, controlFlag, options);
      entries.add(entry);
   }
}
