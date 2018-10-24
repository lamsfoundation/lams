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

package org.opensaml.saml1.core.impl;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml1.core.ResponseAbstractType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * A thread-safe {@link org.opensaml.xml.io.Unmarshaller} for {@link org.opensaml.saml1.core.ResponseAbstractType}
 * objects.
 */
public abstract class ResponseAbstractTypeUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(ResponseUnmarshaller.class);

    /** {@inheritDoc} */
    public XMLObject unmarshall(Element domElement) throws UnmarshallingException {
        // After regular unmarshalling, check the minor version and set ID-ness if not SAML 1.0
        ResponseAbstractType response = (ResponseAbstractType) super.unmarshall(domElement);
        if (response.getMinorVersion() != 0 && !DatatypeHelper.isEmpty(response.getID())) {
            domElement.setIdAttributeNS(null, ResponseAbstractType.ID_ATTRIB_NAME, true);
        }
        return response;
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        ResponseAbstractType response = (ResponseAbstractType) parentSAMLObject;

        if (childSAMLObject instanceof Signature) {
            response.setSignature((Signature) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        ResponseAbstractType response = (ResponseAbstractType) samlObject;

        if (attribute.getLocalName().equals(ResponseAbstractType.ID_ATTRIB_NAME)) {
            response.setID(attribute.getValue());
        } else if (attribute.getLocalName().equals(ResponseAbstractType.INRESPONSETO_ATTRIB_NAME)) {
            response.setInResponseTo(attribute.getValue());
        } else if (attribute.getLocalName().equals(ResponseAbstractType.ISSUEINSTANT_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            response.setIssueInstant(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(ResponseAbstractType.MINORVERSION_ATTRIB_NAME)) {
            int minor;
            try {
                minor = Integer.parseInt(attribute.getValue());
            } catch (NumberFormatException n) {
                log.error("Parsing minor version ", n);
                throw new UnmarshallingException(n);
            }
            if (minor == 0) {
                response.setVersion(SAMLVersion.VERSION_10);
            } else if (minor == 1) {
                response.setVersion(SAMLVersion.VERSION_11);
            }
        } else if (attribute.getLocalName().equals(ResponseAbstractType.RECIPIENT_ATTRIB_NAME)) {
            response.setRecipient(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }

}