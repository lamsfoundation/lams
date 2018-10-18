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

import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.jboss.security.auth.spi.Users;
import org.jboss.security.auth.spi.Users.User;


/**
 * Parse the Users configuration embeddable within
 * {@code XMLLoginModule} module option
 * @author Anil.Saldhana@redhat.com
 * @since Jan 27, 2010
 */
public class UsersConfigParser implements ParserNamespaceSupport
{ 
   private static final String NAMESPACE_URI ="urn:jboss:user-roles"; 
   
   /**
    * Parse the embedded xml in the module option representing
    * the {@code Users} object
    * @param xmlEventReader
    * @return
    * @throws XMLStreamException
    */
   @SuppressWarnings("unchecked")
   public Users parse(XMLEventReader xmlEventReader) throws XMLStreamException
   {
      Users users = new Users();
      XMLEvent xmlEvent = null;
      while(xmlEventReader.hasNext())
      {   
         xmlEvent = xmlEventReader.peek(); 
         if(xmlEvent instanceof StartElement)
         {
            StartElement se = (StartElement) xmlEvent;
            if("module-option".equals(StaxParserUtil.getStartElementName(se)))
               return users;
         }
         if(xmlEvent instanceof EndElement)
         { 
            xmlEvent = xmlEventReader.nextEvent(); 
            continue;
         }
         
         xmlEvent = xmlEventReader.nextEvent();
         User user = new Users.User();
         
         StartElement peekedStartElement = (StartElement) xmlEvent;
         Iterator<Attribute> attribs = peekedStartElement.getAttributes();
         while(attribs.hasNext())
         {
            Attribute attrib  = attribs.next();
            if("name".equals(attrib.getName().getLocalPart()))
            {
               user.setName(attrib.getValue()); 
            }
            else if("password".equals(attrib.getName().getLocalPart()))
            {
               user.setPassword(attrib.getValue()); 
            }
            else if("encoding".equals(attrib.getName().getLocalPart()))
            {
               user.setEncoding(attrib.getValue()); 
            }
         }
         //Get the roles
         xmlEvent = xmlEventReader.peek();
         while(xmlEvent != null && xmlEvent.getEventType() == XMLStreamConstants.START_ELEMENT)
         {
            StartElement roleElement = (StartElement) xmlEvent;
            if("role".equals(roleElement.getName().getLocalPart()))
            {
               xmlEvent = xmlEventReader.nextEvent();
               Iterator<Attribute> roleAttribs = roleElement.getAttributes();

               String roleName = null;
               String groupName = "Roles";
               
               while(roleAttribs.hasNext())
               {
                  Attribute roleAttribute = roleAttribs.next();
                  String attributeValue = StaxParserUtil.getAttributeValue(roleAttribute);
                  
                  if("name".equals(roleAttribute.getName().getLocalPart()))
                  {
                    roleName = attributeValue;  
                  }
                  else if("group".equals(roleAttribute.getName().getLocalPart()))
                  {
                     groupName = attributeValue;  
                  } 
               }
               if(roleName != null)
                  user.addRole(roleName, groupName);
            } 
            else break;
            xmlEvent = xmlEventReader.peek();
         } 
         
         users.addUser(user);
      }
      return users; 
   } 
    
   /**
    * @see {@code ParserNamespaceSupport#supports(String)}
    */
   public boolean supports(String namespaceURI)
   {
      return NAMESPACE_URI.equals(namespaceURI);
   }
}