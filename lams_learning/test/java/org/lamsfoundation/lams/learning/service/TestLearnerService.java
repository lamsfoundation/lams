/*
	Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
    USA

    http://www.gnu.org/licenses/gpl.txt
*/
package org.lamsfoundation.lams.learning.service;

import org.lamsfoundation.lams.AbstractLamsTestCase;

/**
 * 
 * @author Jacky Fang 2005-2-22
 * 
 */
public class TestLearnerService extends AbstractLamsTestCase
{

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestLearnerService.
     * @param name
     */
    public TestLearnerService(String name)
    {
        super(name);
    }
    protected String[] getContextConfigLocation()
    {
        return new String[] { "/WEB-INF/spring/learningApplicationContext.xml",
        					  "/WEB-INF/spring/applicationContext.xml"};
    }
    public void testJoinLesson()
    {
    }

}
