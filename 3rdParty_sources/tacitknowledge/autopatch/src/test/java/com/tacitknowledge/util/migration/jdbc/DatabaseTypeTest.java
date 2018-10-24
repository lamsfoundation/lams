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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * @author Alex Soto (apsoto@gmail.com)
 */
public class DatabaseTypeTest extends TestCase
{
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /**
     * Test method for {@link com.tacitknowledge.util.migration.jdbc.DatabaseType#getDatabaseType()}.
     */
    public void testGetDatabaseType()
    {
        String type = "mysql";
        DatabaseType databseType = new DatabaseType(type);
        assertEquals(type, databseType.getDatabaseType());
    }

    /**
     * Test method for {@link com.tacitknowledge.util.migration.jdbc.DatabaseType#isMultipleStatementsSupported()}.
     */
    public void testOverrideProperties()
    {
        /*
         * Load a known database properties file, and make sure the value
         * is what we expect before we override it.
         */
        String type = "mysql";
        String dbTypeExpectedValue="false";
        DatabaseType databaseType = new DatabaseType(type);
        Properties dbProperties = new Properties();
        
        InputStream is = DatabaseType.class.getResourceAsStream(type + ".properties");
        if (is == null)
        {
            fail("Could not find SQL properties "
                + " file for database '" + databaseType + "'; make sure that there "
                + " is a '" + databaseType + ".properties' file in package '"
                + DatabaseType.class.getPackage().getName() + "'.");
            return;
        }
        
        try
        {
            dbProperties.load(is);
            assertEquals(dbTypeExpectedValue, dbProperties.getProperty("supportsMultipleStatements"));
        }
        catch (IOException e)
        {
            fail("Could not read SQL properties file for database '" + type + "'.");
            return;
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e1)
            {
                // not important
            }
        }
        
        /*
         * Load a known migration.properties file and make sure the expected property exists
         * and is the value we assume.
         */
        Properties migrationProperties = new Properties();
        String migrationExpectedValue = "false";
        InputStream is2 = getClass().getResourceAsStream("/migration.properties");
        if (is2 == null)
        {
            fail("Could not find migration.properties.");
            return;
        }
        
        try
        {
            migrationProperties.load(is2);
            assertEquals(migrationExpectedValue, migrationProperties.getProperty("mysql.supportsMultipleStatements"));
        }
        catch (IOException e)
        {
            fail("Could not read migrations properties file.");
            return;
        }
        finally
        {
            try
            {
                is2.close();
            }
            catch (IOException e1)
            {
                // not important
            }
        }
        
        /*
         * Finally, test that the database type properties returns expected override
         */
        boolean overrideExpectedValue = false;
        assertEquals(overrideExpectedValue, databaseType.isMultipleStatementsSupported());
    }


}
