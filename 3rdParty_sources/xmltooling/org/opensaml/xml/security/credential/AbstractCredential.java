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

package org.opensaml.xml.security.credential;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;

import javax.crypto.SecretKey;

/**
 * Base class for {@link org.opensaml.xml.security.credential.Credential} implementations.
 */
public abstract class AbstractCredential implements Credential {

    /** ID of the entity owning this credential. */
    protected String entityID;
    
    /** Usage type of this credential. */
    protected UsageType usageType;
    
    /** Key names for this credential. */
    protected Collection<String> keyNames;
    
    /** Public key of this credential. */
    protected PublicKey publicKey;
    
    /** Secret key for this credential. */
    protected SecretKey secretKey;
    
    /** Private key of this credential. */
    protected PrivateKey privateKey;
    
    /** Credential context of this credential. */
    protected final CredentialContextSet credentialContextSet;
    
    /**
     * Constructor.
     */
    public AbstractCredential() {
        credentialContextSet = new CredentialContextSet(); 
    }
    
    /** {@inheritDoc}  */
    public String getEntityId() {
        return entityID;
    }

    /** {@inheritDoc}  */
    public UsageType getUsageType() {
        return usageType;
    }

    /** {@inheritDoc} */
    public Collection<String> getKeyNames() {
        return keyNames;
    }
    
    /** {@inheritDoc}  */
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    /** {@inheritDoc} */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /** {@inheritDoc}  */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /** {@inheritDoc} */
    public CredentialContextSet getCredentalContextSet() {
        return credentialContextSet;
    }
    
}