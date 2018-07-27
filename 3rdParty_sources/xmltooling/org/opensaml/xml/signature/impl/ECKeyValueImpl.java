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

package org.opensaml.xml.signature.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.ECKeyValue;
import org.opensaml.xml.signature.NamedCurve;
import org.opensaml.xml.signature.PublicKey;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link ECKeyValue}.
 */
public class ECKeyValueImpl extends AbstractValidatingXMLObject implements ECKeyValue {
    
    /** Id attribute value. */
    private String id;
    
    /** ECParameters child element value. */
    private XMLObject ecParams;
    
    /** NamedCurve child element value. */
    private NamedCurve namedCurve;

    /** PublicKey child element value. */
    private PublicKey publicKey;

    /**
     * Constructor.
     *
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected ECKeyValueImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getID() {
        return id;
    }

    /** {@inheritDoc} */
    public void setID(String newID) {
        String oldID = id;
        id = prepareForAssignment(id, newID);
        registerOwnID(oldID, id);
    }
    
    /** {@inheritDoc} */
    public XMLObject getECParameters() {
        return ecParams;
    }

    /** {@inheritDoc} */
    public void setECParameters(XMLObject newParams) {
        ecParams = prepareForAssignment(ecParams, newParams);
    }
    
    /** {@inheritDoc} */
    public NamedCurve getNamedCurve() {
        return namedCurve;
    }

    /** {@inheritDoc} */
    public void setNamedCurve(NamedCurve newCurve) {
        namedCurve = prepareForAssignment(namedCurve, newCurve);
    }

    /** {@inheritDoc} */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /** {@inheritDoc} */
    public void setPublicKey(PublicKey newKey) {
        publicKey = prepareForAssignment(publicKey, newKey);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        if (ecParams != null) {
            children.add(ecParams);
        }
        if (namedCurve != null) {
            children.add(namedCurve);
        }
        if (publicKey != null) {
            children.add(publicKey);
        }
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}