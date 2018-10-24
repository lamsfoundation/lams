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

package com.tacitknowledge.util.migration;

/**
 * An exception we can use to type things as we send problems up
 *
 * @author Scott Askew (scott@tacitknowledge.com)
 */
public class MigrationException extends Exception
{
    /**
     * Make a new MigrationException with the given message
     *
     * @param message the message to include in the exception
     */
    public MigrationException(String message)
    {
        super(message);
    }

    /**
     * Make a new MigrationException with the given message and cause
     *
     * @param message the message to include in the exception
     * @param cause   the cause of the problem
     */
    public MigrationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
