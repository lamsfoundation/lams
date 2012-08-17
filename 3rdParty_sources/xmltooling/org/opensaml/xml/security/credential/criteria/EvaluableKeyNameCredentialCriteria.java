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
import org.opensaml.xml.security.criteria.KeyNameCriteria;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance of evaluable credential criteria for evaluating credential key names.
 */
public class EvaluableKeyNameCredentialCriteria implements EvaluableCredentialCriteria {
    
    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(EvaluableKeyNameCredentialCriteria.class);
    
    /** Base criteria. */
    private String keyName;
    
    /**
     * Constructor.
     *
     * @param criteria the criteria which is the basis for evaluation
     */
    public EvaluableKeyNameCredentialCriteria(KeyNameCriteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria instance may not be null");
        }
        keyName = criteria.getKeyName();
    }
    
    /**
     * Constructor.
     *
     * @param newKeyName the criteria value which is the basis for evaluation
     */
    public EvaluableKeyNameCredentialCriteria(String newKeyName) {
        if (DatatypeHelper.isEmpty(newKeyName)) {
            throw new IllegalArgumentException("Key name may not be null");
        }
        keyName = newKeyName;
    }

    /** {@inheritDoc} */
    public Boolean evaluate(Credential target) {
        if (target == null) {
            log.error("Credential target was null");
            return null;
        }
        if (target.getKeyNames().isEmpty()) {
            log.info("Could not evaluate criteria, credential contained no key names");
            return null;
        }
        Boolean result = target.getKeyNames().contains(keyName);
        return result;
    }

}
