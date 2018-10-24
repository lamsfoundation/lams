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

package org.opensaml.xml.security.keyinfo.provider;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialContext;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.security.keyinfo.KeyInfoProvider;
import org.opensaml.xml.security.keyinfo.KeyInfoResolutionContext;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.security.x509.InternalX500DNHandler;
import org.opensaml.xml.security.x509.X500DNHandler;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.security.x509.X509Util;
import org.opensaml.xml.signature.KeyValue;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.signature.X509Digest;
import org.opensaml.xml.signature.X509IssuerSerial;
import org.opensaml.xml.signature.X509SKI;
import org.opensaml.xml.signature.X509SubjectName;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.LazySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link KeyInfoProvider} which provides basic support for extracting a {@link X509Credential} 
 * from an {@link X509Data} child of KeyInfo.
 * 
 * This provider supports only inline {@link X509Certificate}'s and  {@link X509CRL}'s.
 * If only one certificate is present, it is assumed to be the end-entity certificate containing
 * the public key represented by this KeyInfo.  If multiple certificates are present, and any instances
 * of {@link X509SubjectName}, {@link X509IssuerSerial}, or {@link X509SKI} are also present, they
 * will be used to identify the end-entity certificate, in accordance with the XML Signature specification.
 * If a public key from a previously resolved {@link KeyValue} is available in the resolution context,
 * it will also be used to identify the end-entity certificate. If the end-entity certificate can not
 * otherwise be identified, the cert contained in the first X509Certificate element will be treated as
 * the end-entity certificate.
 * 
 */
public class InlineX509DataProvider extends AbstractKeyInfoProvider {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(InlineX509DataProvider.class);
    
    /** Responsible for parsing and serializing X.500 names to/from {@link X500Principal} instances. */
    private X500DNHandler x500DNHandler;
    
    /**
     * Constructor.
     */
    public InlineX509DataProvider() {
        x500DNHandler = new InternalX500DNHandler();
    }

    /**
     * Get the handler which process X.500 distinguished names.
     * 
     * @return returns the X500DNHandler instance
     */
    public X500DNHandler getX500DNHandler() {
        return x500DNHandler;
    }

    /**
     * Set the handler which process X.500 distinguished names.
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
    public boolean handles(XMLObject keyInfoChild) {
        return keyInfoChild instanceof X509Data;
    }

    /** {@inheritDoc} */
    public Collection<Credential> process(KeyInfoCredentialResolver resolver, XMLObject keyInfoChild, 
            CriteriaSet criteriaSet, KeyInfoResolutionContext kiContext) throws SecurityException {
        
        if (! handles(keyInfoChild)) {
            return null;
        }
        
        X509Data x509Data = (X509Data) keyInfoChild;
        
        log.debug("Attempting to extract credential from an X509Data");
        
        List<X509Certificate> certs = extractCertificates(x509Data);
        if (certs.isEmpty()) {
            log.info("The X509Data contained no X509Certificate elements, skipping credential extraction");
            return null;
        }
        List<X509CRL> crls = extractCRLs(x509Data);
        
        PublicKey resolvedPublicKey = null;
        if (kiContext != null && kiContext.getKey() != null && kiContext.getKey() instanceof PublicKey) {
            resolvedPublicKey = (PublicKey) kiContext.getKey();
        }
        X509Certificate entityCert = findEntityCert(certs, x509Data, resolvedPublicKey);
        if (entityCert == null) {
            log.warn("The end-entity cert could not be identified, skipping credential extraction");
            return null;
        }
        
        BasicX509Credential cred = new BasicX509Credential();
        cred.setEntityCertificate(entityCert);
        cred.setCRLs(crls);
        cred.setEntityCertificateChain(certs);
        
        if (kiContext != null) {
            cred.getKeyNames().addAll(kiContext.getKeyNames());
        }
        
        CredentialContext credContext = buildCredentialContext(kiContext);
        if (credContext != null) {
            cred.getCredentalContextSet().add(credContext);
        }
        
        LazySet<Credential> credentialSet = new LazySet<Credential>();
        credentialSet.add(cred);
        return credentialSet;
    }

