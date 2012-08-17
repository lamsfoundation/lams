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

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.metadata.AssertionIDRequestService;
import org.opensaml.saml2.metadata.AttributeProfile;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.NameIDMappingService;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSBooleanValue;
import org.w3c.dom.Attr;

/**
 * A thread safe Unmarshaller for {@link org.opensaml.saml2.metadata.SSODescriptor} objects.
 */
public class IDPSSODescriptorUnmarshaller extends SSODescriptorUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        IDPSSODescriptor descriptor = (IDPSSODescriptor) parentObject;

        if (childObject instanceof SingleSignOnService) {
            descriptor.getSingleSignOnServices().add((SingleSignOnService) childObject);
        } else if (childObject instanceof NameIDMappingService) {
            descriptor.getNameIDMappingServices().add((NameIDMappingService) childObject);
        } else if (childObject instanceof AssertionIDRequestService) {
            descriptor.getAssertionIDRequestServices().add((AssertionIDRequestService) childObject);
        } else if (childObject instanceof AttributeProfile) {
            descriptor.getAttributeProfiles().add((AttributeProfile) childObject);
        } else if (childObject instanceof Attribute) {
            descriptor.getAttributes().add((Attribute) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        IDPSSODescriptor descriptor = (IDPSSODescriptor) samlObject;

        if (attribute.getLocalName().equals(IDPSSODescriptor.WANT_AUTHN_REQ_SIGNED_ATTRIB_NAME)) {
            descriptor.setWantAuthnRequestsSigned(XSBooleanValue.valueOf(attribute.getValue()));
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }
}