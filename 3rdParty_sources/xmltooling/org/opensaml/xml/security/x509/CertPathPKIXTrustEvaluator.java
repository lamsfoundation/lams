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

import java.security.GeneralSecurityException;
import java.security.cert.CRL;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opensaml.xml.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link PKIXTrustEvaluator} that is based on the Java CertPath API.
 */
public class CertPathPKIXTrustEvaluator implements PKIXTrustEvaluator {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(CertPathPKIXTrustEvaluator.class);
    
    /** 
     * Responsible for parsing and serializing X.500 names to/from {@link javax.security.auth.x500.X500Principal} 
     * instances.
     */
    private X500DNHandler x500DNHandler;
    
    /** Options influencing processing behavior. */
    private PKIXValidationOptions options;

    /** Constructor. */
    public CertPathPKIXTrustEvaluator() {
        options = new PKIXValidationOptions();
        x500DNHandler = new InternalX500DNHandler();
    }
    
    /**
     * Constructor.
     * 
     * @param newOptions PKIX validation options
     */
    public CertPathPKIXTrustEvaluator(PKIXValidationOptions newOptions) {
        if (newOptions == null) {
            throw new IllegalArgumentException("PKIXValidationOptions may not be null");
        }
        options = newOptions;
        x500DNHandler = new InternalX500DNHandler();
    }
    
    /** {@inheritDoc} */
    public PKIXValidationOptions getPKIXValidationOptions() {
        return options;
    }

