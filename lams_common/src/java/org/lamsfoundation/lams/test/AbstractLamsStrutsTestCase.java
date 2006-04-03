/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
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
package org.lamsfoundation.lams.test;

import javax.servlet.http.HttpSession;

import junit.framework.TestCase;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
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
    //private String CONFIG_LOCATIONS;
    protected HttpServletRequestSimulator httpRequest;
    protected HttpSession httpSession ; 
    protected WebApplicationContext wac;
    
    /**
     * @param arg0
     */
    public AbstractLamsStrutsTestCase(String testName)
    {
        super(testName);
        //this.CONFIG_LOCATIONS = location;
        
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
                                 getContextConfigLocation());
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
		SessionFactory sessionFactory = (SessionFactory) this.wac.getBean(getHibernateSessionFactoryBeanName());
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
    }   
    
    /**
     * @throws HibernateException
     */
    protected void finalizeHibernateSession() throws HibernateException
    {
        //clean the hibernate session
		SessionFactory sessionFactory = (SessionFactory)this.wac.getBean(getHibernateSessionFactoryBeanName());
	    SessionHolder holder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
	    if (holder != null) {
	    	Session s = holder.getSession(); 
		    s.flush();
		    TransactionSynchronizationManager.unbindResource(sessionFactory);
		    SessionFactoryUtils.releaseSession(s, sessionFactory);
	    }
    }
    
    /**
     * @return
     */
    protected abstract String getHibernateSessionFactoryBeanName();
    
    protected abstract String getContextConfigLocation();
}
