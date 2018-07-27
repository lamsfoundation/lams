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

import java.util.ArrayList;
import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.samlext.saml2mdui.Description;
import org.opensaml.samlext.saml2mdui.DisplayName;
import org.opensaml.samlext.saml2mdui.InformationURL;
import org.opensaml.samlext.saml2mdui.Keywords;
import org.opensaml.samlext.saml2mdui.Logo;
import org.opensaml.samlext.saml2mdui.PrivacyStatementURL;
import org.opensaml.samlext.saml2mdui.UIInfo;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.samlext.saml2mdui.UIInfo}.
 * @author Rod Widdowson
 */
public class UIInfoImpl extends AbstractSAMLObject implements UIInfo {
    
    /** localized descriptions. */
    private final XMLObjectChildrenList<Description> descriptions;
    
    /** localized displayNames. */
    private final XMLObjectChildrenList<DisplayName> displayNames;
        
    /** localized displayNames. */
    private final XMLObjectChildrenList<Keywords> keywords;
        
    /** (posibly) localized Logos. */
    private final XMLObjectChildrenList<Logo> logos;
 
    /** localized Informational URLs. */
    private final XMLObjectChildrenList<InformationURL> urls;
 
    /** localized PrivacyStatementURLs. */
    private final XMLObjectChildrenList<PrivacyStatementURL> privacyStatementURLs;
 
    /**
     * Constructor.
     * @param namespaceURI namespaceURI
     * @param elementLocalName elementLocalName
     * @param namespacePrefix namespacePrefix
     */
    protected UIInfoImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        
        descriptions = new XMLObjectChildrenList<Description>(this);
        displayNames = new XMLObjectChildrenList<DisplayName>(this);
        logos = new XMLObjectChildrenList<Logo>(this);
        urls = new XMLObjectChildrenList<InformationURL>(this);
        keywords = new XMLObjectChildrenList<Keywords>(this);
        privacyStatementURLs = new XMLObjectChildrenList<PrivacyStatementURL>(this);
    }

    
    /** {@inheritDoc} */
    public List<Description> getDescriptions() {
        return descriptions;
    }

    /** {@inheritDoc} */
    public List<DisplayName> getDisplayNames() {
        return displayNames;
    }

    /** {@inheritDoc} */
    public List<Keywords> getKeywords() {
        return keywords;
    }

    /** {@inheritDoc} */
    public List<InformationURL> getInformationURLs() {
        return urls;
    }

    /** {@inheritDoc} */
    public List<Logo> getLogos() {
        return logos;
    }

    /** {@inheritDoc} */
    public List<PrivacyStatementURL> getPrivacyStatementURLs() {
        return privacyStatementURLs;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        children.addAll(displayNames);
        children.addAll(descriptions);
        children.addAll(keywords);
        children.addAll(urls);
        children.addAll(logos);
        children.addAll(privacyStatementURLs);
        return children;
    }

}
