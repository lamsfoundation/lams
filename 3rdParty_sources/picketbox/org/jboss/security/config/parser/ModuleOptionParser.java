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
import java.util.Map;

import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jboss.security.config.Element;

/**
 * Parses the Module Option
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jan 22, 2010
 */
public class ModuleOptionParser implements XMLStreamConstants
{
   private static final Map<String, ParserNamespaceSupport> parsers = Collections.synchronizedMap(new HashMap<String, ParserNamespaceSupport>());
   
   static
   { 
      parsers.put("urn:jboss:user-roles", new UsersConfigParser());
      parsers.put("urn:jboss:java-properties", new JavaPropertiesConfigParser()); 
   }
   
   public static void addParser(String parserName, ParserNamespaceSupport parser)
   {
      parsers.put(parserName, parser);
   }
   
   /**
    * Parse the module-option element
    * @param xmlEventReader
    * @return
    * @throws XMLStreamException
    */
   public Map<String, Object> parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      Map<String, Object> options = new HashMap<String,Object>();
      
      //See if there are options
      while(true)
      {
         XMLEvent xmlEvent = xmlEventReader.peek();
         if(xmlEvent instanceof EndElement) break;
         StartElement peekedStartElement = (StartElement) xmlEvent;
         if(xmlEvent == null)
            break; //no module options
         
         String peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
         
         if("module-option".equals(peekedStartElementName))
         {
            xmlEvent = xmlEventReader.nextEvent();
            Attribute attribute = (Attribute) peekedStartElement.getAttributes().next();
            
            //Sometime, there may be embedded xml in the option. We cannot use peek
            //next event here because the event reader jumps to the next module option
            //in the presence of a text (and not embedded xml). Since embedded xml is rare,
            //we are going to rely on exceptions as a mode of control. The issue is that
            //we have used an event filter on the XMLEventReader for convenience
            Object val = null;
            try
            {
               val = xmlEventReader.getElementText();
            }
            catch(XMLStreamException xse)
            {
               //Look for embedded xml
               XMLEvent embeddedOrText = xmlEventReader.peek();
               if(embeddedOrText.getEventType() == XMLStreamConstants.START_ELEMENT)
               { 
                  val = embeddedXMLParsing(xmlEventReader); 
               }   
            } 
            options.put(attribute.getValue(), val );
         }
         else break; 
      }
      return options;
   }
   
   private Object embeddedXMLParsing(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      Object retVal = null;
      
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      xmlEventReader = xmlInputFactory.createFilteredReader(xmlEventReader, new EventFilter()
      {
          public boolean accept(XMLEvent xmlEvent)
          {
              return xmlEvent.isStartElement() ;
          }
      });
      while (xmlEventReader.hasNext())
      {
          XMLEvent xmlEvent = xmlEventReader.peek();
          int eventType = xmlEvent.getEventType();
          switch (eventType)
          {
             case XMLStreamConstants.START_ELEMENT:  
                StartElement xmlStartElement = (StartElement) xmlEvent;
                String nsURI = xmlStartElement.getName().getNamespaceURI();
                ParserNamespaceSupport parser = getSupportingParser(nsURI);
                if(parser == null)
                   throw StaxParserUtil.unexpectedNS(nsURI, xmlEvent);
                return parser.parse(xmlEventReader);
          }
      } 
      return retVal;  
   } 
   
   /**
    * Get the parser that supports the particular namespace
    * @param nsURI
    * @return
    */
   private ParserNamespaceSupport getSupportingParser(String nsURI)
   {
      return parsers.get(nsURI);
   }
   
   /**
    * Parse the module-option element
    * @param reader
    * @return
    * @throws XMLStreamException
    */
   public Map<String, Object> parse(XMLStreamReader reader) throws XMLStreamException
   {
      Map<String, Object> options = new HashMap<String, Object>();

      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         if (element.equals(Element.MODULE_OPTION))
         {
            final int count = reader.getAttributeCount();
            if (count == 0)
            {
               throw StaxParserUtil.missingRequired(reader, Collections.singleton(org.jboss.security.config.Attribute.NAME));
            }
            String name = null;
            Object optionValue = null;
            for (int i = 0; i < count; i++)
            {
               final String value = reader.getAttributeValue(i);
               final org.jboss.security.config.Attribute attribute = org.jboss.security.config.Attribute.forName(reader
                     .getAttributeLocalName(i));
               switch (attribute)
               {
                  case NAME : {
                     name = value;
                     break;
                  }
                  case VALUE : {
                     optionValue = value;
                     break;
                  }
                  default :
                     throw StaxParserUtil.unexpectedAttribute(reader, i);
               }
            }
            if (optionValue == null)
            {
               optionValue = reader.getElementText();
            }
            else
               StaxParserUtil.requireNoContent(reader);
            options.put(name, optionValue);
         }
         else
            throw StaxParserUtil.unexpectedElement(reader);
      }
      return options;
   }
}