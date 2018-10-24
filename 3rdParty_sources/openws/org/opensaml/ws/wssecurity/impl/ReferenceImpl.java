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

package org.opensaml.ws.wssecurity.impl;

import org.opensaml.ws.wssecurity.Reference;
import org.opensaml.xml.util.AttributeMap;

/**
 * ReferenceImpl.
 * 
 */
public class ReferenceImpl extends AbstractWSSecurityObject implements Reference {
    
    /** wsse:Reference/@URI attribute. */
    private String uri;

    /** wsse:Reference/@ValueType attribute. */
    private String valueType;
    
    /** Wildcard attributes. */
    private AttributeMap unknownAttributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI namespace of the element
     * @param elementLocalName name of the element
     * @param namespacePrefix namespace prefix of the element
     */
    public ReferenceImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
    }

    /** {@inheritDoc} */
    public String getURI() {
        return uri;
    }

    /** {@inheritDoc} */
    public void setURI(String newURI) {
        uri = prepareForAssignment(uri, newURI);
    }

    /** {@inheritDoc} */
    public String getValueType() {
        return valueType;
    }

    /** {@inheritDoc} */
    public void setValueType(String newValueType) {
        valueType = prepareForAssignment(valueType, newValueType);
    }

    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

}
