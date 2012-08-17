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

import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

/**
 * Base implementation for XMLObject builders.
 * 
 * <strong>Note:</strong> This class only works with {@link org.opensaml.xml.AbstractXMLObject}s
 * 
 * @param <XMLObjectType> the XMLObject type that this builder produces
 */
public abstract class AbstractXMLObjectBuilder<XMLObjectType extends XMLObject> implements
        XMLObjectBuilder<XMLObjectType> {

    /** {@inheritDoc} */
    public XMLObjectType buildObject(QName objectName){
        return buildObject(objectName.getNamespaceURI(), objectName.getLocalPart(), objectName.getPrefix());
    }
    
    /** {@inheritDoc} */
    public XMLObjectType buildObject(QName objectName, QName schemaType){
        return buildObject(objectName.getNamespaceURI(), objectName.getLocalPart(), objectName.getPrefix(), schemaType);
    }
    
    /** {@inheritDoc} */
    public abstract XMLObjectType buildObject(String namespaceURI, String localName, String namespacePrefix);

    /** {@inheritDoc} */
    public XMLObjectType buildObject(String namespaceURI, String localName, String namespacePrefix, QName schemaType) {
        XMLObjectType xmlObject;

        xmlObject = buildObject(namespaceURI, localName, namespacePrefix);
        ((AbstractXMLObject) xmlObject).setSchemaType(schemaType);

        return xmlObject;
    }

    /** {@inheritDoc} */
    public XMLObjectType buildObject(Element element) {
        XMLObjectType xmlObject;

        String localName = element.getLocalName();
        String nsURI = element.getNamespaceURI();
        String nsPrefix = element.getPrefix();
        QName schemaType = XMLHelper.getXSIType(element);

        xmlObject = buildObject(nsURI, localName, nsPrefix, schemaType);

        return xmlObject;
    }
}