    /**
     * Extract CRL's from the X509Data.
     * 
     * @param x509Data the X509Data element
     * @return a list of X509CRLs
     * @throws SecurityException thrown if there is an error extracting CRL's
     */
    private List<X509CRL> extractCRLs(X509Data x509Data) throws SecurityException {
        List<X509CRL> crls = null;
        try {
            crls = KeyInfoHelper.getCRLs(x509Data);
        } catch (CRLException e) {
            log.error("Error extracting CRL's from X509Data", e);
            throw new SecurityException("Error extracting CRL's from X509Data", e);
        }
        
        log.debug("Found {} X509CRLs", crls.size());
        return crls;
    }

    /**
     * Extract certificates from the X509Data.
     * 
     * @param x509Data the X509Data element
     * @return a list of X509Certificates
     * @throws SecurityException thrown if there is an error extracting certificates
     */
    private List<X509Certificate> extractCertificates(X509Data x509Data) throws SecurityException {
        List<X509Certificate> certs = null;
        try {
            certs = KeyInfoHelper.getCertificates(x509Data);
        } catch (CertificateException e) {
            log.error("Error extracting certificates from X509Data", e);
            throw new SecurityException("Error extracting certificates from X509Data", e);
        }
        log.debug("Found {} X509Certificates", certs.size());
        return certs;
    }

    /**
     * Find the end-entity cert in the list of certs contained in the X509Data.
     * 
     * @param certs list of {@link java.security.cert.X509Certificate}
     * @param x509Data X509Data element which might contain other info helping to finding the end-entity cert
     * @param resolvedKey a key which might have previously been resolved from a KeyValue
     * @return the end-entity certificate, if found
     */
    protected X509Certificate findEntityCert(List<X509Certificate> certs, X509Data x509Data, PublicKey resolvedKey) {
        if (certs == null || certs.isEmpty()) {
            return null;
        }
        
        // If there is only 1 certificate, treat it as the end-entity certificate
        if (certs.size() == 1) {
            log.debug("Single certificate was present, treating as end-entity certificate");
            return certs.get(0);
        }
        
        X509Certificate cert = null;
        
        //Check against public key already resolved in resolution context
        cert = findCertFromKey(certs, resolvedKey);
        if (cert != null) {
            log.debug("End-entity certificate resolved by matching previously resolved public key");
            return cert;
        }
 
        //Check against any subject names
        cert = findCertFromSubjectNames(certs, x509Data.getX509SubjectNames());
        if (cert != null) {
            log.debug("End-entity certificate resolved by matching X509SubjectName");
            return cert;
        }

        //Check against issuer serial
        cert = findCertFromIssuerSerials(certs, x509Data.getX509IssuerSerials());
        if (cert != null) {
            log.debug("End-entity certificate resolved by matching X509IssuerSerial");
            return cert;
        }

        //Check against any subject key identifiers
        cert = findCertFromSubjectKeyIdentifier(certs, x509Data.getX509SKIs());
        if (cert != null) {
            log.debug("End-entity certificate resolved by matching X509SKI");
            return cert;
        }
        
        cert = findCertFromDigest(certs, x509Data.getXMLObjects(X509Digest.DEFAULT_ELEMENT_NAME));
        if (cert != null) {
            log.debug("End-entity certificate resolved by matching X509Digest");
            return cert;
        }
        
        // TODO use some heuristic algorithm to try and figure it out based on the cert list alone.
        //      This would be in X509Utils or somewhere else external to this class.
        
        // As a final fallback, treat the first cert in the X509Data element as the entity cert
        log.debug("Treating the first certificate in the X509Data as the end-entity certificate");
        return certs.get(0);
    }
    
    /**
     * Find the certificate from the chain that contains the specified key.
     * 
     * @param certs list of certificates to evaluate
     * @param key key to use as search criteria
     * @return the matching certificate, or null
     */
    protected X509Certificate findCertFromKey(List<X509Certificate> certs, PublicKey key) {
        if (key != null) {
            for (X509Certificate cert : certs) {
                if (cert.getPublicKey().equals(key)) {
                    return cert;
                }
            }
        }
        return null;
    }
    
