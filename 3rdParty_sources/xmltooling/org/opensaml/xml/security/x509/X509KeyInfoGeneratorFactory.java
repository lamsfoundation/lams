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

import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.EncryptionConstants;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.BasicKeyInfoGeneratorFactory;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.X509CRL;
import org.opensaml.xml.signature.X509Certificate;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.signature.X509Digest;
import org.opensaml.xml.signature.X509SKI;
import org.opensaml.xml.signature.impl.KeyInfoBuilder;
import org.opensaml.xml.signature.impl.X509DataBuilder;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.LazySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory implementation which produces instances of {@link KeyInfoGenerator} capable of 
 * handling the information contained within an {@link X509Credential}.
 * 
 * All boolean options default to false.  The default implementation of {@link X500DNHandler} used is
 * {@link InternalX500DNHandler}.  The default output format for subject and issuer DN's is RFC2253.
 * The default set of subject alternative names to process is empty.
 */
public class X509KeyInfoGeneratorFactory extends BasicKeyInfoGeneratorFactory {
    
    /** The set of options configured for the factory. */
    private X509Options options;
    
    /** Constructor. */
    public X509KeyInfoGeneratorFactory() {
        super();
        options = (X509Options) super.getOptions();
    }
    
    /** {@inheritDoc} */
    public Class<? extends Credential> getCredentialType() {
        return X509Credential.class;
    }

    /** {@inheritDoc} */
    public boolean handles(Credential credential) {
        return credential instanceof X509Credential;
    }

    /** {@inheritDoc} */
    public KeyInfoGenerator newInstance() {
        //TODO lock options during cloning ?
        X509Options newOptions = options.clone();
        return new X509KeyInfoGenerator(newOptions);
    }
    
    /**
     * Get the option to emit the CRL list as sequence of X509CRL elements within X509Data.
     * 
     * @return the option value
     */
    public boolean emitCRLs() {
        return options.emitCRLs;
    }

    /**
     * Set the option to emit the CRL list as sequence of X509CRL elements within X509Data.
     * 
     * @param newValue the new option value
     */
    public void setEmitCRLs(boolean newValue) {
        options.emitCRLs = newValue;
    }

    /**
     * Get the option to emit the entity certificate as an X509Certificate element within X509Data. 
     *
     * @return the option value
     */
    public boolean emitEntityCertificate() {
        return options.emitEntityCertificate;
    }

    /**
     * Set the option to emit the entity certificate as an X509Certificate element within X509Data. 
     *
     * @param newValue the new option value
     */
    public void setEmitEntityCertificate(boolean newValue) {
        options.emitEntityCertificate = newValue;
    }

    /**
     * Get the option to emit the entity certificate chain as sequence of X509Certificate elements within X509Data.
     * 
     * @return the option value
     */
    public boolean emitEntityCertificateChain() {
        return options.emitEntityCertificateChain;
    }

    /**
     * Set the option to emit the entity certificate chain as sequence of X509Certificate elements within X509Data.
     * 
     * @param newValue the new option value
     */
    public void setEmitEntityCertificateChain(boolean newValue) {
        options.emitEntityCertificateChain = newValue;
    }

    /**
     * Get the option to emit the entity certificate subject alternative name extension values as KeyName elements.
     * 
     * @return the option value
     */
    public boolean emitSubjectAltNamesAsKeyNames() {
        return options.emitSubjectAltNamesAsKeyNames;
    }

    /**
     * Set the option to emit the entity certificate subject alternative name extension values as KeyName elements.
     * 
     * @param newValue the new option value
     */
    public void setEmitSubjectAltNamesAsKeyNames(boolean newValue) {
        options.emitSubjectAltNamesAsKeyNames = newValue;
    }

    /**
     * Get the option to emit the entity certificate subject DN common name (CN) fields as KeyName elements.
     * 
     * @return the option value
     */
    public boolean emitSubjectCNAsKeyName() {
        return options.emitSubjectCNAsKeyName;
    }

    /**
     * Set the option to emit the entity certificate subject DN common name (CN) fields as KeyName elements.
     * 
     * @param newValue the new option value
     */
    public void setEmitSubjectCNAsKeyName(boolean newValue) {
        options.emitSubjectCNAsKeyName = newValue;
    }

