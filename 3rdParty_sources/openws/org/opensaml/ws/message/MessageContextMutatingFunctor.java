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
 * An interface for components which mutate a message context, or data contained therein,
 * based on an input value of a particular type.
 * 
 * @param <T> the type of input to the operation to be performed on the message context
 */
public interface MessageContextMutatingFunctor<T> {
    
    /**
     * Mutate the specified message context based on the supplied input value.
     * 
     * @param msgContext the current message context
     * @param input the input to the mutation operation
     * 
     * @throws MessageException if there is a fatal error processing the context
     */
    public void mutate(MessageContext msgContext, T input) throws MessageException;

}
