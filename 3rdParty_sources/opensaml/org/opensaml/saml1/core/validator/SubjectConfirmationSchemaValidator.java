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

package org.opensaml.saml1.core.validator;

import org.opensaml.saml1.core.SubjectConfirmation;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml1.core.SubjectConfirmation} for Schema compliance.
 */
public class SubjectConfirmationSchemaValidator implements Validator<SubjectConfirmation> {

    /** {@inheritDoc} */
    public void validate(SubjectConfirmation subjectConfirmation) throws ValidationException {
        validateSubjectConfirmationMethods(subjectConfirmation);
    }

    /**
     * Validates that the given subject confirmation has a confirmation method.
     * 
     * @param subjectConfirmation subject confirmation to validate
     * 
     * @throws ValidationException thrown if the given confirmation does not have a confirmation method
     */
    protected void validateSubjectConfirmationMethods(SubjectConfirmation subjectConfirmation)
            throws ValidationException {
        if (subjectConfirmation.getConfirmationMethods().size() == 0) {
            throw new ValidationException("At least Confirmation Method should be present");
        }
    }
}