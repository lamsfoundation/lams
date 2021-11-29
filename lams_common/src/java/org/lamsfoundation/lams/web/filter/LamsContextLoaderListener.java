package org.lamsfoundation.lams.web.filter;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

/**
 * This class mirrors ContextLoaderListener from Spring 4 with BeanFactoryLocator mechanism enabled.
 *
 * @author Marcin Cieslak
 */
public class LamsContextLoaderListener extends ContextLoaderListener {
    private static ApplicationContext parentContext;

    private static final String PARENT_CONTEXT_XML_LOCATION = "classpath:/org/lamsfoundation/lams/beanRefContext.xml";
    private static final String PARENT_CONTEXT_NAME = "context.central";

    @Override
    protected ApplicationContext loadParentContext(ServletContext servletContext) {
	return LamsContextLoaderListener.loadParentContext();
    }

    private static synchronized ApplicationContext loadParentContext() {
	if (parentContext == null) {
	    parentContext = new ClassPathXmlApplicationContext(PARENT_CONTEXT_XML_LOCATION);
	    parentContext = (ApplicationContext) parentContext.getBean(PARENT_CONTEXT_NAME);
	}
	return parentContext;
    }
}