/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $Id$ */

package org.lamsfoundation.lams.tool.deploy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.w3c.dom.Document;


public class TestUpdateWebXmlTask extends TestCase {

   public static final String dummyWarNoNBEntryPath = "test/file/test-dummy.war";
   public static final String dummyWarNBEntryPath = "test/file/test-dummy-with-nb.war";

   public TestUpdateWebXmlTask(String testName)
    {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception
    {
        super.setUp();
    }

    protected void tearDown() throws java.lang.Exception
    {
        super.tearDown();
    }

    public void testInsertExecute() throws Exception
    {
		InsertContextWebXmlTask task = new InsertContextWebXmlTask();
    	webXmlFileTest(task, "test/file/web.xml");
	    
    }

    public void testInsertEntryAlreadyExistsExecute() throws Exception
    {
		InsertContextWebXmlTask task = new InsertContextWebXmlTask();
    	webXmlFileTest(task, "test/file/web1EntryInsert.xml");
    }

    public void testRemoveExecuteNoEntry() throws Exception
    {
    	// tests removing it from the end of the list of contexts
    	RemoveContextWebXmlTask task = new RemoveContextWebXmlTask();
    	webXmlFileTest(task, "test/file/webNoEntry.xml");
    } 

    public void testRemoveExecuteEnd() throws Exception
    {
    	// tests removing it from the end of the list of contexts
    	RemoveContextWebXmlTask task = new RemoveContextWebXmlTask();
    	webXmlFileTest(task, "test/file/web1Entry.xml");
    } 

    public void testRemoveExecuteBeginMidEnd() throws Exception
    {
    	// tests removing it from the beginning and middle of the list of contexts
    	RemoveContextWebXmlTask task = new RemoveContextWebXmlTask();
    	webXmlFileTest(task, "test/file/web3Entry.xml");
    } 

	/**
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void webXmlFileTest(UpdateWebXmlTask task, String filename) throws FileNotFoundException, IOException {
		
	   	task.setApplicationContextPath("/org/lamsfoundation/lams/tool/noticeboard/applicationContext.xml");
	   	
	    InputStream is = new FileInputStream(filename);
		Document doc = task.parseWebXml(is);
		is.close();
		task.updateWebXml(doc);
	    OutputStream os = new FileOutputStream(filename+".new");
	    task.writeWebXml(doc, os);
	    os.close();
	}

	public void testAddToWar() throws Exception
    {
    	InsertContextWebXmlTask task = new InsertContextWebXmlTask();
    	task.setApplicationContextPath("/org/lamsfoundation/lams/tool/noticeboard/applicationContext.xml");
    	List<String> warFiles = new LinkedList<String>();
    	warFiles.add(dummyWarNoNBEntryPath);
    	task.setWarsToUpdate(warFiles);
    	task.setLamsEarPath("");
        task.execute();
    }

    public void testRemoveFromToWar() throws Exception
    {
    	RemoveContextWebXmlTask task = new RemoveContextWebXmlTask();
    	task.setApplicationContextPath("/org/lamsfoundation/lams/tool/noticeboard/applicationContext.xml");
    	List<String> warFiles = new LinkedList<String>();
    	warFiles.add(dummyWarNBEntryPath);
    	task.setWarsToUpdate(warFiles);
    	task.setLamsEarPath("");
        task.execute();
    }
}