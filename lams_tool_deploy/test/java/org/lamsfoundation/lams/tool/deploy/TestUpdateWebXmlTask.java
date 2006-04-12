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


public class TestUpdateWebXmlTask extends ToolDBTest {

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

    public void testExecute() throws Exception
    {
    	UpdateWebXmlTask task = new UpdateWebXmlTask("/org/lamsfoundation/lams/tool/noticeboard/applicationContext.xml",null,"test/file/web.xml",null,null);
        task.execute();
    }

}
