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

package org.opensaml.ws.message;


/**
 * An interface for components which evaluate a message context as the basis for extracting, calculating,
 * or otherwise producing a specific data value.
 * 
 * <p>
 * Implementations should not have side effects and should not modify any data in the 
 * underlying message context.  For a component that is intended to allow message context
 * modification, see {@link MessageContextMutatingFunctor}.
 * </p>
 * 
 * @param <T> the type of product of the component
 */
public interface MessageContextEvaluatingFunctor<T> {
    
    /**
     * Using the specified MessageContext as the evaluation context,
     * produce a data value product of the appropriate type.
     * 
     * @param msgContext the message context to evaluate
     * @return value product based on the message context, or null
     * 
     * @throws MessageException if there is a fatal error evaluating the context
     */
    public T evaluate(MessageContext msgContext) throws MessageException;

}
