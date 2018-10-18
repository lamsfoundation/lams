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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.container.config.AuthModuleEntry;
import org.jboss.security.config.Attribute;
import org.jboss.security.config.BaseSecurityInfo;
import org.jboss.security.config.Element;
import org.jboss.security.config.parser.AuthenticationConfigParser;

// $Id$

/**
 * AuthenticationInfo based on JSR-196
 * 
 * @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Dec 21, 2005
 */
public class JASPIAuthenticationInfo extends BaseAuthenticationInfo
{
   List<LoginModuleStackHolder> loginModuleStack = Collections
         .synchronizedList(new ArrayList<LoginModuleStackHolder>());

   /**
    * <p>
    * Creates an instance of {@code JASPIAuthenticationInfo}.
    * </p>
    */
   public JASPIAuthenticationInfo()
   {
      super();
   }

   /**
    * <p>
    * Creates an instance of {@code JASPIAuthenticationInfo} with the specified name.
    * </p>
    * 
    * @param name the name of the enclosing {@code ApplicationPolicy}.
    */
   public JASPIAuthenticationInfo(String name)
   {
      super(name);
   }

   public void add(LoginModuleStackHolder lmsHolder)
   {
      this.loginModuleStack.add(lmsHolder);
   }

   public void add(AuthModuleEntry ame)
   {
      moduleEntries.add(ame);
   }

   public AuthModuleEntry[] getAuthModuleEntry()
   {
      AuthModuleEntry[] entries = new AuthModuleEntry[moduleEntries.size()];
      moduleEntries.toArray(entries);
      return entries;
   }

   public LoginModuleStackHolder getLoginModuleStackHolder(String name)
   {
      for (LoginModuleStackHolder holder : this.loginModuleStack)
      {
         if (holder.getName().equals(name))
            return holder;
      }
      return null;
   }

   public LoginModuleStackHolder[] getLoginModuleStackHolder()
   {
      LoginModuleStackHolder[] lmshArr = new LoginModuleStackHolder[this.loginModuleStack.size()];
      this.loginModuleStack.toArray(lmshArr);
      return lmshArr;
   }

   public LoginModuleStackHolder removeLoginModuleStackHolder(String name)
   {
      for (Iterator<LoginModuleStackHolder> it = this.loginModuleStack.iterator(); it.hasNext();)
      {
         LoginModuleStackHolder holder = it.next();
         if (holder.getName().equals(name))
         {
            it.remove();
            return holder;
         }
      }
      return null;
   }

   public void copy(JASPIAuthenticationInfo pc)
   {
      this.loginModuleStack.addAll(pc.loginModuleStack);
      moduleEntries.addAll(pc.moduleEntries);
   }

   /**
    * <p>
    * Overridden to return the entries that have been configured in the login-config-stack. If more than one
    * stack has been configured, then the entries corresponding to the first stack will be returned.
    * </p>
    */
   @Override
   public AppConfigurationEntry[] getAppConfigurationEntry()
   {
      if (this.loginModuleStack.size() > 0)
         return loginModuleStack.get(0).getAppConfigurationEntry();
      else
         return new AppConfigurationEntry[0];
   }

   /**
    * <p>
    * Overridden to copy the entries that have been configured in the login-module-stack. If more than one stack
    * has been configured, then the entries corresponding to the first stack will be copied and returned.
    * </p>
    */
   @Override
   public AppConfigurationEntry[] copyAppConfigurationEntry()
   {
      List<Object> entries = new ArrayList<Object>();
      for(AppConfigurationEntry entry : this.getAppConfigurationEntry())
         entries.add(entry);
      return super.copyAppConfigurationEntry(entries);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.security.auth.login.BaseAuthenticationInfo#create(java.lang.String)
    */
   @Override
   protected BaseSecurityInfo<Object> create(String name)
   {
      return new JASPIAuthenticationInfo(name);
   }

   /**
    * <p>
    * Overriden to include the stacks of login modules in the merged object.
    * </p>
    */
   @Override
   public BaseSecurityInfo<Object> merge(BaseSecurityInfo<Object> bi)
   {
      if (bi instanceof JASPIAuthenticationInfo == false)
         throw PicketBoxMessages.MESSAGES.invalidType(JASPIAuthenticationInfo.class.getName());
      // merge the auth modules
      JASPIAuthenticationInfo merged = (JASPIAuthenticationInfo) super.merge(bi);
      // merge the stacks of login modules
      JASPIAuthenticationInfo parent = (JASPIAuthenticationInfo) bi;
      for (LoginModuleStackHolder holder : parent.getLoginModuleStackHolder())
         merged.add(holder);
      for (LoginModuleStackHolder holder : this.getLoginModuleStackHolder())
         merged.add(holder);

      return merged;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      StringBuffer buffer = new StringBuffer("AuthModuleEntry[]:\n");
      for (int i = 0; i < moduleEntries.size(); i++)
      {
         AuthModuleEntry entry = (AuthModuleEntry) moduleEntries.get(i);
         buffer.append("[" + i + "]");
         buffer.append("\nAuthModule Class: " + entry.getAuthModuleName());
         buffer.append("\nOptions:");
         Map<String, Object> options = entry.getOptions();
         for (Entry<String, Object> optionsEntry : options.entrySet())
         {
            buffer.append("name=" + optionsEntry.getKey());
            buffer.append(", value=" + optionsEntry.getValue());
            buffer.append("\n");
         }
      }
      return buffer.toString();
   }
   
   /**
    * Write element content. The start element is already written.
    * 
    * @param writer
    * @throws XMLStreamException
    */
   public void writeContent(XMLStreamWriter writer) throws XMLStreamException
   {
      for (int i = 0; i < loginModuleStack.size(); i++)
      {
         LoginModuleStackHolder entry = loginModuleStack.get(i);
         writer.writeStartElement(Element.LOGIN_MODULE_STACK.getLocalName());
         writer.writeAttribute(Attribute.NAME.getLocalName(), entry.getName());
         for (int j = 0; j < entry.getAppConfigurationEntry().length; j++)
         {
            writer.writeStartElement(Element.LOGIN_MODULE.getLocalName());
            AppConfigurationEntry ace = entry.getAppConfigurationEntry()[j];
            String code = ace.getLoginModuleName();
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
            writer.writeAttribute(Attribute.FLAG.getLocalName(), valueOf(ace.getControlFlag()));
            Map<String, ?> options = ace.getOptions();
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
      for (int i = 0; i < moduleEntries.size(); i++)
      {
         AuthModuleEntry entry = (AuthModuleEntry) moduleEntries.get(i);
         writer.writeStartElement(Element.AUTH_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getAuthModuleName());
         writer.writeAttribute(Attribute.FLAG.getLocalName(), entry.getControlFlag().toString().toLowerCase(Locale.ENGLISH));
         writer.writeAttribute(Attribute.LOGIN_MODULE_STACK_REF.getLocalName(), entry.getLoginModuleStackHolderName());
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
