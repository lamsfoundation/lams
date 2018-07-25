/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration.tasks.instantiation;

import com.tacitknowledge.util.migration.tasks.BaseTestMigrationTask;

/**
 * 
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class TestMigrationTaskInstantiationException extends BaseTestMigrationTask
{
    /**
     * Constructor throws an exception - this task never works
     * 
     * @exception RuntimeException when instantiated
     */
    public TestMigrationTaskInstantiationException() throws RuntimeException
    {
        super("TestMigrationTaskInstantiationException", 1);
        throw new RuntimeException("This class always throws exceptions when instantiated");
    }
}
