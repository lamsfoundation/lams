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

import org.opensaml.Configuration;
import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

/**
 * A thread safe Marshaller for {@link org.opensaml.saml2.core.RequestAbstractType} objects.
 */
public abstract class RequestAbstractTypeMarshaller extends AbstractSAMLObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlObject, Element domElement) throws MarshallingException {
        RequestAbstractType req = (RequestAbstractType) samlObject;

        if (req.getVersion() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.VERSION_ATTRIB_NAME, req.getVersion().toString());
        }

        if (req.getID() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.ID_ATTRIB_NAME, req.getID());
            domElement.setIdAttributeNS(null, RequestAbstractType.ID_ATTRIB_NAME, true);
        }

        if (req.getVersion() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.VERSION_ATTRIB_NAME, req.getVersion().toString());
        }

        if (req.getIssueInstant() != null) {
            String iiStr = Configuration.getSAMLDateFormatter().print(req.getIssueInstant());
            domElement.setAttributeNS(null, RequestAbstractType.ISSUE_INSTANT_ATTRIB_NAME, iiStr);
        }

        if (req.getDestination() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.DESTINATION_ATTRIB_NAME, req.getDestination());
        }

        if (req.getConsent() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.CONSENT_ATTRIB_NAME, req.getConsent());
        }
    }
}