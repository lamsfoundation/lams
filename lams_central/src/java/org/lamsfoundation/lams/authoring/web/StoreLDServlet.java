/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
/* $$Id$$ */ 
package org.lamsfoundation.lams.authoring.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Store a learning design.
 * 
 * @author Fiona Malikoff
 *
 * @web:servlet name="storeLD"
 * @web:servlet-mapping url-pattern="/authoring/storeLearningDesignDetails"
 */
public class StoreLDServlet extends AbstractStoreWDDXPacketServlet {

	private static Logger log = Logger.getLogger(AuthoringAction.class);

	public IAuthoringService getAuthoringService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		return (IAuthoringService) webContext.getBean(AuthoringConstants.AUTHORING_SERVICE_BEAN_NAME);		
	}

	protected String process(String designDetails, HttpServletRequest request) 
		throws Exception
		{
				
			IAuthoringService authoringService = getAuthoringService();
			return authoringService.storeLearningDesignDetails(designDetails);
		}
	
	protected String getMessageKey(String designDetails, HttpServletRequest request) {
		return IAuthoringService.STORE_LD_MESSAGE_KEY;
	}

}
