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
import org.opensaml.saml2.core.Advice;

/**
 * Builder for {@link org.opensaml.saml2.core.impl.AdviceImpl} objects.
 */
public class AdviceBuilder extends AbstractSAMLObjectBuilder<Advice> {

    /** Constructor. */
    public AdviceBuilder() {
    }

    /** {@inheritDoc} */
    public Advice buildObject() {
        return buildObject(SAMLConstants.SAML20_NS, Advice.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
    }

    /** {@inheritDoc} */
    public Advice buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new AdviceImpl(namespaceURI, localName, namespacePrefix);
    }
}