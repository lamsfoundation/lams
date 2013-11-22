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

package org.opensaml.xml.signature.impl;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.ECKeyValue;
import org.opensaml.xml.signature.NamedCurve;
import org.opensaml.xml.signature.PublicKey;
import org.opensaml.xml.util.XMLConstants;
import org.w3c.dom.Attr;


/**
 * A thread-safe Unmarshaller for {@link ECKeyValue} objects.
 */
public class ECKeyValueUnmarshaller extends AbstractXMLSignatureUnmarshaller {

    /** ECParameters element name. */
    public static final QName ECPARAMETERS_ELEMENT_NAME = new QName(XMLConstants.XMLSIG11_NS, "ECParameters");

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        ECKeyValue ec = (ECKeyValue) xmlObject;

        if (attribute.getLocalName().equals(ECKeyValue.ID_ATTRIB_NAME)) {
            ec.setID(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else {
            super.processAttribute(xmlObject, attribute);
        }
    }
    
    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        ECKeyValue keyValue = (ECKeyValue) parentXMLObject;

        if (childXMLObject instanceof NamedCurve) {
            keyValue.setNamedCurve((NamedCurve) childXMLObject);
        } else if (childXMLObject instanceof PublicKey) {
            keyValue.setPublicKey((PublicKey) childXMLObject);
        } else if (childXMLObject.getElementQName().equals(ECPARAMETERS_ELEMENT_NAME)) {
            keyValue.setECParameters(childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}