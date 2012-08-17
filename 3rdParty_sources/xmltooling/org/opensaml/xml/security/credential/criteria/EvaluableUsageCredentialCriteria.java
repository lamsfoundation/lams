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

package org.opensaml.xml.security.credential.criteria;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance of evaluable credential criteria for evaluating whether a credential contains a particular usage specifier.
 */
public class EvaluableUsageCredentialCriteria implements EvaluableCredentialCriteria {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(EvaluableUsageCredentialCriteria.class);

    /** Base criteria. */
    private UsageType usage;

    /**
     * Constructor.
     * 
     * @param criteria the criteria which is the basis for evaluation
     */
    public EvaluableUsageCredentialCriteria(UsageCriteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria instance may not be null");
        }
        usage = criteria.getUsage();
    }

    /**
     * Constructor.
     * 
     * @param newUsage the criteria value which is the basis for evaluation
     */
    public EvaluableUsageCredentialCriteria(UsageType newUsage) {
        if (newUsage == null) {
            throw new IllegalArgumentException("Usage may not be null");
        }
        usage = newUsage;
    }

    /** {@inheritDoc} */
    public Boolean evaluate(Credential target) {
        if (target == null) {
            log.error("Credential target was null");
            return null;
        }
        UsageType credUsage = target.getUsageType();
        if (credUsage == null) {
            log.info("Could not evaluate criteria, credential contained no usage specifier");
            return null;
        }

        Boolean result = matchUsage(credUsage, usage);
        return result;
    }

    /**
     * Match usage enum type values from credential and criteria.
     * 
     * @param credentialUsage the usage value from the credential
     * @param criteriaUsage the usage value from the criteria
     * @return true if the two usage specifiers match for purposes of resolving credentials, false otherwise
     */
    protected boolean matchUsage(UsageType credentialUsage, UsageType criteriaUsage) {
        if (credentialUsage == UsageType.UNSPECIFIED || criteriaUsage == UsageType.UNSPECIFIED) {
            return true;
        }
        return credentialUsage == criteriaUsage;
    }

}
