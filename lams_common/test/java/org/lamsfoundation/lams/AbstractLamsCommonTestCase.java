/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams;

import junit.framework.TestCase;

import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * 
 * @author Jacky Fang 2/02/2005
 * 
 */
public abstract class AbstractLamsCommonTestCase extends TestCase
{
    protected ApplicationContext ac;
    
    /**
     * 
     */
    public AbstractLamsCommonTestCase(String name)
    {
        super(name);
    }
    
    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        ac = new ClassPathXmlApplicationContext(getContextConfigLocation());
        //hold the hibernate session
		SessionFactory sessionFactory = (SessionFactory) this.ac.getBean("coreSessionFactory");
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));

    }
    
	protected abstract String[] getContextConfigLocation();
	
    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        //clean the hibernate session
		SessionFactory sessionFactory = (SessionFactory)this.ac.getBean("coreSessionFactory");
	    SessionHolder holder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
	    if (holder != null) {
	    	Session s = holder.getSession(); 
		    s.flush();
		    TransactionSynchronizationManager.unbindResource(sessionFactory);
		    SessionFactoryUtils.closeSessionIfNecessary(s, sessionFactory);
	    }
    }
}
