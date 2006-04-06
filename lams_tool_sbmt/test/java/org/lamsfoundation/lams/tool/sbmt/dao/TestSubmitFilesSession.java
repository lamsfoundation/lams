/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.tool.sbmt.SbmtBaseTestCase;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.hibernate.SubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.hibernate.SubmitFilesSessionDAO;

/**
 * @author Manpreet Minhas
 */
public class TestSubmitFilesSession extends SbmtBaseTestCase {
	
	protected SubmitFilesSession submitFilesSession;
	protected SubmitFilesContent submitFilesContent;
	
	protected ISubmitFilesContentDAO submitFilesContentDAO;
	protected ISubmitFilesSessionDAO submitFilesSessionDAO;
	
	public TestSubmitFilesSession(String name){
		super(name);
	}

	public void setUp() throws Exception{
		super.setUp();
		submitFilesSessionDAO = (SubmitFilesSessionDAO)context.getBean("submitFilesSessionDAO");
		submitFilesContentDAO = (SubmitFilesContentDAO)context.getBean("submitFilesContentDAO");
	}
	public void testAddSubmitFilesSession(){
		submitFilesContent = submitFilesContentDAO.getContentByID(new Long(1));
		submitFilesSession = new SubmitFilesSession(new Long(1),SubmitFilesSession.INCOMPLETE);
		submitFilesSessionDAO.insert(submitFilesSession);
		assertNotNull(submitFilesSession.getSessionID());
	}
	public void testGetSessionByID(){
		submitFilesSession = submitFilesSessionDAO.getSessionByID(new Long(1));
		assertEquals(submitFilesSession.getStatus(), new Integer(1));
	}

}