    /**
     * Get the option to emit the entity certificate subject DN as a KeyName element.
     * 
     * @return the option value
     */
    public boolean emitSubjectDNAsKeyName() {
        return options.emitSubjectDNAsKeyName;
    }

    /**
     * Set the option to emit the entity certificate subject DN as a KeyName element.
     * 
     * @param newValue the new option value
     */
    public void setEmitSubjectDNAsKeyName(boolean newValue) {
        options.emitSubjectDNAsKeyName = newValue;
    }

    /**
     * Get the option to emit the entity certificate issuer name and serial number as 
     * an X509IssuerSerial element within X509Data.
     * 
     * @return the option value
     */
    public boolean emitX509IssuerSerial() {
        return options.emitX509IssuerSerial;
    }

    /**
     * Set the option to emit the entity certificate issuer name and serial number as 
     * an X509IssuerSerial element within X509Data.
     * 
     * @param newValue the new option value
     */
    public void setEmitX509IssuerSerial(boolean newValue) {
        options.emitX509IssuerSerial = newValue;
    }

    /**
     * Get the option to emit the entity certificate subject key identifier as an X509SKI element within X509Data.
     * 
     * @return the option value
     */
    public boolean emitX509SKI() {
        return options.emitX509SKI;
    }

    /**
     * Set the option to emit the entity certificate subject key identifier as an X509SKI element within X509Data.
     * 
     * @param newValue the new option value
     */
    public void setEmitX509SKI(boolean newValue) {
        options.emitX509SKI = newValue;
    }

    /**
     * Get the option to emit the entity certificate digest as an X509Digest element within X509Data.
     * 
     * @return the option value
     */
    public boolean emitX509Digest() {
        return options.emitX509Digest;
    }

    /**
     * Set the option to emit the entity certificate digest as an X509Digest element within X509Data.
     * 
     * @param newValue the new option value
     */
    public void setEmitX509Digest(boolean newValue) {
        options.emitX509Digest = newValue;
    }

    
    /**
     * Get the algorithm URI for X509Digest digests.
     * 
     * Defaults to SHA-256.
     * 
     * @return returns the digest algorithm URI
     */
    public String getX509DigestAlgorithmURI() {
        return options.x509DigestAlgorithmURI;
    }

    /**
     * Set the algorithm URI for X509Digest digests.
     * 
     * Defaults to SHA-256.
     * 
     * @param alg the new digest algorithmURI
     */
    public void setX509DigestAlgorithmURI(String alg) {
        options.x509DigestAlgorithmURI = alg;
    }
    
    /**
     * Get the option to emit the entity certificate subject DN as an X509SubjectName element within X509Data.
     * 
     * @return the option value
     */
    public boolean emitX509SubjectName() {
        return options.emitX509SubjectName;
    }

    /**
     * Set the option to emit the entity certificate subject DN as an X509SubjectName element within X509Data.
     * 
     * @param newValue the new option value
     */
    public void setEmitX509SubjectName(boolean newValue) {
        options.emitX509SubjectName = newValue;
    }

    /**
     * The set of types of subject alternative names to process.
     * 
     * Name types are represented using the constant OID tag name values defined 
     * in {@link X509Util}.
     * 
     * 
     * @return the modifiable set of alt name identifiers
     */
    public Set<Integer> getSubjectAltNames() {
        return options.subjectAltNames;
    }
    
    /**
     * Get the handler which process X.500 distinguished names.
     * 
     * Defaults to {@link InternalX500DNHandler}.
     * 
     * @return returns the X500DNHandler instance
     */
    public X500DNHandler getX500DNHandler() {
        return options.x500DNHandler;
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
        options.x500DNHandler = handler;
    }
    
    /**
     * Get the output format specifier for X.500 subject names.
     * 
     * Defaults to RFC2253 format. The meaning of this format specifier value
     * is dependent upon the implementation of {@link X500DNHandler} which is used.
     * 
     * @return returns the format specifier
     */
    public String getX500SubjectDNFormat() {
        return options.x500SubjectDNFormat;
    }

    /**
     * Set the output format specifier for X.500 subject names.
     * 
     * Defaults to RFC2253 format. The meaning of this format specifier value
     * is dependent upon the implementation of {@link X500DNHandler} which is used.
     * 
     * @param format the new X500DNHandler instance
     */
    public void setX500SubjectDNFormat(String format) {
        options.x500SubjectDNFormat = format;
    }
    
