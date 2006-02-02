/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 * @web:servlet name="performChosenGrouping"
 * @web:servlet-mapping url-pattern="/monitoring/performChosenGrouping"
 */
public class PerformChosenGroupingServlet extends AbstractStoreWDDXPacketServlet {
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(PerformChosenGroupingServlet.class);
	private static final long serialVersionUID = -3423985351915607659L;
	
	public IMonitoringService getMonitoringService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		return (IMonitoringService) webContext.getBean(MonitoringConstants.MONITORING_SERVICE_BEAN_NAME);		
	}
	
	protected String process(String chosenGroupingPacket, HttpServletRequest request) throws Exception {
		FlashMessage flashMessage = null;
		try {
			Hashtable table = (Hashtable)WDDXProcessor.deserialize(chosenGroupingPacket);
			//get back value from WDDX package
			long activityId = WDDXProcessor.convertToLong(MonitoringConstants.KEY_GROUPING_ACTIVITY,
					table.get(MonitoringConstants.KEY_GROUPING_ACTIVITY)).longValue();
			List groups = (List) table.get(MonitoringConstants.KEY_GROUPS);
			
			IMonitoringService monitoringService = getMonitoringService();
			//get the activity according to the given activity ID
			GroupingActivity activity = (GroupingActivity) monitoringService.getActivityById(activityId);
			//perform grouping
			monitoringService.performChosenGrouping(activity,groups);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String message = "Failed on creating flash message:" + flashMessage;
		try {
			message = flashMessage.serializeMessage();
		} catch (IOException e) {
			log.error(message);
		}
		
        return message;
		
	}

	protected String getMessageKey(String packet, HttpServletRequest request) {
		return MonitoringConstants.PERFORM_CHOSEN_GROUPING_KEY;
	}

}
