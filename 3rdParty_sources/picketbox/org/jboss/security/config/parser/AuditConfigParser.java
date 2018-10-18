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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jboss.security.audit.config.AuditProviderEntry;
import org.jboss.security.config.Element;

/**
 * Stax based audit configuration Parser
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jan 22, 2010
 */
public class AuditConfigParser implements XMLStreamConstants
{ 
   /**
    * Parse the <audit> element
    * @param xmlEventReader
    * @return
    * @throws XMLStreamException
    */
   public List<AuditProviderEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      List<AuditProviderEntry> entries = new ArrayList<AuditProviderEntry>();
      while(xmlEventReader.hasNext())
      {   
         XMLEvent xmlEvent = xmlEventReader.peek(); 
         
         StartElement peekedStartElement = (StartElement) xmlEvent;
         AuditProviderEntry entry = null;
         if("provider-module".equals(StaxParserUtil.getStartElementName(peekedStartElement)))
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
   private AuditProviderEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      XMLEvent xmlEvent = xmlEventReader.nextEvent(); 
      Map<String, Object> options = new HashMap<String,Object>();
      
      
      String codeName = null; 
      
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
      } 
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      
      AuditProviderEntry entry =  new AuditProviderEntry(codeName, options);  
      return entry;
   }
   
   /**
    * Parse the <audit> element
    * @param reader
    * @return
    * @throws XMLStreamException
    */
   public List<AuditProviderEntry> parse(XMLStreamReader reader) throws XMLStreamException
   {
      List<AuditProviderEntry> entries = new ArrayList<AuditProviderEntry>();
      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         AuditProviderEntry entry = null;
         if (element.equals(Element.PROVIDER_MODULE))
         {
            entry = getEntry(reader);
         }
         else
            throw StaxParserUtil.unexpectedElement(reader);
         entries.add(entry);
      }
      return entries;
   }

   private AuditProviderEntry getEntry(XMLStreamReader reader) throws XMLStreamException
   {
      String codeName = null;
      Map<String, Object> options = new HashMap<String, Object>();
      final int count = reader.getAttributeCount();
      if (count < 1)
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
            default :
               throw StaxParserUtil.unexpectedAttribute(reader, i);
         }
      }
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(reader));

      AuditProviderEntry entry = new AuditProviderEntry(codeName, options);
      return entry;
   }
}