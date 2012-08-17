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

import java.util.List;

import org.opensaml.saml2.core.impl.AttributeImpl;
import org.opensaml.saml2.metadata.RequestedAttribute;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.RequestedAttribute}
 */
public class RequestedAttributeImpl extends AttributeImpl implements RequestedAttribute {

    /** isRequired attribute */
    private XSBooleanValue isRequired;

    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected RequestedAttributeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }
    
    /** {@inheritDoc} */
    public Boolean isRequired(){
        if(isRequired != null){
            return isRequired.getValue();
        }
        
        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isRequiredXSBoolean() {
        return isRequired;
    }
    
    /** {@inheritDoc} */
    public void setIsRequired(Boolean newIsRequired){
        if(newIsRequired != null){
            isRequired = prepareForAssignment(isRequired, new XSBooleanValue(newIsRequired, false));
        }else{
            isRequired = prepareForAssignment(isRequired, null);
        }
    }

    /** {@inheritDoc} */
    public void setIsRequired(XSBooleanValue newIsRequired) {
        isRequired = prepareForAssignment(isRequired, newIsRequired);

    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }
}