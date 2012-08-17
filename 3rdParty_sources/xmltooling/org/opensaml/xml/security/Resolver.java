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


/**
 * Generic interface for resolvers which process specified criteria and produce some implementation-specific
 * result information.
 * 
 * @param <ProductType> the type of objects produced by this resolver
 * @param <CriteriaType> the type of criteria to process during resolution
 */
public interface Resolver<ProductType, CriteriaType> {
    

    /**
     * Process the specified criteria and return the resulting instances the the product type
     * which satisfy the criteria.
     * 
     * @param criteria the criteria to evaluate or process
     * @return instances which satisfy the criteria
     * @throws SecurityException thrown if there is an error processing the specified criteria
     */
    Iterable<ProductType> resolve(CriteriaType criteria) throws SecurityException;
    
    /**
     * Process the specified criteria and return a single instance of the product type
     * which satisfies the criteria.
     * 
     * If multiple items satisfy the criteria, the choice of which single item to 
     * return is implementation-dependent.
     * 
     * @param criteria the criteria to evaluate or process
     * @return instances which satisfy the criteria
     * @throws SecurityException thrown if there is an error processing the specified criteria
     */
    ProductType resolveSingle(CriteriaType criteria) throws SecurityException;
}
