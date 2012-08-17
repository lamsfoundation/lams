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

package org.opensaml.samlext.saml2mdquery.impl;

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.samlext.saml2mdquery.AttributeQueryDescriptorType;

/**
 * Builder of {@link AttributeQueryDescriptorTypeImpl} objects.
 */
public class AttributeQueryDescriptorTypeBuilder extends AbstractSAMLObjectBuilder<AttributeQueryDescriptorType> {

    /** {@inheritDoc} */
    public AttributeQueryDescriptorType buildObject() {
        return buildObject(SAMLConstants.SAML20MDQUERY_NS, AttributeQueryDescriptorType.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20MDQUERY_PREFIX, AttributeQueryDescriptorType.TYPE_NAME);
    }

    /** {@inheritDoc} */
    public AttributeQueryDescriptorType buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new AttributeQueryDescriptorTypeImpl(namespaceURI, localName, namespacePrefix);
    }
}