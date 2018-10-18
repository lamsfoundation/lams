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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jboss.security.authorization.config.AuthorizationModuleEntry;
import org.jboss.security.config.ControlFlag;
import org.jboss.security.config.Element;

/**
 * Stax based authorization configuration Parser
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jan 22, 2010
 */
public class AuthorizationConfigParser implements XMLStreamConstants
{ 
   /**
    * Parse the <authorization> element
    * @param xmlEventReader
    * @return
    * @throws XMLStreamException
    */
   public Set<AuthorizationModuleEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      Set<AuthorizationModuleEntry> entries = new LinkedHashSet<AuthorizationModuleEntry>();
      while(xmlEventReader.hasNext())
      {   
         XMLEvent xmlEvent = xmlEventReader.peek(); 
         
         StartElement peekedStartElement = (StartElement) xmlEvent;
         AuthorizationModuleEntry entry = null;
         if("policy-module".equals(StaxParserUtil.getStartElementName(peekedStartElement)))
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
   private AuthorizationModuleEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      XMLEvent xmlEvent = xmlEventReader.nextEvent(); 
      Map<String, Object> options = new HashMap<String,Object>();
      
      
      String codeName = null;
      ControlFlag controlFlag = ControlFlag.REQUIRED;
      
      //We got the login-module element
      StartElement policyModuleElement = (StartElement) xmlEvent;
      //We got the login-module element
      Iterator<Attribute> attrs = policyModuleElement.getAttributes();
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
            controlFlag = ControlFlag.valueOf(attributeValue);
         } 
      } 
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      
      AuthorizationModuleEntry entry =  new AuthorizationModuleEntry(codeName, options); 
      entry.setControlFlag(controlFlag);
      return entry;
   }
   
   /**
    * Parse the <authorization> element
    * @param reader
    * @return
    * @throws XMLStreamException
    */
   public Set<AuthorizationModuleEntry> parse(XMLStreamReader reader) throws XMLStreamException
   {
      Set<AuthorizationModuleEntry> entries = new LinkedHashSet<AuthorizationModuleEntry>();
      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         AuthorizationModuleEntry entry = null;
         if (element.equals(Element.POLICY_MODULE))
         {
            entry = getEntry(reader);
         }
         else
            throw StaxParserUtil.unexpectedElement(reader);
         entries.add(entry);
      }
      return entries;
   }

   private AuthorizationModuleEntry getEntry(XMLStreamReader reader) throws XMLStreamException
   {
      Map<String, Object> options = new HashMap<String, Object>();
      String codeName = null;
      ControlFlag controlFlag = ControlFlag.REQUIRED;

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
               codeName = value;
               break;
            }
            case FLAG : {
               controlFlag = ControlFlag.valueOf(value);
               break;
            }
            default :
               throw StaxParserUtil.unexpectedAttribute(reader, i);
         }
      }
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(reader));

      AuthorizationModuleEntry entry = new AuthorizationModuleEntry(codeName, options);
      entry.setControlFlag(controlFlag);
      return entry;
   }
}