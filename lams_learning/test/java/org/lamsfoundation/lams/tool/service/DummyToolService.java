/*
 * Created on 21/02/2005
 *
 */
package org.lamsfoundation.lams.tool.service;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author daveg
 *
 */
public class DummyToolService implements ILamsToolService {

	public List getAllPotentialLearners(long toolContentID) throws LamsToolServiceException {
		return null;
	}

    public ToolSession createToolSession(User learner, Activity activity) throws LamsToolServiceException {
    	return null;
    }

	public ToolSession getToolSession(User learner, Activity activity) throws LamsToolServiceException {
		ToolSession toolSession = new NonGroupedToolSession();
		toolSession.setToolSessionId(activity.getActivityId());
		return toolSession;
	}

}
