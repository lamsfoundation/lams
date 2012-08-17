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

import org.opensaml.saml1.core.Evidence;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml1.core.Evidence} for Schema compliance.
 */
public class EvidenceSchemaValidator implements Validator<Evidence> {

    /** {@inheritDoc} */
    public void validate(Evidence evidence) throws ValidationException {
         validateEvidence(evidence);
    }
    
    /**
     * Check that there is an assertion of AddsertionIDRef
     * @param evidence
     * @throws ValidationException
     */
    protected void validateEvidence(Evidence evidence) throws ValidationException {
        if (evidence.getEvidence().size() == 0) {
            throw new ValidationException("At least one Assertion or AssertionIDReference is required");
        }    
    }
}