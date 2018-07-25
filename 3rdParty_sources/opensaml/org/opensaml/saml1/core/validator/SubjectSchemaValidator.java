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

import org.opensaml.saml1.core.Subject;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml1.core.Subject} for Schema compliance.
 */
public class SubjectSchemaValidator implements Validator<Subject> {

    /** {@inheritDoc} */
    public void validate(Subject subject) throws ValidationException {
        validateNameIdentifierSubjectConfirmation(subject);
    }

    /**
     * Validates that the subject has either a name identifier or subject confirmation
     * 
     * @param subject subject to validate
     * 
     * @throws ValidationException thrown if the subject has neither a name identifier or subject confirmation
     */
    protected void validateNameIdentifierSubjectConfirmation(Subject subject) throws ValidationException {
        if (subject.getNameIdentifier() == null && subject.getSubjectConfirmation() == null) {
            throw new ValidationException("Either a NameIdentifier or SubjectConfirmation should be present");
        }
    }
}