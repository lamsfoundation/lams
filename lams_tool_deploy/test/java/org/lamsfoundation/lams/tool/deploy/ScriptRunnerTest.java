/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.deploy;

import junit.framework.*;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.dbutils.DbUtils;
import java.io.File;
import org.apache.commons.io.FileUtils;
/**
 *
 * @author chris
 */
public class ScriptRunnerTest extends TestCase
{
    private Connection conn = null;
    private String testScript = null;
    private ScriptRunner runner = null;
    private String[] testStatements =
    {"CREATE TABLE TEST_3 (TEST_3_INT INT, TEST_3_STR VARCHAR(255), TEST_3_DATE DATETIME) TYPE=InnoDB",
             "INSERT INTO TEST_3 VALUES(1, 'foo', NOW())",
             "ALTER TABLE TEST_3 ADD INDEX IDX_TEST_3 (TEST_3_INT)",
             "UPDATE TEST_3 SET TEST_3_STR = 'bar' WHERE TEST_3_INT = 1",
             "DROP TABLE TEST_3"};
             
             public ScriptRunnerTest(String testName)
             {
                 super(testName);
             }
             
             protected void setUp() throws java.lang.Exception
             {
                 try
                 {
                     testScript = loadTestScript("test/file/sql/test_script.sql");
                     DBConnector connector = new DBConnector("test/file/test.properties");
                     conn = connector.connect();
                     conn.setAutoCommit(false);
                     runner = new ScriptRunner(testScript, conn);
                 }
                 catch (Exception ex)
                 {
                     DbUtils.closeQuietly(conn);
                     System.out.println("SET UP FAILED");
                     ex.printStackTrace();
                     throw ex;
                 }
             }
             
             protected void tearDown() throws java.lang.Exception
             {
                 conn.commit();
                 DbUtils.closeQuietly(conn);
             }
             
             public static junit.framework.Test suite()
             {
                 junit.framework.TestSuite suite = new junit.framework.TestSuite(ScriptRunnerTest.class);
                 
                 return suite;
             }
             
             public void testExecuteStatements() throws Exception
             {
                 
                 runner.executeStatements(testStatements, conn);
                 
             }
             
             public void testParseScript() throws Exception
             {
                 String[] statements = runner.parseScript(testScript);
                 System.out.println("PARSED SCRIPT");
                 for (int i = 0, length = statements.length; i < length; i++)
                 {
                     System.out.println(statements[i]);
                 }
                 assertNotNull(statements);
                 assertEquals(6, statements.length);
                 
             }
             
             public void testRun() throws Exception
             {
                 runner.run();
             }
             
             
             private String loadTestScript(String scriptPath) throws Exception
             {
                 File file = new File(scriptPath);
                 return FileUtils.readFileToString(file, "UTF8");
             }
             
}
