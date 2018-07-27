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

package org.opensaml.xml.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract Template design pattern implementation of {@link SingletonFactory}.
 *
 * @param <Input> the factory input class type
 * @param <Output> the factory output class type
 */
public abstract class AbstractSingletonFactory<Input, Output> implements SingletonFactory<Input, Output> {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AbstractSingletonFactory.class);
    
    /** {@inheritDoc} */
    public synchronized Output getInstance(Input input) {
        Output output = get(input);
        if (output != null) {
            log.trace("Input key mapped to a non-null value, returning output");
            return output;
        } else {
            log.trace("Input key mapped to a null value");
        }
        
        log.trace("Creating new output instance and inserting to factory map");
        output = createNewInstance(input);
        if (output == null) {
            log.error("New output instance was not created");
            return null;
         }
        
        put(input, output);
        
        return output;
    }
    
    /**
     * Get the output instance currently associated with
     * the input instance.
     * 
     * @param input the input instance key
     * @return the output instance which corresponds to the input instance,
     *              or null if not present
     */
    protected abstract Output get(Input input);
    
    /**
     * Store the input and output instance association.
     * 
     * @param input the input instance key
     * @param output the output instance value
     */
    protected abstract void put(Input input, Output output);

    /**
     * Create a new instance of the output class based on the input
     * class instance.
     * 
     * @param input the input class instance
     * @return an output class instance
     */
    protected abstract Output createNewInstance(Input input);

}
