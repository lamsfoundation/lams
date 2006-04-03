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

import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;


/*
 * 
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class TestMcOptionsContent extends McDataAccessTestCase
{
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestMcOptionsContent(String name)
    {
        super(name);
    }
    
    public void testRetrieveMcOptionsContent()
    {
    	List list=mcOptionsContentDAO.findMcOptionsContentByQueId(new Long(1));
    	System.out.print("list:" + list);
    	
    	Iterator listIterator=list.iterator();
    	while (listIterator.hasNext())
    	{
    		McOptsContent mcOptsContent=(McOptsContent)listIterator.next();
    		System.out.println("option text:" + mcOptsContent.getMcQueOptionText());
    	}
    }
}



