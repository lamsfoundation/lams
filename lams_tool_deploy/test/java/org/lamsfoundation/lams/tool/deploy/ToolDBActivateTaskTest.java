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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author chris
 */
public class ToolDBActivateTaskTest extends ToolDBTest
{
    
    public ToolDBActivateTaskTest(String testName)
    {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception
    {
        super.setUp();
        insertTestRecords("test/file/test.properties");
    }

    protected void tearDown() throws java.lang.Exception
    {
        super.tearDown();
    }
    
    public void testExecute() throws Exception
    {
        ToolDBActivateTask task = new ToolDBActivateTask();
        task.setDbUsername(props.getString("dbUsername"));
        task.setDbPassword(props.getString("dbPassword"));
        task.setDbDriverClass(props.getString("dbDriverClass"));
        task.setDbDriverUrl(props.getString("dbDriverUrl"));
        task.setToolId(1);
        task.setLearningLibraryId(1);
        task.execute();
    }

    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(ToolDBActivateTaskTest.class);
        
        return suite;
    }
    
    
    
}
