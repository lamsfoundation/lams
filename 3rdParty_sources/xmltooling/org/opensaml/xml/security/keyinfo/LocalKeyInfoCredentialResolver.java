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

package org.opensaml.xml.security.keyinfo;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opensaml.xml.encryption.Decrypter;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialResolver;
import org.opensaml.xml.security.criteria.KeyNameCriteria;
import org.opensaml.xml.security.criteria.PublicKeyCriteria;
import org.opensaml.xml.signature.KeyInfo;

/**
 * A simple specialization of {@link BasicProviderKeyInfoCredentialResolver}
 * which is capable of using information from a {@link KeyInfo} to resolve
 * local credentials from a supplied {@link CredentialResolver} which manages local credentials.
 * 
 * <p>
 * The local credential resolver supplied should manage and return credentials
 * which contain either a secret (symmetric) key or the private key half of a
 * key pair.
 * </p>
 * 
 * <p>
 * A typical use case for this class would be as a resolver of decryption keys,
 * such as is needed by {@link Decrypter}.
 * </p>
 * 
 * <p>
 * Resolution proceeds as follows:
 * <ol>
 *   <li>Any credential resolved via the standard {@link BasicProviderKeyInfoCredentialResolver}
 *       resolution process which is not a local credential will be removed
 *       from the effective set of credentials to be returned.  Note that a configured
 *       {@link KeyInfoProvider} may have itself already resolved local credentials using a
 *       different mechanism.  These will not be removed.</li>
 *   <li>If a credential so removed contained a public key, that key will be used as a
 *       resolution criteria input to the local credential resolver.  Any local credentials
 *       so resolved will be added to the set to be returned.</li>
 *   <li>Similarly, any key names from {@link KeyInfoResolutionContext#getKeyNames()} will also
 *       be used as resolution criteria for local credentials and the resultant credentials
 *       added to the set to be returned.</li>
 * </ol>
 * </p>
 */
public class LocalKeyInfoCredentialResolver extends BasicProviderKeyInfoCredentialResolver {
    
    /** The resovler which is used to resolve local credentials. */
    private CredentialResolver localCredResolver;

    /**
     * Constructor.
     *
     * @param keyInfoProviders the list of KeyInfoProvider's to use in this resolver
     * @param localCredentialResolver resolver of local credentials
     */
    public LocalKeyInfoCredentialResolver(List<KeyInfoProvider> keyInfoProviders,
            CredentialResolver localCredentialResolver) {
        super(keyInfoProviders);
        
        if (localCredentialResolver == null) {
            throw new IllegalArgumentException("Local credential resolver must be supplied");
        }
        
        localCredResolver = localCredentialResolver;
    }
    
    /**
     * Get the resolver for local credentials.
     * 
     * The credentials managed and returned by this resolver should all contain
     * either a secret (symmetric) or private key.
     *
     * @return resolver of local credentials
     */
    public CredentialResolver getLocalCredentialResolver() {
        return localCredResolver;
    }

    /** {@inheritDoc} */
    protected void postProcess(KeyInfoResolutionContext kiContext, CriteriaSet criteriaSet,
            List<Credential> credentials) throws SecurityException {
        
        ArrayList<Credential> localCreds = new ArrayList<Credential>();
        
        for (Credential cred : credentials) {
            if (isLocalCredential(cred)) {
                localCreds.add(cred);
            } else if (cred.getPublicKey() != null) {
               localCreds.addAll(resolveByPublicKey(cred.getPublicKey()));
            }
        }
        
        // Also resolve local creds based on any key names that are known
        for (String keyName : kiContext.getKeyNames()) {
            localCreds.addAll(resolveByKeyName(keyName));
        }
        
        credentials.clear();
        credentials.addAll(localCreds);
    }
    
    /**
     * Determine whether the credential is a local credential.
     * 
     * A local credential will have either a private key or a secret (symmetric) key.
     * 
     * @param credential the credential to evaluate
     * @return true if the credential has either a private or secret key, false otherwise
     */
    protected boolean isLocalCredential(Credential credential) {
        return credential.getPrivateKey() != null || credential.getSecretKey() != null;
    }

    /**
     * Resolve credentials from local resolver using key name criteria.
     * 
     * @param keyName the key name criteria
     * @return collection of local credentials identified by the specified key name
     * @throws SecurityException  thrown if there is a problem resolving credentials from the 
     *          local credential resolver
     */
    protected Collection<? extends Credential> resolveByKeyName(String keyName) throws SecurityException {
        ArrayList<Credential> localCreds = new ArrayList<Credential>();
        
        CriteriaSet criteriaSet = new CriteriaSet( new KeyNameCriteria(keyName) );
        for (Credential cred : getLocalCredentialResolver().resolve(criteriaSet)) {
            if (isLocalCredential(cred)) {
                localCreds.add(cred);
            }
        }
        
        return localCreds;
    }

    /**
     * Resolve credentials from local resolver using public key criteria.
     * 
     * @param publicKey the public key criteria
     * @return collection of local credentials which contain the private key
     *          corresponding to the specified public key
     * @throws SecurityException  thrown if there is a problem resolving credentials from the 
     *          local credential resolver
     */
    protected Collection<? extends Credential> resolveByPublicKey(PublicKey publicKey) throws SecurityException {
        ArrayList<Credential> localCreds = new ArrayList<Credential>();
        
        CriteriaSet criteriaSet = new CriteriaSet( new PublicKeyCriteria(publicKey) );
        for (Credential cred : getLocalCredentialResolver().resolve(criteriaSet)) {
            if (isLocalCredential(cred)) {
                localCreds.add(cred);
            }
        }
        
        return localCreds;
    }

}
