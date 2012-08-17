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

package org.opensaml.saml2.metadata.impl;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.metadata.EncryptionMethod;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.signature.KeyInfo;
import org.w3c.dom.Attr;

/**
 * A thread-safe unmarshaller for {@link org.opensaml.saml2.metadata.KeyDescriptor}s.
 */
public class KeyDescriptorUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        KeyDescriptor keyDescriptor = (KeyDescriptor) parentSAMLObject;

        if (childSAMLObject instanceof KeyInfo) {
            keyDescriptor.setKeyInfo((KeyInfo) childSAMLObject);
        } else if (childSAMLObject instanceof EncryptionMethod) {
            keyDescriptor.getEncryptionMethods().add((EncryptionMethod) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        KeyDescriptor keyDescriptor = (KeyDescriptor) samlObject;

        if (attribute.getName().equals(KeyDescriptor.USE_ATTRIB_NAME)) {
            try {
                UsageType usageType = UsageType.valueOf(UsageType.class, attribute.getValue().toUpperCase());
                // Only allow the enum values specified in the schema.
                if (usageType != UsageType.SIGNING && usageType != UsageType.ENCRYPTION) {
                    throw new UnmarshallingException("Invalid key usage type: " + attribute.getValue());
                }
                keyDescriptor.setUse(usageType);
            } catch (IllegalArgumentException e) {
                throw new UnmarshallingException("Invalid key usage type: " + attribute.getValue());
            }
        }

        super.processAttribute(samlObject, attribute);
    }
}