/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;

/**
 * A builder for XMLObjects.
 * 
 * @param <XMLObjectType> the XMLObject type that this builder produces
 */
public interface XMLObjectBuilder<XMLObjectType extends XMLObject> {
    
    /**
     * Creates an XMLObject with a given fully qualified name.
     * 
     * @param objectName fully qualified name of the object
     * 
     * @return the constructed XMLObject
     */
    public XMLObjectType buildObject(QName objectName);
    
    /**
     * Creates an XMLObject with a given fully qualified name and schema type.
     * 
     * @param objectName fully qualified name of the object
     * @param schemaType the schema type of the Element represented by this XMLObject
     * 
     * @return the constructed XMLObject
     */
    public XMLObjectType buildObject(QName objectName, QName schemaType);

    /**
     * Creates an XMLObject with a given fully qualified name.
     * 
     * @param namespaceURI the URI of the namespace the Element represented by this XMLObject will be in
     * @param localName the local name of the Element represented by this XMLObject
     * @param namespacePrefix the namespace prefix of the Element represented by this XMLObject
     * 
     * @return the constructed XMLObject
     */
    public XMLObjectType buildObject(String namespaceURI, String localName, String namespacePrefix);

    /**
     * Creates an XMLObject with a given fully qualified name.
     * 
     * @param namespaceURI the URI of the namespace the Element represented by this XMLObject will be in
     * @param localName the local name of the Element represented by this XMLObject
     * @param namespacePrefix the namespace prefix of the Element represented by this XMLObject
     * @param schemaType the schema type of the Element represented by this XMLObject
     * 
     * @return the constructed XMLObject
     */
    public XMLObjectType buildObject(String namespaceURI, String localName, String namespacePrefix, QName schemaType);

    /**
     * Creates an XMLObject using information from the given DOM element. This method must set the QName for the Element
     * QName within the constructed XMLObject.
     * 
     * This method is used by {@link org.opensaml.xml.io.AbstractXMLObjectUnmarshaller}.
     * 
     * @param element the DOM Element containing information about the object to be built.
     * 
     * @return the constructed XMLObject
     */
    public XMLObjectType buildObject(Element element);
}