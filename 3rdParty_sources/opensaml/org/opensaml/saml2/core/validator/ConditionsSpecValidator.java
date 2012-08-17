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

import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.OneTimeUse;
import org.opensaml.saml2.core.ProxyRestriction;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks the {@link org.opensaml.saml2.core.Conditions} for Spec compliance.
 */
public class ConditionsSpecValidator implements Validator<Conditions> {

    /** Constructor */
    public ConditionsSpecValidator() {

    }

    /** {@inheritDoc} */
    public void validate(Conditions conditions) throws ValidationException {
        validateOneTimeUseCondition(conditions);
        validateProxyRestrictionCondition(conditions);
    }

    /**
     * Checks that there is at most one OneTimeUse condition.
     * 
     * @param conditions
     * @throws ValidationException
     */
    protected void validateOneTimeUseCondition(Conditions conditions) throws ValidationException {
        int oneTimeUseCount = 0;
        for (int i = 0; i < conditions.getConditions().size(); i++) {
            if (conditions.getConditions().get(i) instanceof OneTimeUse) {
                oneTimeUseCount++;
            }
        }
        
        if (oneTimeUseCount > 1) {
            throw new ValidationException("At most one instance of OneTimeUse allowed");
        }
    }

    protected void validateProxyRestrictionCondition(Conditions conditions) throws ValidationException {
        int proxyRestrictionCount = 0;
        for (int i = 0; i < conditions.getConditions().size(); i++) {
            if (conditions.getConditions().get(i) instanceof ProxyRestriction) {
                proxyRestrictionCount++;
            }
        }
        
        if (proxyRestrictionCount > 1) {
            throw new ValidationException("At most one instance of ProxyRestriction allowed");
        }
    }
}