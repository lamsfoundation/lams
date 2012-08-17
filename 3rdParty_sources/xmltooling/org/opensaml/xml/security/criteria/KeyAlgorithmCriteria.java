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

package org.opensaml.xml.security.criteria;

import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.util.DatatypeHelper;

/**
 * An implementation of {@link Criteria} which specifies key algorithm criteria.
 */
public final class KeyAlgorithmCriteria implements Criteria {
    
    /** Key algorithm type of resolved credentials. */
    private String keyAlgorithm;
    
    /**
     * Constructor.
     *
     * @param algorithm key algorithm
     */
    public KeyAlgorithmCriteria(String algorithm) {
        setKeyAlgorithm(algorithm);
    }
 
    /**
     * Get the key algorithm criteria.
     * 
     * @return returns the keyAlgorithm.
     */
    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    /**
     * Set the key algorithm criteria.
     * 
     * @param algorithm The keyAlgorithm to set.
     */
    public void setKeyAlgorithm(String algorithm) {
        if (DatatypeHelper.isEmpty(algorithm)) {
            throw new IllegalArgumentException("Key algorithm criteria value must be supplied");
        }
        keyAlgorithm = DatatypeHelper.safeTrimOrNullString(algorithm);
    }

}
