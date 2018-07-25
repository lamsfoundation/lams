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

import java.security.Key;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.criteria.KeyAlgorithmCriteria;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance of evaluable credential criteria for evaluating the credential key algorithm.
 */
public class EvaluableKeyAlgorithmCredentialCriteria implements EvaluableCredentialCriteria {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(EvaluableKeyAlgorithmCredentialCriteria.class);

    /** Base criteria. */
    private String keyAlgorithm;

    /**
     * Constructor.
     * 
     * @param criteria the criteria which is the basis for evaluation
     */
    public EvaluableKeyAlgorithmCredentialCriteria(KeyAlgorithmCriteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria instance may not be null");
        }
        keyAlgorithm = criteria.getKeyAlgorithm();
    }

    /**
     * Constructor.
     * 
     * @param newKeyAlgorithm the criteria value which is the basis for evaluation
     */
    public EvaluableKeyAlgorithmCredentialCriteria(String newKeyAlgorithm) {
        if (DatatypeHelper.isEmpty(newKeyAlgorithm)) {
            throw new IllegalArgumentException("Key algorithm may not be null");
        }
        keyAlgorithm = newKeyAlgorithm;
    }

    /** {@inheritDoc} */
    public Boolean evaluate(Credential target) {
        if (target == null) {
            log.error("Credential target was null");
            return null;
        }
        Key key = getKey(target);
        if (key == null) {
            log.info("Could not evaluate criteria, credential contained no key");
            return null;
        }
        String algorithm = DatatypeHelper.safeTrimOrNullString(key.getAlgorithm());
        if (algorithm == null) {
            log.info("Could not evaluate criteria, key does not specify an algorithm via getAlgorithm()");
            return null;
        }

        Boolean result = keyAlgorithm.equals(algorithm);
        return result;
    }

    /**
     * Get the key contained within the credential.
     * 
     * @param credential the credential containing a key
     * @return the key from the credential
     */
    private Key getKey(Credential credential) {
        if (credential.getPublicKey() != null) {
            return credential.getPublicKey();
        } else if (credential.getSecretKey() != null) {
            return credential.getSecretKey();
        } else if (credential.getPrivateKey() != null) {
            // There should have been a corresponding public key, but just in case...
            return credential.getPrivateKey();
        } else {
            return null;
        }

    }

}
