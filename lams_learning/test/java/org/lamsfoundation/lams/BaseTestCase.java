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

package org.lamsfoundation.lams;

import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import junit.framework.TestCase;

/**
 * @author daveg
 *
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
