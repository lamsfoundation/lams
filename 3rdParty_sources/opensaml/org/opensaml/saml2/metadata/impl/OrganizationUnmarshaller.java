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

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.metadata.Organization;
import org.opensaml.saml2.metadata.OrganizationDisplayName;
import org.opensaml.saml2.metadata.OrganizationName;
import org.opensaml.saml2.metadata.OrganizationURL;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.metadata.Organization} objects.
 */
public class OrganizationUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        Organization org = (Organization) parentSAMLObject;

        if (childSAMLObject instanceof Extensions) {
            org.setExtensions((Extensions) childSAMLObject);
        } else if (childSAMLObject instanceof OrganizationName) {
            org.getOrganizationNames().add((OrganizationName) childSAMLObject);
        } else if (childSAMLObject instanceof OrganizationDisplayName) {
            org.getDisplayNames().add((OrganizationDisplayName) childSAMLObject);
        } else if (childSAMLObject instanceof OrganizationURL) {
            org.getURLs().add((OrganizationURL) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        Organization org = (Organization) samlObject;

        QName attribQName = XMLHelper.getNodeQName(attribute);
        if (attribute.isId()) {
            org.getUnknownAttributes().registerID(attribQName);
        }
        org.getUnknownAttributes().put(attribQName, attribute.getValue());
    }
}