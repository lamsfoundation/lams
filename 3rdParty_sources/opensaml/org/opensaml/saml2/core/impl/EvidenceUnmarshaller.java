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

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AssertionIDRef;
import org.opensaml.saml2.core.AssertionURIRef;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.Evidence}.
 */
public class EvidenceUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        Evidence evidence = (Evidence) parentObject;

        if (childObject instanceof AssertionIDRef) {
            evidence.getAssertionIDReferences().add((AssertionIDRef) childObject);
        } else if (childObject instanceof AssertionURIRef) {
            evidence.getAssertionURIReferences().add((AssertionURIRef) childObject);
        } else if (childObject instanceof Assertion) {
            evidence.getAssertions().add((Assertion) childObject);
        } else if (childObject instanceof EncryptedAssertion) {
            evidence.getEncryptedAssertions().add((EncryptedAssertion) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }
}