    /**
     * Find the certificate from the chain that contains one of the specified subject names.
     * 
     * @param certs list of certificates to evaluate
     * @param names X509 subject names to use as search criteria
     * @return the matching certificate, or null
     */
    protected X509Certificate findCertFromSubjectNames(List<X509Certificate> certs, List<X509SubjectName> names) {
        for (X509SubjectName subjectName : names) {
            if (! DatatypeHelper.isEmpty(subjectName.getValue())) {
                X500Principal subjectX500Principal = null;
                try {
                    subjectX500Principal = x500DNHandler.parse(subjectName.getValue());
                } catch (IllegalArgumentException e) {
                    log.warn("X500 subject name '{}' could not be parsed by configured X500DNHandler '{}'",
                            subjectName.getValue(), x500DNHandler.getClass().getName());
                    return null;
                }
                for (X509Certificate cert : certs) {
                    if (cert.getSubjectX500Principal().equals(subjectX500Principal)) {
                        return cert;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Find the certificate from the chain identified by one of the specified issuer serials.
     * 
     * @param certs list of certificates to evaluate
     * @param serials X509 issuer serials to use as search criteria
     * @return the matching certificate, or null
     */
    protected X509Certificate findCertFromIssuerSerials(List<X509Certificate> certs, List<X509IssuerSerial> serials) {
        for (X509IssuerSerial issuerSerial : serials) {
            if (issuerSerial.getX509IssuerName() == null || issuerSerial.getX509SerialNumber() == null) {
                continue;
            }
            String issuerNameValue = issuerSerial.getX509IssuerName().getValue();
            BigInteger serialNumber  = issuerSerial.getX509SerialNumber().getValue();
            if (! DatatypeHelper.isEmpty(issuerNameValue)) {
                X500Principal issuerX500Principal = null;
                try {
                    issuerX500Principal = x500DNHandler.parse(issuerNameValue);
                } catch (IllegalArgumentException e) {
                    log.warn("X500 issuer name '{}' could not be parsed by configured X500DNHandler '{}'",
                            issuerNameValue, x500DNHandler.getClass().getName());
                    return null;
                }
                for (X509Certificate cert : certs) {
                    if (cert.getIssuerX500Principal().equals(issuerX500Principal) &&
                            cert.getSerialNumber().equals(serialNumber)) {
                        return cert;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Find the certificate from the chain that contains one of the specified subject key identifiers.
     * 
     * @param certs list of certificates to evaluate
     * @param skis X509 subject key identifiers to use as search criteria
     * @return the matching certificate, or null
     */
    protected X509Certificate findCertFromSubjectKeyIdentifier(List<X509Certificate> certs, List<X509SKI> skis) {
        for (X509SKI ski : skis) {
            if (! DatatypeHelper.isEmpty(ski.getValue())) {
                byte[] xmlValue = Base64.decode(ski.getValue());
                for (X509Certificate cert : certs) {
                    byte[] certValue = X509Util.getSubjectKeyIdentifier(cert);
                    if (certValue != null && Arrays.equals(xmlValue, certValue)) {
                        return cert;
                    }
                }
            }
        }
        return null;
    } 

    /**
     * Find the certificate from the chain that matches one of the specified digests.
     * 
     * @param certs list of certificates to evaluate
     * @param digests X509 digests to use as search criteria
     * @return the matching certificate, or null
     */
    protected X509Certificate findCertFromDigest(List<X509Certificate> certs, List<XMLObject> digests) {
        byte[] certValue;
        byte[] xmlValue;
        
        for (XMLObject xo : digests) {
            if (!(xo instanceof X509Digest)) {
                continue;
            }
            X509Digest digest = (X509Digest) xo;
            if (!DatatypeHelper.isEmpty(digest.getValue())) {
                xmlValue = Base64.decode(digest.getValue());
                for (X509Certificate cert : certs) {
                    try {
                        certValue = X509Util.getX509Digest(cert, digest.getAlgorithm());
                        if (certValue != null && Arrays.equals(xmlValue, certValue)) {
                            return cert;
                        }
                    } catch (SecurityException e) {
                        // Ignore as no match.
                    }
                }
            }
        }
        return null;
    } 
}
