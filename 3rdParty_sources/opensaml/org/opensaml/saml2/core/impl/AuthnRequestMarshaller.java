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
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

/**
 * A thread-safe Marshaller for {@link org.opensaml.saml2.core.AuthnRequest}.
 */
public class AuthnRequestMarshaller extends RequestAbstractTypeMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlObject, Element domElement) throws MarshallingException {
        AuthnRequest req = (AuthnRequest) samlObject;

        if (req.isForceAuthnXSBoolean() != null) {
            domElement.setAttributeNS(null, AuthnRequest.FORCE_AUTHN_ATTRIB_NAME, req.isForceAuthnXSBoolean()
                    .toString());
        }

        if (req.isPassiveXSBoolean() != null) {
            domElement.setAttributeNS(null, AuthnRequest.IS_PASSIVE_ATTRIB_NAME, req.isPassiveXSBoolean().toString());
        }

        if (req.getProtocolBinding() != null) {
            domElement.setAttributeNS(null, AuthnRequest.PROTOCOL_BINDING_ATTRIB_NAME, req.getProtocolBinding());
        }

        if (req.getAssertionConsumerServiceIndex() != null) {
            domElement.setAttributeNS(null, AuthnRequest.ASSERTION_CONSUMER_SERVICE_INDEX_ATTRIB_NAME, req
                    .getAssertionConsumerServiceIndex().toString());
        }

        if (req.getAssertionConsumerServiceURL() != null) {
            domElement.setAttributeNS(null, AuthnRequest.ASSERTION_CONSUMER_SERVICE_URL_ATTRIB_NAME, req
                    .getAssertionConsumerServiceURL());
        }

        if (req.getAttributeConsumingServiceIndex() != null) {
            domElement.setAttributeNS(null, AuthnRequest.ATTRIBUTE_CONSUMING_SERVICE_INDEX_ATTRIB_NAME, req
                    .getAttributeConsumingServiceIndex().toString());
        }

        if (req.getProviderName() != null) {
            domElement.setAttributeNS(null, AuthnRequest.PROVIDER_NAME_ATTRIB_NAME, req.getProviderName());
        }

        super.marshallAttributes(samlObject, domElement);
    }
}