package org.lamsfoundation.lams.util;


import java.util.HashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

/** 
 * Access a message service related to a programatically loaded message file.
 * Authoring uses this to access the message files for tools and activities.
 */ 
public class LoadedMessageSourceService implements ILoadedMessageSourceService, BeanFactoryAware {

	private static final String LOADED_MESSAGE_SOURCE_BEAN = "loadedMessageSource";
	private HashMap<String,MessageSource> messageServices = new HashMap<String,MessageSource>();
	private BeanFactory beanFactory = null;

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.authoring.service.ILoadMessageService#getMessageService(java.lang.String)
	 */
	public MessageSource getMessageService(String messageFilename) {
		if ( messageFilename != null ) {
			MessageSource ms = messageServices.get(messageFilename);
			if ( ms == null ) {
				ResourceBundleMessageSource rbms = (ResourceBundleMessageSource) beanFactory.getBean(LOADED_MESSAGE_SOURCE_BEAN);
				rbms.setBasename(messageFilename);
				messageServices.put(messageFilename,rbms);
				ms = rbms;
			}
			return ms;
		} else {
			return null;
		}
	}
	
	/* **** Method for BeanFactoryAware interface *****************/
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;	
	}

}
