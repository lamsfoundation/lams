/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.test;

import java.util.Map;

import junit.framework.TestCase;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.allaire.wddx.WddxDeserializationException;


/**
 * 
 * @author Jacky Fang 2/02/2005
 * 
 */
public abstract class AbstractLamsTestCase extends TestCase
{
    protected ApplicationContext context;
    
    private boolean shouldFlush = true;
    /**
     * 
     */
    public AbstractLamsTestCase(String name)
    {
        super(name);
    }
    
    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        context = new ClassPathXmlApplicationContext(getContextConfigLocation());
        initializeHibernateSession();
    }
    
    protected abstract String[] getContextConfigLocation();
	
    /**
     * @return
     */
    protected abstract String getHibernateSessionFactoryName();
    
    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        finalizeHibernateSession();
    }
    
	/**
     * @throws HibernateException
     */
    protected void initializeHibernateSession() throws HibernateException
    {
        //hold the hibernate session
		SessionFactory sessionFactory = (SessionFactory) this.context.getBean(getHibernateSessionFactoryName());
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
    }    


    /**
     * @throws HibernateException
     */
    protected void finalizeHibernateSession() throws HibernateException
    {
        //clean the hibernate session
		SessionFactory sessionFactory = (SessionFactory)this.context.getBean(getHibernateSessionFactoryName());
	    SessionHolder holder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
	    if (holder != null&&shouldFlush) {
	    	Session s = holder.getSession(); 
		    s.flush();
		    TransactionSynchronizationManager.unbindResource(sessionFactory);
		    SessionFactoryUtils.closeSessionIfNecessary(s, sessionFactory);
	    }
    }

    protected Session getSession()
    {
		SessionFactory sessionFactory = (SessionFactory)this.context.getBean(getHibernateSessionFactoryName());
	    SessionHolder holder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
	    if (holder != null) 
	    	return holder.getSession(); 
	    else
	        return null;
    }
    /**
     * @param shouldFlush The shouldFlush to set.
     */
    public void setShouldFlush(boolean shouldFlush)
    {
        this.shouldFlush = shouldFlush;
    }
    
	/**
	 * Given a WDDX packet in our normal format, return the map object in the 
	 * messageValue parameter. This should contain any returned ids.
	 * 
     * @param wddxPacket
     * @return Map
     */
    public Map extractIdMapFromWDDXPacket(String wddxPacket) {
    	
    	Object obj = null;
    	try {
			obj = WDDXProcessor.deserialize(wddxPacket);
		} catch (WddxDeserializationException e1) {
			fail("WddxDeserializationException "+e1.getMessage());
			e1.printStackTrace();
		}
		
		Map map = (Map) obj;
		Object messageValueObj = map.get("messageValue");
		assertNotNull("messageValue object found", messageValueObj);
		if ( ! Map.class.isInstance( messageValueObj ) ) {
			fail("messageValue is not a Map - try extractIdFromWDDXPacket(packet)");
		}
		
		return (Map) messageValueObj;
	}

	/**
	 * Given a WDDX packet in our normal format, gets the id number from within 
	 * the &lt;var name='messageValue'&gt;&lt;number&gt;num&lt;/number&gt;&lt;/var&gt;
     * @param wddxPacket
     * @return id
     */
    public Long extractIdFromWDDXPacket(String wddxPacket) {
        int indexMessageValue = wddxPacket.indexOf("<var name='messageValue'><number>");
		assertTrue("<var name='messageValue'><number> string found", indexMessageValue > 0);
		int endIndexMessageValue = wddxPacket.indexOf(".0</number></var>",indexMessageValue);
		String idString = wddxPacket.substring(indexMessageValue+33, endIndexMessageValue);
		try {
		    long id = Long.parseLong(idString);
		    return new Long(id);
		} catch (NumberFormatException e) {
		    fail("Unable to get id number from WDDX packet. Format exception. String was "+idString);
		}
		return null;
    }	

}
