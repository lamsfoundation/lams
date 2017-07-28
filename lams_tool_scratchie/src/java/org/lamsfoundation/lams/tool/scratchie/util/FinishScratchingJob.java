package org.lamsfoundation.lams.tool.scratchie.util;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.web.action.LearningWebsocketServer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * The Quartz sheduling job that finishes scratching for the given ToolSession. It will lead to showing Finish button to
 * all non-leaders in case they are not waiting for notebook/burning question to be submitted by the leader, and showing
 * waitForLeaderSubmit page if they are waiting.
 *
 * @author Andrey Balan
 */
public class FinishScratchingJob extends QuartzJobBean {

    private static Logger log = Logger.getLogger(FinishScratchingJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	IScratchieService scratchieService = getScratchieService(context);

	//getting toolSessionId set from scheduler
	Map properties = context.getJobDetail().getJobDataMap();
	Long toolSessionId = (Long) properties.get("toolSessionId");
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionId);

	//proceed only in case the leader hasn't finished scratching on his own
	if (!toolSession.isScratchingFinished()) {
	    try {
		//mark scratching as finished to stop showing learning.jsp to the leader
		scratchieService.setScratchingFinished(toolSessionId);
	    } catch (JSONException | IOException e) {
		throw new RuntimeException(e);
	    }
	}

	if (log.isDebugEnabled()) {
	    log.debug("Scratching is finished for toolSessionId: " + toolSessionId.longValue());
	}
    }

    protected IScratchieService getScratchieService(JobExecutionContext context) throws JobExecutionException {
	try {
	    SchedulerContext sc = context.getScheduler().getContext();
	    ApplicationContext cxt = (ApplicationContext) sc.get("context.central");
	    return (IScratchieService) cxt.getBean("scratchieService");
	} catch (SchedulerException e) {
	    throw new JobExecutionException("Failed look up the scratchieService" + e.toString());
	}
    }
}
