package org.lamsfoundation.lams.events;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Quartz job for resending messages. This job is scheduled from the start-up and periodically attempts to resend failed
 * and marked for resending messages.
 *
 * @author Marcin Cieslak
 *
 */
public class ResendMessagesJob extends QuartzJobBean {
    @Autowired
    private IEventNotificationService eventNotificationService;

    public ResendMessagesJob() {
	//First recover context of the job since it is created by Quartz, not Spring
	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	eventNotificationService.resendMessages();
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }
}