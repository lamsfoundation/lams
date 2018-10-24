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

package org.opensaml.xml.encryption.impl;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.CipherData;
import org.opensaml.xml.encryption.EncryptedType;
import org.opensaml.xml.encryption.EncryptionMethod;
import org.opensaml.xml.encryption.EncryptionProperties;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.KeyInfo;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.xml.encryption.EncryptedType} objects.
 */
public abstract class EncryptedTypeUnmarshaller extends AbstractXMLEncryptionUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        EncryptedType et = (EncryptedType) parentXMLObject;

        if (childXMLObject instanceof EncryptionMethod) {
            et.setEncryptionMethod((EncryptionMethod) childXMLObject);
        } else if (childXMLObject instanceof KeyInfo) {
            et.setKeyInfo((KeyInfo) childXMLObject);
        } else if (childXMLObject instanceof CipherData) {
            et.setCipherData((CipherData) childXMLObject);
        } else if (childXMLObject instanceof EncryptionProperties) {
            et.setEncryptionProperties((EncryptionProperties) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }

    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        EncryptedType et = (EncryptedType) xmlObject;

        if (attribute.getLocalName().equals(EncryptedType.ID_ATTRIB_NAME)) {
            et.setID(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else if (attribute.getLocalName().equals(EncryptedType.TYPE_ATTRIB_NAME)) {
            et.setType(attribute.getValue());
        } else if (attribute.getLocalName().equals(EncryptedType.MIMETYPE_ATTRIB_NAME)) {
            et.setMimeType(attribute.getValue());
        } else if (attribute.getLocalName().equals(EncryptedType.ENCODING_ATTRIB_NAME)) {
            et.setEncoding(attribute.getValue());
        } else {
            super.processAttribute(xmlObject, attribute);
        }

    }

}
