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

package org.lamsfoundation.lams.learning.web.action;

import org.lamsfoundation.lams.test.AbstractLamsStrutsTestCase;

public class AbstractTestAction extends AbstractLamsStrutsTestCase {

	public AbstractTestAction(String testName) {
		super(testName);
	}

	/*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /**
     * @see org.lamsfoundation.lams.AbstractLamsStrutsTestCase#getContextConfigLocation()
     */
    protected String getContextConfigLocation()
    {
        return "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml "
  			   +"/org/lamsfoundation/lams/tool/toolApplicationContext.xml "
  			   +"/org/lamsfoundation/lams/localApplicationContext.xml ";
    }
    /**
     * @see org.lamsfoundation.lams.AbstractLamsStrutsTestCase#getHibernateSessionFactoryBeanName()
     */
    protected String getHibernateSessionFactoryBeanName()
    {
        return "coreSessionFactory";
    }


}