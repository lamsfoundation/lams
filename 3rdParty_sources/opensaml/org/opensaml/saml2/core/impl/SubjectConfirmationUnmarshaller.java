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

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.core.BaseID;
import org.opensaml.saml2.core.EncryptedID;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.SubjectConfirmation} objects.
 */
public class SubjectConfirmationUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        SubjectConfirmation subjectConfirmation = (SubjectConfirmation) parentObject;

        if (childObject instanceof BaseID) {
            subjectConfirmation.setBaseID((BaseID) childObject);
        } else if (childObject instanceof NameID) {
            subjectConfirmation.setNameID((NameID) childObject);
        } else if (childObject instanceof EncryptedID) {
            subjectConfirmation.setEncryptedID((EncryptedID) childObject);
        } else if (childObject instanceof SubjectConfirmationData) {
            subjectConfirmation.setSubjectConfirmationData((SubjectConfirmationData) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        SubjectConfirmation subjectConfirmation = (SubjectConfirmation) samlObject;

        if (attribute.getLocalName().equals(SubjectConfirmation.METHOD_ATTRIB_NAME)) {
            subjectConfirmation.setMethod(attribute.getValue());
        } else {
            super.processAttribute(samlObject, attribute);
        }
    }
}