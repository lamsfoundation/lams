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
import org.opensaml.xml.encryption.CarriedKeyName;
import org.opensaml.xml.encryption.EncryptedKey;
import org.opensaml.xml.encryption.ReferenceList;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.xml.encryption.EncryptedKey} objects.
 */
public class EncryptedKeyUnmarshaller extends EncryptedTypeUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        EncryptedKey ek = (EncryptedKey) xmlObject;

        if (attribute.getLocalName().equals(EncryptedKey.RECIPIENT_ATTRIB_NAME)) {
            ek.setRecipient(attribute.getValue());
        } else {
            super.processAttribute(xmlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {
        EncryptedKey ek = (EncryptedKey) parentXMLObject;

        if (childXMLObject instanceof ReferenceList) {
            ek.setReferenceList((ReferenceList) childXMLObject);
        } else if (childXMLObject instanceof CarriedKeyName) {
            ek.setCarriedKeyName((CarriedKeyName) childXMLObject);
        } else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}
