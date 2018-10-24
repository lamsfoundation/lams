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

import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.credential.UsageType;
import org.w3c.dom.Element;

/**
 * A thread-safe marshaller for {@link org.opensaml.saml2.metadata.KeyDescriptor}s.
 */
public class KeyDescriptorMarshaller extends AbstractSAMLObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        KeyDescriptor keyDescriptor = (KeyDescriptor) xmlObject;

        if (keyDescriptor.getUse() != null) {
            UsageType use = keyDescriptor.getUse();
            // UsageType enum contains more values than are allowed by SAML 2 schema
            if (use.equals(UsageType.SIGNING) || use.equals(UsageType.ENCRYPTION)) {
                domElement.setAttribute(KeyDescriptor.USE_ATTRIB_NAME, use.toString().toLowerCase());
            } else if (use.equals(UsageType.UNSPECIFIED)) {
                // emit nothing for unspecified - this is semantically equivalent to non-existent attribute
            } else {
                // Just in case values are unknowingly added to UsageType in the future...
                throw new MarshallingException("KeyDescriptor had illegal value for use attribute: " + use.toString());
            }
        }
    }
}