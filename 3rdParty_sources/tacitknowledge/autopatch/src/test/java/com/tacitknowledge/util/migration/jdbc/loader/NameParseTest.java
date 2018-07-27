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

import junit.framework.TestCase;

/**
 * Test used to verify that the table name is correctly 
 * parsed.
 * 
 * @author Chris A. (chris@tacitknowledge.com)
 */
public class NameParseTest extends TestCase
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
    public NameParseTest(String name)
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
     * Tests several combinations of input names to insure things are 
     * working well.
     */
    public void testNameParsing()
    {
        String longName = DelimitedFileLoader.PATH_SEPARATOR + "parent-dir"
            + DelimitedFileLoader.PATH_SEPARATOR + "child-dir"
            + DelimitedFileLoader.PATH_SEPARATOR + "table_db20040704.load";
        String medName = DelimitedFileLoader.PATH_SEPARATOR + "table_db20040704.load";
        String[] names = {"table_db.dat", longName, medName};
        for (int i = 0; i < names.length; i++)
        {
            loader.setName(names[i]);
            String tb = loader.getTableFromName();
            assertTrue("table".equals(tb));
        }
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
    }
}
