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
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.core.Assertion} for Schema compliance.
 */
public class AssertionSchemaValidator implements Validator<Assertion> {

    /** Constructor */
    public AssertionSchemaValidator() {

    }

    /** {@inheritDoc} */
    public void validate(Assertion assertion) throws ValidationException {
        validateIssuer(assertion);
        validateVersion(assertion);
        validateID(assertion);
        validateIssueInstant(assertion);
    }

    /**
     * Checks that Issuer element is present.
     * 
     * @param assertion
     * @throws ValidationException
     */
    protected void validateIssuer(Assertion assertion) throws ValidationException {
        if (assertion.getIssuer() == null) {
            throw new ValidationException("Issuer is required element");
        }
    }

    /**
     * Checks that the Version attribute is present.
     * 
     * @param assertion
     * @throws ValidationException
     */
    protected void validateVersion(Assertion assertion) throws ValidationException {
        if (assertion.getVersion() == null) {
            throw new ValidationException("Version is required attribute");
        }
    }

    /**
     * Checks that the ID attribute is present.
     * 
     * @param assertion
     * @throws ValidationException
     */
    protected void validateID(Assertion assertion) throws ValidationException {
        if (DatatypeHelper.isEmpty(assertion.getID())) {
            throw new ValidationException("ID is required attribute");
        }
    }

    /**
     * Checks that the IssueInstant attribute is present.
     * 
     * @param assertion
     * @throws ValidationException
     */
    protected void validateIssueInstant(Assertion assertion) throws ValidationException {
        if (assertion.getIssueInstant() == null) {
            throw new ValidationException("IssueInstant is required attribute");
        }
    }
}