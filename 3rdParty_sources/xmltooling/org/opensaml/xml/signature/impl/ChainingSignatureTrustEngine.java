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

package org.opensaml.xml.signature.impl;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureTrustEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Evaluate a signature in sequence using a chain of subordinate trust engines. If the signature may be established as
 * trusted by any of the subordinate engines, the token is considered trusted. Otherwise it is considered untrusted.
 * 
 */
public class ChainingSignatureTrustEngine implements SignatureTrustEngine {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(ChainingSignatureTrustEngine.class);

    /** The chain of subordinate trust engines. */
    private List<SignatureTrustEngine> engines;

    /** Constructor. */
    public ChainingSignatureTrustEngine() {
        engines = new ArrayList<SignatureTrustEngine>();
    }

    /**
     * Get the list of configured trust engines which constitute the trust evaluation chain.
     * 
     * @return the modifiable list of trust engines in the chain
     */
    public List<SignatureTrustEngine> getChain() {
        return engines;
    }

    /** {@inheritDoc} */
    public KeyInfoCredentialResolver getKeyInfoResolver() {
        // Chaining signature trust engine does not support an attached KeyInfoResolver
        return null;
    }

    /** {@inheritDoc} */
    public boolean validate(Signature token, CriteriaSet trustBasisCriteria) throws SecurityException {
        for (SignatureTrustEngine engine : engines) {
            if (engine.validate(token, trustBasisCriteria)) {
                log.debug("Signature was trusted by chain member: {}", engine.getClass().getName());
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    public boolean validate(byte[] signature, byte[] content, String algorithmURI, CriteriaSet trustBasisCriteria,
            Credential candidateCredential) throws SecurityException {
        for (SignatureTrustEngine engine : engines) {
            if (engine.validate(signature, content, algorithmURI, trustBasisCriteria, candidateCredential)) {
                log.debug("Signature was trusted by chain member: {}", engine.getClass().getName());
                return true;
            }
        }
        return false;
    }

}
