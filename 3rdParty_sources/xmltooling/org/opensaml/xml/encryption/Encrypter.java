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

package org.opensaml.xml.encryption;

import java.security.Key;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.xml.security.Init;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.signature.DigestMethod;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.XMLSignatureBuilder;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Supports encryption of XMLObjects, their content and keys, according to the XML Encryption specification, version
 * 20021210.
 * 
 * <p>
 * Various overloaded method variants are supplied for encrypting XMLObjects and their contents (with or without
 * encryption of the associated data encryption key), as well as for encrypting keys separately.
 * </p>
 * 
 * <p>
 * The parameters for data encryption are specified with an instance of {@link EncryptionParameters}. The parameters
 * for key encryption are specified with one or more instances of {@link KeyEncryptionParameters}.
 * </p>
 * 
 * <p>
 * The data encryption credential supplied by {@link EncryptionParameters#getEncryptionCredential()} is mandatory unless
 * key encryption is also being performed and all associated key encryption parameters contain a valid key encryption
 * credential containing a valid key encryption key. In this case the data encryption key will be randomly generated
 * based on the algorithm URI supplied by {@link EncryptionParameters#getAlgorithm()}.
 * </p>
 * 
 * <p>
 * If encryption of the data encryption key is being performed using the overloaded methods for elements or content, the
 * resulting EncryptedKey(s) will be placed inline within the KeyInfo of the resulting EncryptedData. If this is not the
 * desired behavior, the XMLObject and the data encryption key should be encrypted separately, and the placement of
 * EncryptedKey(s) handled by the caller. Specialized subclasses of this class maybe also handle key placement in an
 * application-specific manner.
 * </p>
 * 
 */
public class Encrypter {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(Encrypter.class);

    /** Unmarshaller used to create EncryptedData objects from DOM element. */
    private Unmarshaller encryptedDataUnmarshaller;

    /** Unmarshaller used to create EncryptedData objects from DOM element. */
    private Unmarshaller encryptedKeyUnmarshaller;

    /** Builder instance for building KeyInfo objects. */
    private XMLSignatureBuilder<KeyInfo> keyInfoBuilder;

    /** The name of the JCA security provider to use. */
    private String jcaProviderName;

    /**
     * Constructor.
     * 
     */
    public Encrypter() {
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
        encryptedDataUnmarshaller = unmarshallerFactory.getUnmarshaller(EncryptedData.DEFAULT_ELEMENT_NAME);
        encryptedKeyUnmarshaller = unmarshallerFactory.getUnmarshaller(EncryptedKey.DEFAULT_ELEMENT_NAME);

        XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        keyInfoBuilder = (XMLSignatureBuilder<KeyInfo>) builderFactory.getBuilder(KeyInfo.DEFAULT_ELEMENT_NAME);

        jcaProviderName = null;
    }

    /**
     * Get the Java Cryptography Architecture (JCA) security provider name that should be used to provide the encryption
     * support.
     * 
     * Defaults to <code>null</code>, which means that the first registered provider which supports the requested
     * encryption algorithm URI will be used.
     * 
     * @return the JCA provider name to use
     */
    public String getJCAProviderName() {
        return jcaProviderName;
    }

    /**
     * Set the Java Cryptography Architecture (JCA) security provider name that should be used to provide the encryption
     * support.
     * 
     * Defaults to <code>null</code>, which means that the first registered provider which supports the requested
     * encryption algorithm URI will be used.
     * 
     * @param providerName the JCA provider name to use
     */
    public void setJCAProviderName(String providerName) {
        jcaProviderName = providerName;
    }

    /**
     * Encrypts the DOM representation of the XMLObject.
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams parameters for encrypting the data
     * 
     * @return the resulting EncryptedData element
     * @throws EncryptionException exception thrown on encryption errors
     */
    public EncryptedData encryptElement(XMLObject xmlObject, EncryptionParameters encParams) 
            throws EncryptionException {
        List<KeyEncryptionParameters> emptyKEKParamsList = new ArrayList<KeyEncryptionParameters>();
        return encryptElement(xmlObject, encParams, emptyKEKParamsList, false);
    }

    /**
     * Encrypts the DOM representation of the XMLObject, encrypts the encryption key using the specified key encryption
     * parameters and places the resulting EncryptedKey within the EncryptedData's KeyInfo.
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams parameters for encrypting the data
     * @param kekParams parameters for encrypting the encryption key
     * 
     * @return the resulting EncryptedData element
     * @throws EncryptionException exception thrown on encryption errors
     */
    public EncryptedData encryptElement(XMLObject xmlObject, EncryptionParameters encParams,
            KeyEncryptionParameters kekParams) throws EncryptionException {
        List<KeyEncryptionParameters> kekParamsList = new ArrayList<KeyEncryptionParameters>();
        kekParamsList.add(kekParams);
        return encryptElement(xmlObject, encParams, kekParamsList, false);
    }

    /**
     * Encrypts the DOM representation of the XMLObject, encrypts the encryption key using the specified key encryption
     * parameters and places the resulting EncryptedKey(s) within the EncryptedData's KeyInfo.
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams parameters for encrypting the data
     * @param kekParamsList parameters for encrypting the encryption key
     * 
     * @return the resulting EncryptedData element
     * @throws EncryptionException exception thrown on encryption errors
     */
    public EncryptedData encryptElement(XMLObject xmlObject, EncryptionParameters encParams,
            List<KeyEncryptionParameters> kekParamsList) throws EncryptionException {
        return encryptElement(xmlObject, encParams, kekParamsList, false);
    }

    /**
     * Encrypts the DOM representation of the content of an XMLObject.
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams parameters for encrypting the data
     * 
     * @return the resulting EncryptedData element
     * @throws EncryptionException exception thrown on encryption errors
     */
    public EncryptedData encryptElementContent(XMLObject xmlObject, EncryptionParameters encParams)
            throws EncryptionException {
        List<KeyEncryptionParameters> emptyKEKParamsList = new ArrayList<KeyEncryptionParameters>();
        return encryptElement(xmlObject, encParams, emptyKEKParamsList, true);
    }

    /**
     * Encrypts the DOM representation of the content of an XMLObject, encrypts the encryption key using the specified
     * key encryption parameters and places the resulting EncryptedKey within the EncryptedData's KeyInfo..
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams parameters for encrypting the data
     * @param kekParams parameters for encrypting the encryption key
     * 
     * @return the resulting EncryptedData element
     * @throws EncryptionException exception thrown on encryption errors
     */
    public EncryptedData encryptElementContent(XMLObject xmlObject, EncryptionParameters encParams,
            KeyEncryptionParameters kekParams) throws EncryptionException {
        List<KeyEncryptionParameters> kekParamsList = new ArrayList<KeyEncryptionParameters>();
        kekParamsList.add(kekParams);
        return encryptElement(xmlObject, encParams, kekParamsList, true);
    }

    /**
     * Encrypts the DOM representation of the content of an XMLObject, encrypts the encryption key using the specified
     * key encryption parameters and places the resulting EncryptedKey(s) within the EncryptedData's KeyInfo..
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams parameters for encrypting the data
     * @param kekParamsList parameters for encrypting the encryption key
     * 
     * @return the resulting EncryptedData element
     * @throws EncryptionException exception thrown on encryption errors
     */
    public EncryptedData encryptElementContent(XMLObject xmlObject, EncryptionParameters encParams,
            List<KeyEncryptionParameters> kekParamsList) throws EncryptionException {
        return encryptElement(xmlObject, encParams, kekParamsList, true);
    }

    /**
     * Encrypts a key once for each key encryption parameters set that is supplied.
     * 
     * @param key the key to encrypt
     * @param kekParamsList a list parameters for encrypting the key
     * @param containingDocument the document that will own the DOM element underlying the resulting EncryptedKey
     *            objects
     * 
     * @return the resulting list of EncryptedKey objects
     * 
     * @throws EncryptionException exception thrown on encryption errors
     */
    public List<EncryptedKey> encryptKey(Key key, List<KeyEncryptionParameters> kekParamsList,
            Document containingDocument) throws EncryptionException {

        checkParams(kekParamsList, false);

        List<EncryptedKey> encKeys = new ArrayList<EncryptedKey>();

        for (KeyEncryptionParameters kekParam : kekParamsList) {
            encKeys.add(encryptKey(key, kekParam, containingDocument));
        }
        return encKeys;
    }

    /**
     * Encrypts a key.
     * 
     * @param key the key to encrypt
     * @param kekParams parameters for encrypting the key
     * @param containingDocument the document that will own the DOM element underlying the resulting EncryptedKey object
     * 
     * @return the resulting EncryptedKey object
     * 
     * @throws EncryptionException exception thrown on encryption errors
     */
    public EncryptedKey encryptKey(Key key, KeyEncryptionParameters kekParams, Document containingDocument)
            throws EncryptionException {

        checkParams(kekParams, false);

        Key encryptionKey = SecurityHelper.extractEncryptionKey(kekParams.getEncryptionCredential());
        String encryptionAlgorithmURI = kekParams.getAlgorithm();

        EncryptedKey encryptedKey = encryptKey(key, encryptionKey, encryptionAlgorithmURI, containingDocument);

        if (kekParams.getKeyInfoGenerator() != null) {
            KeyInfoGenerator generator = kekParams.getKeyInfoGenerator();
            log.debug("Dynamically generating KeyInfo from Credential for EncryptedKey using generator: {}",
                    generator.getClass().getName());
            try {
                encryptedKey.setKeyInfo(generator.generate(kekParams.getEncryptionCredential()));
            } catch (SecurityException e) {
                log.error("Error during EncryptedKey KeyInfo generation", e);
                throw new EncryptionException("Error during EncryptedKey KeyInfo generation", e);
            }
        }

        if (kekParams.getRecipient() != null) {
            encryptedKey.setRecipient(kekParams.getRecipient());
        }

        return encryptedKey;
    }

    /**
     * Encrypts a key using the specified encryption key and algorithm URI.
     * 
     * @param targetKey the key to encrypt
     * @param encryptionKey the key with which to encrypt the target key
     * @param encryptionAlgorithmURI the XML Encryption algorithm URI corresponding to the encryption key
     * @param containingDocument the document that will own the resulting element
     * @return the new EncryptedKey object
     * @throws EncryptionException exception thrown on encryption errors
     */
    protected EncryptedKey encryptKey(Key targetKey, Key encryptionKey, String encryptionAlgorithmURI,
            Document containingDocument) throws EncryptionException {

        if (targetKey == null) {
            log.error("Target key for key encryption was null");
            throw new EncryptionException("Target key was null");
        }
        if (encryptionKey == null) {
            log.error("Encryption key for key encryption was null");
            throw new EncryptionException("Encryption key was null");
        }

        log.debug("Encrypting encryption key with algorithm: {}", encryptionAlgorithmURI);
        XMLCipher xmlCipher;
        try {
            if (getJCAProviderName() != null) {
                xmlCipher = XMLCipher.getProviderInstance(encryptionAlgorithmURI, getJCAProviderName());
            } else {
                xmlCipher = XMLCipher.getInstance(encryptionAlgorithmURI);
            }
            xmlCipher.init(XMLCipher.WRAP_MODE, encryptionKey);
        } catch (XMLEncryptionException e) {
            log.error("Error initializing cipher instance on key encryption", e);
            throw new EncryptionException("Error initializing cipher instance on key encryption", e);
        }

        org.apache.xml.security.encryption.EncryptedKey apacheEncryptedKey;
        try {
            apacheEncryptedKey = xmlCipher.encryptKey(containingDocument, targetKey);
            postProcessApacheEncryptedKey(apacheEncryptedKey, targetKey, encryptionKey,
                    encryptionAlgorithmURI, containingDocument);
        } catch (XMLEncryptionException e) {
            log.error("Error encrypting element on key encryption", e);
            throw new EncryptionException("Error encrypting element on key encryption", e);
        }

        EncryptedKey encryptedKey;
        try {
            Element encKeyElement = xmlCipher.martial(containingDocument, apacheEncryptedKey);
            encryptedKey = (EncryptedKey) encryptedKeyUnmarshaller.unmarshall(encKeyElement);
        } catch (UnmarshallingException e) {
            log.error("Error unmarshalling EncryptedKey element", e);
            throw new EncryptionException("Error unmarshalling EncryptedKey element");
        }

        return encryptedKey;
    }

    /**
     *  
     * Post-process the Apache EncryptedKey, prior to marshalling to DOM and unmarshalling into an XMLObject.
     *  
     * @param apacheEncryptedKey the Apache EncryptedKeyObject to post-process
     * @param targetKey the key to encrypt
     * @param encryptionKey the key with which to encrypt the target key
     * @param encryptionAlgorithmURI the XML Encryption algorithm URI corresponding to the encryption key
     * @param containingDocument the document that will own the resulting element
     * 
     * @throws EncryptionException exception thrown on encryption errors
     */
    protected void postProcessApacheEncryptedKey(org.apache.xml.security.encryption.EncryptedKey apacheEncryptedKey,
            Key targetKey, Key encryptionKey, String encryptionAlgorithmURI, Document containingDocument)
            throws EncryptionException {
        
        // Workaround for XML-Security library issue.  To maximize interop, explicitly express the library
        // default of SHA-1 digest method input parameter to RSA-OAEP key transport algorithm.
        // Check and only add if the library hasn't already done so, which it currently doesn't.
        if (EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP.equals(encryptionAlgorithmURI)) {
            boolean sawDigestMethod = false;
            Iterator childIter = apacheEncryptedKey.getEncryptionMethod().getEncryptionMethodInformation();
            while (childIter.hasNext()) {
                Element child = (Element) childIter.next();
                if (DigestMethod.DEFAULT_ELEMENT_NAME.equals(XMLHelper.getNodeQName(child))) {
                    sawDigestMethod = true;
                    break;
                }
            }
            if (! sawDigestMethod) {
                Element digestMethodElem = XMLHelper.constructElement(containingDocument,
                        DigestMethod.DEFAULT_ELEMENT_NAME);
                XMLHelper.appendNamespaceDeclaration(digestMethodElem, 
                        XMLConstants.XMLSIG_NS, XMLConstants.XMLSIG_PREFIX);
                digestMethodElem.setAttributeNS(null, DigestMethod.ALGORITHM_ATTRIB_NAME, 
                        SignatureConstants.ALGO_ID_DIGEST_SHA1);
                apacheEncryptedKey.getEncryptionMethod().addEncryptionMethodInformation(digestMethodElem);
            }
        }
        
    }

    /**
     * Encrypts the given XMLObject using the specified encryption key, algorithm URI and content mode flag.
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encryptionKey the key with which to encrypt the XMLObject
     * @param encryptionAlgorithmURI the XML Encryption algorithm URI corresponding to the encryption key
     * @param encryptContentMode whether just the content of the XMLObject should be encrypted
     * @return the resulting EncryptedData object
     * @throws EncryptionException exception thrown on encryption errors
     */
    protected EncryptedData encryptElement(XMLObject xmlObject, Key encryptionKey, String encryptionAlgorithmURI,
            boolean encryptContentMode) throws EncryptionException {

        if (xmlObject == null) {
            log.error("XMLObject for encryption was null");
            throw new EncryptionException("XMLObject was null");
        }
        if (encryptionKey == null) {
            log.error("Encryption key for key encryption was null");
            throw new EncryptionException("Encryption key was null");
        }
        log.debug("Encrypting XMLObject using algorithm URI {} with content mode {}", encryptionAlgorithmURI,
                encryptContentMode);

        checkAndMarshall(xmlObject);

        Element targetElement = xmlObject.getDOM();
        Document ownerDocument = targetElement.getOwnerDocument();

        XMLCipher xmlCipher;
        try {
            if (getJCAProviderName() != null) {
                xmlCipher = XMLCipher.getProviderInstance(encryptionAlgorithmURI, getJCAProviderName());
            } else {
                xmlCipher = XMLCipher.getInstance(encryptionAlgorithmURI);
            }
            xmlCipher.init(XMLCipher.ENCRYPT_MODE, encryptionKey);
        } catch (XMLEncryptionException e) {
            log.error("Error initializing cipher instance on XMLObject encryption", e);
            throw new EncryptionException("Error initializing cipher instance", e);
        }

        org.apache.xml.security.encryption.EncryptedData apacheEncryptedData;
        try {
            apacheEncryptedData = xmlCipher.encryptData(ownerDocument, targetElement, encryptContentMode);
        } catch (Exception e) {
            log.error("Error encrypting XMLObject", e);
            throw new EncryptionException("Error encrypting XMLObject", e);
        }

        EncryptedData encryptedData;
        try {
            Element encDataElement = xmlCipher.martial(ownerDocument, apacheEncryptedData);
            encryptedData = (EncryptedData) encryptedDataUnmarshaller.unmarshall(encDataElement);
        } catch (UnmarshallingException e) {
            log.error("Error unmarshalling EncryptedData element", e);
            throw new EncryptionException("Error unmarshalling EncryptedData element", e);
        }

        return encryptedData;
    }

    /**
     * Encrypts the given XMLObject using the specified encryption key, algorithm URI and content mode flag.
     * EncryptedKeys, if any, are placed inline within the KeyInfo of the resulting EncryptedData.
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams the encryption parameters to use
     * @param kekParamsList the key encryption parameters to use
     * @param encryptContentMode whether just the content of the XMLObject should be encrypted
     * 
     * @return the resulting EncryptedData object
     * @throws EncryptionException exception thrown on encryption errors
     */
    private EncryptedData encryptElement(XMLObject xmlObject, EncryptionParameters encParams,
            List<KeyEncryptionParameters> kekParamsList, boolean encryptContentMode) throws EncryptionException {

        checkParams(encParams, kekParamsList);

        String encryptionAlgorithmURI = encParams.getAlgorithm();
        Key encryptionKey = SecurityHelper.extractEncryptionKey(encParams.getEncryptionCredential());
        if (encryptionKey == null) {
            encryptionKey = generateEncryptionKey(encryptionAlgorithmURI);
        }

        EncryptedData encryptedData = encryptElement(xmlObject, encryptionKey, encryptionAlgorithmURI,
                encryptContentMode);
        Document ownerDocument = encryptedData.getDOM().getOwnerDocument();

        if (encParams.getKeyInfoGenerator() != null) {
            KeyInfoGenerator generator = encParams.getKeyInfoGenerator();
            log.debug("Dynamically generating KeyInfo from Credential for EncryptedData using generator: {}",
                    generator.getClass().getName());
            try {
                encryptedData.setKeyInfo(generator.generate(encParams.getEncryptionCredential()));
            } catch (SecurityException e) {
                log.error("Error during EncryptedData KeyInfo generation", e);
                throw new EncryptionException("Error during EncryptedData KeyInfo generation", e);
            }
        }

        for (KeyEncryptionParameters kekParams : kekParamsList) {
            EncryptedKey encryptedKey = encryptKey(encryptionKey, kekParams, ownerDocument);
            if (encryptedData.getKeyInfo() == null) {
                KeyInfo keyInfo = keyInfoBuilder.buildObject();
                encryptedData.setKeyInfo(keyInfo);
            }
            encryptedData.getKeyInfo().getEncryptedKeys().add(encryptedKey);
        }

        return encryptedData;
    }

    /**
     * Ensure that the XMLObject is marshalled.
     * 
     * @param xmlObject the object to check and marshall
     * @throws EncryptionException thrown if there is an error when marshalling the XMLObject
     */
    protected void checkAndMarshall(XMLObject xmlObject) throws EncryptionException {
        Element targetElement = xmlObject.getDOM();
        if (targetElement == null) {
            Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(xmlObject);
            try {
                targetElement = marshaller.marshall(xmlObject);
            } catch (MarshallingException e) {
                log.error("Error marshalling target XMLObject", e);
                throw new EncryptionException("Error marshalling target XMLObject", e);
            }
        }
    }

    /**
     * Check data encryption parameters for consistency and required values.
     * 
     * @param encParams the data encryption parameters to check
     * 
     * @throws EncryptionException thrown if any parameters are missing or have invalid values
     */
    protected void checkParams(EncryptionParameters encParams) throws EncryptionException {
        if (encParams == null) {
            log.error("Data encryption parameters are required");
            throw new EncryptionException("Data encryption parameters are required");
        }
        if (DatatypeHelper.isEmpty(encParams.getAlgorithm())) {
            log.error("Data encryption algorithm URI is required");
            throw new EncryptionException("Data encryption algorithm URI is required");
        }
    }

    /**
     * Check key encryption parameters for consistency and required values.
     * 
     * @param kekParams the key encryption parameters to check
     * @param allowEmpty if false, a null parameter is treated as an error
     * 
     * @throws EncryptionException thrown if any parameters are missing or have invalid values
     */
    protected void checkParams(KeyEncryptionParameters kekParams, boolean allowEmpty) throws EncryptionException {
        if (kekParams == null) {
            if (allowEmpty) {
                return;
            } else {
                log.error("Key encryption parameters are required");
                throw new EncryptionException("Key encryption parameters are required");
            }
        }
        Key key = SecurityHelper.extractEncryptionKey(kekParams.getEncryptionCredential());
        if (key == null) {
            log.error("Key encryption credential and contained key are required");
            throw new EncryptionException("Key encryption credential and contained key are required");
        }
        if (key instanceof DSAPublicKey) {
            log.error("Attempt made to use DSA key for encrypted key transport");
            throw new EncryptionException("DSA keys may not be used for encrypted key transport");
        }
        if (DatatypeHelper.isEmpty(kekParams.getAlgorithm())) {
            log.error("Key encryption algorithm URI is required");
            throw new EncryptionException("Key encryption algorithm URI is required");
        }
    }

    /**
     * Check a list of key encryption parameters for consistency and required values.
     * 
     * @param kekParamsList the key encryption parameters list to check
     * @param allowEmpty if false, a null or empty list is treated as an error
     * 
     * @throws EncryptionException thrown if any parameters are missing or have invalid values
     */
    protected void checkParams(List<KeyEncryptionParameters> kekParamsList, boolean allowEmpty)
            throws EncryptionException {
        if (kekParamsList == null || kekParamsList.isEmpty()) {
            if (allowEmpty) {
                return;
            } else {
                log.error("Key encryption parameters list may not be empty");
                throw new EncryptionException("Key encryption parameters list may not be empty");
            }
        }
        for (KeyEncryptionParameters kekParams : kekParamsList) {
            checkParams(kekParams, false);
        }
    }

    /**
     * Check the encryption parameters and key encryption parameters for valid combinations of options.
     * 
     * @param encParams the encryption parameters to use
     * @param kekParamsList the key encryption parameters to use
     * @throws EncryptionException exception thrown on encryption errors
     */
    protected void checkParams(EncryptionParameters encParams, List<KeyEncryptionParameters> kekParamsList)
            throws EncryptionException {

        checkParams(encParams);
        checkParams(kekParamsList, true);

        if (SecurityHelper.extractEncryptionKey(encParams.getEncryptionCredential()) == null
                && (kekParamsList == null || kekParamsList.isEmpty())) {
            log.error("Using a generated encryption key requires a KeyEncryptionParameters "
                    + "object and key encryption key");
            throw new EncryptionException("Using a generated encryption key requires a KeyEncryptionParameters "
                    + "object and key encryption key");
        }
    }

    /**
     * Generate a random symmetric encryption key.
     * 
     * @param encryptionAlgorithmURI the encryption algorithm URI
     * @return a randomly generated symmetric key
     * @throws EncryptionException thrown if the key can not be generated based on the specified algorithm URI
     */
    protected SecretKey generateEncryptionKey(String encryptionAlgorithmURI) throws EncryptionException {
        try {
            log.debug("Generating random symmetric data encryption key from algorithm URI: {}", 
                    encryptionAlgorithmURI);
            return SecurityHelper.generateSymmetricKey(encryptionAlgorithmURI);
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not generate encryption key, algorithm URI was invalid: " + encryptionAlgorithmURI);
            throw new EncryptionException("Could not generate encryption key, algorithm URI was invalid: "
                    + encryptionAlgorithmURI);
        } catch (KeyException e) {
            log.error("Could not generate encryption key from algorithm URI: " + encryptionAlgorithmURI);
            throw new EncryptionException("Could not generate encryption key from algorithm URI: "
                    + encryptionAlgorithmURI);
        }
    }

    /*
     * Initialize the Apache XML security library if it hasn't been already
     */
    static {
        if (!Init.isInitialized()) {
            Init.init();
        }
    }

}