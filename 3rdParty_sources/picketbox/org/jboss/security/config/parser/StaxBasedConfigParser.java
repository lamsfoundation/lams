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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.security.auth.login.Configuration;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.ApplicationPolicyRegistration;
import org.jboss.security.config.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Configuration Parser based on Stax
 * 
 * @author Anil.Saldhana@redhat.com
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @since Jan 22, 2010
 */
public class StaxBasedConfigParser implements XMLStreamConstants
{

   private String schemaFile = "schema/security-config_5_0.xsd";
   
   /**
    * Validate the input file against a schema
    * @param configStream
    * @throws SAXException
    * @throws IOException
    */
   public void schemaValidate(InputStream configStream) throws SAXException, IOException
   {
      Validator validator = schemaValidator();
      Source xmlSource = new StreamSource(configStream);
      validator.validate(xmlSource);
   }
   
   /**
    * Parse the Input stream of configuration
    * @param configStream
    * @throws XMLStreamException
    * @throws IOException 
    * @throws SAXException 
    */
   public void parse(InputStream configStream) throws XMLStreamException, SAXException, IOException
   {
      Configuration config = Configuration.getConfiguration();
      if(config instanceof ApplicationPolicyRegistration == false)
         throw PicketBoxMessages.MESSAGES.invalidType(ApplicationPolicyRegistration.class.getName());

      ApplicationPolicyRegistration appPolicyRegistration = (ApplicationPolicyRegistration) config;
      
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      //XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(xmlSource);
      XMLEventReader xmlEventReader = getXMLEventReader(configStream);
      
      xmlEventReader = xmlInputFactory.createFilteredReader(xmlEventReader, new EventFilter()
      {
         public boolean accept(XMLEvent xmlEvent)
         {
            return xmlEvent.isStartElement() ;
         }
      });
      
      while (xmlEventReader.hasNext())
      {
         XMLEvent xmlEvent = xmlEventReader.nextEvent();
         int eventType = xmlEvent.getEventType();
         switch (eventType)
         {
            case XMLStreamConstants.START_ELEMENT: 
               //We got the policy element. We can go over the attributes if we want
               //But there is no immediate need.
               StartElement policyConfigElement = (StartElement) xmlEvent;
               String elementName = StaxParserUtil.getStartElementName(policyConfigElement);
               if("policy".equals(elementName) == false)
                   throw StaxParserUtil.unexpectedElement(elementName, xmlEvent);

               ApplicationPolicyParser appPolicyParser = new ApplicationPolicyParser(); 
               List<ApplicationPolicy> appPolicies = appPolicyParser.parse(xmlEventReader);
               for(ApplicationPolicy appPolicy: appPolicies)
               {
                  appPolicyRegistration.addApplicationPolicy(appPolicy.getName(), appPolicy); 
               }
         }
      }
   }
   
   public void parse2(InputStream configStream) throws XMLStreamException
   {
      Configuration config = Configuration.getConfiguration();
      if (!(config instanceof ApplicationPolicyRegistration))
      {
         throw PicketBoxMessages.MESSAGES.invalidType(ApplicationPolicyRegistration.class.getName());
      }
      
      ApplicationPolicyRegistration appPolicyRegistration = (ApplicationPolicyRegistration) config;
      XMLStreamReader reader = getXMLStreamReader(configStream);
      while (reader.hasNext() && reader.nextTag() != END_ELEMENT)
      {
         final Element element = Element.forName(reader.getLocalName());
         if (element.equals(Element.POLICY))
         {
            ApplicationPolicyParser appPolicyParser = new ApplicationPolicyParser();
            List<ApplicationPolicy> appPolicies = appPolicyParser.parse(reader);
            for(ApplicationPolicy appPolicy: appPolicies)
            {
               appPolicyRegistration.addApplicationPolicy(appPolicy.getName(), appPolicy); 
            }
         }
         else
            throw StaxParserUtil.unexpectedElement(reader);
         if (reader.isEndElement())
            break;
      }
   }
   
   private Validator schemaValidator()
   {
      try
      {
         ClassLoader tcl = SecurityActions.getContextClassLoader();
         URL schemaURL = tcl.getResource(schemaFile);
         if(schemaURL == null)
            throw PicketBoxMessages.MESSAGES.unableToFindSchema(schemaFile);

         SchemaFactory schemaFactory = SchemaFactory.newInstance( "http://www.w3.org/2001/XMLSchema" );
         Schema schemaGrammar = schemaFactory.newSchema( schemaURL );
         Validator schemaValidator = schemaGrammar.newValidator();
         schemaValidator.setErrorHandler( new ErrorHandler()
         {

            public void error(SAXParseException ex) throws SAXException
            {
               logException(ex);
            }

            public void fatalError(SAXParseException ex) throws SAXException
            {
               logException(ex);  
            }

            public void warning(SAXParseException ex) throws SAXException
            {
               logException(ex);  
            }
            
            private void logException(SAXParseException sax)
            {
               StringBuilder builder = new StringBuilder();
               
               if(PicketBoxLogger.LOGGER.isTraceEnabled())
               {
                  builder.append("[").append(sax.getLineNumber()).append(",").append(sax.getColumnNumber()).append("]");
                  builder.append(":").append(sax.getLocalizedMessage());
                  PicketBoxLogger.LOGGER.trace(builder.toString());
               }  
            }
         }); 
         return schemaValidator;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   private XMLEventReader getXMLEventReader(InputStream is) 
   {
      XMLInputFactory xmlInputFactory = null;
      XMLEventReader xmlEventReader = null;
      try 
      {
        xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
 
        xmlEventReader = xmlInputFactory.createXMLEventReader(is);
      } 
      catch (Exception ex) 
      {
        throw new RuntimeException(ex);
      }
      return xmlEventReader;
    }
   
   private XMLStreamReader getXMLStreamReader(InputStream is) 
   {
      XMLInputFactory xmlInputFactory = null;
      XMLStreamReader xmlStreamReader = null;
      try 
      {
        xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
 
        xmlStreamReader = xmlInputFactory.createXMLStreamReader(is);
      } 
      catch (Exception ex) 
      {
        throw new RuntimeException(ex);
      }
      return xmlStreamReader;
    }
}