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

import java.util.ArrayList;
import java.util.List;

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;

/**
 * Simple implementation of {@link CredentialResolver} which just stores and returns a static set of credentials.
 * 
 * <p>
 * Note: no filtering or other evaluation of the credentials is performed.  Any Criteria
 * specified are ignored.  For a similar Collection-based CredentialResolver implementation which does support 
 * evaluation and filtering based on supplied evaluable criteria, see {@link CollectionCredentialResolver}.
 * </p>
 */
public class StaticCredentialResolver extends AbstractCredentialResolver {
    
    /** List of credentials held by this resolver. */
    private List<Credential> creds;
    
    /**
     * Constructor.
     *
     * @param credentials collection of credentials to be held by this resolver
     */
    public StaticCredentialResolver(List<Credential> credentials) {
        creds = new ArrayList<Credential>();
        creds.addAll(credentials);
    }
    
    /**
     * Constructor.
     *
     * @param credential a single credential to be held by this resolver
     */
    public StaticCredentialResolver(Credential credential) {
        creds = new ArrayList<Credential>();
        creds.add(credential);
    }

    /** {@inheritDoc} */
    public Iterable<Credential> resolve(CriteriaSet criteria) throws SecurityException {
        return creds;
    }

}
