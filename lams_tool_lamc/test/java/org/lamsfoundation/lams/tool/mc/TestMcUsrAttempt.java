/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

import java.util.Date;

import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.service.ILamsToolService;



/*
 * 
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class TestMcUsrAttempt extends McDataAccessTestCase
{
	public org.lamsfoundation.lams.tool.dao.IToolDAO toolDAO;
	public ILamsToolService lamsToolService;
	
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestMcUsrAttempt(String name)
    {
        super(name);
    }
    
  
    public void testCreateNewMcUsrAttempt()
    {    
    	McQueContent mcQueContent = mcQueContentDAO.getMcQueContentByUID(new Long(1));
    	
    	McOptsContent mcOptionsContent=mcOptionsContentDAO.getMcOptionsContentByUID(new Long(1));
    	
    	McQueUsr mcQueUsr=mcUserDAO.findMcUserById(TEST_MY_USER_ID); 
	    
    	
	    McUsrAttempt mcUsrAttempt= new McUsrAttempt(new Long(33),
	    											new Date(System.currentTimeMillis()),
													"Sydney",
													mcQueContent,	
													mcQueUsr,
													mcOptionsContent					
	    											);

	    McUsrAttempt mcUsrAttempt2= new McUsrAttempt(new Long(34),
				new Date(System.currentTimeMillis()),
				"ACT",
				mcQueContent,	
				mcQueUsr,
				mcOptionsContent					
				);
	    
	    mcUsrAttemptDAO.saveMcUsrAttempt(mcUsrAttempt);
	    mcUsrAttemptDAO.saveMcUsrAttempt(mcUsrAttempt2);
    }
    
}