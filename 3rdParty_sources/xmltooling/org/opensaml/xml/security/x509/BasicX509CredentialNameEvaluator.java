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

package org.opensaml.xml.security.x509;

import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A basic implementaion of {@link X509CredentialNameEvaluator} which evaluates various identifiers 
 * extracted from an {@link X509Credential}'s entity certificate against a set of trusted names.
 * 
 * <p>
 * Supported types of entity certificate-derived names for name checking purposes are:
 * <ol>
 * <li>Subject alternative names.</li>
 * <li>The first (i.e. most specific) common name (CN) from the subject distinguished name.</li>
 * <li>The complete subject distinguished name.</li>
 * </ol>
 * </p>
 * 
 * <p>
 * Name checking is enabled by default for all of the supported name types. The types of subject alternative names to
 * process are specified by using the appropriate constant values defined in {@link X509Util}. By default the following
 * types of subject alternative names are checked: DNS ({@link X509Util#DNS_ALT_NAME}) 
 * and URI ({@link X509Util#URI_ALT_NAME}).
 * </p>
 * 
 * <p>
 * The subject distinguished name from the entity certificate is compared to the trusted key names for complete DN
 * matching purposes by parsing each trusted key name into an {@link X500Principal} as returned by the configured
 * instance of {@link X500DNHandler}. The resulting distinguished name is then compared with the certificate subject
 * using {@link X500Principal#equals(Object)}. The default X500DNHandler used is {@link InternalX500DNHandler}.
 * </p>
 * 
 */
public class BasicX509CredentialNameEvaluator implements X509CredentialNameEvaluator {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(BasicX509CredentialNameEvaluator.class);

    /** Flag as to whether to perform name checking using credential's subject alt names. */
    private boolean checkSubjectAltNames;

    /** Flag as to whether to perform name checking using credential's subject DN's common name (CN). */
    private boolean checkSubjectDNCommonName;

    /** Flag as to whether to perform name checking using credential's subject DN. */
    private boolean checkSubjectDN;

    /** The set of types of subject alternative names to process. */
    private Set<Integer> subjectAltNameTypes;

    /** Responsible for parsing and serializing X.500 names to/from {@link X500Principal} instances. */
    private X500DNHandler x500DNHandler;

    /** Constructor. */
    public BasicX509CredentialNameEvaluator() {

        x500DNHandler = new InternalX500DNHandler();
        subjectAltNameTypes = new HashSet<Integer>(5);

        // Add some defaults
        setCheckSubjectAltNames(true);
        setCheckSubjectDNCommonName(true);
        setCheckSubjectDN(true);
        subjectAltNameTypes.add(X509Util.DNS_ALT_NAME);
        subjectAltNameTypes.add(X509Util.URI_ALT_NAME);
    }

    /**
     * Gets whether any of the supported name type checking is currently enabled.
     * 
     * @return true if any of the supported name type checking categories is currently enabled, false otherwise
     */
    public boolean isNameCheckingActive() {
        return checkSubjectAltNames() || checkSubjectDNCommonName() || checkSubjectDN();
    }

    /**
     * The set of types of subject alternative names to process.
     * 
     * Name types are represented using the constant OID tag name values defined in {@link X509Util}.
     * 
     * 
     * @return the modifiable set of alt name identifiers
     */
    public Set<Integer> getSubjectAltNameTypes() {
        return subjectAltNameTypes;
    }

    /**
     * Gets whether to check the credential's entity certificate subject alt names against the trusted key
     * name values.
     * 
     * @return whether to check the credential's entity certificate subject alt names against the trusted key
     *         names
     */
    public boolean checkSubjectAltNames() {
        return checkSubjectAltNames;
    }

    /**
     * Sets whether to check the credential's entity certificate subject alt names against the trusted key
     * name values.
     * 
     * @param check whether to check the credential's entity certificate subject alt names against the trusted
     *            key names
     */
    public void setCheckSubjectAltNames(boolean check) {
        checkSubjectAltNames = check;
    }

    /**
     * Gets whether to check the credential's entity certificate subject DN's common name (CN) against the
     * trusted key name values.
     * 
     * @return whether to check the credential's entity certificate subject DN's CN against the trusted key
     *         names
     */
    public boolean checkSubjectDNCommonName() {
        return checkSubjectDNCommonName;
    }

    /**
     * Sets whether to check the credential's entity certificate subject DN's common name (CN) against the
     * trusted key name values.
     * 
     * @param check whether to check the credential's entity certificate subject DN's CN against the trusted
     *            key names
     */
    public void setCheckSubjectDNCommonName(boolean check) {
        checkSubjectDNCommonName = check;
    }

    /**
     * Gets whether to check the credential's entity certificate subject DN against the trusted key name
     * values.
     * 
     * @return whether to check the credential's entity certificate subject DN against the trusted key names
     */
    public boolean checkSubjectDN() {
        return checkSubjectDN;
    }

    /**
     * Sets whether to check the credential's entity certificate subject DN against the trusted key name
     * values.
     * 
     * @param check whether to check the credential's entity certificate subject DN against the trusted key
     *            names
     */
    public void setCheckSubjectDN(boolean check) {
        checkSubjectDN = check;
    }

    /**
     * Get the handler which process X.500 distinguished names.
     * 
     * Defaults to {@link InternalX500DNHandler}.
     * 
     * @return returns the X500DNHandler instance
     */
    public X500DNHandler getX500DNHandler() {
        return x500DNHandler;
    }

    /**
     * Set the handler which process X.500 distinguished names.
     * 
     * Defaults to {@link InternalX500DNHandler}.
     * 
     * @param handler the new X500DNHandler instance
     */
    public void setX500DNHandler(X500DNHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("X500DNHandler may not be null");
        }
        x500DNHandler = handler;
    }

    /**
     * {@inheritDoc} 
     * 
     * <p>
     * If the set of trusted names is null or empty, or if no supported name types are configured to be
     * checked, then the evaluation is considered successful.
     * </p>
     * 
     */
    @SuppressWarnings("unchecked")
    public boolean evaluate(X509Credential credential, Set<String> trustedNames) throws SecurityException {
        if (!isNameCheckingActive()) {
            log.debug("No trusted name options are active, skipping name evaluation");
            return true;
        } else if (trustedNames == null || trustedNames.isEmpty()) {
            log.debug("Supplied trusted names are null or empty, skipping name evaluation");
            return true;
        }

        if (log.isDebugEnabled()) {
            log.debug("Checking trusted names against credential: {}",
                    X509Util.getIdentifiersToken(credential, x500DNHandler));
            log.debug("Trusted names being evaluated are: {}",
                    trustedNames.toString());
        }        
        return processNameChecks(credential, trustedNames);
    }

    /**
     * Process any name checks that are enabled.
     * 
     * @param credential the credential for the entity to validate
     * @param trustedNames trusted names against which the credential will be evaluated
     * @return if true the name check succeeds, false if not
     */
    protected boolean processNameChecks(X509Credential credential, Set<String> trustedNames) {
        X509Certificate entityCertificate = credential.getEntityCertificate();

        if (checkSubjectAltNames()) {
            if (processSubjectAltNames(entityCertificate, trustedNames)) {
                if (log.isDebugEnabled()) {
                    log.debug("Credential {} passed name check based on subject alt names.",
                            X509Util.getIdentifiersToken(credential, x500DNHandler));
                }                
                return true;
            }
        }

        if (checkSubjectDNCommonName()) {
            if (processSubjectDNCommonName(entityCertificate, trustedNames)) {
                if (log.isDebugEnabled()) {
                    log.debug("Credential {} passed name check based on subject common name.",
                            X509Util.getIdentifiersToken(credential, x500DNHandler));
                }                
                return true;
            }
        }

        if (checkSubjectDN()) {
            if (processSubjectDN(entityCertificate, trustedNames)) {
                if (log.isDebugEnabled()) {
                    log.debug("Credential {} passed name check based on subject DN.",
                            X509Util.getIdentifiersToken(credential, x500DNHandler));
                }                
                return true;
            }
        }

        log.error("Credential failed name check: " 
                + X509Util.getIdentifiersToken(credential, x500DNHandler));
        return false;
    }

    /**
     * Process name checking for a certificate subject DN's common name.
     * 
     * @param certificate the certificate to process
     * @param trustedNames the set of trusted names
     * 
     * @return true if the subject DN common name matches the set of trusted names, false otherwise
     * 
     */
    protected boolean processSubjectDNCommonName(X509Certificate certificate, Set<String> trustedNames) {
        log.debug("Processing subject DN common name");
        X500Principal subjectPrincipal = certificate.getSubjectX500Principal();
        List<String> commonNames = X509Util.getCommonNames(subjectPrincipal);
        if (commonNames == null || commonNames.isEmpty()) {
            return false;
        }
        // TODO We only check the first one returned by X509Util. Maybe we should check all,
        // if there are multiple CN AVA's from the same (first) RDN.
        String commonName = commonNames.get(0);
        log.debug("Extracted common name from certificate: {}", commonName);

        if (DatatypeHelper.isEmpty(commonName)) {
            return false;
        }
        if (trustedNames.contains(commonName)) {
            log.debug("Matched subject DN common name to trusted names: {}", commonName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Process name checking for the certificate subject DN.
     * 
     * @param certificate the certificate to process
     * @param trustedNames the set of trusted names
     * 
     * @return true if the subject DN matches the set of trusted names, false otherwise
     */
    protected boolean processSubjectDN(X509Certificate certificate, Set<String> trustedNames) {
        log.debug("Processing subject DN");
        X500Principal subjectPrincipal = certificate.getSubjectX500Principal();

        if (log.isDebugEnabled()) {
            log.debug("Extracted X500Principal from certificate: {}", x500DNHandler.getName(subjectPrincipal));
        }        
        for (String trustedName : trustedNames) {
            X500Principal trustedNamePrincipal = null;
            try {
                trustedNamePrincipal = x500DNHandler.parse(trustedName);
                log.debug("Evaluating principal successfully parsed from trusted name: {}", trustedName);
                if (subjectPrincipal.equals(trustedNamePrincipal)) {
                    if (log.isDebugEnabled()) {
                        log.debug("Matched subject DN to trusted names: {}", x500DNHandler.getName(subjectPrincipal));
                    }
                    return true;
                }
            } catch (IllegalArgumentException e) {
                // Do nothing, probably wasn't a distinguished name.
                // TODO maybe try and match only the "suspected" DN values above
                // - maybe match with regex for '='or something
                log.debug("Trusted name was not a DN or could not be parsed: {}", trustedName);
                continue;
            }
        }
        return false;
    }

    /**
     * Process name checking for the subject alt names within the certificate.
     * 
     * @param certificate the certificate to process
     * @param trustedNames the set of trusted names
     * 
     * @return true if one of the subject alt names matches the set of trusted names, false otherwise
     */
    protected boolean processSubjectAltNames(X509Certificate certificate, Set<String> trustedNames) {
        log.debug("Processing subject alt names");
        Integer[] nameTypes = new Integer[subjectAltNameTypes.size()];
        subjectAltNameTypes.toArray(nameTypes);
        List altNames = X509Util.getAltNames(certificate, nameTypes);

        log.debug("Extracted subject alt names from certificate: {}", altNames);

        for (Object altName : altNames) {
            if (trustedNames.contains(altName)) {
                log.debug("Matched subject alt name to trusted names: {}", altName.toString());
                return true;
            }
        }
        return false;
    }
    
}