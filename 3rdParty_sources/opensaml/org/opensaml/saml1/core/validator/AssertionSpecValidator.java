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

import org.opensaml.saml1.core.Assertion;
import org.opensaml.saml1.core.Condition;
import org.opensaml.saml1.core.Conditions;
import org.opensaml.saml1.core.DoNotCacheCondition;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Spec validator for {@link org.opensaml.saml1.core.Assertion}
 */
public class AssertionSpecValidator implements Validator<Assertion> {

    public void validate(Assertion assertion) throws ValidationException {
        validateDoNotCache(assertion);
    }
    
    protected void validateDoNotCache(Assertion assertion) throws ValidationException {
        
        if (assertion.getMinorVersion() == 0) {
            Conditions conditions = assertion.getConditions();
            if (conditions != null) {
                for (Condition condition : conditions.getConditions()) {
                    if (condition instanceof DoNotCacheCondition) {
                        throw new ValidationException("DoNotCacheCondition not valid in SAML1.0");
                    }
                }
            }
        }
    }
}
