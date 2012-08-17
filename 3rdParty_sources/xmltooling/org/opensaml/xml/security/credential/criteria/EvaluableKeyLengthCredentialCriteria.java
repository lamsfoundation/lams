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

import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.criteria.KeyLengthCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance of evaluable credential criteria for evaluating the credential key length.
 */
public class EvaluableKeyLengthCredentialCriteria implements EvaluableCredentialCriteria {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(EvaluableKeyLengthCredentialCriteria.class);

    /** Base criteria. */
    private Integer keyLength;

    /**
     * Constructor.
     * 
     * @param criteria the criteria which is the basis for evaluation
     */
    public EvaluableKeyLengthCredentialCriteria(KeyLengthCriteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria instance may not be null");
        }
        keyLength = criteria.getKeyLength();
    }

    /**
     * Constructor.
     * 
     * @param newKeyLength the criteria value which is the basis for evaluation
     */
    public EvaluableKeyLengthCredentialCriteria(Integer newKeyLength) {
        if (newKeyLength == null) {
            throw new IllegalArgumentException("Key length may not be null");
        }
        keyLength = newKeyLength;
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
        Integer length = SecurityHelper.getKeyLength(key);
        if (length == null) {
            log.info("Could not evaluate criteria, can not determine length of key");
            return null;
        }

        Boolean result = keyLength.equals(length);
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
