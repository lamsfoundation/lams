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

package org.lamsfoundation.lams.admin.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.dto.ScheduledJobDTO;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Steve.Ni
 */
@Controller
public class ScheduledJobListController {
    private static final Logger log = Logger.getLogger(ScheduledJobListController.class);
    
    @Autowired
    private Scheduler scheduler;

    /**
     * Get all waitting queue jobs scheduled in Quartz table and display job name, job start time and description. The
     * description will be in format "Lesson Name":"the lesson creator", or "The gate name":"The relatived lesson name".
     */
    @RequestMapping(path = "/joblist", method = RequestMethod.POST)
    public String execute(HttpServletRequest request) throws Exception {
	ArrayList<ScheduledJobDTO> jobList = new ArrayList<>();
	try {
	    Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Scheduler.DEFAULT_GROUP));
	    for (JobKey jobKey : jobKeys) {
		ScheduledJobDTO jobDto = new ScheduledJobDTO();
		JobDetail detail = scheduler.getJobDetail(jobKey);
		jobDto.setName(jobKey.getName());
		jobDto.setDescription(detail.getDescription());
		List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
		for (Trigger trigger : triggers) {
		    jobDto.setStartDate(trigger.getStartTime());
		    jobList.add(jobDto);
		}
	    }
	} catch (SchedulerException e) {
	    ScheduledJobListController.log.equals("Failed get job names:" + e.getMessage());
	}

	request.setAttribute("jobList", jobList);
	return "joblist";
    }

}
