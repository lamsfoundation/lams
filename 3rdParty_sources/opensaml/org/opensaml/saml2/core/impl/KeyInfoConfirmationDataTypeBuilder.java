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

/**
 * 
 */

package org.opensaml.saml2.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.KeyInfoConfirmationDataType;

/**
 * Builder for {@link org.opensaml.saml2.core.impl.KeyInfoConfirmationDataTypeImpl} objects.
 */
public class KeyInfoConfirmationDataTypeBuilder extends AbstractSAMLObjectBuilder<KeyInfoConfirmationDataType> {

    /** Constructor. */
    public KeyInfoConfirmationDataTypeBuilder() {
    }

    /** {@inheritDoc} */
    public KeyInfoConfirmationDataType buildObject() {
        return buildObject(SAMLConstants.SAML20_NS, KeyInfoConfirmationDataType.DEFAULT_ELEMENT_LOCAL_NAME, 
                SAMLConstants.SAML20_PREFIX, KeyInfoConfirmationDataType.TYPE_NAME);
    }

    /** {@inheritDoc} */
    public KeyInfoConfirmationDataType buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new KeyInfoConfirmationDataTypeImpl(namespaceURI, localName, namespacePrefix);
    }
}