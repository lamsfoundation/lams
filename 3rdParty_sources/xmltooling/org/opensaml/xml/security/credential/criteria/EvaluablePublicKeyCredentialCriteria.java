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

import java.security.PublicKey;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.criteria.PublicKeyCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance of evaluable credential criteria for evaluating whether a credential contains a particular
 * public key.
 */
public class EvaluablePublicKeyCredentialCriteria implements EvaluableCredentialCriteria {
    
    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(EvaluablePublicKeyCredentialCriteria.class);
    
    /** Base criteria. */
    private PublicKey publicKey;
    
    /**
     * Constructor.
     *
     * @param criteria the criteria which is the basis for evaluation
     */
    public EvaluablePublicKeyCredentialCriteria(PublicKeyCriteria criteria) {
        if (criteria == null) {
            throw new NullPointerException("Criteria instance may not be null");
        }
        publicKey = criteria.getPublicKey();
    }
    
    /**
     * Constructor.
     *
     * @param newPublicKey the criteria value which is the basis for evaluation
     */
    public EvaluablePublicKeyCredentialCriteria(PublicKey newPublicKey) {
        if (newPublicKey == null) {
            throw new IllegalArgumentException("Public key may not be null");
        }
        publicKey = newPublicKey;
    }

    /** {@inheritDoc} */
    public Boolean evaluate(Credential target) {
        if (target == null) {
            log.error("Credential target was null");
            return null;
        }
        PublicKey key = target.getPublicKey();
        if (key == null) {
            log.info("Credential contained no public key, does not satisfy public key criteria");
            return Boolean.FALSE;
        }
        
        Boolean result = publicKey.equals(key);
        return result;
    }

}
