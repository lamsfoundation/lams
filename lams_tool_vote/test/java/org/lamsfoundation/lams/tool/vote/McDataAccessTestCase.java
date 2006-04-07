/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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

package org.lamsfoundation.lams.tool.vote;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;

/**
 * @author Ozgur Demirtas
 */
public class McDataAccessTestCase extends AbstractLamsTestCase
{
	//These both refer to the same entry in the db.
	protected final Long DEFAULT_CONTENT_ID = new Long(10);
	protected final Long TEST_CONTENT_ID = new Long(2);
	protected final Long TEST_CONTENT_ID_OTHER = new Long(3);
	
	protected final Long TEST_SESSION_ID = new Long(20);
	protected final Long TEST_SESSION_ID_OTHER = new Long(21);
	
	protected final Long TEST_QUE_ID1 = new Long(1);
	protected final Long TEST_QUE_OPTION_ID1 = new Long(1);
	protected final Long TEST_QUE_OPTION_ID2 = new Long(2);
	protected final Long TEST_QUE_OPTION_ID3 = new Long(3);
	
	protected final Long TEST_NEW_USER_ID = new Long(100);
	protected final Long TEST_MY_USER_ID = new Long(77);
		
    protected final long ONE_DAY = 60 * 60 * 1000 * 24;
    
    public final String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    public final String INCOMPLETE = "INCOMPLETE";
    public static String COMPLETED = "COMPLETED";
	
	
	public McDataAccessTestCase(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected String[] getContextConfigLocation()
    {
    	return new String[] {"/org/lamsfoundation/lams/tool/vote/testmcApplicationContext.xml" };
    }
    
    protected String getHibernateSessionFactoryName()
    {
    	return "mcSessionFactory";
    }
    
    protected void tearDown() throws Exception
    {
    	super.tearDown();
    }

}
    
