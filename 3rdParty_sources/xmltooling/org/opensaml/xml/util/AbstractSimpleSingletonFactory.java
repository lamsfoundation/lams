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

import java.util.WeakHashMap;

/**
 * A simple implementation of {@link SingletonFactory}.
 * 
 * <p>
 * A {@link WeakHashMap} is used as the underlying store. This ensures that if the input class 
 * instance become otherwise unused (weakly reachable), the input class instance key used 
 * within the factory will not prevent the input class instance from being garbage-collected,
 * thereby preventing a memory leak.
 * </p>
 * 
 * <p>
 * <strong>NOTE: </strong>If the output class instance holds a strong or soft reference to the input class,
 * do not use this factory.  See instead {@link AbstractWrappedSingletonFactory}. Usage of this
 * class in that scenario will result in a memory leak, as the input class instance will never
 * become weakly reachable and therefore never garbage collected.
 * </p>
 * 
 *
 * @param <Input> the factory input class type
 * @param <Output> the factory output class type
 */
public abstract class AbstractSimpleSingletonFactory<Input, Output> 
        extends AbstractSingletonFactory<Input, Output> {
    
    /** Storage for the factory. */
    private WeakHashMap<Input, Output> map;
    
    /** Constructor. */
    public AbstractSimpleSingletonFactory() {
        map = new WeakHashMap<Input, Output>();
    }
    
    /** {@inheritDoc} */
    protected synchronized Output get(Input input) {
        return map.get(input);
    }
    
    /** {@inheritDoc} */
    protected synchronized void put(Input input, Output output) {
        map.put(input, output);
    }
    
}
