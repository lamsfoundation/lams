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

import java.util.HashSet;
import java.util.Set;

import org.opensaml.xml.security.Criteria;
import org.opensaml.xml.security.CriteriaFilteringIterable;
import org.opensaml.xml.security.CriteriaFilteringIterator;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.EvaluableCriteria;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.criteria.EvaluableCredentialCriteria;
import org.opensaml.xml.security.credential.criteria.EvaluableCredentialCriteriaRegistry;

/**
 * An abstract implementation of {@link CredentialResolver} which filters the returned Credentials
 * based on the instances of {@link EvaluableCredentialCriteria} which are present in the set of
 * criteria, or which are obtained via lookup in the {@link EvaluableCredentialCriteriaRegistry}.
 */
public abstract class AbstractCriteriaFilteringCredentialResolver extends AbstractCredentialResolver {
    
    /** Flag to pass to CriteriaFilteringIterable constructor parameter 'meetAllCriteria'. */
    private boolean meetAllCriteria;
    
    /** Flag to pass to CriteriaFilteringIterable constructor 'unevaluableSatisfies'. */
    private boolean unevaluableSatisfies;
    
    /**
     * Constructor.
     *
     */
    public AbstractCriteriaFilteringCredentialResolver() {
        super();
        meetAllCriteria = true;
        unevaluableSatisfies = true;
    }

    /** {@inheritDoc} */
    public Iterable<Credential> resolve(CriteriaSet criteriaSet) throws SecurityException {
        Iterable<Credential> storeCandidates = resolveFromSource(criteriaSet);
        Set<EvaluableCriteria<Credential>> evaluableCriteria = getEvaluableCriteria(criteriaSet);
        if (evaluableCriteria.isEmpty()) {
            return storeCandidates;
        } else {
            return new CriteriaFilteringIterable<Credential>(storeCandidates, evaluableCriteria, 
                    meetAllCriteria, unevaluableSatisfies);
        }
    }
    
    /**
     * Get whether all {@link EvaluableCredentialCriteria} must be met to return
     * a credential, or only one or more evaluable criteria.
     * 
     * See also {@link CriteriaFilteringIterator}.
     * 
     * @return Returns the meetAllCriteria flag.
     */
    public boolean isMeetAllCriteria() {
        return meetAllCriteria;
    }

    /**
     * Set whether all {@link EvaluableCredentialCriteria} must be met to return
     * a credential, or only one or more evaluable criteria.
     * 
     * See also {@link CriteriaFilteringIterator}.
     * 
     * @param flag the new meetAllCriteria flag value.
     */
    public void setMeetAllCriteria(boolean flag) {
        meetAllCriteria = flag;
    }

    /**
     * Get the flag which determines the processing behavior when 
     * an {@link EvaluableCredentialCriteria} is unable to evaluate
     * a Credential.
     * 
     * See also {@link CriteriaFilteringIterator}.
     * 
     * @return Returns the unevaluableSatisfies flag.
     */
    public boolean isUnevaluableSatisfies() {
        return unevaluableSatisfies;
    }

    /**
     * Set the flag which determines the processing behavior when 
     * an {@link EvaluableCredentialCriteria} is unable to evaluate
     * a Credential.
     * 
     * See also {@link CriteriaFilteringIterator}.
     * 
     * @param flag the new unevaluableSatisfies flag value.
     */
    public void setUnevaluableSatisfies(boolean flag) {
        unevaluableSatisfies = flag;
    }

    /**
     * Subclasses are required to implement this method to resolve credentials from the 
     * implementation-specific type of underlying credential source.
     * 
     * @param criteriaSet the set of criteria used to resolve credentials from the credential source
     * @return an Iterable for the resolved set of credentials
     * @throws SecurityException thrown if there is an error resolving credentials from the credential source
     */
    protected abstract Iterable<Credential> resolveFromSource(CriteriaSet criteriaSet)
        throws SecurityException;

    /**
     * Extract the evaluable credential criteria from the criteria set.
     * 
     * @param criteriaSet the set of credential criteria to process.
     * @return a set of evaluable Credential criteria
     * @throws SecurityException thrown if there is an error obtaining an instance of EvaluableCredentialCriteria
     *                           from the EvaluableCredentialCriteriaRegistry
     */
    private Set<EvaluableCriteria<Credential>> getEvaluableCriteria(CriteriaSet criteriaSet) throws SecurityException {
        Set<EvaluableCriteria<Credential>> evaluable = new HashSet<EvaluableCriteria<Credential>>(criteriaSet.size());
        for (Criteria criteria : criteriaSet) {
            if (criteria instanceof EvaluableCredentialCriteria) {
                evaluable.add((EvaluableCredentialCriteria) criteria);
            } else {
                EvaluableCredentialCriteria evaluableCriteria = 
                    EvaluableCredentialCriteriaRegistry.getEvaluator(criteria);
                if (evaluableCriteria != null) {
                    evaluable.add(evaluableCriteria);
                }
            }
        }
        return evaluable;
    }

}
