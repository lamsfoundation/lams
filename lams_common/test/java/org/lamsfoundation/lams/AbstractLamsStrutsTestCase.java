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


import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import servletunit.HttpServletRequestSimulator;
import servletunit.struts.MockStrutsTestCase;


/**
 * The abstract test case that initialize the Spring context using context
 * loader. It also use a session holder to simulate the 
 * <code>OpenSessionInViewFilter</code>. Without this session holder, we can
 * not run integration testing with lazy loading hibernate object configured.
 * 
 * @author Jacky Fang
 * @since  2005-3-8
 * @version 1.0
 * 
 */
public abstract class AbstractLamsStrutsTestCase extends MockStrutsTestCase
{
    //protected ApplicationContext context;
    private final String CONFIG_LOCATIONS;
    protected HttpServletRequestSimulator httpRequest;
    protected HttpSession httpSession ; 
    protected WebApplicationContext wac;
    
    /**
     * @param arg0
     */
    public AbstractLamsStrutsTestCase(String testName,String location)
    {
        super(testName);
        this.CONFIG_LOCATIONS = location;
        
    }
    
    /**
     * Set up spring context and hibernate session holder into mock servlet
     * context.
     * 
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
        
        wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        initializeHibernateSession();
        httpRequest = (HttpServletRequestSimulator)getRequest();
        httpSession = getSession();
    }

    /**
     * @see MockStrutsTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
	/**
     * @throws HibernateException
     */
    protected void initializeHibernateSession() throws HibernateException
    {
        //hold the hibernate session
		SessionFactory sessionFactory = (SessionFactory) this.wac.getBean("coreSessionFactory");
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
    }   
    
    /**
     * @throws HibernateException
     */
    protected void finalizeHibernateSession() throws HibernateException
    {
        //clean the hibernate session
		SessionFactory sessionFactory = (SessionFactory)this.wac           .getBean("coreSessionFactory");
	    SessionHolder holder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
	    if (holder != null) {
	    	Session s = holder.getSession(); 
		    s.flush();
		    TransactionSynchronizationManager.unbindResource(sessionFactory);
		    SessionFactoryUtils.closeSessionIfNecessary(s, sessionFactory);
	    }
    }
}
