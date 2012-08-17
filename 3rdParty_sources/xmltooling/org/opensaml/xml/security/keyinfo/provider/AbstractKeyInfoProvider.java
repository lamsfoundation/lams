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

import java.security.Key;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialContext;
import org.opensaml.xml.security.keyinfo.KeyInfoProvider;
import org.opensaml.xml.security.keyinfo.KeyInfoResolutionContext;

/**
 * Abstract super class for {@link KeyInfoProvider} implementations.
 */
public abstract class AbstractKeyInfoProvider implements KeyInfoProvider {
    
    /**
     * Utility method to extract any key that might be present in the specified Credential.
     * 
     * @param cred the Credential to evaluate
     * @return the Key contained in the credential, or null if it does not contain a key.
     */
    protected Key extractKeyValue(Credential cred) {
        if (cred == null) {
            return null;
        }
        if (cred.getPublicKey() != null) {
            return cred.getPublicKey();
        } 
        // This could happen if key is derived, e.g. key agreement, etc
        if (cred.getSecretKey() != null) {
            return cred.getSecretKey();
        }
        // Perhaps unlikely, but go ahead and check
        if (cred.getPrivateKey() != null) {
            return cred.getPrivateKey(); 
        }
        return null;
    }
    
    /**
     * Build a credential context based on the current KeyInfo context, for return 
     * in a resolved credential.
     * 
     * @param kiContext the current KeyInfo resolution context
     * 
     * @return a new KeyInfo credential context
     */
    protected KeyInfoCredentialContext buildCredentialContext(KeyInfoResolutionContext kiContext) {
        // Simple for now, might do other stuff later.
        // Just want to provide a single place to build credential contexts for
        // a provider.
        if (kiContext != null) {
            return new KeyInfoCredentialContext(kiContext.getKeyInfo());
        } else {
            return null;
        }
    }

}
