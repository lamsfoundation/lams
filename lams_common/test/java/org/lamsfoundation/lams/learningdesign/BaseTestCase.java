package org.lamsfoundation.lams.learningdesign;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
	protected ApplicationContext context;
	
	public BaseTestCase(){
		String path ="/com/lamsinternational/lams/learningdesign/learningDesignApplicationContext.xml";
		context = new ClassPathXmlApplicationContext(path);		
	}
}
