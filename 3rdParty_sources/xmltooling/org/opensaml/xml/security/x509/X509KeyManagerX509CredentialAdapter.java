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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.net.ssl.X509KeyManager;

import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.util.DatatypeHelper;

/** A class that wraps a {@link X509KeyManager} and exposes it as an {@link X509Credential}. */
public class X509KeyManagerX509CredentialAdapter extends BasicCredential implements X509Credential {

    /** Alias used to reference the credential in the key manager. */
    private String credentialAlias;

    /** Wrapped key manager. */
    private X509KeyManager keyManager;

    /**
     * Constructor.
     * 
     * @param manager wrapped key manager
     * @param alias alias used to reference the credential in the key manager
     */
    public X509KeyManagerX509CredentialAdapter(X509KeyManager manager, String alias) {
        if (manager == null) {
            throw new IllegalArgumentException("Key manager may not be null");
        }
        keyManager = manager;

        credentialAlias = DatatypeHelper.safeTrimOrNullString(alias);
        if (credentialAlias == null) {
            throw new IllegalArgumentException("Entity alias may not be null");
        }
    }

    /** {@inheritDoc} */
    public Collection<X509CRL> getCRLs() {
        return Collections.EMPTY_LIST;
    }

    /** {@inheritDoc} */
    public X509Certificate getEntityCertificate() {
        X509Certificate[] certs = keyManager.getCertificateChain(credentialAlias);
        if (certs != null && certs.length > 0) {
            return certs[0];
        }

        return null;
    }

    /** {@inheritDoc} */
    public Collection<X509Certificate> getEntityCertificateChain() {
        X509Certificate[] certs = keyManager.getCertificateChain(credentialAlias);
        if (certs != null && certs.length > 0) {
            return Arrays.asList(certs);
        }

        return null;
    }

    /** {@inheritDoc} */
    public PrivateKey getPrivateKey() {
        return keyManager.getPrivateKey(credentialAlias);
    }

    /** {@inheritDoc} */
    public PublicKey getPublicKey() {
        return getEntityCertificate().getPublicKey();
    }
}