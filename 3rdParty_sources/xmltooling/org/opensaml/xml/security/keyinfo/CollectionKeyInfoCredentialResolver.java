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

import java.util.ArrayList;
import java.util.Collection;

import org.opensaml.xml.security.credential.CollectionCredentialResolver;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.criteria.EvaluableCredentialCriteria;
import org.opensaml.xml.security.credential.criteria.EvaluableCredentialCriteriaRegistry;
import org.opensaml.xml.signature.KeyName;
import org.opensaml.xml.signature.X509SubjectName;

/**
 * An implementation of {@link KeyInfoCredentialResolver} which uses a {@link Collection} as the
 * underlying credential source.
 * 
 * <p>
 * Like the
 * {@link CollectionCredentialResolver}, credentials returned are filtered based on any
 * {@link EvaluableCredentialCriteria} which may have been present in the specified criteria set, or
 * which are resolved by lookup in the {@link EvaluableCredentialCriteriaRegistry}.
 * </p>
 * 
 * <p>
 * This implementation may be used to address use cases where use of a
 * KeyInfoCredentialResolver is required, but a KeyInfo element containing keys or other keying 
 * material is not necessarily supplied or expected in an instance document and keys/credentials
 * are known in advance (e.g. validation keys belonging to a peer, decryption keys belonging to the caller).
 * In this use case, credentials are expected to be resolved from other contextual information,
 * including information possibly supplied as criteria to the resolver.  Such credentials would be stored
 * in and returned from the {@link Collection} managed by this resolver.
 * </p>
 * 
 * <p>
 * Note that a KeyInfo element
 * passed in a {@link KeyInfoCriteria} in the criteria set is <code>NOT</code> directly processed by this
 * implementation in any way as a source for extracting keys or other key-related material.
 * However, if the evaluable credential criteria registry described above were
 * for example to contain a mapping from KeyInfoCriteria to some type of EvaluableCredentialCriteria,
 * where the latter used KeyInfo-derived information as its basis for evaluation of a credential (e.g.
 * based on contents of a {@link KeyName} or {@link X509SubjectName}), then such KeyInfo-derived 
 * evaluable criteria would be used to filter or select the specific credentials that would be returned
 * from the underlying credential collection of this resolver.  Such KeyInfo-derived evaluable criteria
 * may also be specified directly in the criteria set, per the above.
 * </p>
 * 
 * <p>
 * This implementation might also be used at the end of a chain of KeyInfoCredentialResolvers in
 * order to supply a default, fallback set of credentials, if none could otherwise be resolved.
 * </p>
 * 
 */
public class CollectionKeyInfoCredentialResolver extends CollectionCredentialResolver implements
        KeyInfoCredentialResolver {
    
    /**
     * Constructor.
     * 
     * An {@link ArrayList} is used as the underlying collection implementation.
     *
     */
    public CollectionKeyInfoCredentialResolver() {
        this(new ArrayList<Credential>());
    }
    
    /**
     * Constructor.
     *
     * @param credentials the credential collection which is the backing store for the resolver
     */
    public CollectionKeyInfoCredentialResolver(Collection<Credential> credentials) {
        super(credentials);
    }

}
