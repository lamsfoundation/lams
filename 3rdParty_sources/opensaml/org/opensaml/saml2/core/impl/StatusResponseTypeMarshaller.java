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
import org.opensaml.saml2.core.StatusResponseType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

/**
 * A thread safe Marshaller for {@link org.opensaml.saml2.core.StatusResponseType} objects.
 */
public abstract class StatusResponseTypeMarshaller extends AbstractSAMLObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlObject, Element domElement) throws MarshallingException {
        StatusResponseType sr = (StatusResponseType) samlObject;

        if (sr.getVersion() != null) {
            domElement.setAttributeNS(null, StatusResponseType.VERSION_ATTRIB_NAME, sr.getVersion().toString());
        }

        if (sr.getID() != null) {
            domElement.setAttributeNS(null, StatusResponseType.ID_ATTRIB_NAME, sr.getID());
            domElement.setIdAttributeNS(null, StatusResponseType.ID_ATTRIB_NAME, true);
        }

        if (sr.getInResponseTo() != null) {
            domElement.setAttributeNS(null, StatusResponseType.IN_RESPONSE_TO_ATTRIB_NAME, sr.getInResponseTo());
        }

        if (sr.getVersion() != null) {
            domElement.setAttributeNS(null, StatusResponseType.VERSION_ATTRIB_NAME, sr.getVersion().toString());
        }

        if (sr.getIssueInstant() != null) {
            String iiStr = Configuration.getSAMLDateFormatter().print(sr.getIssueInstant());
            domElement.setAttributeNS(null, StatusResponseType.ISSUE_INSTANT_ATTRIB_NAME, iiStr);
        }

        if (sr.getDestination() != null) {
            domElement.setAttributeNS(null, StatusResponseType.DESTINATION_ATTRIB_NAME, sr.getDestination());
        }

        if (sr.getConsent() != null) {
            domElement.setAttributeNS(null, StatusResponseType.CONSENT_ATTRIB_NAME, sr.getConsent());
        }
    }
}