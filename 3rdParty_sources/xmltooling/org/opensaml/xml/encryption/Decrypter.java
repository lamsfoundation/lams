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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.xml.security.Init;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.KeyAlgorithmCriteria;
import org.opensaml.xml.security.criteria.KeyLengthCriteria;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.KeyInfoCriteria;
import org.opensaml.xml.signature.DigestMethod;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Supports decryption of XMLObjects which represent data encrypted according to the XML Encryption specification,
 * version 20021210.
 * 
 * <p>
 * Details on the components specified as constructor options are as follows:
 * <ol>
 * 
 * <li>
 * <code>newResolver</code>: This {@link KeyInfoCredentialResolver} instance is used to resolve keys (as Credentials)
 * based on the KeyInfo of EncryptedData elements. While it could in theory be used to handle the complete process of 
 * resolving the data decryption key, including decrypting any necessary EncryptedKey's, it would typically
 * be used in cases where encrypted key transport via an EncryptedKey is not being employed. 
 * This corresponds to scenarios where decryption is instead based on resolving the (presumably shared secret)
 * symmetric data decryption key directly, based on either context or information present in the 
 * EncryptedData's KeyInfo. In cases where the data decryption key is to be resolved by decrypting an EncryptedKey,
 * this resolver would typically not be used and may be <code>null</code>.
 * </li>
 * 
 * <li>
 * <code>newKEKResolver</code>: This {@link KeyInfoCredentialResolver} instance is used to resolve keys (as Credentials)
 * used to decrypt EncryptedKey elements, based on the KeyInfo information contained within the EncryptedKey element 
 * (also known as a Key Encryption Key or KEK). For asymmetric key transport of encrypted keys, this would entail 
 * resolving the private key which corresponds to the public key which was used to encrypt the EncryptedKey.
 * </li>
 * 
 * <li>
 * <code>newEncKeyResolver</code>: This {@link EncryptedKeyResolver} instance is responsible for resolving
 * the EncryptedKey element(s) which hold(s) the encrypted data decryption key which would be used to
 * decrypt an EncryptedData element. 
 * </li>
 * 
 * </ol>
 * </p>
 * 
 * <p>
 * XML Encryption can encrypt either a single {@link Element} or the contents of an Element. The caller of this class
 * must select the decryption method which is most appropriate for their specific use case.
 * </p>
 * 
 * <p>
 * Note that the type of plaintext data contained by an {@link EncryptedData} can be checked prior to decryption by
 * examining that element's <code>type</code> attribute ({@link EncryptedData#getType}). This (optional) attribute
 * may contain one of the following two constant values to aid in the decryption process:
 * {@link EncryptionConstants#TYPE_ELEMENT} or {@link EncryptionConstants#TYPE_CONTENT}.
 * </p>
 * 
 * <p>
 * By nature the fundamental output of XML decryption is a DOM {@link DocumentFragment} with 1 or more immediate
 * top-level DOM {@link Node} children. This case is reflected in the method {@link #decryptDataToDOM(EncryptedData)}.
 * It is up to the caller to properly process the DOM Nodes which are the children of this document fragment. The
 * DocumentFragment and its Node children will be owned by the DOM {@link Document} which owned the original
 * EncryptedData before decryption. Note, however, that the Node children will not be a part of the tree of Nodes rooted
 * at that Document's document element.
 * </p>
 * 
 * <p>
 * A typical use case will be that the content which was encrypted contained solely {@link Element} nodes. For this use
 * case a convenience method is provided as {@link #decryptDataToList(EncryptedData)}, which returns a list of
 * {@link XMLObject}'s which are the result of unmarshalling each of the child Elements of the decrypted
 * DocumentFragment.
 * </p>
 * 
 * <p>
 * Another typical use case is that the content which was encrypted was a single Element. For this use case a
 * convenience method is provided as {@link #decryptData(EncryptedData)}, which returns a single XMLObject which was
 * the result of unmarshalling this decrypted Element.
 * </p>
 * 
 * <p>
 * In both of these cases the underlying DOM Element which is represented by each of the returned XMLObjects will be
 * owned by the DOM Document which also owns the original EncrytpedData Element. However, note that these cached DOM
 * Elements are <strong>not</strong> part of the tree of Nodes rooted at that Document's document element. If these
 * returned XMLObjects are then inserted as the children of other XMLObjects, it is up to the caller to ensure that the
 * XMLObject tree is then remarshalled if the relationship of the cached DOM nodes is important (e.g. resolution of
 * ID-typed attributes via {@link Document#getElementById(String)}).
 * </p>
 * 
 * <p>
 * For some use cases where the returned XMLObjects will not necessarily be stored back as children of another parent
 * XMLObject, it may still necessary for the DOM Elements of the resultant XMLObjects to exist within the tree of Nodes
 * rooted at a DOM Document's document element (e.g. signature verification on the standalone decrypted XMLObject). For
 * these cases these method variants may be used: {@link #decryptDataToList(EncryptedData, boolean)} and
 * {@link #decryptData(EncryptedData, boolean)}.  The <code>rootInNewDocument</code> parameter is explained below.
 * A default value for this parameter, for the overloaded convenience methods
 * which do not take this parameter explicitly, may be set via {@link #setRootInNewDocument(boolean)}.
 * This default value is initialized to <code>false</code>.
 * </p>
 * 
 * <p>If the boolean option <code>rootInNewDocument</code> is true at the time of decryption,
 * then for each top-level child Element of the decrypted DocumentFragment, the following will occur:
 * 
 * <ol>
 * <li>A new DOM Document will be created.</li>
 * <li>The Element will be adopted into that Document.</li>
 * <li>The Element will be made the root element of the Document.</li>
 * <li>The Element will be unmarshalled into an XMLObject as in the single argument variant.</li>
 * </ol>
 * 
 * <p>
 * Note that new Document creation, node adoption and rooting the new document element are potentially very expensive.
 * This should only be done where the caller's use case really requires it.
 * </p>
 * 
 */
public class Decrypter {

    /** ParserPool used in parsing decrypted data. */
    private final BasicParserPool parserPool;

    /** Unmarshaller factory, used in decryption of EncryptedData objects. */
    private UnmarshallerFactory unmarshallerFactory;

    /** Load-and-Save DOM Implementation singleton. */
    // private DOMImplementationLS domImplLS;
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(Decrypter.class);

    /** Resolver for data encryption keys. */
    private KeyInfoCredentialResolver resolver;

    /** Resolver for key encryption keys. */
    private KeyInfoCredentialResolver kekResolver;

    /** Resolver for EncryptedKey instances which contain the encrypted data encryption key. */
    private EncryptedKeyResolver encKeyResolver;

    /** Additional criteria to use when resolving credentials based on an EncryptedData's KeyInfo. */
    private CriteriaSet resolverCriteria;

    /** Additional criteria to use when resolving credentials based on an EncryptedKey's KeyInfo. */
    private CriteriaSet kekResolverCriteria;

    /** The name of the JCA security provider to use. */
    private String jcaProviderName;
    
    /** Flag to determine whether by default the Element which backs the underlying decrypted SAMLObject will be the 
     * root of a new DOM document. */
    private boolean defaultRootInNewDocument;
    

    /**
     * Constructor.
     * 
     * @param newResolver resolver for data encryption keys.
     * @param newKEKResolver resolver for key encryption keys.
     * @param newEncKeyResolver resolver for EncryptedKey elements
     */
    public Decrypter(KeyInfoCredentialResolver newResolver, KeyInfoCredentialResolver newKEKResolver,
            EncryptedKeyResolver newEncKeyResolver) {
        resolver = newResolver;
        kekResolver = newKEKResolver;
        encKeyResolver = newEncKeyResolver;

        resolverCriteria = null;
        kekResolverCriteria = null;

        // Note: this is hopefully only temporary, until Xerces implements DOM 3 LSParser.parseWithContext().
        parserPool = new BasicParserPool();
        parserPool.setNamespaceAware(true);

        // Note: this is necessary due to an unresolved Xerces deferred DOM issue/bug
        HashMap<String, Boolean> features = new HashMap<String, Boolean>();
        features.put("http://apache.org/xml/features/dom/defer-node-expansion", Boolean.FALSE);
        parserPool.setBuilderFeatures(features);

        unmarshallerFactory = Configuration.getUnmarshallerFactory();
        
        defaultRootInNewDocument = false;
    }
    
    /**
     * Get the flag which indicates whether by default the DOM Element which backs a decrypted SAML object
     * will be the root of a new DOM document.  Defaults to false.
     * 
     * @return the current value of the flag for this decrypter instance
     */
    public boolean isRootInNewDocument() {
        return defaultRootInNewDocument;
    }
    
    /**
     * Set the flag which indicates whether by default the DOM Element which backs a decrypted SAML object
     * will be the root of a new DOM document.  Defaults to false.
     * 
     * @param flag the current value of the flag for this decrypter instance
     */
    public void setRootInNewDocument(boolean flag) {
       defaultRootInNewDocument = flag; 
    }

    /**
     * Get the Java Cryptography Architecture (JCA) security provider name that should be used to provide the decryption
     * support.
     * 
     * Defaults to <code>null</code>, which means that the first registered provider which supports the indicated
     * encryption algorithm URI will be used.
     * 
     * @return the JCA provider name to use
     */
    public String getJCAProviderName() {
        return jcaProviderName;
    }

    /**
     * Set the Java Cryptography Architecture (JCA) security provider name that should be used to provide the decryption
     * support.
     * 
     * Defaults to <code>null</code>, which means that the first registered provider which supports the indicated
     * encryption algorithm URI will be used.
     * 
     * @param providerName the JCA provider name to use
     */
    public void setJCAProviderName(String providerName) {
        jcaProviderName = providerName;
    }

    /**
     * Get the data encryption key credential resolver.
     * 
     * @return the data encryption key resolver
     */
    public KeyInfoCredentialResolver getKeyResolver() {
        return resolver;
    }

    /**
     * Set a new data encryption key credential resolver.
     * 
     * @param newResolver the new data encryption key resolver
     */
    public void setKeyResolver(KeyInfoCredentialResolver newResolver) {
        resolver = newResolver;
    }

    /**
     * Get the key encryption key credential resolver.
     * 
     * @return the key encryption key resolver
     */
    public KeyInfoCredentialResolver getKEKResolver() {
        return kekResolver;
    }

    /**
     * Set a new key encryption key credential resolver.
     * 
     * @param newKEKResolver the new key encryption key resolver
     */
    public void setKEKResolver(KeyInfoCredentialResolver newKEKResolver) {
        kekResolver = newKEKResolver;
    }

    /**
     * Get the encrypted key resolver.
     * 
     * @return the encrypted key resolver
     */
    public EncryptedKeyResolver getEncryptedKeyResolver() {
        return encKeyResolver;
    }

    /**
     * Set a new encrypted key resolver.
     * 
     * @param newResolver the new encrypted key resolver
     */
    public void setEncryptedKeyResolver(EncryptedKeyResolver newResolver) {
        encKeyResolver = newResolver;
    }

    /**
     * Get the optional static set of criteria used when resolving credentials based on the KeyInfo of an EncryptedData
     * element.
     * 
     * @return the static criteria set to use
     */
    public CriteriaSet setKeyResolverCriteria() {
        return resolverCriteria;
    }

    /**
     * Set the optional static set of criteria used when resolving credentials based on the KeyInfo of an EncryptedData
     * element.
     * 
     * @param newCriteria the static criteria set to use
     */
    public void setKeyResolverCriteria(CriteriaSet newCriteria) {
        resolverCriteria = newCriteria;
    }

    /**
     * Get the optional static set of criteria used when resolving credentials based on the KeyInfo of an EncryptedKey
     * element.
     * 
     * @return the static criteria set to use
     */
    public CriteriaSet getKEKResolverCriteria() {
        return kekResolverCriteria;
    }

    /**
     * Set the optional static set of criteria used when resolving credentials based on the KeyInfo of an EncryptedKey
     * element.
     * 
     * @param newCriteria the static criteria set to use
     */
    public void setKEKResolverCriteria(CriteriaSet newCriteria) {
        kekResolverCriteria = newCriteria;
    }

    /**
     * This is a convenience method for calling {@link #decryptData(EncryptedData, boolean)},
     * with the <code>rootInNewDocument</code> parameter value supplied by {@link #isRootInNewDocument()}.
     * 
     * @param encryptedData encrypted data element containing the data to be decrypted
     * @return the decrypted XMLObject
     * @throws DecryptionException exception indicating a decryption error, possibly because the decrypted data
     *             contained more than one top-level Element, or some non-Element Node type.
     */
    public XMLObject decryptData(EncryptedData encryptedData) throws DecryptionException {
        return decryptData(encryptedData, isRootInNewDocument());
    }

    /**
     * Decrypts the supplied EncryptedData and returns the resulting XMLObject.
     * 
     * This will only succeed if the decrypted EncryptedData contains exactly one DOM Node of type Element.
     * 
     * @param encryptedData encrypted data element containing the data to be decrypted
     * @param rootInNewDocument if true, root the underlying Element of the returned XMLObject in a new Document as
     *            described in {@link Decrypter}
     * @return the decrypted XMLObject
     * @throws DecryptionException exception indicating a decryption error, possibly because the decrypted data
     *             contained more than one top-level Element, or some non-Element Node type.
     */
    public XMLObject decryptData(EncryptedData encryptedData, boolean rootInNewDocument) throws DecryptionException {

        List<XMLObject> xmlObjects = decryptDataToList(encryptedData, rootInNewDocument);
        if (xmlObjects.size() != 1) {
            log.error("The decrypted data contained more than one top-level XMLObject child");
            throw new DecryptionException("The decrypted data contained more than one XMLObject child");
        }

        return xmlObjects.get(0);
    }

    /**
     * This is a convenience method for calling {@link #decryptDataToList(EncryptedData, boolean)},
     * with the <code>rootInNewDocument</code> parameter value supplied by {@link #isRootInNewDocument()}.
     * 
     * @param encryptedData encrypted data element containing the data to be decrypted
     * @return the list decrypted top-level XMLObjects
     * @throws DecryptionException exception indicating a decryption error, possibly because the decrypted data
     *             contained DOM nodes other than type of Element
     */
    public List<XMLObject> decryptDataToList(EncryptedData encryptedData) throws DecryptionException {
        return decryptDataToList(encryptedData, isRootInNewDocument());
    }

    /**
     * Decrypts the supplied EncryptedData and returns the resulting list of XMLObjects.
     * 
     * This will succeed only if the decrypted EncryptedData contains at the top-level only DOM Elements (not other
     * types of DOM Nodes).
     * 
     * @param encryptedData encrypted data element containing the data to be decrypted
     * @param rootInNewDocument if true, root the underlying Elements of the returned XMLObjects in a new Document as
     *            described in {@link Decrypter}
     * @return the list decrypted top-level XMLObjects
     * @throws DecryptionException exception indicating a decryption error, possibly because the decrypted data
     *             contained DOM nodes other than type of Element
     */
    public List<XMLObject> decryptDataToList(EncryptedData encryptedData, boolean rootInNewDocument)
            throws DecryptionException {
        List<XMLObject> xmlObjects = new LinkedList<XMLObject>();

        DocumentFragment docFragment = decryptDataToDOM(encryptedData);

        XMLObject xmlObject;
        Node node;
        Element element;

        NodeList children = docFragment.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                log.error("Decryption returned a top-level node that was not of type Element: " + node.getNodeType());
                throw new DecryptionException("Top-level node was not of type Element");
            } else {
                element = (Element) node;
                if (rootInNewDocument) {
                    Document newDoc = null;
                    try {
                        newDoc = parserPool.newDocument();
                    } catch (XMLParserException e) {
                        log.error("There was an error creating a new DOM Document", e);
                        throw new DecryptionException("Error creating new DOM Document", e);
                    }
                    newDoc.adoptNode(element);
                    newDoc.appendChild(element);
                }
            }

            try {
                xmlObject = unmarshallerFactory.getUnmarshaller(element).unmarshall(element);
            } catch (UnmarshallingException e) {
                log.error("There was an error during unmarshalling of the decrypted element", e);
                throw new DecryptionException("Unmarshalling error during decryption", e);
            }

            xmlObjects.add(xmlObject);
        }

        return xmlObjects;
    }

    /**
     * Decrypts the supplied EncryptedData and returns the resulting DOM {@link DocumentFragment}.
     * 
     * @param encryptedData encrypted data element containing the data to be decrypted
     * @return the decrypted DOM {@link DocumentFragment}
     * @throws DecryptionException exception indicating a decryption error
     */
    public DocumentFragment decryptDataToDOM(EncryptedData encryptedData) throws DecryptionException {
        if (resolver == null && encKeyResolver == null) {
            log.error("Decryption can not be attempted, required resolvers are not available");
            throw new DecryptionException("Unable to decrypt EncryptedData, required resolvers are not available");
        }

        DocumentFragment docFrag = null;

        if (resolver != null) {
            docFrag = decryptUsingResolvedKey(encryptedData);
            if (docFrag != null) {
                return docFrag;
            } else {
                log.debug("Failed to decrypt EncryptedData using standard KeyInfo resolver");
            }
        }

        String algorithm = encryptedData.getEncryptionMethod().getAlgorithm();
        if (DatatypeHelper.isEmpty(algorithm)) {
            String msg = "EncryptedData's EncryptionMethod Algorithm attribute was empty, "
                + "key decryption could not be attempted";
            log.error(msg);
            throw new DecryptionException(msg);
        }

        if (encKeyResolver != null) {
            docFrag = decryptUsingResolvedEncryptedKey(encryptedData, algorithm);
            if (docFrag != null) {
                return docFrag;
            } else {
                log.debug("Failed to decrypt EncryptedData using EncryptedKeyResolver");
            }
        }

        log.error("Failed to decrypt EncryptedData using either EncryptedData KeyInfoCredentialResolver "
                + "or EncryptedKeyResolver + EncryptedKey KeyInfoCredentialResolver");
        
        throw new DecryptionException("Failed to decrypt EncryptedData");
    }

    /**
     * Decrypts the supplied EncryptedData using the specified key, and returns the resulting DOM
     * {@link DocumentFragment}.
     * 
     * @param encryptedData encrypted data element containing the data to be decrypted
     * @param dataEncKey Java Key with which to attempt decryption of the encrypted data
     * @return the decrypted DOM {@link DocumentFragment}
     * @throws DecryptionException exception indicating a decryption error
     */
    public DocumentFragment decryptDataToDOM(EncryptedData encryptedData, Key dataEncKey) throws DecryptionException {

        // TODO Until Xerces supports LSParser.parseWithContext(), or we come up with another solution
        // to parse a bytestream into a DocumentFragment, we can only support encryption of type
        // Element (i.e. a single Element), not content.
        if (!EncryptionConstants.TYPE_ELEMENT.equals(encryptedData.getType())) {
            log.error("EncryptedData was of unsupported type '" + encryptedData.getType()
                    + "', could not attempt decryption");
            throw new DecryptionException("EncryptedData of unsupported type was encountered");
        }
        if (dataEncKey == null) {
            log.error("Data decryption key was null");
            throw new IllegalArgumentException("Data decryption key may not be null");
        }

        try {
            checkAndMarshall(encryptedData);
        } catch (DecryptionException e) {
            log.error("Error marshalling EncryptedData for decryption", e);
            throw e;
        }
        Element targetElement = encryptedData.getDOM();

        XMLCipher xmlCipher;
        try {
            if (getJCAProviderName() != null) {
                xmlCipher = XMLCipher.getProviderInstance(getJCAProviderName());
            } else {
                xmlCipher = XMLCipher.getInstance();
            }
            xmlCipher.init(XMLCipher.DECRYPT_MODE, dataEncKey);
        } catch (XMLEncryptionException e) {
            log.error("Error initialzing cipher instance on data decryption", e);
            throw new DecryptionException("Error initialzing cipher instance on data decryption", e);
        }

        byte[] bytes = null;
        try {
            bytes = xmlCipher.decryptToByteArray(targetElement);
        } catch (XMLEncryptionException e) {
            log.error("Error decrypting the encrypted data element", e);
            throw new DecryptionException("Error decrypting the encrypted data element", e);
        }
        if (bytes == null) {
            throw new DecryptionException("EncryptedData could not be decrypted");
        }
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        DocumentFragment docFragment = parseInputStream(input, encryptedData.getDOM().getOwnerDocument());
        return docFragment;
    }

    /**
     * Attempts to decrypt the supplied EncryptedKey and returns the resulting Java security Key object. The algorithm
     * of the decrypted key must be supplied by the caller based on knowledge of the associated EncryptedData
     * information.
     * 
     * @param encryptedKey encrypted key element containing the encrypted key to be decrypted
     * @param algorithm the algorithm associated with the decrypted key
     * @return the decrypted key
     * @throws DecryptionException exception indicating a decryption error
     */
    public Key decryptKey(EncryptedKey encryptedKey, String algorithm) throws DecryptionException {
        if (kekResolver == null) {
            log.warn("No KEK KeyInfo credential resolver is available, can not attempt EncryptedKey decryption");
            throw new DecryptionException("No KEK KeyInfo resolver is available for EncryptedKey decryption");
        }

        if (DatatypeHelper.isEmpty(algorithm)) {
            log.error("Algorithm of encrypted key not supplied, key decryption cannot proceed.");
            throw new DecryptionException("Algorithm of encrypted key not supplied, key decryption cannot proceed.");
        }

        CriteriaSet criteriaSet = buildCredentialCriteria(encryptedKey, kekResolverCriteria);
        try {
            for (Credential cred : kekResolver.resolve(criteriaSet)) {
                try {
                    return decryptKey(encryptedKey, algorithm, SecurityHelper.extractDecryptionKey(cred));
                } catch (DecryptionException e) {
                    String msg = "Attempt to decrypt EncryptedKey using credential from KEK KeyInfo resolver failed: ";
                    log.debug(msg, e);
                    continue;
                }
            }
        } catch (SecurityException e) {
            log.error("Error resolving credentials from EncryptedKey KeyInfo", e);
        }

        log.error("Failed to decrypt EncryptedKey, valid decryption key could not be resolved");
        throw new DecryptionException("Valid decryption key for EncryptedKey could not be resolved");
    }

    /**
     * Decrypts the supplied EncryptedKey and returns the resulting Java security Key object. The algorithm of the
     * decrypted key must be supplied by the caller based on knowledge of the associated EncryptedData information.
     * 
     * @param encryptedKey encrypted key element containing the encrypted key to be decrypted
     * @param algorithm the algorithm associated with the decrypted key
     * @param kek the key encryption key with which to attempt decryption of the encrypted key
     * @return the decrypted key
     * @throws DecryptionException exception indicating a decryption error
     */
    public Key decryptKey(EncryptedKey encryptedKey, String algorithm, Key kek) throws DecryptionException {
        if (kek == null) {
            log.error("Data encryption key was null");
            throw new IllegalArgumentException("Data encryption key may not be null");
        }
        if (DatatypeHelper.isEmpty(algorithm)) {
            log.error("Algorithm of encrypted key not supplied, key decryption cannot proceed.");
            throw new DecryptionException("Algorithm of encrypted key not supplied, key decryption cannot proceed.");
        }

        try {
            checkAndMarshall(encryptedKey);
        } catch (DecryptionException e) {
            log.error("Error marshalling EncryptedKey for decryption", e);
            throw e;
        }
        
        preProcessEncryptedKey(encryptedKey, algorithm, kek);
        
        Element targetElement = encryptedKey.getDOM();

        XMLCipher xmlCipher;
        try {
            if (getJCAProviderName() != null) {
                xmlCipher = XMLCipher.getProviderInstance(getJCAProviderName());
            } else {
                xmlCipher = XMLCipher.getInstance();
            }
            xmlCipher.init(XMLCipher.UNWRAP_MODE, kek);
        } catch (XMLEncryptionException e) {
            log.error("Error initialzing cipher instance on key decryption", e);
            throw new DecryptionException("Error initialzing cipher instance on key decryption", e);
        }

        org.apache.xml.security.encryption.EncryptedKey encKey;
        try {
            encKey = xmlCipher.loadEncryptedKey(targetElement.getOwnerDocument(), targetElement);
        } catch (XMLEncryptionException e) {
            log.error("Error when loading library native encrypted key representation", e);
            throw new DecryptionException("Error when loading library native encrypted key representation", e);
        }

        Key key = null;
        try {
            key = xmlCipher.decryptKey(encKey, algorithm);
        } catch (XMLEncryptionException e) {
            log.error("Error decrypting encrypted key", e);
            throw new DecryptionException("Error decrypting encrypted key", e);
        }
        if (key == null) {
            throw new DecryptionException("Key could not be decrypted");
        }
        return key;
    }

    /**
     * Preprocess the EncryptedKey. For example, check for supported algorithms.
     * 
     * @param encryptedKey encrypted key element containing the encrypted key to be decrypted
     * @param algorithm the algorithm associated with the decrypted key
     * @param kek the key encryption key with which to attempt decryption of the encrypted key
     * 
     * @throws DecryptionException exception indicating a decryption error
     */
    protected void preProcessEncryptedKey(EncryptedKey encryptedKey, String algorithm, Key kek) 
            throws DecryptionException {
        
        // Apache XML-Security currently only supports an internal, hard-coded default
        // SHA-1 digest method with RSA-OAEP key transport.
        String keyTransportAlgorithm = encryptedKey.getEncryptionMethod().getAlgorithm();
        if (EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP.equals(keyTransportAlgorithm)) {
            List<XMLObject> digestMethods = 
                encryptedKey.getEncryptionMethod().getUnknownXMLObjects(DigestMethod.DEFAULT_ELEMENT_NAME);
            if (!digestMethods.isEmpty()) {
                DigestMethod dm = (DigestMethod) digestMethods.get(0);
                if (! SignatureConstants.ALGO_ID_DIGEST_SHA1
                        .equals(DatatypeHelper.safeTrimOrNullString(dm.getAlgorithm())) ) {
                    log.error("EncryptedKey/EncryptionMethod/DigestMethod contains unsupported algorithm URI: {}",
                            dm.getAlgorithm());
                    throw new DecryptionException(
                            "EncryptedKey/EncryptionMethod/DigestMethod contains unsupported algorithm URI");
                }
            }
        }
        
    }

    /**
     * Attempt to decrypt by resolving the decryption key using the standard credential resolver.
     * 
     * @param encryptedData the encrypted data to decrypt
     * @return the decrypted document fragment, or null if decryption key could not be resolved or decryption failed
     */
    private DocumentFragment decryptUsingResolvedKey(EncryptedData encryptedData) {
        if (resolver != null) {
            CriteriaSet criteriaSet = buildCredentialCriteria(encryptedData, resolverCriteria);
            try {
                for (Credential cred : resolver.resolve(criteriaSet)) {
                    try {
                        return decryptDataToDOM(encryptedData, SecurityHelper.extractDecryptionKey(cred));
                    } catch (DecryptionException e) {
                        String msg = "Decryption attempt using credential from standard KeyInfo resolver failed: ";
                        log.debug(msg, e);
                        continue;
                    }
                }
            } catch (SecurityException e) {
                log.error("Error resolving credentials from EncryptedData KeyInfo", e);
            }
        }
        return null;
    }

    /**
     * Attempt to decrypt by resolving the decryption key by first resolving EncryptedKeys, and using the KEK credential
     * resolver to resolve the key decryption for each.
     * 
     * @param encryptedData the encrypted data to decrypt
     * @param algorithm the algorithm of the key to be decrypted
     * @return the decrypted document fragment, or null if decryption key could not be resolved or decryption failed
     */
    private DocumentFragment decryptUsingResolvedEncryptedKey(EncryptedData encryptedData, String algorithm) {
        if (encKeyResolver != null) {
            for (EncryptedKey encryptedKey : encKeyResolver.resolve(encryptedData)) {
                try {
                    Key decryptedKey = decryptKey(encryptedKey, algorithm);
                    return decryptDataToDOM(encryptedData, decryptedKey);
                } catch (DecryptionException e) {
                    String msg = "Attempt to decrypt EncryptedData using key extracted from EncryptedKey failed: ";
                    log.debug(msg, e);
                    continue;
                }
            }
        }
        return null;
    }

    /**
     * Parse the specified input stream in a DOM DocumentFragment, owned by the specified Document.
     * 
     * @param input the InputStream to parse
     * @param owningDocument the Document which will own the returned DocumentFragment
     * @return a DocumentFragment
     * @throws DecryptionException thrown if there is an error parsing the input stream
     */
    private DocumentFragment parseInputStream(InputStream input, Document owningDocument) throws DecryptionException {
        // Since Xerces currently seems not to handle parsing into a DocumentFragment
        // without a bit hackery, use this to simulate, so we can keep the API
        // the way it hopefully will look in the future. Obviously this only works for
        // input streams containing valid XML instances, not fragments.

        Document newDocument = null;
        try {
            newDocument = parserPool.parse(input);
        } catch (XMLParserException e) {
            log.error("Error parsing decrypted input stream", e);
            throw new DecryptionException("Error parsing input stream", e);
        }

        Element element = newDocument.getDocumentElement();
        owningDocument.adoptNode(element);

        DocumentFragment container = owningDocument.createDocumentFragment();
        container.appendChild(element);

        return container;
    }

    /**
     * Utility method to build a new set of credential criteria based on the KeyInfo of an EncryptedData or
     * EncryptedKey, and any additional static criteria which might have been supplied to the decrypter.
     * 
     * @param encryptedType an EncryptedData or EncryptedKey for which to resolve decryption credentials
     * @param staticCriteria static set of credential criteria to add to the new criteria set
     * @return the new credential criteria set
     */
    private CriteriaSet buildCredentialCriteria(EncryptedType encryptedType, CriteriaSet staticCriteria) {

        CriteriaSet newCriteriaSet = new CriteriaSet();

        // This is the main criteria based on the encrypted type's KeyInfo
        newCriteriaSet.add(new KeyInfoCriteria(encryptedType.getKeyInfo()));

        // Also attemtpt to dynamically construct key criteria based on information
        // in the encrypted object
        Set<Criteria> keyCriteria = buildKeyCriteria(encryptedType);
        if (keyCriteria != null && !keyCriteria.isEmpty()) {
            newCriteriaSet.addAll(keyCriteria);
        }

        // Add any static criteria which may have been supplied to the decrypter
        if (staticCriteria != null && !staticCriteria.isEmpty()) {
            newCriteriaSet.addAll(staticCriteria);
        }

        // If don't have a usage criteria yet from static criteria, add encryption usage
        if (!newCriteriaSet.contains(UsageCriteria.class)) {
            newCriteriaSet.add(new UsageCriteria(UsageType.ENCRYPTION));
        }

        return newCriteriaSet;
    }

    /**
     * Build decryption key credential criteria according to information in the encrypted type object.
     * 
     * @param encryptedType the encrypted type from which to deduce decryption key criteria
     * @return a set of credential criteria pertaining to the decryption key
     */
    private Set<Criteria> buildKeyCriteria(EncryptedType encryptedType) {
        EncryptionMethod encMethod = encryptedType.getEncryptionMethod();
        if (encMethod == null) {
            // This element is optional
            return Collections.emptySet();
        }
        String encAlgorithmURI = DatatypeHelper.safeTrimOrNullString(encMethod.getAlgorithm());
        if (encAlgorithmURI == null) {
            return Collections.emptySet();
        }

        Set<Criteria> critSet = new HashSet<Criteria>(2);

        KeyAlgorithmCriteria algoCrit = buildKeyAlgorithmCriteria(encAlgorithmURI);
        if (algoCrit != null) {
            critSet.add(algoCrit);
            log.debug("Added decryption key algorithm criteria: {}", algoCrit.getKeyAlgorithm());
        }

        KeyLengthCriteria lengthCrit = buildKeyLengthCriteria(encAlgorithmURI);
        if (lengthCrit != null) {
            critSet.add(lengthCrit);
            log.debug("Added decryption key length criteria from EncryptionMethod algorithm URI: {}", lengthCrit
                    .getKeyLength());
        } else {
            if (encMethod.getKeySize() != null && encMethod.getKeySize().getValue() != null) {
                lengthCrit = new KeyLengthCriteria(encMethod.getKeySize().getValue());
                critSet.add(lengthCrit);
                log.debug("Added decryption key length criteria from EncryptionMethod/KeySize: {}", lengthCrit
                        .getKeyLength());
            }
        }

        return critSet;
    }

    /**
     * Dynamically construct key algorithm credential criteria based on the specified algorithm URI.
     * 
     * @param encAlgorithmURI the algorithm URI
     * @return a new key algorithm credential criteria instance, or null if criteria could not be determined
     */
    private KeyAlgorithmCriteria buildKeyAlgorithmCriteria(String encAlgorithmURI) {
        if (DatatypeHelper.isEmpty(encAlgorithmURI)) {
            return null;
        }

        String jcaKeyAlgorithm = SecurityHelper.getKeyAlgorithmFromURI(encAlgorithmURI);
        if (!DatatypeHelper.isEmpty(jcaKeyAlgorithm)) {
            return new KeyAlgorithmCriteria(jcaKeyAlgorithm);
        }

        return null;
    }

    /**
     * Dynamically construct key length credential criteria based on the specified algorithm URI.
     * 
     * @param encAlgorithmURI the algorithm URI
     * @return a new key length credential criteria instance, or null if the value could not be determined
     */
    private KeyLengthCriteria buildKeyLengthCriteria(String encAlgorithmURI) {
        if (!DatatypeHelper.isEmpty(encAlgorithmURI)) {
            return null;
        }

        Integer keyLength = SecurityHelper.getKeyLengthFromURI(encAlgorithmURI);
        if (keyLength != null) {
            return new KeyLengthCriteria(keyLength);
        }

        return null;
    }

    /**
     * Ensure that the XMLObject is marshalled.
     * 
     * @param xmlObject the object to check and marshall
     * @throws DecryptionException thrown if there is an error when marshalling the XMLObject
     */
    protected void checkAndMarshall(XMLObject xmlObject) throws DecryptionException {
        Element targetElement = xmlObject.getDOM();
        if (targetElement == null) {
            Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(xmlObject);
            try {
                targetElement = marshaller.marshall(xmlObject);
            } catch (MarshallingException e) {
                log.error("Error marshalling target XMLObject", e);
                throw new DecryptionException("Error marshalling target XMLObject", e);
            }
        }
    }

    /*
     * NOTE: this currently won't work because Xerces doesn't implement LSParser.parseWithContext(). Hopefully they will
     * in the future.
     */
    /*
     * private DocumentFragment parseInputStreamLS(InputStream input, Document owningDocument) throws
     * DecryptionException {
     * 
     * DOMImplementationLS domImpl = getDOMImplemenationLS();
     * 
     * LSParser parser = domImpl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null); if (parser == null) { throw
     * new DecryptionException("LSParser was null"); }
     * 
     * //DOMConfiguration config=parser.getDomConfig(); //DOMErrorHandlerImpl errorHandler=new DOMErrorHandlerImpl();
     * //config.setParameter("error-handler", errorHandler);
     * 
     * LSInput lsInput = domImpl.createLSInput(); if (lsInput == null) { throw new DecryptionException("LSInput was
     * null"); } lsInput.setByteStream(input);
     * 
     * DocumentFragment container = owningDocument.createDocumentFragment(); //TODO Xerces currently doesn't support
     * LSParser.parseWithContext() parser.parseWithContext(lsInput, container, LSParser.ACTION_REPLACE_CHILDREN);
     * 
     * return container; }
     */

    /*
     * private DOMImplementationLS getDOMImplemenationLS() throws DecryptionException { if (domImplLS != null) { return
     * domImplLS; }
     *  // get an instance of the DOMImplementation registry DOMImplementationRegistry registry; try { registry =
     * DOMImplementationRegistry.newInstance(); } catch (ClassCastException e) { throw new DecryptionException("Error
     * creating new error of DOMImplementationRegistry", e); } catch (ClassNotFoundException e) { throw new
     * DecryptionException("Error creating new error of DOMImplementationRegistry", e); } catch (InstantiationException
     * e) { throw new DecryptionException("Error creating new error of DOMImplementationRegistry", e); } catch
     * (IllegalAccessException e) { throw new DecryptionException("Error creating new error of
     * DOMImplementationRegistry", e); }
     *  // get a new instance of the DOM Level 3 Load/Save implementation DOMImplementationLS newDOMImplLS =
     * (DOMImplementationLS) registry.getDOMImplementation("LS 3.0"); if (newDOMImplLS == null) { throw new
     * DecryptionException("No LS DOMImplementation could be found"); } else { domImplLS = newDOMImplLS; }
     * 
     * return domImplLS; }
     */

    /*
     * Initialize the Apache XML security library if it hasn't been already
     */
    static {
        if (!Init.isInitialized()) {
            Init.init();
        }
    }

}