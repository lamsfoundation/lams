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



/*
 * 
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class TestMcQueContent extends McDataAccessTestCase
{
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestMcQueContent(String name)
    {
        super(name);
    }
    
    /*
    public void testCreateMcQueContent()
    {
    	McContent mcContent = mcContentDAO.findMcContentById(new Long(2));
    	assertEquals("Check ids:", mcContent.getMcContentId() , new Long(2));
    	
    	
    	 McQueContent mcQueContent=  new McQueContent("What planet are you from?",
													 new Integer(444),
													 mcContent,
													 new HashSet(),
													 new HashSet()
    												);
    	
    	 mcQueContentDAO.saveOrUpdateMcQueContent(mcQueContent);
    	 
    	 McQueContent mcQueContent2=  new McQueContent("What is a good question?",
				 new Integer(555),
				 mcContent,
				 new HashSet(),
				 new HashSet()
				);

    	 mcQueContentDAO.saveOrUpdateMcQueContent(mcQueContent2);
    	 mcQueContentDAO.flush();
    }


    public void testSaveOrUpdateMcQueContent()
    {
    	McContent mcContent2 = mcContentDAO.findMcContentById(new Long(2));
    	
    	McQueContent mcQueContent2=  new McQueContent(
    												 "Where is the sky?",
    												 new Integer(777),
    												 mcContent2,
    												 new HashSet(),
    												 new HashSet()
    												);
    	
    	mcQueContentDAO.saveOrUpdateMcQueContent(mcQueContent2);
        	
    }
    
    
	
    public void testRetrieveMcQueContent()
    {
    	McQueContent mcQueContent= mcQueContentDAO.getMcQueContentByUID(new Long(1));
    	mcQueContent.setDisplayOrder(new Integer(88));
    	mcQueContentDAO.saveMcQueContent(mcQueContent);
    }
    */
    
    /*
    public void testGetToolDefaultQuestionContent()
    {
    	McQueContent mcQueContent = mcQueContentDAO.getToolDefaultQuestionContent(new Long(1).longValue());
    	System.out.print("mcQueContent:" + mcQueContent);
    }
    */
    
    public void testRemoveQuestionContentByMcUid()
    {
    	mcQueContentDAO.removeQuestionContentByMcUid(new Long(2));
    	
    }
    
    
    /*
    public void testCreateSampleQuestionContent()
    {
    	McContent mcContent = mcContentDAO.findMcContentById(DEFAULT_CONTENT_ID);
    	System.out.print("mcContent:" + mcContent);
    	
    	McQueContent mcQueContent=  new McQueContent("A sample question",
    												 new Integer(1),
    												 mcContent,
    												 new HashSet(),
    												 new HashSet()
    												);
    	mcQueContentDAO.saveOrUpdateMcQueContent(mcQueContent);
    }
    */
    
    /*
    public void testGetQuestionContentByQuestionText()
    {
    	McQueContent mcQueContent = mcQueContentDAO.getQuestionContentByQuestionText("A sample question");
    	System.out.print("mcQueContent:" + mcQueContent);
    }
    */

}