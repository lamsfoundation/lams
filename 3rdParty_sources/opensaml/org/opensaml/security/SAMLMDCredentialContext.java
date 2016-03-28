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

package org.opensaml.security;

import java.util.List;

import org.opensaml.saml2.metadata.EncryptionMethod;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.xml.security.credential.CredentialContext;

/**
 * A credential context for credentials resolved from a {@link org.opensaml.xml.signature.KeyInfo} that was found in
 * SAML 2 metadata.
 */
public class SAMLMDCredentialContext implements CredentialContext {

    /** Key descriptor which contained the KeyInfo used. */
    private KeyDescriptor keyDescriptor;

    /** Role in which credential was resolved. */
    private RoleDescriptor role;

    /** Encryption methods associated with the credential. */
    private List<EncryptionMethod> encMethods;

    /**
     * Constructor.
     * 
     * @param descriptor the KeyDescriptor context from which a credential was resolved
     */
    public SAMLMDCredentialContext(KeyDescriptor descriptor) {
        keyDescriptor = descriptor;
        if (descriptor != null) {
            // KeyDescriptor / EncryptionMethod
            encMethods = descriptor.getEncryptionMethods();
            // KeyDescriptor -> RoleDescriptor
            role = (RoleDescriptor) descriptor.getParent();
        }
    }

    /**
     * Get the key descriptor context.
     * 
     * @return key descriptor
     */
    public KeyDescriptor getKeyDescriptor() {
        return keyDescriptor;
    }

    /**
     * Return the list of {@link EncryptionMethod}'s associated with credential context.
     * 
     * @return a list of SAML metadata encryption method associated with this context
     * @deprecated due to typo, use {@link #getEncryptionMethods()}.
     */
    public List<EncryptionMethod> getEncryptionMethod() {
        return getEncryptionMethods();
    }
    
    /**
     * Return the list of {@link EncryptionMethod}'s associated with credential context.
     * 
     * @return a list of SAML metadata encryption method associated with this context
     */
    public List<EncryptionMethod> getEncryptionMethods() {
        return encMethods;
    }

    /**
     * Get the role descriptor context.
     * 
     * @return role descriptor
     */
    public RoleDescriptor getRoleDescriptor() {
        return role;
    }

}