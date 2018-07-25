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
import org.opensaml.samlext.saml2mdui.Logo;
import org.opensaml.xml.LangBearing;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * Concrete implementation of {@link org.opensaml.samlext.saml2mdui.Logo}.
 * @author rod widdowson
 */
public class LogoImpl extends AbstractSAMLObject implements Logo {
    
    /** Logo URL. */
    private String url;
    
    /** Language. */
    private String lang;

    /** X-Dimension of the logo. */
    private Integer width;

    /** Y-Dimension of the logo. */
    private Integer height;

    /**
     * Constructor.
     * 
     * @param namespaceURI namespaceURI
     * @param elementLocalName elementLocalName
     * @param namespacePrefix namespacePrefix
     */
    protected LogoImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }


    /** {@inheritDoc} */
    public Integer getHeight() {
        return height;
    }

    /** {@inheritDoc} */
    public void setHeight(Integer newHeight) {
         height = prepareForAssignment(height, newHeight);
    }

    /** {@inheritDoc} */
    public Integer getWidth() {
        return width;
    }

    /** {@inheritDoc} */
    public void setWidth(Integer newWidth) {
        width = prepareForAssignment(width, newWidth);
    }

    /** {@inheritDoc} */
    public String getURL() {
        return url;
    }

    /** {@inheritDoc} */
    public void setURL(String newURL) {
       url = prepareForAssignment(url, newURL);
    }

    /** {@inheritDoc} */
    public String getXMLLang() {
        return lang;
    }

    /** {@inheritDoc} */
    public void setXMLLang(String newLang) {
        boolean hasValue = newLang != null && !DatatypeHelper.isEmpty(newLang);
        lang = prepareForAssignment(lang, newLang);
        manageQualifiedAttributeNamespace(LangBearing.XML_LANG_ATTR_NAME, hasValue);
    }


    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = url.hashCode();
        hash = hash * 31 + lang.hashCode();
        hash = hash * 31 + height;
        hash = hash * 31 + width;
        return hash;
    }
}
