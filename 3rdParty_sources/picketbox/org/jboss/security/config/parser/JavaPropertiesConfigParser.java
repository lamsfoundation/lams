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

import java.util.Properties;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Able to read in Java properties into module options
 * @author Anil.Saldhana@redhat.com
 * @since Feb 5, 2010
 */
public class JavaPropertiesConfigParser implements ParserNamespaceSupport
{
   private static final String NAMESPACE_URI ="urn:jboss:java-properties"; 

   /**
    * @see {@code ParserNamespaceSupport#supports(String)}
    */
   public boolean supports(String namespaceURI)
   {
      return NAMESPACE_URI.equals(namespaceURI);
   }

   /**
    * @see {@code ParserNamespaceSupport#parse(XMLEventReader)}
    */ 
   public Object parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      Properties props = new Properties();
      XMLEvent xmlEvent = null;
      while(xmlEventReader.hasNext())
      {   
         xmlEvent = xmlEventReader.peek(); 
         if(xmlEvent instanceof StartElement)
         {
            StartElement se = (StartElement) xmlEvent;
            if("module-option".equals(StaxParserUtil.getStartElementName(se)))
               return props;
         }
         if(xmlEvent instanceof EndElement)
         { 
            xmlEvent = xmlEventReader.nextEvent(); 
            continue;
         }

         xmlEvent = xmlEventReader.nextEvent(); 
         
         StartElement peekedStartElement = (StartElement) xmlEvent;
         String peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
         
         String key = null, value = null;
         if(peekedStartElementName.contains("property") == false)
            throw StaxParserUtil.unexpectedElement(peekedStartElementName, xmlEvent);
         xmlEvent = xmlEventReader.nextEvent();
         peekedStartElement = (StartElement) xmlEvent;
         peekedStartElementName = StaxParserUtil.getStartElementName(peekedStartElement);
         if("key".equals(peekedStartElementName))
         {
            key = xmlEventReader.getElementText();
            xmlEvent = xmlEventReader.nextEvent();
            value = xmlEventReader.getElementText();
         } else
         {
            throw StaxParserUtil.unexpectedElement(peekedStartElementName, xmlEvent);
         }
         props.put(key, value);

      }
      return props;
   }
}