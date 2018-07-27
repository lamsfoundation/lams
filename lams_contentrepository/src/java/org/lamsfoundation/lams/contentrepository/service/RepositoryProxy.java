/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.contentrepository.service;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryRuntimeException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Method of accessing the Repository from outside of the package.
 * Call RepositoryProxy.getRepository to get the IRepository object.
 *
 * These method should only be used when the calling code is not
 * using Spring. It will create a standalone context.
 *
 * If the calling class is using Spring, then it should ensure that
 * /org/lamsfoundation/lams/contentrepository/applicationContext.xml
 * is loaded into the context and the repository service can be accessed
 * using something like:
 * 
 * <pre>
 * WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
 * IRepositoryService service = wac.getBean(IRepositoryService.REPOSITORY_SERVICE_ID);
 * </pre>
 */
public class RepositoryProxy {

    private static Logger log = Logger.getLogger(RepositoryProxy.class);

    public static IRepositoryService getRepositoryService() {

	String[] contextPaths = IRepositoryService.REPOSITORY_CONTEXT_PATH;

	ApplicationContext context = new ClassPathXmlApplicationContext(contextPaths);
	if (context == null) {
	    throw new RepositoryRuntimeException(
		    "Unable to access application context. Cannot create repository object.");
	}

	IRepositoryService repository = (IRepositoryService) context.getBean(IRepositoryService.REPOSITORY_SERVICE_ID);
	return repository;
    }

    /**
     * Get a version of the repository service suitable for using in JUNIT tests.
     * It has been placed here (as well as similar logic existing in the BaseTestCase)
     * so that other projects can access the appropriate Spring configuration.
     * This configuration uses a local datasource and does not use the cache.
     * 
     * @return Repository service built using a local datasource
     */
    public static IRepositoryService getLocalRepositoryService() {

	String[] contextPaths = IRepositoryService.REPOSITORY_LOCAL_CONTEXT_PATH;

	ApplicationContext context = new ClassPathXmlApplicationContext(contextPaths);
	if (context == null) {
	    throw new RepositoryRuntimeException(
		    "Unable to access application context. Cannot create repository object.");
	}

	IRepositoryService repository = (IRepositoryService) context.getBean(IRepositoryService.REPOSITORY_SERVICE_ID);
	return repository;
    }

}
