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

package org.opensaml.xml.security.trust;

import java.security.Key;

import org.opensaml.xml.security.credential.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Auxillary trust evaluator for evaluating an untrusted key or credential against a trusted key or credential. Trust is
 * established if the untrusted key (or public key or symmetric key from the untrusted credential) is matches one of the
 * trusted keys supplied.
 * 
 */
public class ExplicitKeyTrustEvaluator {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(ExplicitKeyTrustEvaluator.class);

    /**
     * Evaluate trust.
     * 
     * @param untrustedKey the untrusted key to evaluate
     * @param trustedKey basis for trust
     * @return true if trust can be established, false otherwise
     */
    public boolean validate(Key untrustedKey, Key trustedKey) {
        return untrustedKey.equals(trustedKey);
    }

    /**
     * Evaluate trust.
     * 
     * @param untrustedKey the untrusted key to evaluate
     * @param trustedKeys basis for trust
     * @return true if trust can be established, false otherwise
     */
    public boolean validate(Key untrustedKey, Iterable<Key> trustedKeys) {
        for (Key trustedKey : trustedKeys) {
            if (untrustedKey.equals(trustedKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluate trust.
     * 
     * @param untrustedCredential the untrusted credential to evaluate
     * @param trustedCredential basis for trust
     * @return true if trust can be established, false otherwise
     */
    public boolean validate(Credential untrustedCredential, Credential trustedCredential) {

        Key untrustedKey = null;
        Key trustedKey = null;
        if (untrustedCredential.getPublicKey() != null) {
            untrustedKey = untrustedCredential.getPublicKey();
            trustedKey = trustedCredential.getPublicKey();
        } else {
            untrustedKey = untrustedCredential.getSecretKey();
            trustedKey = trustedCredential.getSecretKey();
        }
        if (untrustedKey == null) {
            log.debug("Untrusted credential contained no key, unable to evaluate");
            return false;
        } else if (trustedKey == null) {
            log.debug("Trusted credential contained no key of the appropriate type, unable to evaluate");
            return false;
        }

        if (validate(untrustedKey, trustedKey)) {
            log.debug("Successfully validated untrusted credential against trusted key");
            return true;
        }

        log.debug("Failed to validate untrusted credential against trusted key");
        return false;
    }

    /**
     * Evaluate trust.
     * 
     * @param untrustedCredential the untrusted credential to evaluate
     * @param trustedCredentials basis for trust
     * @return true if trust can be established, false otherwise
     */
    public boolean validate(Credential untrustedCredential, Iterable<Credential> trustedCredentials) {

        for (Credential trustedCredential : trustedCredentials) {
            if (validate(untrustedCredential, trustedCredential)) {
                return true;
            }
        }
        return false;
    }

}