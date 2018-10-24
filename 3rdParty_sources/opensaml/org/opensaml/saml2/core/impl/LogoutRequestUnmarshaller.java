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

package org.opensaml.saml2.core.impl;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.saml2.core.BaseID;
import org.opensaml.saml2.core.EncryptedID;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.SessionIndex;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.LogoutRequest} objects.
 */
public class LogoutRequestUnmarshaller extends RequestAbstractTypeUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        LogoutRequest req = (LogoutRequest) samlObject;

        if (attribute.getLocalName().equals(LogoutRequest.REASON_ATTRIB_NAME)) {
            req.setReason(attribute.getValue());
        } else if (attribute.getLocalName().equals(LogoutRequest.NOT_ON_OR_AFTER_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            req.setNotOnOrAfter(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        LogoutRequest req = (LogoutRequest) parentSAMLObject;

        if (childSAMLObject instanceof BaseID) {
            req.setBaseID((BaseID) childSAMLObject);
        } else if (childSAMLObject instanceof NameID) {
            req.setNameID((NameID) childSAMLObject);
        } else if (childSAMLObject instanceof EncryptedID) {
            req.setEncryptedID((EncryptedID) childSAMLObject);
        } else if (childSAMLObject instanceof SessionIndex) {
            req.getSessionIndexes().add((SessionIndex) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }
}