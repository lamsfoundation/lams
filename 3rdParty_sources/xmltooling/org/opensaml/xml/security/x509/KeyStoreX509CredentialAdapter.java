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

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.opensaml.xml.security.credential.BasicCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A wrapper that changes a {@link KeyStore} in to a {@link X509Credential}. */
public class KeyStoreX509CredentialAdapter extends BasicCredential implements X509Credential {

    /** Class logger. */
    private Logger log = LoggerFactory.getLogger(KeyStoreX509CredentialAdapter.class);

    /** Keystore that contains the credential to be exposed. */
    private KeyStore keyStore;

    /** Alias to the credential to be exposed. */
    private String credentialAlias;

    /** Password for the key to be exposed. */
    private char[] keyPassword;

    /**
     * Constructor.
     * 
     * @param store store containing key to be exposed
     * @param alias alias to the credential to be exposed
     * @param password password to the key to be exposed
     */
    public KeyStoreX509CredentialAdapter(KeyStore store, String alias, char[] password) {
        keyStore = store;
        credentialAlias = alias;
        keyPassword = password;
    }

    /** {@inheritDoc} */
    public Collection<X509CRL> getCRLs() {
        return Collections.EMPTY_LIST;
    }

    /** {@inheritDoc} */
    public X509Certificate getEntityCertificate() {
        try {
            return (X509Certificate) keyStore.getCertificate(credentialAlias);
        } catch (KeyStoreException e) {
            log.error("Error accessing {} certificates in keystore", e);
            return null;
        }
    }

    /** {@inheritDoc} */
    public Collection<X509Certificate> getEntityCertificateChain() {
        List<X509Certificate> certsCollection = Collections.EMPTY_LIST;

        try {
            Certificate[] certs = keyStore.getCertificateChain(credentialAlias);
            if (certs != null) {
                certsCollection = new ArrayList<X509Certificate>(certs.length);
                for (Certificate cert : certs) {
                    certsCollection.add((X509Certificate) cert);
                }
            }
        } catch (KeyStoreException e) {
            log.error("Error accessing {} certificates in keystore", e);
        }
        return certsCollection;
    }

    /** {@inheritDoc} */
    public PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(credentialAlias, keyPassword);
        } catch (Exception e) {
            log.error("Error accessing {} private key in keystore", e);
            return null;
        }
    }

    /** {@inheritDoc} */
    public PublicKey getPublicKey() {
        return getEntityCertificate().getPublicKey();
    }
}