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

import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.NameIDPolicy;
import org.opensaml.saml2.core.RequestedAuthnContext;
import org.opensaml.saml2.core.Scoping;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSBooleanValue;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.AuthnRequest} objects.
 */
public class AuthnRequestUnmarshaller extends RequestAbstractTypeUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        AuthnRequest req = (AuthnRequest) samlObject;

        if (attribute.getLocalName().equals(AuthnRequest.FORCE_AUTHN_ATTRIB_NAME)) {
            req.setForceAuthn(XSBooleanValue.valueOf(attribute.getValue()));
        } else if (attribute.getLocalName().equals(AuthnRequest.IS_PASSIVE_ATTRIB_NAME)) {
            req.setIsPassive(XSBooleanValue.valueOf(attribute.getValue()));
        } else if (attribute.getLocalName().equals(AuthnRequest.PROTOCOL_BINDING_ATTRIB_NAME)) {
            req.setProtocolBinding(attribute.getValue());
        } else if (attribute.getLocalName().equals(AuthnRequest.ASSERTION_CONSUMER_SERVICE_INDEX_ATTRIB_NAME)) {
            req.setAssertionConsumerServiceIndex(Integer.valueOf(attribute.getValue()));
        } else if (attribute.getLocalName().equals(AuthnRequest.ASSERTION_CONSUMER_SERVICE_URL_ATTRIB_NAME)) {
            req.setAssertionConsumerServiceURL(attribute.getValue());
        } else if (attribute.getLocalName().equals(AuthnRequest.ATTRIBUTE_CONSUMING_SERVICE_INDEX_ATTRIB_NAME)) {
            req.setAttributeConsumingServiceIndex(Integer.valueOf(attribute.getValue()));
        } else if (attribute.getLocalName().equals(AuthnRequest.PROVIDER_NAME_ATTRIB_NAME)) {
            req.setProviderName(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        AuthnRequest req = (AuthnRequest) parentSAMLObject;

        if (childSAMLObject instanceof Subject) {
            req.setSubject((Subject) childSAMLObject);
        } else if (childSAMLObject instanceof NameIDPolicy) {
            req.setNameIDPolicy((NameIDPolicy) childSAMLObject);
        } else if (childSAMLObject instanceof Conditions) {
            req.setConditions((Conditions) childSAMLObject);
        } else if (childSAMLObject instanceof RequestedAuthnContext) {
            req.setRequestedAuthnContext((RequestedAuthnContext) childSAMLObject);
        } else if (childSAMLObject instanceof Scoping) {
            req.setScoping((Scoping) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }
}