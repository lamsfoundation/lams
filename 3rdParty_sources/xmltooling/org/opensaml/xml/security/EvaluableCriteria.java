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
 * Interface for criteria which are capable of evaluating a target of a particular type.
 * 
 * @param <T> the type of object which may be evaluated
 */
public interface EvaluableCriteria<T> extends Criteria {
    
    /**
     * Evaluate the target.
     * 
     * The result of evaluation is one of the following values:
     * <ul>
     *   <li><code>Boolean.TRUE</code> if the target satisfies the criteria</li>
     *   <li><code>Boolean.FALSE</code> if the target does not satisfy criteria</li>
     *   <li><code>null</code> if the target can not be evaluated against the criteria</li>
     * </ul>
     * 
     * @param target the object to be evaluated
     * @return the result of evaluation
     */
    public Boolean evaluate(T target);

}
