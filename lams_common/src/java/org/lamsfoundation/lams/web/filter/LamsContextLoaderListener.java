package org.lamsfoundation.lams.web.filter;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lamsfoundation.lams.context.BeanFactoryLocator;
import org.lamsfoundation.lams.context.BeanFactoryReference;
import org.lamsfoundation.lams.context.ContextSingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

/**
 * This class mirrors ContextLoaderListener from Spring 4 with BeanFactoryLocator mechanism enabled.
 *
 * @author Marcin Cieslak
 */
public class LamsContextLoaderListener extends ContextLoaderListener {
    public static final String LOCATOR_FACTORY_SELECTOR_PARAM = "locatorFactorySelector";
    public static final String LOCATOR_FACTORY_KEY_PARAM = "parentContextKey";
    private BeanFactoryReference parentContextRef;

    @Override
    protected ApplicationContext loadParentContext(ServletContext servletContext) {
	ApplicationContext parentContext = null;
	String locatorFactorySelector = servletContext.getInitParameter(LOCATOR_FACTORY_SELECTOR_PARAM);
	String parentContextKey = servletContext.getInitParameter(LOCATOR_FACTORY_KEY_PARAM);

	if (parentContextKey != null) {
	    // locatorFactorySelector may be null, indicating the default "classpath*:beanRefContext.xml"
	    BeanFactoryLocator locator = ContextSingletonBeanFactoryLocator.getInstance(locatorFactorySelector);
	    Log logger = LogFactory.getLog(ContextLoader.class);
	    if (logger.isDebugEnabled()) {
		logger.debug("Getting parent context definition: using parent context key of '" + parentContextKey
			+ "' with BeanFactoryLocator");
	    }
	    this.parentContextRef = locator.useBeanFactory(parentContextKey);
	    parentContext = (ApplicationContext) this.parentContextRef.getFactory();
	}

	return parentContext;
    }

}