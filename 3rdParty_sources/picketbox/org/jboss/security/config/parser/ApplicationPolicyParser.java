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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.AppConfigurationEntry;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jboss.security.acl.config.ACLProviderEntry;
import org.jboss.security.audit.config.AuditProviderEntry;
import org.jboss.security.auth.login.AuthenticationInfo;
import org.jboss.security.auth.login.JASPIAuthenticationInfo;
import org.jboss.security.authorization.config.AuthorizationModuleEntry;
import org.jboss.security.config.ACLInfo;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.AuditInfo;
import org.jboss.security.config.AuthorizationInfo;
import org.jboss.security.config.Element;
import org.jboss.security.config.IdentityTrustInfo;
import org.jboss.security.config.MappingInfo;
import org.jboss.security.identitytrust.config.IdentityTrustModuleEntry;
import org.jboss.security.mapping.MappingType;
import org.jboss.security.mapping.config.MappingModuleEntry;

/**
 * Stax based Application Policy Parser
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jan 22, 2010
 */
public class ApplicationPolicyParser implements XMLStreamConstants
{ 
   @SuppressWarnings("unchecked")
   public List<ApplicationPolicy> parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      List<ApplicationPolicy> policies = new ArrayList<ApplicationPolicy>();
      while(xmlEventReader.hasNext())
      { 
          XMLEvent xmlEvent = xmlEventReader.nextEvent();
          int eventType = xmlEvent.getEventType();
          switch(eventType)
          {
              case XMLStreamConstants.START_ELEMENT:  
                 StartElement appPolicyElement = (StartElement) xmlEvent;
                 String elementName = StaxParserUtil.getStartElementName(appPolicyElement);
                 if("application-policy".equals(elementName) == false)
                    throw StaxParserUtil.unexpectedElement(elementName, xmlEvent);
                 //We got the application-policy element. It just has one attribute "name"
                 Iterator<Attribute> attrs = appPolicyElement.getAttributes(); 
                 String extendsName = null;
                 String appPolicyName = null;
                 
                 while(attrs.hasNext())
                 {
                    Attribute attribute = attrs.next();
                    QName attributeName = attribute.getName();
                    String attributeValue = StaxParserUtil.getAttributeValue(attribute);
                    
                    if("name".equals(attributeName.getLocalPart()))
                       appPolicyName = attributeValue; 
                    else if("extends".equals(attributeName.getLocalPart()))
                       extendsName = attributeValue;  
                 }

                 ApplicationPolicy applicationPolicy = new ApplicationPolicy(appPolicyName); 
                 if(extendsName != null)
                    applicationPolicy.setBaseApplicationPolicyName(extendsName);
                  
                 route(xmlEventReader, applicationPolicy);
                 policies.add(applicationPolicy); 
          } 
      }
      return policies;
   }
   
   @SuppressWarnings({"unchecked", "rawtypes"})
   private void route(XMLEventReader xmlEventReader, ApplicationPolicy appPolicy) throws XMLStreamException
   {
      while(true)
      {
         XMLEvent xmlEvent = xmlEventReader.peek();
         if(xmlEvent == null)
            return;
         StartElement startElement = xmlEvent.asStartElement();
         String elementName = StaxParserUtil.getStartElementName(startElement);
         if("authentication".equals(elementName))
         {
            xmlEvent = xmlEventReader.nextEvent();
            AuthenticationConfigParser parser = new AuthenticationConfigParser();
            Set<AppConfigurationEntry> entries = parser.parse(xmlEventReader);
            AuthenticationInfo authInfo = new AuthenticationInfo(appPolicy.getName());
             
            authInfo.setAppConfigurationEntry(new ArrayList(entries));
            appPolicy.setAuthenticationInfo(authInfo); 
         }
         else if("authentication-jaspi".equals(elementName))
         {

            xmlEvent = xmlEventReader.nextEvent();
            AuthenticationJASPIConfigParser parser = new AuthenticationJASPIConfigParser();
            JASPIAuthenticationInfo authInfo = parser.parse(xmlEventReader); 
            appPolicy.setAuthenticationInfo(authInfo); 
         } 
         else if("authorization".equals(elementName))
         { 
            xmlEvent = xmlEventReader.nextEvent();
            AuthorizationConfigParser parser = new AuthorizationConfigParser();
            Set<AuthorizationModuleEntry> entries = parser.parse(xmlEventReader);
            AuthorizationInfo authInfo = new AuthorizationInfo(appPolicy.getName());
            authInfo.add(new ArrayList(entries));
            appPolicy.setAuthorizationInfo(authInfo); 
         } 
         else if("acl".equals(elementName))
         { 
            xmlEvent = xmlEventReader.nextEvent();
            AclConfigParser parser = new AclConfigParser();
            Set<ACLProviderEntry> entries = parser.parse(xmlEventReader);
            ACLInfo aclInfo = new ACLInfo(appPolicy.getName());
            aclInfo.add(new ArrayList(entries));
            appPolicy.setAclInfo(aclInfo); 
         }  
         else if("rolemapping".equals(elementName))
         { 
            xmlEvent = xmlEventReader.nextEvent();
            MappingConfigParser parser = new MappingConfigParser();
            List<MappingModuleEntry> entries = parser.parse(xmlEventReader);
            MappingInfo mappingInfo = new MappingInfo(appPolicy.getName()); 
            mappingInfo.add(entries);
            appPolicy.setMappingInfo(MappingType.ROLE.toString(), mappingInfo);
         }  
         else if("mapping".equals(elementName))
         { 
            xmlEvent = xmlEventReader.nextEvent();
            MappingConfigParser parser = new MappingConfigParser();
            List<MappingModuleEntry> entries = parser.parse(xmlEventReader);
            for(MappingModuleEntry entry: entries)
            {
               MappingInfo mappingInfo = new MappingInfo(appPolicy.getName());
               mappingInfo.add(entry);
               String moduleType = entry.getMappingModuleType();
               appPolicy.setMappingInfo(moduleType, mappingInfo); 
            }
         } 
         else if("audit".equals(elementName))
         { 
            xmlEvent = xmlEventReader.nextEvent();
            AuditConfigParser parser = new AuditConfigParser();
            List<AuditProviderEntry> entries = parser.parse(xmlEventReader);
            AuditInfo authInfo = new AuditInfo(appPolicy.getName());
            authInfo.add(entries);
            appPolicy.setAuditInfo(authInfo); 
         } 
         else if("identity-trust".equals(elementName))
         { 
            xmlEvent = xmlEventReader.nextEvent();
            IdentityTrustConfigParser parser = new IdentityTrustConfigParser();
            List<IdentityTrustModuleEntry> entries = parser.parse(xmlEventReader);
            IdentityTrustInfo authInfo = new IdentityTrustInfo(appPolicy.getName());
            authInfo.add(entries);
            appPolicy.setIdentityTrustInfo(authInfo); 
         } 
         else if("application-policy".equals(elementName)){
             break; 
         } 
         else {
             throw StaxParserUtil.unexpectedElement(elementName, xmlEvent);
         }
      }
   }
   
   public List<ApplicationPolicy> parse(XMLStreamReader reader) throws XMLStreamException
   {
      List<ApplicationPolicy> policies = null;
      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         if (element.equals(Element.APPLICATION_POLICY) || element.equals(Element.SECURITY_DOMAIN))
         {
            final int count = reader.getAttributeCount();
            if (count == 0)
               throw StaxParserUtil.missingRequired(reader, Collections.singleton(org.jboss.security.config.Attribute.NAME));
            String name = null;
            String extendsName = null;
            for (int i = 0; i < count; i++)
            {
               final String value = reader.getAttributeValue(i);
               final org.jboss.security.config.Attribute attribute = org.jboss.security.config.Attribute.forName(reader.getAttributeLocalName(i));
               switch (attribute)
               {
                  case NAME : {
                     name = value;
                     break;
                  }
                  case EXTENDS : {
                     extendsName = value;
                     break;
                  }
                  default :
                     throw StaxParserUtil.unexpectedAttribute(reader, i);
               }
            }
            if (name == null)
               throw StaxParserUtil.missingRequired(reader, Collections.singleton(org.jboss.security.config.Attribute.NAME));
            final ApplicationPolicy applicationPolicy = new ApplicationPolicy(name);
            if (extendsName != null)
               applicationPolicy.setBaseApplicationPolicyName(extendsName);
            route(reader, applicationPolicy);
            if (policies == null)
               policies = new ArrayList<ApplicationPolicy>();
            policies.add(applicationPolicy);

         }
         else
            throw StaxParserUtil.unexpectedElement(reader);
      }
      return policies;
   }

   @SuppressWarnings({"unchecked", "rawtypes"})
   private void route(XMLStreamReader reader, ApplicationPolicy appPolicy) throws XMLStreamException
   {
      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         switch (element)
         {
            case ACL : {
               AclConfigParser parser = new AclConfigParser();
               Set<ACLProviderEntry> entries = parser.parse(reader);
               ACLInfo aclInfo = new ACLInfo(appPolicy.getName());
               aclInfo.add(new ArrayList(entries));
               appPolicy.setAclInfo(aclInfo);
               break;
            }
            case AUDIT : {
               AuditConfigParser parser = new AuditConfigParser();
               List<AuditProviderEntry> entries = parser.parse(reader);
               AuditInfo authInfo = new AuditInfo(appPolicy.getName());
               authInfo.add(entries);
               appPolicy.setAuditInfo(authInfo);
               break;
            }
            case AUTHENTICATION : {
               AuthenticationConfigParser parser = new AuthenticationConfigParser();
               Set<AppConfigurationEntry> entries = parser.parse(reader);
               AuthenticationInfo authInfo = new AuthenticationInfo(appPolicy.getName());
               authInfo.setAppConfigurationEntry(new ArrayList(entries));
               appPolicy.setAuthenticationInfo(authInfo);
               break;
            }
            case AUTHENTICATION_JASPI : {
               AuthenticationJASPIConfigParser parser = new AuthenticationJASPIConfigParser();
               JASPIAuthenticationInfo authInfo = parser.parse(reader);
               appPolicy.setAuthenticationInfo(authInfo);
               break;
            }
            case AUTHORIZATION : {
               AuthorizationConfigParser parser = new AuthorizationConfigParser();
               Set<AuthorizationModuleEntry> entries = parser.parse(reader);
               AuthorizationInfo authInfo = new AuthorizationInfo(appPolicy.getName());
               authInfo.add(new ArrayList(entries));
               appPolicy.setAuthorizationInfo(authInfo);
               break;
            }
            case IDENTITY_TRUST : {
               IdentityTrustConfigParser parser = new IdentityTrustConfigParser();
               List<IdentityTrustModuleEntry> entries = parser.parse(reader);
               IdentityTrustInfo authInfo = new IdentityTrustInfo(appPolicy.getName());
               authInfo.add(entries);
               appPolicy.setIdentityTrustInfo(authInfo);
               break;
            }
            case MAPPING : {
               MappingConfigParser parser = new MappingConfigParser();
               List<MappingModuleEntry> entries = parser.parse(reader);
               for (MappingModuleEntry entry : entries)
               {
                  MappingInfo mappingInfo = new MappingInfo(appPolicy.getName());
                  mappingInfo.add(entry);
                  String moduleType = entry.getMappingModuleType();
                  appPolicy.setMappingInfo(moduleType, mappingInfo);
               }
               break;
            }
            case ROLE_MAPPING : {
               MappingConfigParser parser = new MappingConfigParser();
               List<MappingModuleEntry> entries = parser.parse(reader);
               MappingInfo mappingInfo = new MappingInfo(appPolicy.getName());
               mappingInfo.add(entries);
               appPolicy.setMappingInfo(MappingType.ROLE.toString(), mappingInfo);
               break;
            }
            default :
               throw StaxParserUtil.unexpectedElement(reader);
         }
      }
   }
}
