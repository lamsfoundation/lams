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

import java.util.*;

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

import org.jboss.security.ClientLoginModule;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.spi.BaseCertLoginModule;
import org.jboss.security.auth.spi.CertRolesLoginModule;
import org.jboss.security.auth.spi.DatabaseCertLoginModule;
import org.jboss.security.auth.spi.DatabaseServerLoginModule;
import org.jboss.security.auth.spi.IdentityLoginModule;
import org.jboss.security.auth.spi.LdapExtLoginModule;
import org.jboss.security.auth.spi.LdapLoginModule;
import org.jboss.security.auth.spi.RoleMappingLoginModule;
import org.jboss.security.auth.spi.RunAsLoginModule;
import org.jboss.security.auth.spi.SimpleServerLoginModule;
import org.jboss.security.auth.spi.UsersRolesLoginModule;
import org.jboss.security.config.Element;
import org.picketbox.datasource.security.CallerIdentityLoginModule;
import org.picketbox.datasource.security.ConfiguredIdentityLoginModule;
import org.picketbox.datasource.security.JaasSecurityDomainIdentityLoginModule;
import org.picketbox.datasource.security.PBEIdentityLoginModule;
import org.picketbox.datasource.security.SecureIdentityLoginModule;

/**
 * Stax based JAAS authentication configuration Parser
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jan 22, 2010
 */
public class AuthenticationConfigParser implements XMLStreamConstants
{

   public static final Map<String, String> loginModulesMap;

   static
   {
      final Map<String, String> loginModules = new HashMap<String,String>();
      loginModules.put("Client", ClientLoginModule.class.getName());
      loginModules.put("Certificate", BaseCertLoginModule.class.getName());
      loginModules.put("CertificateRoles", CertRolesLoginModule.class.getName());
      loginModules.put("DatabaseCertificate", DatabaseCertLoginModule.class.getName());
      loginModules.put("Database", DatabaseServerLoginModule.class.getName());
      loginModules.put("Identity", IdentityLoginModule.class.getName());
      loginModules.put("Ldap", LdapLoginModule.class.getName());
      loginModules.put("LdapExtended", LdapExtLoginModule.class.getName());
      loginModules.put("RoleMapping", RoleMappingLoginModule.class.getName());
      loginModules.put("RunAs", RunAsLoginModule.class.getName());
      loginModules.put("Simple", SimpleServerLoginModule.class.getName());
      loginModules.put("UsersRoles", UsersRolesLoginModule.class.getName());
      loginModules.put("CallerIdentity", CallerIdentityLoginModule.class.getName());
      loginModules.put("ConfiguredIdentity", ConfiguredIdentityLoginModule.class.getName());
      loginModules.put("JaasSecurityDomainIdentity", JaasSecurityDomainIdentityLoginModule.class.getName());
      loginModules.put("PBEIdentity", PBEIdentityLoginModule.class.getName());
      loginModules.put("SecureIdentity", SecureIdentityLoginModule.class.getName());
      loginModulesMap = Collections.unmodifiableMap(loginModules);
   }

   /**
    * Parse the <authentication> element
    * @param xmlEventReader
    * @return
    * @throws XMLStreamException
    */
   public Set<AppConfigurationEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      Set<AppConfigurationEntry> entries = new LinkedHashSet<AppConfigurationEntry>();
      while(xmlEventReader.hasNext())
      {   
         XMLEvent xmlEvent = xmlEventReader.peek(); 
         
         StartElement peekedStartElement = (StartElement) xmlEvent;
         AppConfigurationEntry entry = null;
         if("login-module".equals(StaxParserUtil.getStartElementName(peekedStartElement)))
         {
            entry = this.getEntry(xmlEventReader);
         } 
         else
            break;
         entries.add(entry); 
      }
      return entries;
   }
   
   @SuppressWarnings("unchecked")
   private AppConfigurationEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      XMLEvent xmlEvent = xmlEventReader.nextEvent();
      Map<String, Object> options = new HashMap<String,Object>();
      
      String codeName = null;
      LoginModuleControlFlag controlFlag = LoginModuleControlFlag.REQUIRED;
      
      //We got the login-module element
      StartElement loginModuleElement = (StartElement) xmlEvent;
      //We got the login-module element
      Iterator<Attribute> attrs = loginModuleElement.getAttributes();
      while(attrs.hasNext())
      {
         Attribute attribute = attrs.next();
         
         QName attQName = attribute.getName();
         String attributeValue = StaxParserUtil.getAttributeValue(attribute);
         
         if("code".equals(attQName.getLocalPart()))
         {
            codeName = attributeValue;
         }
         else if("flag".equals(attQName.getLocalPart()))
         {
            controlFlag = getControlFlag(attributeValue);
         } 
      } 
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      
      return new AppConfigurationEntry(codeName, controlFlag, options); 
   }  
   
   private LoginModuleControlFlag getControlFlag(String flag)
   {
      if("required".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.REQUIRED;
      if("sufficient".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.SUFFICIENT;
      if("optional".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.OPTIONAL;
      if("requisite".equalsIgnoreCase(flag))
         return LoginModuleControlFlag.REQUISITE;

      throw PicketBoxMessages.MESSAGES.invalidControlFlag(flag);
   }
   
   /**
    * Parse the <authentication> element
    * @param reader
    * @return
    * @throws XMLStreamException
    */
   public Set<AppConfigurationEntry> parse(XMLStreamReader reader) throws XMLStreamException
   {
      Set<AppConfigurationEntry> entries = new LinkedHashSet<AppConfigurationEntry>();
      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         AppConfigurationEntry entry = null;
         if (element.equals(Element.LOGIN_MODULE))
         {
            entry = getEntry(reader);
         }
         else
            throw StaxParserUtil.unexpectedElement(reader);
         entries.add(entry);
      }
      return entries;
   }

   private AppConfigurationEntry getEntry(XMLStreamReader reader) throws XMLStreamException
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
               if (loginModulesMap.containsKey(value))
                   codeName = loginModulesMap.get(value);
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
}