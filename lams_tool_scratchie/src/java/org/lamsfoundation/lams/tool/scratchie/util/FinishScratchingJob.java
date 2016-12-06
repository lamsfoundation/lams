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
 * The Quartz sheduling job that finishes scratching for the given ToolSession (which will automatically lead to showing
 * Finish button all non-leaders and thus let then finish the activity).
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
	
	// if leader hasn't finished scratching yet - let all non-leaders see Next Activity button
	if (!toolSession.isScratchingFinished()) {
	    try {
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
