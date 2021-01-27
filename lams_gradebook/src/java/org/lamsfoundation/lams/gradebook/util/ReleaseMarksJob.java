package org.lamsfoundation.lams.gradebook.util;

import java.util.Map;

import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.gradebook.service.IGradebookFullService;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ReleaseMarksJob extends QuartzJobBean {
    private static final String CONTEXT_NAME = "context.central";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	IGradebookFullService gradebookService = (IGradebookFullService) getService(context, "gradebookService");

	Map<String, Object> properties = context.getJobDetail().getJobDataMap();
	long lessonId = (Long) properties.get(AttributeNames.PARAM_LESSON_ID);
	int userId = (Integer) properties.get(AttributeNames.PARAM_USER_ID);
	boolean sendEmails = (Boolean) properties.get("sendEmails");

	boolean marksReleased = gradebookService.releaseMarks(lessonId, userId);
	if (sendEmails && marksReleased) {
	    gradebookService.sendReleaseMarksEmails(lessonId, null,
		    (IEventNotificationService) getService(context, "eventNotificationService"));
	}
    }

    private Object getService(JobExecutionContext context, String serviceName) throws JobExecutionException {
	try {
	    SchedulerContext sc = context.getScheduler().getContext();
	    ApplicationContext cxt = (ApplicationContext) sc.get(CONTEXT_NAME);
	    return cxt.getBean(serviceName);
	} catch (SchedulerException e) {
	    throw new JobExecutionException("Failed look up the " + serviceName + " " + e.toString());
	}
    }
}