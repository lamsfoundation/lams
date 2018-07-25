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

package org.opensaml.samlext.saml2mdui.impl;

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.samlext.saml2mdui.DiscoHints;
import org.opensaml.samlext.saml2mdui.IPHint;

/**
 * Builder of {@link org.opensaml.samlext.saml2mdui.IPHint} objects.
 */
public class IPHintBuilder extends AbstractSAMLObjectBuilder<IPHint> {

    /**
     * Constructor.
     */
    public IPHintBuilder() {

    }

    /** {@inheritDoc} */
    public IPHint buildObject() {
        return buildObject(DiscoHints.MDUI_NS, 
                           IPHint.DEFAULT_ELEMENT_LOCAL_NAME, 
                           DiscoHints.MDUI_PREFIX);
    }

    /** {@inheritDoc} */
    public IPHint buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new IPHintImpl(namespaceURI, localName, namespacePrefix);
    }
}