    /**
     * Get the output format specifier for X.500 issuer names.
     * 
     * Defaults to RFC2253 format. The meaning of this format specifier value
     * is dependent upon the implementation of {@link X500DNHandler} which is used.
     * 
     * @return returns the format specifier
     */
    public String getX500IssuerDNFormat() {
        return options.x500IssuerDNFormat;
    }

    /**
     * Set the output format specifier for X.500 issuer names.
     * 
     * Defaults to RFC2253 format. The meaning of this format specifier value
     * is dependent upon the implementation of {@link X500DNHandler} which is used.
     * 
     * @param format the new X500DNHandler instance
     */
    public void setX500IssuerDNFormat(String format) {
        options.x500IssuerDNFormat = format;
    }

    /** {@inheritDoc} */
    protected X509Options getOptions() {
        return options;
    }

    /** {@inheritDoc} */
    protected X509Options newOptions() {
        return new X509Options();
    }

    /**
     * An implementation of {@link KeyInfoGenerator} capable of  handling the information 
     * contained within a {@link X509Credential}.
     */
    public class X509KeyInfoGenerator extends BasicKeyInfoGenerator {

        /** Class logger. */
        private final Logger log = LoggerFactory.getLogger(X509KeyInfoGenerator.class);
        
        /** The set of options to be used by the generator.*/
        private X509Options options;
       
        /** Builder for KeyInfo objects. */
        private KeyInfoBuilder keyInfoBuilder;
        
        /** Builder for X509Data objects. */
        private X509DataBuilder x509DataBuilder;
       
        /**
         * Constructor.
         * 
         * @param newOptions the options to be used by the generator
         */
        protected X509KeyInfoGenerator(X509Options newOptions) {
            super(newOptions);
            options = newOptions;
            
            keyInfoBuilder = 
                (KeyInfoBuilder) Configuration.getBuilderFactory().getBuilder(KeyInfo.DEFAULT_ELEMENT_NAME);
            x509DataBuilder = 
                (X509DataBuilder) Configuration.getBuilderFactory().getBuilder(X509Data.DEFAULT_ELEMENT_NAME);
        }

        /** {@inheritDoc} */
        public KeyInfo generate(Credential credential) throws SecurityException {
            if ( ! (credential instanceof X509Credential) ) {
                log.warn("X509KeyInfoGenerator was passed a credential that was not an instance of X509Credential: {}",
                        credential.getClass().getName());
                return null;
            }
            X509Credential x509Credential = (X509Credential) credential;
            
            KeyInfo keyInfo =  super.generate(credential);
            if (keyInfo == null) {
                keyInfo = keyInfoBuilder.buildObject();
            }
            X509Data x509Data = x509DataBuilder.buildObject();
            
            processEntityCertificate(keyInfo, x509Data, x509Credential);
            processEntityCertificateChain(keyInfo, x509Data, x509Credential);
            processCRLs(keyInfo, x509Data, x509Credential);
            
            List<XMLObject> x509DataChildren = x509Data.getOrderedChildren();
            if (x509DataChildren != null && x509DataChildren.size() > 0) {
                keyInfo.getX509Datas().add(x509Data);
            }
            
            List<XMLObject> keyInfoChildren = keyInfo.getOrderedChildren();
            if (keyInfoChildren != null && keyInfoChildren.size() > 0) {
                return keyInfo;
            } else {
                return null;
            }
        }
        
        /** Process the value of {@link X509Credential#getEntityCertificate()}.
         * 
         * @param keyInfo the KeyInfo that is being built
         * @param x509Data the X509Data that is being built
         * @param credential the Credential that is being processed
         * @throws SecurityException thrown if the certificate data can not be encoded from the Java certificate object
         */
        protected void processEntityCertificate(KeyInfo keyInfo, X509Data x509Data, X509Credential credential) 
                throws SecurityException {
            
            if (credential.getEntityCertificate() == null) {
                return;
            }
            
            java.security.cert.X509Certificate javaCert = credential.getEntityCertificate();
            
            processCertX509DataOptions(x509Data, javaCert);
            processCertKeyNameOptions(keyInfo, javaCert);
            
            // The cert chain includes the entity cert, so don't add a duplicate
            if (options.emitEntityCertificate && ! options.emitEntityCertificateChain) {
                try {
                    X509Certificate xmlCert = KeyInfoHelper.buildX509Certificate(javaCert);
                    x509Data.getX509Certificates().add(xmlCert);
                } catch (CertificateEncodingException e) {
                    throw new SecurityException("Error generating X509Certificate element " 
                            + "from credential's end-entity certificate", e);
                }
            }
            
        }
        
