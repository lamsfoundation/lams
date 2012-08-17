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

/**
 * An interface for factory classes which implement a singleton pattern for producing an
 * output class based on an input class.
 * 
 * <p>
 * Classes which implement this interface should ensure that exactly one instance of a given output
 * class is returned from the factory for a given instance of an input class.
 * </p>
 *
 * @param <Input> the factory input class type
 * @param <Output> the factory output class type
 */
public interface SingletonFactory<Input, Output> {

    /**
     * Obtain an instance of the output class based on an input class instance.
     * 
     * @param input the input class instance
     * @return an output class instance
     */
    public Output getInstance(Input input);

}