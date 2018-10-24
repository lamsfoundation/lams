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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.security.auth.AuthPermission;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.security.config.Attribute;
import org.jboss.security.config.BaseSecurityInfo;
import org.jboss.security.config.Element;
import org.jboss.security.config.parser.AuthenticationConfigParser;

/**
 * The login module configuration information.
 * 
 * @author Scott.Stark@jboss.org
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @version $Revision$
 */
@SuppressWarnings("rawtypes")
public class AuthenticationInfo extends BaseAuthenticationInfo
{
   public static final AuthPermission GET_CONFIG_ENTRY_PERM = new AuthPermission("getLoginConfiguration");

   public static final AuthPermission SET_CONFIG_ENTRY_PERM = new AuthPermission("setLoginConfiguration");

   private CallbackHandler callbackHandler;

   public AuthenticationInfo()
   {
      this(null);
   }

   public AuthenticationInfo(String name)
   {
      this.name = name;
   }

   public void addAppConfigurationEntry(AppConfigurationEntry entry)
   {
      moduleEntries.add(entry);
   }

   /**
    * Set an application authentication configuration. This requires an AuthPermission("setLoginConfiguration") access.
    */
   public void setAppConfigurationEntry(AppConfigurationEntry[] loginModules)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(SET_CONFIG_ENTRY_PERM);

      moduleEntries.addAll(Arrays.asList(loginModules));
   }
   
   public void setAppConfigurationEntry(List<AppConfigurationEntry> listOfEntries)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(SET_CONFIG_ENTRY_PERM);

      moduleEntries.addAll(listOfEntries);
   }

   /**
    */
   public CallbackHandler getAppCallbackHandler()
   {
      return callbackHandler;
   }

   public void setAppCallbackHandler(CallbackHandler handler)
   {
      this.callbackHandler = handler;
   }

   @Override
   public String toString()
   {
      StringBuffer buffer = new StringBuffer("AppConfigurationEntry[]:\n");
      for (int i = 0; i < moduleEntries.size(); i++)
      {
         AppConfigurationEntry entry = (AppConfigurationEntry) moduleEntries.get(i);
         buffer.append("[" + i + "]");
         buffer.append("\nLoginModule Class: " + entry.getLoginModuleName());
         buffer.append("\nControlFlag: " + entry.getControlFlag());
         buffer.append("\nOptions:\n");
         Map<String, ?> options = entry.getOptions();
         Iterator iter = options.entrySet().iterator();
         while (iter.hasNext())
         {
            Entry e = (Entry) iter.next();
            String name = (String) e.getKey();
            String value = e.getValue() == null ? "" : e.getValue().toString();
            String nameToLower = name.toLowerCase(Locale.ENGLISH);
            if (nameToLower.equals("password") || nameToLower.equals("bindcredential")
                  || nameToLower.equals(Context.SECURITY_CREDENTIALS))
               value = "****";
            buffer.append("name=" + name);
            buffer.append(", value=" + value);
            buffer.append("\n");
         }
      }
      return buffer.toString();
   }

   @Override
   protected BaseSecurityInfo<Object> create(String name)
   {
      return new AuthenticationInfo(name);
   }
   
   /**
    * Write element content. The start element is already written.
    * 
    * @param writer
    * @throws XMLStreamException
    */
   public void writeContent(XMLStreamWriter writer) throws XMLStreamException
   {
      for (int i = 0; i < moduleEntries.size(); i++)
      {
         AppConfigurationEntry entry = (AppConfigurationEntry) moduleEntries.get(i);
         writer.writeStartElement(Element.LOGIN_MODULE.getLocalName());
         String code = entry.getLoginModuleName();
         if (AuthenticationConfigParser.loginModulesMap.containsValue(code)) {
            String value = null;
            Set<Entry<String, String>> entries = AuthenticationConfigParser.loginModulesMap.entrySet();
            for (Entry<String, String> mapEntry : entries) {
                if (mapEntry.getValue().equals(code)) {
                    value = mapEntry.getKey();
                    break;
                }
            }
            writer.writeAttribute(Attribute.CODE.getLocalName(), value);
        }
        else
            writer.writeAttribute(Attribute.CODE.getLocalName(), code);
         writer.writeAttribute(Attribute.FLAG.getLocalName(), valueOf(entry.getControlFlag()));
         Map<String, ?> options = entry.getOptions();
         if (options != null && options.size() > 0)
         {
            for (Entry<String, ?> option : options.entrySet())
            {
               writer.writeStartElement(Element.MODULE_OPTION.getLocalName());
               writer.writeAttribute(Attribute.NAME.getLocalName(), option.getKey());
               writer.writeAttribute(Attribute.VALUE.getLocalName(), option.getValue().toString());
               writer.writeEndElement();
            }
         }
         writer.writeEndElement();
      }
      writer.writeEndElement();
   }
   
   private String valueOf(LoginModuleControlFlag controlFlag)
   {
      if (controlFlag.equals(LoginModuleControlFlag.OPTIONAL))
         return "optional";
      if (controlFlag.equals(LoginModuleControlFlag.REQUIRED))
         return "required";
      if (controlFlag.equals(LoginModuleControlFlag.REQUISITE))
         return "requisite";
      return "sufficient";
   }
}
