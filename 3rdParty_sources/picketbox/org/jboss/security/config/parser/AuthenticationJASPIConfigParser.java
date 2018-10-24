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
package org.jboss.security.config.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.container.config.AuthModuleEntry;
import org.jboss.security.auth.login.JASPIAuthenticationInfo;
import org.jboss.security.auth.login.LoginModuleStackHolder;
import org.jboss.security.config.ControlFlag;
import org.jboss.security.config.Element;

/**
 * Stax based JASPI configuration Parser
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jan 22, 2010
 */
public class AuthenticationJASPIConfigParser implements XMLStreamConstants
{
   /**
    * Parse the <authentication-jaspi> element
    * @param xmlEventReader
    * @return
    * @throws XMLStreamException
    */
   public JASPIAuthenticationInfo parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      JASPIAuthenticationInfo authInfo = new JASPIAuthenticationInfo();
      Map<String, LoginModuleStackHolder> holders = new HashMap<String, LoginModuleStackHolder>();
      while (xmlEventReader.hasNext())
      {
         XMLEvent xmlEvent = xmlEventReader.peek();

         StartElement peekedStartElement = (StartElement) xmlEvent;
         String peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
         if ("login-module-stack".equals(peekedStartElementName))
         {
            StartElement lmshEvent = (StartElement) xmlEventReader.nextEvent();
            Attribute nameAttribute = (Attribute) lmshEvent.getAttributes().next();
            String nameAttributeValue = StaxParserUtil.getAttributeValue(nameAttribute);
            LoginModuleStackHolder holder = new LoginModuleStackHolder(nameAttributeValue, null);
            holders.put(nameAttributeValue, holder);
            authInfo.add(holder);

            while (true)
            {
               //Get all the login modules
               xmlEvent = xmlEventReader.peek();
               peekedStartElement = (StartElement) xmlEvent;
               peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
               if ("login-module".equals(peekedStartElementName))
               {
                  holder.addAppConfigurationEntry(this.getJAASEntry(xmlEventReader));
               }
               else
                  break;
            }
         }
         else if ("auth-module".equals(peekedStartElementName))
         {
            AuthModuleEntry entry = getJaspiEntry(xmlEventReader);
            String stackHolderRefName = entry.getLoginModuleStackHolderName();
            if (stackHolderRefName != null)
            {
               if (holders.containsKey(stackHolderRefName) == false)
                  throw PicketBoxMessages.MESSAGES.invalidLoginModuleStackRef(stackHolderRefName);
               entry.setLoginModuleStackHolder(holders.get(stackHolderRefName));
            }
            authInfo.add(entry);
         }
         else
            break;

      }
      return authInfo;
   }

   @SuppressWarnings("unchecked")
   private AppConfigurationEntry getJAASEntry(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      XMLEvent xmlEvent = xmlEventReader.nextEvent();
      Map<String, Object> options = new HashMap<String, Object>();

      String codeName = null;
      LoginModuleControlFlag controlFlag = LoginModuleControlFlag.REQUIRED;

      //We got the login-module element
      StartElement loginModuleElement = (StartElement) xmlEvent;
      //We got the login-module element
      Iterator<Attribute> attrs = loginModuleElement.getAttributes();
      while (attrs.hasNext())
      {
         Attribute attribute = attrs.next();
         QName attQName = attribute.getName();
         String attributeValue = StaxParserUtil.getAttributeValue(attribute);

         if ("code".equals(attQName.getLocalPart()))
         {
            codeName = attributeValue;
         }
         else if ("flag".equals(attQName.getLocalPart()))
         {
            controlFlag = getControlFlag(attributeValue);
         }
      }
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));

      return new AppConfigurationEntry(codeName, controlFlag, options);
   }

   @SuppressWarnings("unchecked")
   private AuthModuleEntry getJaspiEntry(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      XMLEvent xmlEvent = xmlEventReader.nextEvent();
      Map<String, Object> options = new HashMap<String, Object>();

      String codeName = null;
      String loginModuleStackRef = null;
      ControlFlag flag = ControlFlag.REQUIRED;

      //We got the login-module element
      StartElement authModuleElement = (StartElement) xmlEvent;
      //We got the login-module element
      Iterator<Attribute> attrs = authModuleElement.getAttributes();
      while (attrs.hasNext())
      {
         Attribute attribute = attrs.next();

         QName attQName = attribute.getName();
         String attributeValue = StaxParserUtil.getAttributeValue(attribute);

         if ("code".equals(attQName.getLocalPart()))
         {
            codeName = attributeValue;
         }
         else if ("flag".equals(attQName.getLocalPart()))
         {
            flag = ControlFlag.valueOf(attributeValue);
         }
         else if ("login-module-stack-ref".equals(attQName.getLocalPart()))
         {
            loginModuleStackRef = attributeValue;
         }
      }

      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));

      AuthModuleEntry entry = new AuthModuleEntry(codeName, options, loginModuleStackRef);
      entry.setControlFlag(flag);
      return entry;
   }

   private LoginModuleControlFlag getControlFlag(String flag)
   {
      if ("required".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.REQUIRED;
      if ("sufficient".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.SUFFICIENT;
      if ("optional".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.OPTIONAL;
      if ("requisite".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.REQUISITE;
      throw PicketBoxMessages.MESSAGES.invalidControlFlag(flag);
   }
   
   /**
    * Parse the <authentication-jaspi> element
    * @param reader
    * @return
    * @throws XMLStreamException
    */
   public JASPIAuthenticationInfo parse(XMLStreamReader reader) throws XMLStreamException
   {
      JASPIAuthenticationInfo authInfo = new JASPIAuthenticationInfo();
      Map<String, LoginModuleStackHolder> holders = new HashMap<String, LoginModuleStackHolder>();
      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         switch (element)
         {
            case LOGIN_MODULE_STACK : {
               final int count = reader.getAttributeCount();
               if (count < 1)
               {
                  throw StaxParserUtil.missingRequired(reader, Collections
                        .singleton(org.jboss.security.config.Attribute.NAME));
               }
               LoginModuleStackHolder holder = null;
               for (int i = 0; i < count; i++)
               {
                  final String value = reader.getAttributeValue(i);
                  final org.jboss.security.config.Attribute attribute = org.jboss.security.config.Attribute
                        .forName(reader.getAttributeLocalName(i));
                  switch (attribute)
                  {
                     case NAME : {
                        String name = value;
                        holder = new LoginModuleStackHolder(name, null);
                        holders.put(name, holder);
                        authInfo.add(holder);
                        break;
                     }
                     default :
                        throw StaxParserUtil.unexpectedAttribute(reader, i);
                  }
               }
               while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
               {
                  final Element element2 = Element.forName(reader.getLocalName());
                  if (element2.equals(Element.LOGIN_MODULE))
                  {
                     holder.addAppConfigurationEntry(getJAASEntry(reader));
                  }
               }
               break;
            }
            case AUTH_MODULE : {
               AuthModuleEntry entry = getJaspiEntry(reader);
               String stackHolderRefName = entry.getLoginModuleStackHolderName();
               if (stackHolderRefName != null)
               {
                  if (!holders.containsKey(stackHolderRefName))
                     throw PicketBoxMessages.MESSAGES.invalidLoginModuleStackRef(stackHolderRefName);
                  entry.setLoginModuleStackHolder(holders.get(stackHolderRefName));
               }
               authInfo.add(entry);
               break;
            }
            default :
               throw StaxParserUtil.unexpectedElement(reader);
         }
      }
      return authInfo;
   }

   private AppConfigurationEntry getJAASEntry(XMLStreamReader reader) throws XMLStreamException
   {
      Map<String, Object> options = new HashMap<String, Object>();
      String codeName = null;
      LoginModuleControlFlag controlFlag = LoginModuleControlFlag.REQUIRED;

      final int count = reader.getAttributeCount();
      if (count < 2)
      {
         Set<org.jboss.security.config.Attribute> set = new HashSet<org.jboss.security.config.Attribute>();
         set.add(org.jboss.security.config.Attribute.CODE);
         set.add(org.jboss.security.config.Attribute.FLAG);
         throw StaxParserUtil.missingRequired(reader, set);
      }
      for (int i = 0; i < count; i++)
      {
         final String value = reader.getAttributeValue(i);
         final org.jboss.security.config.Attribute attribute = org.jboss.security.config.Attribute.forName(reader
               .getAttributeLocalName(i));
         switch (attribute)
         {
            case CODE : {
               // check if it's a known login module
               if (AuthenticationConfigParser.loginModulesMap.containsKey(value))
                   codeName = AuthenticationConfigParser.loginModulesMap.get(value);
               else
                   codeName = value;
               break;
            }
            case FLAG : {
               controlFlag = getControlFlag(value);
               break;
            }
            default :
               throw StaxParserUtil.unexpectedAttribute(reader, i);
         }
      }
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(reader));

      return new AppConfigurationEntry(codeName, controlFlag, options);
   }

   private AuthModuleEntry getJaspiEntry(XMLStreamReader reader) throws XMLStreamException
   {
      Map<String, Object> options = new HashMap<String, Object>();
      String codeName = null;
      String loginModuleStackRef = null;
      ControlFlag flag = ControlFlag.REQUIRED;

      final int count = reader.getAttributeCount();
      if (count == 0)
      {
         throw StaxParserUtil.missingRequired(reader, Collections.singleton(org.jboss.security.config.Attribute.CODE));
      }
      for (int i = 0; i < count; i++)
      {
         final String value = reader.getAttributeValue(i);
         final org.jboss.security.config.Attribute attribute = org.jboss.security.config.Attribute.forName(reader
               .getAttributeLocalName(i));
         switch (attribute)
         {
            case CODE : {
               codeName = value;
               break;
            }
            case FLAG : {
               flag = ControlFlag.valueOf(value);
               break;
            }
            case LOGIN_MODULE_STACK_REF : {
               loginModuleStackRef = value;
               break;
            }
            default :
               throw StaxParserUtil.unexpectedAttribute(reader, i);
         }
      }
      if (codeName == null)
      {
         throw StaxParserUtil.missingRequired(reader, Collections.singleton(org.jboss.security.config.Attribute.CODE));
      }
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(reader));

      AuthModuleEntry entry = new AuthModuleEntry(codeName, options, loginModuleStackRef);
      entry.setControlFlag(flag);
      return entry;
   }
}