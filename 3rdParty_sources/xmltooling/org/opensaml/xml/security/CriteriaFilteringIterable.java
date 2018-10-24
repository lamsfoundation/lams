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

package org.opensaml.xml.security;

import java.util.Iterator;
import java.util.Set;

/**
 * An implementation of {@link Iterable} which wraps another underlying Iterable in order to support
 * production of instances of {@link CriteriaFilteringIterator} based on the underlying Iterable's Iterator.
 * 
 * For iterator behavior and meaning and use of the parameters, see {@link CriteriaFilteringIterator}.
 * 
 * @param <T> the type of candidate elements being evaluated
 */
public class CriteriaFilteringIterable<T> implements Iterable<T> {
    
    /** The candidates to evaluate. */
    private Iterable<? extends T> candidates;
    
    /** The set of criteria against which to evaluate the candidates. */
    private Set<EvaluableCriteria<T>> criteriaSet;
    
    /** Flag indicating whether the candidate must satisfy all the criteria in the set, or just one. */
    private boolean meetAll;
    
    /** Flag indicating how candidates which can not be evaluated by a criteria are to be handled. */
    private boolean unevaledSatisfies;
    
    /**
     * Constructor.
     *
     * @param candidatesIterable the candidates to evaluate
     * @param criteria the set of criteria against which to evaluate the candidates
     * @param meetAllCriteria whether a candidate must meet all criteria, or just one
     * @param unevaluableSatisfies whether a can-not-evaluate result of a particular criteria's evaluation 
     *          is treated as the candidate having satisfied or not satisfied the criteria, for purposes
     *          of determinig whether to return the element
     */
    public CriteriaFilteringIterable(Iterable<? extends T> candidatesIterable, Set<EvaluableCriteria<T>> criteria, 
            boolean meetAllCriteria, boolean unevaluableSatisfies) {
        
        candidates = candidatesIterable;
        criteriaSet = criteria;
        meetAll = meetAllCriteria;
        unevaledSatisfies = unevaluableSatisfies;
    }

    /** {@inheritDoc} */
    public Iterator<T> iterator() {
        return new CriteriaFilteringIterator<T>(candidates.iterator(), criteriaSet, meetAll, unevaledSatisfies);
    }
    
}
