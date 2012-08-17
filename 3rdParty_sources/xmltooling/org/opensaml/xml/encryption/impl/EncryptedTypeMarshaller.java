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
import org.opensaml.xml.encryption.EncryptedType;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

/**
 * A thread-safe Marshaller for {@link org.opensaml.xml.encryption.EncryptedType} objects.
 */
public abstract class EncryptedTypeMarshaller extends AbstractXMLEncryptionMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        EncryptedType et = (EncryptedType) xmlObject;

        if (et.getID() != null) {
            domElement.setAttributeNS(null, EncryptedType.ID_ATTRIB_NAME, et.getID());
            domElement.setIdAttributeNS(null, EncryptedType.ID_ATTRIB_NAME, true);
        }

        if (et.getType() != null) {
            domElement.setAttributeNS(null, EncryptedType.TYPE_ATTRIB_NAME, et.getType());
        }

        if (et.getMimeType() != null) {
            domElement.setAttributeNS(null, EncryptedType.MIMETYPE_ATTRIB_NAME, et.getMimeType());
        }

        if (et.getEncoding() != null) {
            domElement.setAttributeNS(null, EncryptedType.ENCODING_ATTRIB_NAME, et.getEncoding());
        }

    }

}