    /**
     * Set the desired PKIX validation options set.
     * 
     * @param newOptions the new set of options
     */
    public void setPKIXValidationOptions(PKIXValidationOptions newOptions) {
        if (newOptions == null) {
            throw new IllegalArgumentException("PKIXValidationOptions may not be null");
        }
        options = newOptions;
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

    /** {@inheritDoc} */
    public boolean validate(PKIXValidationInformation validationInfo, X509Credential untrustedCredential)
            throws SecurityException {
        
        if (log.isDebugEnabled()) {
            log.debug("Attempting PKIX path validation on untrusted credential: {}",
                    X509Util.getIdentifiersToken(untrustedCredential, x500DNHandler));
        }        
        
        try {
            PKIXBuilderParameters params = getPKIXBuilderParameters(validationInfo, untrustedCredential);

            log.trace("Building certificate validation path");

            CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");
            PKIXCertPathBuilderResult buildResult = (PKIXCertPathBuilderResult) builder.build(params);
            if (log.isDebugEnabled()) {
                logCertPathDebug(buildResult, untrustedCredential.getEntityCertificate());
                log.debug("PKIX validation succeeded for untrusted credential: {}",
                        X509Util.getIdentifiersToken(untrustedCredential, x500DNHandler));
            }            
            return true;

        } catch (CertPathBuilderException e) {
            if (log.isTraceEnabled()) {
                log.trace("PKIX path construction failed for untrusted credential: " 
                        + X509Util.getIdentifiersToken(untrustedCredential, x500DNHandler), e);
            } else {
                log.error("PKIX path construction failed for untrusted credential: " 
                        + X509Util.getIdentifiersToken(untrustedCredential, x500DNHandler) + ": " + e.getMessage());
            }
            return false;
        } catch (GeneralSecurityException e) {
            log.error("PKIX validation failure", e);
            throw new SecurityException("PKIX validation failure", e);
        }
    }

    /**
     * Creates the set of PKIX builder parameters to use when building the cert path builder.
     * 
     * @param validationInfo PKIX validation information
     * @param untrustedCredential credential to be validated
     * 
     * @return PKIX builder params
     * 
     * @throws GeneralSecurityException thrown if the parameters can not be created
     */
    protected PKIXBuilderParameters getPKIXBuilderParameters(PKIXValidationInformation validationInfo,
            X509Credential untrustedCredential) throws GeneralSecurityException {
        Set<TrustAnchor> trustAnchors = getTrustAnchors(validationInfo);
        if (trustAnchors == null || trustAnchors.isEmpty()) {
            throw new GeneralSecurityException(
                    "Unable to validate X509 certificate, no trust anchors found in the PKIX validation information");
        }

        X509CertSelector selector = new X509CertSelector();
        selector.setCertificate(untrustedCredential.getEntityCertificate());

        log.trace("Adding trust anchors to PKIX validator parameters");
        PKIXBuilderParameters params = new PKIXBuilderParameters(trustAnchors, selector);

        Integer effectiveVerifyDepth = getEffectiveVerificationDepth(validationInfo);
        log.trace("Setting max verification depth to: {} ", effectiveVerifyDepth);
        params.setMaxPathLength(effectiveVerifyDepth);

        CertStore certStore = buildCertStore(validationInfo, untrustedCredential);
        params.addCertStore(certStore);

        boolean isForceRevocationEnabled = false;
        boolean forcedRevocation = false;
        if (options instanceof CertPathPKIXValidationOptions) {
           CertPathPKIXValidationOptions certpathOptions = (CertPathPKIXValidationOptions) options;
           isForceRevocationEnabled = certpathOptions.isForceRevocationEnabled();
           forcedRevocation = certpathOptions.isRevocationEnabled();
        }
        
        if (isForceRevocationEnabled) {
            log.trace("PKIXBuilderParameters#setRevocationEnabled is being forced to: {}", forcedRevocation);
            params.setRevocationEnabled(forcedRevocation);
        } else {
            if (storeContainsCRLs(certStore)) {
                log.trace("At least one CRL was present in cert store, enabling revocation checking");
                params.setRevocationEnabled(true);
            } else {
                log.trace("No CRLs present in cert store, disabling revocation checking");
                params.setRevocationEnabled(false);
            }
        }

        return params;
    }

    /**
     * Determine whether there are any CRL's in the {@link CertStore} that is to be used.
     * 
     * @param certStore the cert store that will be used for validation
     * @return true if the store contains at least 1 CRL instance, false otherwise
     */
    protected boolean storeContainsCRLs(CertStore certStore) {
        Collection<? extends CRL> crls = null;
        try {
            //Save some cycles and memory: Collection cert store allows null as specifier to return all.
            //crls = certStore.getCRLs( new X509CRLSelector() );
            crls = certStore.getCRLs(null);
        } catch (CertStoreException e) {
            log.error("Error examining cert store for CRL's, treating as if no CRL's present", e);
            return false;
        }
        if (crls != null && !crls.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Get the effective maximum path depth to use when constructing PKIX cert path builder parameters.
     * 
     * @param validationInfo PKIX validation information
     * @return the effective max verification depth to use
     */
    protected Integer getEffectiveVerificationDepth(PKIXValidationInformation validationInfo) {
        Integer effectiveVerifyDepth = validationInfo.getVerificationDepth();
        if (effectiveVerifyDepth == null) {
            effectiveVerifyDepth = options.getDefaultVerificationDepth();
        }
        return effectiveVerifyDepth;
    }

    /**
     * Creates the collection of trust anchors to use during validation.
     * 
     * @param validationInfo PKIX validation information
     * 
     * @return trust anchors to use during validation
     */
    protected Set<TrustAnchor> getTrustAnchors(PKIXValidationInformation validationInfo) {
        Collection<X509Certificate> validationCertificates = validationInfo.getCertificates();

        log.trace("Constructing trust anchors for PKIX validation");
        Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
        for (X509Certificate cert : validationCertificates) {
            trustAnchors.add(buildTrustAnchor(cert));
        }

        if (log.isTraceEnabled()) {
            for (TrustAnchor anchor : trustAnchors) {
                log.trace("TrustAnchor: {}", anchor.toString());
            }
        }

        return trustAnchors;
    }

    /**
     * Build a trust anchor from the given X509 certificate.
     * 
     * This could for example be extended by subclasses to add custom name constraints, if desired.
     * 
     * @param cert the certificate which serves as the trust anchor
     * @return the newly constructed TrustAnchor
     */
    protected TrustAnchor buildTrustAnchor(X509Certificate cert) {
        return new TrustAnchor(cert, null);
    }

    /**
     * Creates the certificate store that will be used during validation.
     * 
     * @param validationInfo PKIX validation information
     * @param untrustedCredential credential to be validated
     * 
     * @return certificate store used during validation
     * 
     * @throws GeneralSecurityException thrown if the certificate store can not be created from the cert and CRL
     *             material
     */
    protected CertStore buildCertStore(PKIXValidationInformation validationInfo, X509Credential untrustedCredential)
            throws GeneralSecurityException {

        log.trace("Creating cert store to use during path validation");

        log.trace("Adding entity certificate chain to cert store");
        List<Object> storeMaterial = new ArrayList<Object>(untrustedCredential.getEntityCertificateChain());
        if (log.isTraceEnabled()) {
            for (X509Certificate cert : untrustedCredential.getEntityCertificateChain()) {
                log.trace(String.format("Added X509Certificate from entity cert chain to cert store "
                        + "with subject name '%s' issued by '%s' with serial number '%s'",
                        x500DNHandler.getName(cert.getSubjectX500Principal()),
                        x500DNHandler.getName(cert.getIssuerX500Principal()),
                        cert.getSerialNumber().toString()));
            }
        }
        
        Date now = new Date();
        
        if (validationInfo.getCRLs() != null && !validationInfo.getCRLs().isEmpty()) {
            log.trace("Processing CRL's from PKIX info set");
            addCRLsToStoreMaterial(storeMaterial, validationInfo.getCRLs(), now);
        }        
        
        if (untrustedCredential.getCRLs() != null && !untrustedCredential.getCRLs().isEmpty() 
                && options.isProcessCredentialCRLs()) {
            log.trace("Processing CRL's from untrusted credential");
            addCRLsToStoreMaterial(storeMaterial, untrustedCredential.getCRLs(), now);
        }        
        
        return CertStore.getInstance("Collection", new CollectionCertStoreParameters(storeMaterial));
    }
    
    /**
     * Add CRL's from the specified collection to the list of certs and CRL's being collected
     * for the CertStore.
     * 
     * @param storeMaterial list of certs and CRL's to be updated.
     * @param crls collection of CRL's to be processed
     * @param now current date/time
     */
    protected void addCRLsToStoreMaterial(List<Object> storeMaterial, Collection<X509CRL> crls, Date now) {
        for (X509CRL crl : crls) {
            boolean isEmpty = crl.getRevokedCertificates() == null || crl.getRevokedCertificates().isEmpty();
            boolean isExpired = crl.getNextUpdate().before(now);
            if (!isEmpty || options.isProcessEmptyCRLs()) {
                if (!isExpired || options.isProcessExpiredCRLs()) {
                    storeMaterial.add(crl);
                    if (log.isTraceEnabled()) {
                        log.trace("Added X509CRL to cert store from issuer {} dated {}",
                                x500DNHandler.getName(crl.getIssuerX500Principal()), crl.getThisUpdate());
                        if (isEmpty) {
                            log.trace("X509CRL added to cert store from issuer {} dated {} was empty",
                                    x500DNHandler.getName(crl.getIssuerX500Principal()), crl.getThisUpdate());
                        }
                    }
                    if (isExpired) {
                        log.warn("Using X509CRL from issuer {} with a nextUpdate in the past: {}",
                                x500DNHandler.getName(crl.getIssuerX500Principal()), crl.getNextUpdate());
                    }
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("Expired X509CRL not added to cert store, from issuer {} nextUpdate {}",
                                x500DNHandler.getName(crl.getIssuerX500Principal()), crl.getNextUpdate());
                    }
                }
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Empty X509CRL not added to cert store, from issuer {} dated {}",
                            x500DNHandler.getName(crl.getIssuerX500Principal()), crl.getThisUpdate());
                }
            }
        }
    }

    /**
     * Log information from the constructed cert path at level debug.
     * 
     * @param buildResult the PKIX cert path builder result containing the cert path and trust anchor
     * @param targetCert the cert untrusted certificate that was being evaluated
     */
    private void logCertPathDebug(PKIXCertPathBuilderResult buildResult, X509Certificate targetCert) {
        log.debug("Built valid PKIX cert path");
        log.debug("Target certificate: {}", x500DNHandler.getName(targetCert.getSubjectX500Principal()));
        for (Certificate cert : buildResult.getCertPath().getCertificates()) {
            log.debug("CertPath certificate: {}", x500DNHandler.getName(((X509Certificate) cert)
                    .getSubjectX500Principal()));
        }
        TrustAnchor ta = buildResult.getTrustAnchor();
        if (ta.getTrustedCert() != null) {
            log.debug("TrustAnchor: {}", x500DNHandler.getName(ta.getTrustedCert().getSubjectX500Principal()));
        } else if (ta.getCA() != null) {
            log.debug("TrustAnchor: {}", x500DNHandler.getName(ta.getCA()));
        } else {
            log.debug("TrustAnchor: {}", ta.getCAName());
        }
    }

}