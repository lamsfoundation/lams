package com.lamsinternational.lams;

import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.lamsinternational.lams.lesson.dao.hibernate.LessonDAO;

import junit.framework.TestCase;
/*
 * Created on Dec 4, 2004
 */

/**
 * @author manpreet
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseTestCase extends TestCase {
	
	private static String CONFIG_PATH = "/WEB-INF/spring/";
	
	protected ApplicationContext context;
	
	public BaseTestCase() {
		String configLocations[] = new String[3];
		configLocations[0] = CONFIG_PATH+"applicationContext.xml";
		configLocations[1] = CONFIG_PATH+"dataAccessContext.xml";
		configLocations[2] = CONFIG_PATH+"learningApplicationContext.xml";
		context = new ClassPathXmlApplicationContext(configLocations);		
	}
	
	protected void setUp() throws Exception{
		SessionFactory sessionFactory = (SessionFactory)getBean("coreSessionFactory");
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
	}
	
    protected Object getBean(String beanName) {
    	if (context == null) context = new FileSystemXmlApplicationContext(CONFIG_PATH+"applicationContext.xml");
		Object o = context.getBean(beanName);
    	return o;
    }
	
	protected void tearDown() throws Exception{
	    super.tearDown();
		SessionFactory sessionFactory = (SessionFactory)getBean("coreSessionFactory");
	    SessionHolder holder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
	    if (holder != null) {
	    	Session s = holder.getSession(); 
		    s.flush();
		    TransactionSynchronizationManager.unbindResource(sessionFactory);
		    SessionFactoryUtils.closeSessionIfNecessary(s, sessionFactory);
	    }
	}
}
