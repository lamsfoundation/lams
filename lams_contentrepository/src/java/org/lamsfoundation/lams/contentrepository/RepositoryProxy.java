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
