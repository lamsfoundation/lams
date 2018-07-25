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

import java.util.List;

import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.StaticCredentialResolver;

/**
 * Simple implementation of {@link KeyInfoCredentialResolver} which just stores and returns a static set of credentials.
 * 
 * <p>
 * Note: no filtering or other evaluation of the credentials is performed.  Any Criteria
 * specified are ignored.  For a similar Collection-based KeyInfoCredentialResolver implementation which does support 
 * evaluation and filtering based on supplied evaluable criteria, see {@link CollectionKeyInfoCredentialResolver}.
 * </p>
 * 
 * <p>
 * This implementation might also be used at the end of a chain of KeyInfoCredentialResolvers in
 * order to supply a default, fallback set of credentials, if none could otherwise be resolved.
 * </p>
 */
public class StaticKeyInfoCredentialResolver extends StaticCredentialResolver implements KeyInfoCredentialResolver {
    
    /**
     * Constructor.
     *
     * @param credentials collection of credentials to be held by this resolver
     */
    public StaticKeyInfoCredentialResolver(List<Credential> credentials) {
        super(credentials);
    }
    
    /**
     * Constructor.
     *
     * @param credential a single credential to be held by this resolver
     */
    public StaticKeyInfoCredentialResolver(Credential credential) {
        super(credential);
    }

}
