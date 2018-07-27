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

import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.metadata.LocalizedString;
import org.opensaml.samlext.saml2mdui.LocalizedURI;
import org.opensaml.xml.LangBearing;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * Concrete implementation of {@link org.opensaml.samlext.saml2mdui.LocalizedURI}.
 */
public class LocalizedURIImpl extends AbstractSAMLObject implements LocalizedURI{

    /** Display name. */
    private LocalizedString uri;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespaceURI
     * @param elementLocalName the elementLocalName
     * @param namespacePrefix the namespacePrefix
     */
    protected LocalizedURIImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public LocalizedString getURI() {
        return uri;
    }

    /** {@inheritDoc} */
    public void setURI(LocalizedString newURI) {
        uri = prepareForAssignment(uri, newURI);
        boolean hasXMLLang = false;
        if (uri != null && !DatatypeHelper.isEmpty(uri.getLanguage())) {
            hasXMLLang = true;
        }
        manageQualifiedAttributeNamespace(LangBearing.XML_LANG_ATTR_NAME, hasXMLLang);
    }

    /** {@inheritDoc} */
    public String getXMLLang() {
        return uri.getLanguage();
    }

    /** {@inheritDoc} */
    public void setXMLLang(String newLang) {
        uri.setLanguage(newLang);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }

}