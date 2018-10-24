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
import org.opensaml.samlext.saml2mdui.Keywords;
import org.opensaml.samlext.saml2mdui.UIInfo;

/**
 * Builder of {@link org.opensaml.samlext.saml2mdui.InformationURL} objects.
 * @author Rod Widdowson
 */
public class KeywordsBuilder extends AbstractSAMLObjectBuilder<Keywords> {
    /**
     * Constructor.
     */
    public KeywordsBuilder() {

    }

    /** {@inheritDoc} */
    public Keywords buildObject() {
        return buildObject(UIInfo.MDUI_NS, 
                           Keywords.DEFAULT_ELEMENT_LOCAL_NAME, 
                           UIInfo.MDUI_PREFIX);
    }

    /** {@inheritDoc} */
    public Keywords buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new KeywordsImpl(namespaceURI, localName, namespacePrefix);
    }
}
