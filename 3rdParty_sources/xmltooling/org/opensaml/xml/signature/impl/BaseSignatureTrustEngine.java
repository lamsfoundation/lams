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

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.KeyInfoCriteria;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureTrustEngine;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base implementation of {@link SignatureTrustEngine} which evaluates the validity and trustworthiness of XML and raw
 * signatures.
 * 
 * <p>
 * When processing XML signatures, the supplied KeyInfoCredentialResolver will be used to resolve credential(s)
 * containing the (advisory) signing key from the KeyInfo element of the Signature, if present. If any of these
 * credentials do contain the valid signing key, they will be evaluated for trustworthiness against trusted information,
 * which will be resolved in an implementation-specific manner.
 * 
 * <p>
 * Subclasses are required to implement {@link #evaluateTrust(Credential, Object)} using an implementation-specific
 * trust model.
 * </p>
 * 
 * @param <TrustBasisType> the type of trusted information which has been resolved and which will serve as the basis for
 *            trust evaluation
 * 
 */
public abstract class BaseSignatureTrustEngine<TrustBasisType> implements SignatureTrustEngine {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(BaseSignatureTrustEngine.class);

    /** KeyInfo credential resolver used to obtain the signing credential from a Signature's KeyInfo. */
    private KeyInfoCredentialResolver keyInfoCredentialResolver;

    /**
     * Constructor.
     * 
     * @param keyInfoResolver KeyInfo credential resolver used to obtain the (advisory) signing credential from a
     *            Signature's KeyInfo element.
     */
    public BaseSignatureTrustEngine(KeyInfoCredentialResolver keyInfoResolver) {
        if (keyInfoResolver == null) {
            throw new IllegalArgumentException("KeyInfo credential resolver may not be null");
        }

        keyInfoCredentialResolver = keyInfoResolver;
    }

    /** {@inheritDoc} */
    public KeyInfoCredentialResolver getKeyInfoResolver() {
        return keyInfoCredentialResolver;
    }

    /**
     * Attempt to establish trust by resolving signature verification credentials from the Signature's KeyInfo. If any
     * credentials so resolved correctly verify the signature, attempt to establish trust using subclass-specific trust
     * logic against trusted information as implemented in {@link #evaluateTrust(Credential, Object)}.
     * 
     * @param signature the Signature to evaluate
     * @param trustBasis the information which serves as the basis for trust evaluation
     * @return true if the signature is verified by any KeyInfo-derived credential which can be established as trusted,
     *         otherwise false
     * @throws SecurityException if an error occurs during signature verification or trust processing
     */
    protected boolean validate(Signature signature, TrustBasisType trustBasis) throws SecurityException {

        log.debug("Attempting to verify signature and establish trust using KeyInfo-derived credentials");

        if (signature.getKeyInfo() != null) {

            KeyInfoCriteria keyInfoCriteria = new KeyInfoCriteria(signature.getKeyInfo());
            CriteriaSet keyInfoCriteriaSet = new CriteriaSet(keyInfoCriteria);

            for (Credential kiCred : getKeyInfoResolver().resolve(keyInfoCriteriaSet)) {
                if (verifySignature(signature, kiCred)) {
                    log.debug("Successfully verified signature using KeyInfo-derived credential");
                    log.debug("Attempting to establish trust of KeyInfo-derived credential");
                    if (evaluateTrust(kiCred, trustBasis)) {
                        log.debug("Successfully established trust of KeyInfo-derived credential");
                        return true;
                    } else {
                        log.debug("Failed to establish trust of KeyInfo-derived credential");
                    }
                }
            }
        } else {
            log.debug("Signature contained no KeyInfo element, could not resolve verification credentials");
        }

        log.debug("Failed to verify signature and/or establish trust using any KeyInfo-derived credentials");
        return false;
    }

    /**
     * Evaluate the untrusted KeyInfo-derived credential with respect to the specified trusted information.
     * 
     * @param untrustedCredential the untrusted credential being evaluated
     * @param trustBasis the information which serves as the basis for trust evaluation
     * 
     * @return true if the trust can be established for the untrusted credential, otherwise false
     * 
     * @throws SecurityException if an error occurs during trust processing
     */
    protected abstract boolean evaluateTrust(Credential untrustedCredential, TrustBasisType trustBasis)
            throws SecurityException;

    /**
     * Attempt to verify a signature using the key from the supplied credential.
     * 
     * @param signature the signature on which to attempt verification
     * @param credential the credential containing the candidate validation key
     * @return true if the signature can be verified using the key from the credential, otherwise false
     */
    protected boolean verifySignature(Signature signature, Credential credential) {
        SignatureValidator validator = new SignatureValidator(credential);
        try {
            validator.validate(signature);
        } catch (ValidationException e) {
            log.debug("Signature validation using candidate validation credential failed", e);
            return false;
        }
        
        log.debug("Signature validation using candidate credential was successful");
        return true;
    }

    /**
     * Check the signature and credential criteria for required values.
     * 
     * @param signature the signature to be evaluated
     * @param trustBasisCriteria the set of trusted credential criteria
     * @throws SecurityException thrown if required values are absent or otherwise invalid
     */
    protected void checkParams(Signature signature, CriteriaSet trustBasisCriteria) throws SecurityException {

        if (signature == null) {
            throw new SecurityException("Signature was null");
        }
        if (trustBasisCriteria == null) {
            throw new SecurityException("Trust basis criteria set was null");
        }
        if (trustBasisCriteria.isEmpty()) {
            throw new SecurityException("Trust basis criteria set was empty");
        }
    }

    /**
     * Check the signature and credential criteria for required values.
     * 
     * @param signature the signature to be evaluated
     * @param content the data over which the signature was computed
     * @param algorithmURI the signing algorithm URI which was used
     * @param trustBasisCriteria the set of trusted credential criteria
     * @throws SecurityException thrown if required values are absent or otherwise invalid
     */
    protected void checkParamsRaw(byte[] signature, byte[] content, String algorithmURI, CriteriaSet trustBasisCriteria)
            throws SecurityException {

        if (signature == null || signature.length == 0) {
            throw new SecurityException("Signature byte array was null or empty");
        }
        if (content == null || content.length == 0) {
            throw new SecurityException("Content byte array was null or empty");
        }
        if (DatatypeHelper.isEmpty(algorithmURI)) {
            throw new SecurityException("Signature algorithm was null or empty");
        }
        if (trustBasisCriteria == null) {
            throw new SecurityException("Trust basis criteria set was null");
        }
        if (trustBasisCriteria.isEmpty()) {
            throw new SecurityException("Trust basis criteria set was empty");
        }
    }

}
