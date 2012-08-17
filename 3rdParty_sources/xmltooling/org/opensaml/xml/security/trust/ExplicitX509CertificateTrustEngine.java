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

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialResolver;
import org.opensaml.xml.security.x509.X509Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trust engine that evaluates a credential's X.509 certificate against certificates expressed within a set of trusted
 * credentials obtained from a credential resolver.
 * 
 * The credential being tested is valid if its entity certificate matches the entity certificate contained within any of
 * the trusted credentials produced by the given credential resolver.
 */
public class ExplicitX509CertificateTrustEngine implements TrustedCredentialTrustEngine<X509Credential> {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(ExplicitX509CertificateTrustEngine.class);

    /** Resolver used for resolving trusted credentials. */
    private CredentialResolver credentialResolver;

    /** Trust evaluator. */
    private ExplicitX509CertificateTrustEvaluator trustEvaluator;

    /**
     * Constructor.
     * 
     * @param resolver credential resolver which is used to resolve trusted credentials
     */
    public ExplicitX509CertificateTrustEngine(CredentialResolver resolver) {
        if (resolver == null) {
            throw new IllegalArgumentException("Credential resolver may not be null");
        }
        credentialResolver = resolver;

        trustEvaluator = new ExplicitX509CertificateTrustEvaluator();
    }

    /** {@inheritDoc} */
    public CredentialResolver getCredentialResolver() {
        return credentialResolver;
    }

    /** {@inheritDoc} */
    public boolean validate(X509Credential untrustedCredential, CriteriaSet trustBasisCriteria)
            throws SecurityException {

        checkParams(untrustedCredential, trustBasisCriteria);

        log.debug("Attempting to validate untrusted credential");
        Iterable<Credential> trustedCredentials = getCredentialResolver().resolve(trustBasisCriteria);

        return trustEvaluator.validate(untrustedCredential, trustedCredentials);
    }

    /**
     * Check the parameters for required values.
     * 
     * @param untrustedCredential the signature to be evaluated
     * @param trustBasisCriteria the set of trusted credential criteria
     * @throws SecurityException thrown if required values are absent or otherwise invalid
     */
    protected void checkParams(X509Credential untrustedCredential, CriteriaSet trustBasisCriteria)
            throws SecurityException {

        if (untrustedCredential == null) {
            throw new SecurityException("Untrusted credential was null");
        }
        if (trustBasisCriteria == null) {
            throw new SecurityException("Trust basis criteria set was null");
        }
        if (trustBasisCriteria.isEmpty()) {
            throw new SecurityException("Trust basis criteria set was empty");
        }
    }

}