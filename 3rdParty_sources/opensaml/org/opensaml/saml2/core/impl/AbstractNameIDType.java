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

package org.opensaml.saml2.core.impl;

import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.xml.XMLObject;

/**
 * Abstract implementation of {@link org.opensaml.saml2.core.NameIDType}.
 */
public class AbstractNameIDType extends AbstractSAMLObject implements NameIDType {

    /** Name of the Name ID. */
    private String name;
    
    /** Name Qualifier of the Name ID. */
    private String nameQualifier;

    /** SP Name Qualifier of the Name ID. */
    private String spNameQualifier;

    /** Format of the Name ID. */
    private String format;

    /** SP ProvidedID of the NameID. */
    private String spProvidedID;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AbstractNameIDType(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getValue() {
        return name;
    }
    
    /** {@inheritDoc} */
    public void setValue(String newName) {
        this.name = prepareForAssignment(this.name, newName);
    }
    
    /** {@inheritDoc} */
    public String getNameQualifier() {
        return nameQualifier;
    }

    /** {@inheritDoc} */
    public void setNameQualifier(String newNameQualifier) {
        this.nameQualifier = prepareForAssignment(this.nameQualifier, newNameQualifier);
    }

    /** {@inheritDoc} */
    public String getSPNameQualifier() {
        return spNameQualifier;
    }

    /** {@inheritDoc} */
    public void setSPNameQualifier(String newSPNameQualifier) {
        this.spNameQualifier = prepareForAssignment(this.spNameQualifier, newSPNameQualifier);
    }

    /** {@inheritDoc} */
    public String getFormat() {
        return format;
    }

    /** {@inheritDoc} */
    public void setFormat(String newFormat) {
        this.format = prepareForAssignment(this.format, newFormat);
    }

    /** {@inheritDoc} */
    public String getSPProvidedID() {
        return spProvidedID;
    }

    /** {@inheritDoc} */
    public void setSPProvidedID(String newSPProvidedID) {
        this.spProvidedID = prepareForAssignment(this.spProvidedID, newSPProvidedID);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }
}