        /**
         * Process the options related to generation of child elements of X509Data based on certificate data.
         * 
         * @param x509Data the X509Data element being processed.
         * @param cert the certificate being processed
         */
        protected void processCertX509DataOptions(X509Data x509Data, java.security.cert.X509Certificate cert) {
            processCertX509SubjectName(x509Data, cert);
            processCertX509IssuerSerial(x509Data, cert);
            processCertX509SKI(x509Data, cert);
            processCertX509Digest(x509Data, cert);
        }
        
        /**
         * Process the options related to generation of KeyName elements based on certificate data.
         * 
         * @param keyInfo the KeyInfo element being processed.
         * @param cert the certificate being processed
         */
        protected void processCertKeyNameOptions(KeyInfo keyInfo, java.security.cert.X509Certificate cert) {
            processSubjectDNKeyName(keyInfo, cert);
            processSubjectCNKeyName(keyInfo, cert);
            processSubjectAltNameKeyNames(keyInfo, cert);
        }
        
        /**
         * Process the options related to generation of the X509SubjectDN child element of X509Data 
         * based on certificate data.
         * 
         * @param x509Data the X509Data element being processed.
         * @param cert the certificate being processed
         */
        protected void processCertX509SubjectName(X509Data x509Data, java.security.cert.X509Certificate cert) {
            if (options.emitX509SubjectName) {
                String subjectNameValue = getSubjectName(cert);
                if (! DatatypeHelper.isEmpty(subjectNameValue)) {
                    x509Data.getX509SubjectNames().add( KeyInfoHelper.buildX509SubjectName(subjectNameValue));
                }
            }
        }
        
        /**
         * Process the options related to generation of the X509IssuerSerial child element of X509Data 
         * based on certificate data.
         * 
         * @param x509Data the X509Data element being processed.
         * @param cert the certificate being processed
         */ 
        protected void processCertX509IssuerSerial(X509Data x509Data, java.security.cert.X509Certificate cert) {
            if (options.emitX509IssuerSerial) {
                String issuerNameValue = getIssuerName(cert);
                if (! DatatypeHelper.isEmpty(issuerNameValue)) {
                    x509Data.getX509IssuerSerials().add( 
                            KeyInfoHelper.buildX509IssuerSerial(issuerNameValue, cert.getSerialNumber()) );
                }
            }
        }
        
        /**
         * Process the options related to generation of the X509SKI child element of X509Data 
         * based on certificate data.
         * 
         * @param x509Data the X509Data element being processed.
         * @param cert the certificate being processed
         */ 
        protected void processCertX509SKI(X509Data x509Data, java.security.cert.X509Certificate cert) {
            if (options.emitX509SKI) {
                X509SKI xmlSKI = KeyInfoHelper.buildX509SKI(cert);
                if (xmlSKI != null) {
                    x509Data.getX509SKIs().add(xmlSKI);
                }
            }
        }
        
        /**
         * Process the options related to generation of the X509Digest child element of X509Data 
         * based on certificate data.
         * 
         * @param x509Data the X509Data element being processed.
         * @param cert the certificate being processed
         */ 
        protected void processCertX509Digest(X509Data x509Data, java.security.cert.X509Certificate cert) {
            if (options.emitX509Digest) {
                try {
                    X509Digest xmlDigest = KeyInfoHelper.buildX509Digest(cert, options.x509DigestAlgorithmURI);
                    if (xmlDigest != null) {
                        x509Data.getXMLObjects(X509Digest.DEFAULT_ELEMENT_NAME).add(xmlDigest);
                    }
                } catch (CertificateEncodingException e) {
                    // TODO: should wrap in SecurityException once API can be changed
                    log.error("Can't digest certificate, certificate encoding error", e);
                } catch (NoSuchAlgorithmException e) {
                    // TODO: should wrap in SecurityException once API can be changed
                    log.error("Can't digest certificate, unsupported digest algorithm", e);
                }
            }
        }
        
