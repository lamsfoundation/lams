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

import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.NameIDPolicy;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSBooleanValue;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.NameIDPolicy}.
 */
public class NameIDPolicyImpl extends AbstractSAMLObject implements NameIDPolicy {

    /** NameID Format URI. */
    private String format;

    /** NameID Format URI. */
    private String spNameQualifier;

    /** NameID Format URI. */
    private XSBooleanValue allowCreate;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected NameIDPolicyImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getFormat() {
        return format;
    }

    /** {@inheritDoc} */
    public void setFormat(String newFormat) {
        format = prepareForAssignment(format, newFormat);

    }

    /** {@inheritDoc} */
    public String getSPNameQualifier() {
        return spNameQualifier;
    }

    /** {@inheritDoc} */
    public void setSPNameQualifier(String newSPNameQualifier) {
        spNameQualifier = prepareForAssignment(spNameQualifier, newSPNameQualifier);

    }
    
    /** {@inheritDoc} */
    public Boolean getAllowCreate(){
        if(allowCreate != null){
            return allowCreate.getValue();
        }
        
        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue getAllowCreateXSBoolean() {
        return allowCreate;
    }

    /** {@inheritDoc} */
    public void setAllowCreate(Boolean newAllowCreate){
        if(newAllowCreate != null){
            allowCreate = prepareForAssignment(allowCreate, new XSBooleanValue(newAllowCreate, false));
        }else{
            allowCreate = prepareForAssignment(allowCreate, null);
        }
    }
    
    /** {@inheritDoc} */
    public void setAllowCreate(XSBooleanValue newAllowCreate) {
        allowCreate = prepareForAssignment(allowCreate, newAllowCreate);

    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        // no children
        return null;
    }
}