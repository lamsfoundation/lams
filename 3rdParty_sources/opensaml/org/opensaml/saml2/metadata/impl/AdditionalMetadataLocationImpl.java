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

package org.opensaml.saml2.metadata.impl;

import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.saml2.metadata.AdditionalMetadataLocation;
import org.opensaml.xml.XMLObject;

/**
 * Concreate implementation of {@link org.opensaml.saml2.metadata.AdditionalMetadataLocation}
 */
public class AdditionalMetadataLocationImpl extends AbstractSAMLObject implements AdditionalMetadataLocation {

    /** The metadata location */
    private String location;

    /** Namespace scope of the root metadata element at the location */
    private String namespace;
    
    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected AdditionalMetadataLocationImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getLocationURI() {
        return location;
    }

    /** {@inheritDoc} */
    public void setLocationURI(String locationURI) {
        location = prepareForAssignment(location, locationURI);
    }

    /** {@inheritDoc} */
    public String getNamespaceURI() {
        return namespace;
    }

    /** {@inheritDoc} */
    public void setNamespaceURI(String namespaceURI) {
        namespace = prepareForAssignment(namespace, namespaceURI);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        // No children for this element
        return null;
    }
}