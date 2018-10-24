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

package com.tacitknowledge.util.migration.jdbc.loader;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

/**
 * Test used to verify that the sql query is correctly 
 * created
 * 
 * @author Chris A. (chris@tacitknowledge.com)
 */
public class QueryTest extends TestCase
{
    /**
     * The dummy loader used for testing
     */
    private TestLoader loader = null;
    
    /**
     * Constructor that invokes its parent's constructor
     * 
     * @param name the test name
     */
    public QueryTest(String name)
    {
        super(name);
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception
    {
        super.setUp();
        loader = new TestLoader();
    }
    
    /**
     * Tests query generation
     */
    public void testQuery()
    {
        loader.setName("mocktable_db.dat");
        String sql = loader.getStatmentSql();
        String answer = "INSERT INTO mocktable (col1, col2, col3, col4) "
            + "VALUES (?, ?, ?, ?)";
        assertTrue(answer.equals(sql));
    }
    
    /**
     * Inner class used to test file loader
     */
    private class TestLoader extends DelimitedFileLoader
    {
        /**
         * The name of the file
         */
        private String name = null;

        /**
         * @see com.tacitknowledge.util.migration.MigrationTask#getName()
         */
        public String getName()
        {
            return name;
        }

        /**
         * @see com.tacitknowledge.util.migration.MigrationTaskSupport#setName(java.lang.String)
         */
        public void setName(String name)
        {
            this.name = name;
        }

        /**
         * @see com.tacitknowledge.util.migration.jdbc.loader.DelimitedFileLoader#getDelimiter()
         */
        public String getDelimiter()
        {
            return "|";
        }
        
        /**
         * Returns the header (first line) of the file.
         * 
         * @param  is the input stream containing the data to load
         * @return the first row
         * @throws IOException if the input stream could not be read
         */
        protected String getHeader(InputStream is) throws IOException
        {
            return "col1|col2|col3|col4";
        }
       
        /**
         * @see com.tacitknowledge.util.migration.jdbc.SqlLoadMigrationTask#getResourceAsStream()
         */
        protected InputStream getResourceAsStream()
        {
            return null;
        }
    }
}

