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

package org.opensaml.saml2.core.validator;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.Assertion} for Spec compliance.
 */
public class AssertionSpecValidator implements Validator<Assertion> {

    /** Constructor */
    public AssertionSpecValidator() {

    }

    /** {@inheritDoc} */
    public void validate(Assertion assertion) throws ValidationException {
        validateSubject(assertion);
    }

    /**
     * Checks that the Subject element is present when required.
     * 
     * @param assertion
     * @throws ValidationException
     */
    protected void validateSubject(Assertion assertion) throws ValidationException {
        if ((assertion.getStatements() == null || assertion.getStatements().size() == 0)
                && (assertion.getAuthnStatements() == null || assertion.getAuthnStatements().size() == 0)
                && (assertion.getAttributeStatements() == null || assertion.getAttributeStatements().size() == 0)
                && (assertion.getAuthzDecisionStatements() == null || assertion.getAuthzDecisionStatements().size() == 0)
                && assertion.getSubject() == null) {
            throw new ValidationException("Subject is required when Statements are absent");
        }

        if (assertion.getAuthnStatements().size() > 0 && assertion.getSubject() == null) {
            throw new ValidationException("Assertions containing AuthnStatements require a Subject");
        }
        if (assertion.getAuthzDecisionStatements().size() > 0 && assertion.getSubject() == null) {
            throw new ValidationException("Assertions containing AuthzDecisionStatements require a Subject");
        }
        if (assertion.getAttributeStatements().size() > 0 && assertion.getSubject() == null) {
            throw new ValidationException("Assertions containing AttributeStatements require a Subject");
        }
    }
}