/*
 * ScriptRunnerTest.java
 * JUnit based test
 *
 * Created on 06 April 2005, 13:36
 */

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
                     testScript = loadTestScript("test/file/sql/test-script.sql");
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
                 assertEquals(statements.length, 5);
                 
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
