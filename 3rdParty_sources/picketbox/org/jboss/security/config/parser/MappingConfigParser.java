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

import org.jboss.security.config.Element;
import org.jboss.security.mapping.MappingType;
import org.jboss.security.mapping.config.MappingModuleEntry;

/**
 * Stax based mapping configuration Parser
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jan 22, 2010
 */
public class MappingConfigParser implements XMLStreamConstants
{ 
   /**
    * The mapping module by default can be ROLE type
    * or it can be defined by the module with a 'type' attribute 
    */
   private String typeName = MappingType.ROLE.toString();
   
   /**
    * Parse the <mapping> element
    * @param xmlEventReader
    * @return
    * @throws XMLStreamException
    */
   public List<MappingModuleEntry> parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      List<MappingModuleEntry> entries = new ArrayList<MappingModuleEntry>();
      while(xmlEventReader.hasNext())
      {   
         XMLEvent xmlEvent = xmlEventReader.peek(); 
         
         StartElement peekedStartElement = (StartElement) xmlEvent;
         String peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
         MappingModuleEntry entry = null;
         if("mapping-module".equals(peekedStartElementName))
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
   private MappingModuleEntry getEntry(XMLEventReader xmlEventReader) throws XMLStreamException
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
         else if("type".equals(attQName.getLocalPart()))
         {
            typeName = attributeValue; 
         }
      } 
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(xmlEventReader));
      
      return new MappingModuleEntry(codeName, options,typeName);  
   }
   
   /**
    * Parse the <mapping> element
    * @param reader
    * @return
    * @throws XMLStreamException
    */
   public List<MappingModuleEntry> parse(XMLStreamReader reader) throws XMLStreamException
   {
      List<MappingModuleEntry> entries = new ArrayList<MappingModuleEntry>();
      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         MappingModuleEntry entry = null;
         if (element.equals(Element.MAPPING_MODULE))
         {
            entry = getEntry(reader);
         }
         else
            throw StaxParserUtil.unexpectedElement(reader);
         entries.add(entry);
      }
      return entries;
   }

   private MappingModuleEntry getEntry(XMLStreamReader reader) throws XMLStreamException
   {
      Map<String, Object> options = new HashMap<String, Object>();
      String codeName = null;

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
            case TYPE : {
               typeName = value;
               break;
            }
            default :
               throw StaxParserUtil.unexpectedAttribute(reader, i);
         }
      }
      if (codeName == null)
         throw StaxParserUtil.missingRequired(reader, Collections.singleton(org.jboss.security.config.Attribute.CODE));
      //See if there are options
      ModuleOptionParser moParser = new ModuleOptionParser();
      options.putAll(moParser.parse(reader));

      return new MappingModuleEntry(codeName, options, typeName);
   }
}