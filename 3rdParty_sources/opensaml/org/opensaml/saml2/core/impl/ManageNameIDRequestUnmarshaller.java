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

import org.opensaml.saml2.core.EncryptedID;
import org.opensaml.saml2.core.ManageNameIDRequest;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.NewEncryptedID;
import org.opensaml.saml2.core.NewID;
import org.opensaml.saml2.core.Terminate;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.ManageNameIDRequest} objects.
 */
public class ManageNameIDRequestUnmarshaller extends RequestAbstractTypeUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        ManageNameIDRequest req = (ManageNameIDRequest) parentSAMLObject;

        if (childSAMLObject instanceof NameID) {
            req.setNameID((NameID) childSAMLObject);
        } else if (childSAMLObject instanceof EncryptedID) {
            req.setEncryptedID((EncryptedID) childSAMLObject);
        } else if (childSAMLObject instanceof NewID) {
            req.setNewID((NewID) childSAMLObject);
        } else if (childSAMLObject instanceof NewEncryptedID) {
            req.setNewEncryptedID((NewEncryptedID) childSAMLObject);
        } else if (childSAMLObject instanceof Terminate) {
            req.setTerminate((Terminate) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }
}