        /**
         * Get subject name from a certificate, using the currently configured X500DNHandler
         * and subject DN output format.
         * 
         * @param cert the certificate being processed
         * @return the subject name
         */
        protected String getSubjectName(java.security.cert.X509Certificate cert) {
            if (cert == null) {
                return null;
            }
            if (! DatatypeHelper.isEmpty(options.x500SubjectDNFormat)) {
                return options.x500DNHandler.getName(cert.getSubjectX500Principal(), options.x500SubjectDNFormat);
            } else {
                return options.x500DNHandler.getName(cert.getSubjectX500Principal());
            }
        }
        
        /**
         * Get issuer name from a certificate, using the currently configured X500DNHandler
         * and issuer DN output format.
         * 
         * @param cert the certificate being processed
         * @return the issuer name
         */
        protected String getIssuerName(java.security.cert.X509Certificate cert) {
            if (cert == null) {
                return null;
            }
            if (! DatatypeHelper.isEmpty(options.x500IssuerDNFormat)) {
                return options.x500DNHandler.getName(cert.getIssuerX500Principal(), options.x500IssuerDNFormat);
            } else {
                return options.x500DNHandler.getName(cert.getIssuerX500Principal());
            }
        }

        /**
         * Process the options related to generation of KeyName elements based on the certificate's
         * subject DN value.
         * 
         * @param keyInfo the KeyInfo element being processed.
         * @param cert the certificate being processed
         */
        protected void processSubjectDNKeyName(KeyInfo keyInfo, java.security.cert.X509Certificate cert) {
            if (options.emitSubjectDNAsKeyName) {
                String subjectNameValue = getSubjectName(cert);
                if (! DatatypeHelper.isEmpty(subjectNameValue)) {
                   KeyInfoHelper.addKeyName(keyInfo, subjectNameValue); 
                }
            }
        }
        
        /**
         * Process the options related to generation of KeyName elements based on the
         * the common name field(s) of the certificate's subject DN.
         * 
         * @param keyInfo the KeyInfo element being processed.
         * @param cert the certificate being processed
         */
        protected void processSubjectCNKeyName(KeyInfo keyInfo, java.security.cert.X509Certificate cert) {
            if (options.emitSubjectCNAsKeyName) {
                for (String name : X509Util.getCommonNames(cert.getSubjectX500Principal())) {
                    if (! DatatypeHelper.isEmpty(name)) {
                        KeyInfoHelper.addKeyName(keyInfo, name);
                    }
                }
            }
        }
        
        /**
         * Process the options related to generation of KeyName elements based on subject
         * alternative name information within the certificate data.
         * 
         * @param keyInfo the KeyInfo element being processed.
         * @param cert the certificate being processed
         */
        protected void processSubjectAltNameKeyNames(KeyInfo keyInfo, java.security.cert.X509Certificate cert) {
            if (options.emitSubjectAltNamesAsKeyNames && options.subjectAltNames.size() > 0) {
                Integer[] nameTypes = new Integer[ options.subjectAltNames.size() ];
                options.subjectAltNames.toArray(nameTypes);
                for (Object altNameValue : X509Util.getAltNames(cert, nameTypes)) {
                    // Each returned value should either be a String or a DER-encoded byte array.
                    // See X509Certificate#getSubjectAlternativeNames for the type rules.
                    if (altNameValue instanceof String) {
                        KeyInfoHelper.addKeyName(keyInfo, (String) altNameValue);
                    } else if (altNameValue instanceof byte[]){
                        log.warn("Certificate contained an alt name value as a DER-encoded byte[] (not supported)");
                    } else {
                        log.warn("Certificate contained an alt name value with an unexpected type: {}",
                                altNameValue.getClass().getName());
                    }
                }
            }
        }
        
        /** Process the value of {@link X509Credential#getEntityCertificateChain()}.
         * 
         * @param keyInfo the KeyInfo that is being built
         * @param x509Data the X509Data that is being built
         * @param credential the Credential that is being processed
         * @throws SecurityException thrown if the certificate data can not be encoded from the Java certificate object
         */
        protected void processEntityCertificateChain(KeyInfo keyInfo, X509Data x509Data, X509Credential credential) 
                throws SecurityException {
            
            if (options.emitEntityCertificateChain && credential.getEntityCertificateChain() != null) {
                for (java.security.cert.X509Certificate javaCert : credential.getEntityCertificateChain()) {
                    try {
                        X509Certificate xmlCert = KeyInfoHelper.buildX509Certificate(javaCert);
                        x509Data.getX509Certificates().add(xmlCert);
                    } catch (CertificateEncodingException e) {
                        throw new SecurityException("Error generating X509Certificate element " 
                                + "from a certificate in credential's certificate chain", e);
                    }
                }
            }
        }

