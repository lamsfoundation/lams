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

package org.opensaml.saml1.core.impl;

import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml1.core.NameIdentifier;
import org.opensaml.xml.XMLObject;

/**
 * Complete implementation of {@link org.opensaml.saml1.core.impl.NameIdentifierImpl}
 */
public class NameIdentifierImpl extends AbstractSAMLObject implements NameIdentifier {

    /** Contents of the NameQualifierAttribute */
    String nameQualifier;

    /** Contents of the Format */
    String format;

    /** Contents of the elemen body */
    String nameIdentifier;

    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected NameIdentifierImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getNameQualifier() {
        return nameQualifier;
    }

    /** {@inheritDoc} */
    public String getFormat() {
        return this.format;
    }

    /** {@inheritDoc} */
    public String getNameIdentifier() {
        return nameIdentifier;
    }

    /** {@inheritDoc} */
    public void setNameQualifier(String nameQualifier) {
        this.nameQualifier = prepareForAssignment(this.nameQualifier, nameQualifier);
    }

    public void setFormat(String format) {
        this.format = prepareForAssignment(this.format, format);
    }

    public void setNameIdentifier(String nameIdentifier) {
        this.nameIdentifier = prepareForAssignment(this.nameIdentifier, nameIdentifier);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }
}