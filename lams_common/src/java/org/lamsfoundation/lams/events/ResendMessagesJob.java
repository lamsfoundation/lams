package org.lamsfoundation.lams.events;

import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Quartz job for resending messages. This job is scheduled from the start-up and periodically attempts to resend failed
 * and marked for resending messages.
 * 
 * @author Administrator
 * 
 */
public class ResendMessagesJob extends QuartzJobBean {
    protected static final Logger log = Logger.getLogger(ResendMessagesJob.class);
    
    /**
	 * Period after which the thread gives up on attempting to resend messages.
	 * Currently - 2 days.
	 */
	private static final long RESEND_TIME_LIMIT = 2 * 24 * 60 * 60 * 1000;

	@Autowired
	private IEventNotificationService eventNotificationService;

	@Autowired
	private MessageService commonMessageService;
	
	@Autowired
	private IUserManagementService userManagementService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    	//First recover context of the job since it is created by Quartz, not Spring
    	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    	resendMessages();
    }

	private void resendMessages() throws JobExecutionException {
		try {
		    EventNotificationService.log.debug("Event notification service is running a resend messages thread...");
		    List<Event> events = eventNotificationService.getEventsToResend();
		    for (Event event : events) {
			event.referenceCounter++;
			if (event.getFailTime() != null) {
			    event.trigger(event.getSubject(), event.getMessage());
			    event.notificationThread.join();
			    if (event.getSubscriptions().isEmpty()) {
				event.deleted = true;
			    } else if (System.currentTimeMillis() - event.getFailTime().getTime() >= ResendMessagesJob.RESEND_TIME_LIMIT) {
				event.deleted = true;
				StringBuilder body = new StringBuilder(
						commonMessageService.getMessage("mail.resend.abandon.body1")).append(
					event.getDefaultMessage()).append(
							commonMessageService.getMessage("mail.resend.abandon.body2"));
				for (Subscription subscription : event.getSubscriptions()) {
				    User user = (User) userManagementService.findById(User.class, subscription.getUserId());
				    body.append(user.getLogin()).append('\n');
				}
				((DeliveryMethodMail) IEventNotificationService.DELIVERY_METHOD_MAIL).notifyAdmin(
						commonMessageService.getMessage("mail.resend.abandon.subject"),
					body.toString(), event.isHtmlFormat());
			    }
			} else {
			    for (Subscription subscription : event.getSubscriptions()) {
				if ((subscription.getLastOperationTime() != null)
					&& (System.currentTimeMillis() - subscription.getLastOperationTime().getTime() > subscription
						.getPeriodicity())) {
				    String subject = event.getSubject() == null ? event.getDefaultSubject() : event
					    .getSubject();
				    String message = event.getMessage() == null ? event.getDefaultMessage() : event
					    .getMessage();
				    boolean isHtmlFormat = event.isHtmlFormat();
				    subscription.notifyUser(subject, message, isHtmlFormat);
				}
			    }
			}
			eventNotificationService.saveEvent(event);
		    }
		} catch (Exception e) {
		    log.error("Error while resending messages", e);
		    throw new JobExecutionException("Error while resending messages");
		}
	}
	
	public IEventNotificationService getEventNotificationService() {
		return eventNotificationService;
	}


	public void setEventNotificationService(IEventNotificationService eventNotificationService) {
		this.eventNotificationService = eventNotificationService;
	}

	public MessageService getCommonMessageService() {
		return commonMessageService;
	}

	public void setCommonMessageService(MessageService commonMessageService) {
		this.commonMessageService = commonMessageService;
	}

	public IUserManagementService getUserManagementService() {
		return userManagementService;
	}

	public void setUserManagementService(IUserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

}