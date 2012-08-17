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

import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.saml2.metadata.AdditionalMetadataLocation;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

/**
 * A thread safe marshaller for {@link org.opensaml.saml2.metadata.AdditionalMetadataLocation} objects.
 */
public class AdditionalMetadataLocationMarshaller extends AbstractSAMLObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlElement, Element domElement) throws MarshallingException {
        AdditionalMetadataLocation aml = (AdditionalMetadataLocation) samlElement;

        if (aml.getNamespaceURI() != null) {
            domElement.setAttributeNS(null, AdditionalMetadataLocation.NAMESPACE_ATTRIB_NAME, aml.getNamespaceURI());
        }
    }

    /** {@inheritDoc} */
    protected void marshallElementContent(XMLObject samlObject, Element domElement) throws MarshallingException {
        super.marshallElementContent(samlObject, domElement);

        AdditionalMetadataLocation aml = (AdditionalMetadataLocation) samlObject;
        if (aml.getLocationURI() != null) {
            domElement.appendChild(domElement.getOwnerDocument().createTextNode(aml.getLocationURI()));
        }
    }
}