/*
 * Created on Feb 14, 2005
 */
package org.lamsfoundation.lams.contentrepository;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Method of accessing the Repository from outside of the package.
 * Call RepositoryProxy.getRepository to get the IRepository object.
 * 
 * This will create the IRepository object using a Spring bean factory.
 * Note: the repository objects will be ???? different context ?????
 * to any context used by the calling jar.
 */
public class RepositoryProxy {

	private static Logger log = Logger.getLogger(RepositoryProxy.class);
	
	public static IRepository getRepositoryService()  {
    	
		ApplicationContext context = new ClassPathXmlApplicationContext(IRepository.REPOSITORY_CONTEXT_PATH);
		if ( context == null ) 
			throw new RepositoryRuntimeException("Unable to access application context. Cannot create repository object.");
		
		IRepository repository =(IRepository)context.getBean(IRepository.REPOSITORY_SERVICE_ID);
    	return repository;
	}

}
