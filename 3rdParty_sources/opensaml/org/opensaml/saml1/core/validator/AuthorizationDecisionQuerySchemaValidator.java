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

import org.opensaml.saml1.core.AuthorizationDecisionQuery;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml1.core.AuthorizationDecisionQuery} for Schema compliance.
 */
public class AuthorizationDecisionQuerySchemaValidator extends SubjectQuerySchemaValidator<AuthorizationDecisionQuery> {

    /** {@inheritDoc} */
    public void validate(AuthorizationDecisionQuery query) throws ValidationException {
        super.validate(query);
        validateActions(query);

        validateResourcePresent(query);

    }

    /**
     * Validates that the Resource attribute is present and valid
     * 
     * @param query
     * @throws ValidationException
     */
    protected void validateResourcePresent(AuthorizationDecisionQuery query) throws ValidationException {
        if (DatatypeHelper.isEmpty(query.getResource())) {
            throw new ValidationException("No Resource attribute present");
        }
    }

    /**
     * Validates that there is at least one Action Element present.
     * 
     * @param query
     * @throws ValidationException
     */
    protected void validateActions(AuthorizationDecisionQuery query) throws ValidationException {
        if (query.getActions().size() == 0) {
            throw new ValidationException("No Action elements present");
        }
    }
}