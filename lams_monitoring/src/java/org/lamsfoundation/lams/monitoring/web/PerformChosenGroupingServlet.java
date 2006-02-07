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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
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
		FlashMessage flashMessage;
		String message;
		try {
			Hashtable table = (Hashtable)WDDXProcessor.deserialize(chosenGroupingPacket);
			//get back value from WDDX package
			long activityId = WDDXProcessor.convertToLong(MonitoringConstants.KEY_GROUPING_ACTIVITY,
					table.get(MonitoringConstants.KEY_GROUPING_ACTIVITY)).longValue();
			List groups = (List) table.get(MonitoringConstants.KEY_GROUPS);
			
			IMonitoringService monitoringService = getMonitoringService();
			//get the activity according to the given activity ID
			Activity act = monitoringService.getActivityById(activityId);
			if(!act.isGroupingActivity()){
				 throw new Exception("The given activity ["+activityId+"] is not grouping type.");
			}
			GroupingActivity activity = (GroupingActivity) act;
			//perform grouping
			monitoringService.performChosenGrouping(activity,groups);
			
			//construct return WDDX message
			Grouping grouping = activity.getCreateGrouping();
			Map map = new HashMap();
			map.put(MonitoringConstants.KEY_GROUPING_ACTIVITY,new Long(activityId));
			map.put("grouping",grouping.getGroupingDTO());
			flashMessage = new FlashMessage("performChosenGrouping",map);
		} catch (Exception e) {
			log.error(e);
			flashMessage = new FlashMessage("performChosenGrouping","Perfrom chosen grouping occurs error:" 
					+ e.getMessage(),FlashMessage.ERROR);
		}
		
		try{
			message = flashMessage.serializeMessage();
		}catch(IOException e){
			 message = "Failed on creating flash message:" + flashMessage;
		}
		
        return message;
		
	}

	protected String getMessageKey(String packet, HttpServletRequest request) {
		return MonitoringConstants.PERFORM_CHOSEN_GROUPING_KEY;
	}

}
