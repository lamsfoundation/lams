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
package org.lamsfoundation.lams.tool.qa;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;


/**
 * @author Jacky Fang
 *
 */
public abstract class AbstractQaTestCase extends TestCase
{
    protected ApplicationContext context;
    protected final long TEST_LESSON_ID=1;
    /**
     * @param name
     */
    public AbstractQaTestCase(String name)
    {
        super(name);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        System.out.println("AbstractQaTestCase: done with abstract super setup");
        context = new ClassPathXmlApplicationContext(getContextConfigLocation());
        System.out.println("AbstractQaTestCase: context" + context);
    }

	protected abstract String[] getContextConfigLocation();
    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
