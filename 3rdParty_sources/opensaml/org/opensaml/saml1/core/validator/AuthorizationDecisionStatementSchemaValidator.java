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

import org.opensaml.saml1.core.AuthorizationDecisionStatement;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml1.core.AuthorizationDecisionStatement} for Schema compliance.
 */
public class AuthorizationDecisionStatementSchemaValidator extends SubjectStatementSchemaValidator<AuthorizationDecisionStatement> {

    /** {@inheritDoc} */
    public void validate(AuthorizationDecisionStatement authorizationDecisionStatement) throws ValidationException {
        super.validate(authorizationDecisionStatement);
        
        validateResource(authorizationDecisionStatement);
        
        validateDecision(authorizationDecisionStatement);
        
        validateActions(authorizationDecisionStatement);
    }
    
    /**
     * Check that the resource attribute is present and valid
     * @param statement the AuthorizationDecisionStatement under question
     * @throws ValidationException
     */
    protected void validateResource(AuthorizationDecisionStatement statement) throws ValidationException {
        if (DatatypeHelper.isEmpty(statement.getResource())) {
            throw new ValidationException("Resource attribute not present or invalid");
        }
     }
    
    /**
     * Check that the Decision element is present
     * @param statement the AuthorizationDecisionStatement under question
     * @throws ValidationException
     */
    protected void validateDecision(AuthorizationDecisionStatement statement) throws ValidationException {
        if (statement.getDecision() == null) {
            throw new ValidationException("No Decision element present");
        }
    }

    /**
     * Check that there is at least one Action element
     * @param statement the AuthorizationDecisionStatement under question
     * @throws ValidationException
     */
    protected void validateActions(AuthorizationDecisionStatement statement) throws ValidationException {
        if (statement.getActions().size() == 0) {
            throw new ValidationException("No Action elements present");
        }
    }
}