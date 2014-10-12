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

package com.tacitknowledge.util.migration.jdbc.util;

import junit.framework.TestCase;

/**
 * Test class for {@link SybaseUtil}.
 * 
 * @author Alex Soto (apsoto@gmail.com)
 */
public class SybaseUtilTest extends TestCase 
{

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception 
    {
        super.setUp();
    }
    
    /**
     * Test the containsIllegalMultiStatementTransactionCommand method.
     *
     */
    public void testContainsIllegalMultiStatementTransactionCommand()
    {
        String simpleSql = "ALTER TABLE foo ADD version DEFAULT 0";
        String multiLineSql = "INSERT INTO TABLE foo(id) VALUES(1)\n"
                              + "alter table foo ADD VERSION DEFAULT 0\n"
                              + "INSERT INTO TABLE foo(id, version) VALUES (1, 1)\n";
        String noIllegalSql = "SELECT * FROM foo";
        
        assertTrue(SybaseUtil.containsIllegalMultiStatementTransactionCommand(simpleSql));
        assertTrue(SybaseUtil.containsIllegalMultiStatementTransactionCommand(multiLineSql));
        assertFalse(SybaseUtil.containsIllegalMultiStatementTransactionCommand(noIllegalSql));
    }

}
