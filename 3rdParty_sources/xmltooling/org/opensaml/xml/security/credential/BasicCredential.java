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

import javax.crypto.SecretKey;

import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.LazySet;

/**
 * A basic implementation of {@link Credential}.
 */
public class BasicCredential extends AbstractCredential {

    /** Constructor. */
    public BasicCredential() {
        super();
        keyNames = new LazySet<String>();
        usageType = UsageType.UNSPECIFIED;
    }
    
    /** {@inheritDoc} */
    public Class<? extends Credential> getCredentialType() {
        return Credential.class;
    }

    /**
     * Sets the ID of the entity this credential is for.
     * 
     * @param id ID of the entity this credential is for
     */
    public void setEntityId(String id) {
        entityID = DatatypeHelper.safeTrimOrNullString(id);
    }

    /**
     * Sets the usage type for this credential.
     * 
     * @param usage usage type for this credential
     */
    public void setUsageType(UsageType usage) {
        if (usage != null) {
            usageType = usage;
        } else {
            usageType = UsageType.UNSPECIFIED;
        }
    }

    /**
     * Sets the public key for this credential.
     * 
     * @param key public key for this credential
     */
    public void setPublicKey(PublicKey key) {
        publicKey = key;
        if (key != null) {
            setSecretKey(null);
        }
    }

    /**
     * Sets the secret key for this credential.
     * 
     * @param key secret key for this credential
     */
    public void setSecretKey(SecretKey key) {
        secretKey = key;
        if (key != null) {
            setPublicKey(null);
            setPrivateKey(null);
        }
    }

    /**
     * Sets the private key for this credential.
     * 
     * @param key private key for this credential
     */
    public void setPrivateKey(PrivateKey key) {
        privateKey = key;
        if (key != null) {
            setSecretKey(null);
        }
    }
}