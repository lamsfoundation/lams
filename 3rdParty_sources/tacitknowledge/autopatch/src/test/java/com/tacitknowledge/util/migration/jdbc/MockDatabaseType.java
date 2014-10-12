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

package com.tacitknowledge.util.migration.jdbc;

/**
 * MockDatabaseType since DatabaseType is not interface based and
 * can't mock it via easymock (without upgrading version to use the 
 * class extension lib)
 *
 * @author Alex Soto (apsoto@gmail.com)
 */
class MockDatabaseType extends DatabaseType
{
    /** does database type support multipe sql statements per stmt.execute() */
    private boolean multipleStatementsSupported;
    
    /**
     * constructor
     * @param databaseType set the type
     */
    public MockDatabaseType(String databaseType) 
    {
        super(databaseType);
    }

    /** {@inheritDoc} */
    public boolean isMultipleStatementsSupported() 
    {
        return multipleStatementsSupported;
    }

    /**
     * simple setter
     * @param multipleStatementsSupported the value to set
     */
    public void setMultipleStatementsSupported(boolean multipleStatementsSupported) 
    {
        this.multipleStatementsSupported = multipleStatementsSupported;
    }
    
}

