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

package org.lamsfoundation.lams;

import javax.servlet.http.HttpSession;

import junit.framework.TestCase;


import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.XmlWebApplicationContext;

import servletunit.HttpServletRequestSimulator;
import servletunit.struts.MockStrutsTestCase;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-8
 * @version
 * 
 */
public abstract class AbstractLamsStrutsTestCase extends MockStrutsTestCase
{
    //protected ApplicationContext context;
    private final String CONFIG_LOCATIONS;
    protected HttpServletRequestSimulator httpRequest;
    protected HttpSession httpSession ; 
    
    /**
     * @param arg0
     */
    public AbstractLamsStrutsTestCase(String testName,String location)
    {
        super(testName);
        this.CONFIG_LOCATIONS = location;
        
    }
    
    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        ContextLoader ctxLoader = new ContextLoader();
        context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM,
                                 XmlWebApplicationContext.class.getName());
        context.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
                                 CONFIG_LOCATIONS);
        ctxLoader.initWebApplicationContext(context);
        
        httpRequest = (HttpServletRequestSimulator)getRequest();
        httpSession = getSession();
    }

    /*
     * @see MockStrutsTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
}