        /** Process the value of {@link X509Credential#getCRLs()}.
         * 
         * @param keyInfo the KeyInfo that is being built
         * @param x509Data the X509Data that is being built
         * @param credential the Credential that is being processed
         * @throws SecurityException thrown if the CRL data can not be encoded from the Java certificate object
         */
        protected void processCRLs(KeyInfo keyInfo, X509Data x509Data, X509Credential credential) 
                throws SecurityException {
            
            if (options.emitCRLs && credential.getCRLs() != null) {
                for (java.security.cert.X509CRL javaCRL : credential.getCRLs()) {
                    try {
                        X509CRL xmlCRL = KeyInfoHelper.buildX509CRL(javaCRL);
                        x509Data.getX509CRLs().add(xmlCRL);
                    } catch (CRLException e) {
                        throw new SecurityException("Error generating X509CRL element " 
                                + "from a CRL in credential's CRL list", e);
                    }
                }
            }
        }
        
    }
    
    /**
    * Options to be used in the production of a {@link KeyInfo} from an {@link X509Credential}.
    */
   protected class X509Options extends BasicOptions {
       
       /** Emit the entity certificate as an X509Certificate element within X509Data. */
       private  boolean emitEntityCertificate;
       
       /** Emit the entity certificate chain as sequence of X509Certificate elements within X509Data. */
       private boolean emitEntityCertificateChain;
       
       /** Emit the CRL list as sequence of X509CRL elements within X509Data. */
       private boolean emitCRLs;
       
       /** Emit the entity certificate subject DN as an X509SubjectName element within X509Data. */
       private boolean emitX509SubjectName;
       
       /** Emit the entity certificate issuer name and serial number as an X509IssuerSerial element within X509Data. */
       private boolean emitX509IssuerSerial;
       
       /** Emit the entity certificate subject key identifier as an X509SKI element within X509Data. */
       private boolean emitX509SKI;
       
       /** Emit the entity certificate digest as an X509Digest element within X509Data. */
       private boolean emitX509Digest;
       
       /** X509Digest digest algorithm URI. */
       private String x509DigestAlgorithmURI;

       /** Emit the entity certificate subject DN as a KeyName element. */
       private boolean emitSubjectDNAsKeyName;
       
       /** Emit the entity certificate subject DN common name (CN) fields as KeyName elements. */
       private boolean emitSubjectCNAsKeyName;
       
       /** Emit the entity certificate subject alternative name extension values as KeyName elements. */
       private boolean emitSubjectAltNamesAsKeyNames;
       
       /** The set of types of subject alternative names to process. */
       private Set<Integer> subjectAltNames;
       
       /** Responsible for parsing and serializing X.500 names to/from {@link X500Principal} instances. */
       private X500DNHandler x500DNHandler;
       
       /** The format specifier for outputting X.500 subject names. */
       private String x500SubjectDNFormat;
       
       /** The format specifier for outputting X.500 issuer names. */
       private String x500IssuerDNFormat;
       
       /** Constructor. */
       protected X509Options() {
           x509DigestAlgorithmURI = EncryptionConstants.ALGO_ID_DIGEST_SHA256;
           subjectAltNames = new LazySet<Integer>();
           x500DNHandler = new InternalX500DNHandler();
           x500SubjectDNFormat = X500DNHandler.FORMAT_RFC2253;
           x500IssuerDNFormat = X500DNHandler.FORMAT_RFC2253;
       }
       
       /** {@inheritDoc} */
       protected X509Options clone() {
           X509Options clonedOptions = (X509Options) super.clone();
           
           clonedOptions.subjectAltNames = new LazySet<Integer>();
           clonedOptions.subjectAltNames.addAll(this.subjectAltNames);
           
           clonedOptions.x500DNHandler = this.x500DNHandler.clone();
           
           return clonedOptions;
       }
       
   }

}
