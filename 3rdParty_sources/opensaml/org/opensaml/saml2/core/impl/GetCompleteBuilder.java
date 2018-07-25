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
import org.opensaml.saml2.core.GetComplete;

/**
 * Builder of {@link org.opensaml.saml2.core.impl.GetCompleteImpl}.
 */
public class GetCompleteBuilder extends AbstractSAMLObjectBuilder<GetComplete> {

    /**
     * Constructor.
     */
    public GetCompleteBuilder() {
    }

    /** {@inheritDoc} */
    public GetComplete buildObject() {
        return buildObject(SAMLConstants.SAML20P_NS, GetComplete.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20P_PREFIX);
    }

    /** {@inheritDoc} */
    public GetComplete buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new GetCompleteImpl(namespaceURI, localName, namespacePrefix);
    }
}