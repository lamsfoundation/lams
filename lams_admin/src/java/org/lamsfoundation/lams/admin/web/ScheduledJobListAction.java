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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.admin.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * @author Steve.Ni
 * @version $Revision$
 * 
 * @struts:action path="/jobList"
 * 				input=".joblist"
 * 
 * @struts:action-forward name="list" path=".joblist"
 */
public class ScheduledJobListAction extends Action{

	private static final Logger log = Logger.getLogger(ScheduledJobListAction.class);
	/**
     * Get all waitting queue jobs scheduled in Quartz table and display job name, job start time and 
     * description. The description will be in format "Lesson Name":"the lesson creator", or 
     * "The gate name":"The relatived lesson name".
     * 
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     * 
     */
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
    	
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServlet().getServletContext());
		Scheduler scheduler = (Scheduler) ctx.getBean("scheduler");
		ArrayList<ScheduledJobDTO> jobList = new ArrayList<ScheduledJobDTO>();
		try {
			String[] jobNames = scheduler.getJobNames(Scheduler.DEFAULT_GROUP);
			for (String name : jobNames) {
				ScheduledJobDTO jobDto = new ScheduledJobDTO();
				JobDetail detail = scheduler.getJobDetail(name,Scheduler.DEFAULT_GROUP);
				jobDto.setName(name);
				jobDto.setDescription(detail.getDescription());
				Trigger[] triggers = scheduler.getTriggersOfJob(name,Scheduler.DEFAULT_GROUP);
				for (Trigger trigger : triggers) {
					jobDto.setStartDate(trigger.getStartTime());
					jobList.add(jobDto);
				}
			}
		} catch (SchedulerException e) {
			log.equals("Failed get job names:" + e.getMessage());
		}
		
		request.setAttribute("jobList",jobList);
    	return mapping.findForward("list");
    }
            
}
