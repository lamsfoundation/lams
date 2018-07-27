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

package org.opensaml.saml2.metadata.impl;

import org.opensaml.saml2.metadata.IndexedEndpoint;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.IndexedEndpoint}
 */
public abstract class IndexedEndpointImpl extends EndpointImpl implements IndexedEndpoint {

    /** Index of this endpoint */
    private Integer index;

    /** isDefault attribute */
    private XSBooleanValue isDefault;
    
    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected IndexedEndpointImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public Integer getIndex() {
        return index;
    }

    /** {@inheritDoc} */
    public void setIndex(Integer index) {
        this.index = prepareForAssignment(this.index, index);
    }
    
    /** {@inheritDoc} */
    public Boolean isDefault() {
        if (isDefault == null) {
            return Boolean.FALSE;
        }
        return isDefault.getValue();
    }

    /** {@inheritDoc} */
    public XSBooleanValue isDefaultXSBoolean() {
        return isDefault;
    }
    
    /** {@inheritDoc} */
    public void setIsDefault(Boolean newIsDefault){
        if(newIsDefault != null){
            isDefault = prepareForAssignment(isDefault, new XSBooleanValue(newIsDefault, false));
        }else{
            isDefault = prepareForAssignment(isDefault, null);
        }
    }

    /** {@inheritDoc} */
    public void setIsDefault(XSBooleanValue isDefault) {
        this.isDefault = prepareForAssignment(this.isDefault, isDefault);
    }
}