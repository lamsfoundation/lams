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

package org.opensaml.saml1.core.validator;

import java.util.List;

import org.opensaml.saml1.core.Assertion;
import org.opensaml.saml1.core.Statement;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml1.core.Assertion} for Schema compliance.
 */
public class AssertionSchemaValidator implements Validator<Assertion> {

    /** {@inheritDoc} */
    public void validate(Assertion assertion) throws ValidationException {
         validateVersion(assertion);
         validateId(assertion);
         validateIssuer(assertion);
         validateIssueInstant(assertion);
         validateStatements(assertion);
    }
    
    /**
     * Test that the version is SAML1.1 or 1.0
     * @param assertion what to test
     * @throws ValidationException
     */
    protected void validateVersion(Assertion assertion) throws ValidationException {
         if ((assertion.getMajorVersion() != 1) &&
             (assertion.getMinorVersion() != 0 || assertion.getMinorVersion() != 1)) {
             throw new ValidationException("Invalid Version");
         }
    }    
    
    /**
     * Test that the ID is present
     * @param assertion
     * @throws ValidationException
     */
    protected void validateId(Assertion assertion) throws ValidationException {
         if (DatatypeHelper.isEmpty(assertion.getID())) {
             throw new ValidationException("ID not present");
         }
    }
         
    /**
     * Test that the issuer is present
     * @param assertion
     * @throws ValidationException
     */
    protected void validateIssuer(Assertion assertion) throws ValidationException {
        if (DatatypeHelper.isEmpty(assertion.getIssuer())) {
             throw new ValidationException("Issuer not present");
         }
    }
    
    /**
     * Test that the IssueInstant is present
     * @param assertion
     * @throws ValidationException
     */
    protected void validateIssueInstant(Assertion assertion) throws ValidationException {
         if (assertion.getIssueInstant() == null) {
             throw new ValidationException("IssueInstant not present");
         }
    }
         
    /**
     * Test that the provided assertion has some statements in 
     * @param assertion
     * @throws ValidationException
     */
    protected void validateStatements(Assertion assertion) throws ValidationException {
        List <Statement> list = assertion.getStatements();
         if (list == null || list.size() == 0) {
             throw new ValidationException("No Statements present");
         }
    }
}