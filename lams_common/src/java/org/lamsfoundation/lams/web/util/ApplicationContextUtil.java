package org.lamsfoundation.lams.web.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextUtil {
    private static ApplicationContext parentContext = null;

    public static ApplicationContext getParentContext() {
	if (parentContext == null) {
	    try {
		ApplicationContext commonContext = new ClassPathXmlApplicationContext(
			"/org/lamsfoundation/lams/beanRefContext.xml");
		parentContext = (ApplicationContext) commonContext.getBean("context.central");
		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return parentContext;
    }
}
