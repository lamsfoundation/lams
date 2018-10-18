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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

/**
 * Interface to indicate the parser
 * supports a particular namespace
 * @author Anil.Saldhana@redhat.com
 * @since Feb 5, 2010
 */
public interface ParserNamespaceSupport
{
   /**
    * Parse the event stream
    * @param xmlEventReader
    * @return
    * @throws XMLStreamException
    */
   Object parse(XMLEventReader xmlEventReader)  throws XMLStreamException;
   
   /**
    * Returns whether the parser supports parsing a particular namespace
    * @param namespaceURI
    * @return
    */
   boolean supports(String namespaceURI); 
}