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

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusResponseType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.StatusResponseType} objects.
 */
public abstract class StatusResponseTypeUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        StatusResponseType sr = (StatusResponseType) samlObject;

        if (attribute.getLocalName().equals(StatusResponseType.VERSION_ATTRIB_NAME)) {
            sr.setVersion(SAMLVersion.valueOf(attribute.getValue()));
        } else if (attribute.getLocalName().equals(StatusResponseType.ID_ATTRIB_NAME)) {
            sr.setID(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else if (attribute.getLocalName().equals(StatusResponseType.IN_RESPONSE_TO_ATTRIB_NAME)) {
            sr.setInResponseTo(attribute.getValue());
        } else if (attribute.getLocalName().equals(StatusResponseType.ISSUE_INSTANT_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            sr.setIssueInstant(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(StatusResponseType.DESTINATION_ATTRIB_NAME)) {
            sr.setDestination(attribute.getValue());
        } else if (attribute.getLocalName().equals(StatusResponseType.CONSENT_ATTRIB_NAME)) {
            sr.setConsent(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        StatusResponseType sr = (StatusResponseType) parentSAMLObject;

        if (childSAMLObject instanceof Issuer) {
            sr.setIssuer((Issuer) childSAMLObject);
        } else if (childSAMLObject instanceof Signature) {
            sr.setSignature((Signature) childSAMLObject);
        } else if (childSAMLObject instanceof Extensions) {
            sr.setExtensions((Extensions) childSAMLObject);
        } else if (childSAMLObject instanceof Status) {
            sr.setStatus((Status) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }
}