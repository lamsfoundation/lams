/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/

package org.lamsfoundation.lams.tool.service;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
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

	public ToolSession getToolSession(User learner, Activity activity) throws LamsToolServiceException {
		ToolSession toolSession = new NonGroupedToolSession();
		toolSession.setToolSessionId(activity.getActivityId());
		return toolSession;
	}

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#createToolSession(org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.learningdesign.ToolActivity, org.lamsfoundation.lams.lesson.Lesson)
     */
    public ToolSession createToolSession(User learner, ToolActivity activity, Lesson lesson) throws LamsToolServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#notifyToolsToCreateSession(java.lang.Long, org.lamsfoundation.lams.learningdesign.ToolActivity)
     */
    public void notifyToolsToCreateSession(Long toolSessionId, ToolActivity activity)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#copyToolContent(org.lamsfoundation.lams.learningdesign.ToolActivity)
     */
    public Long copyToolContent(ToolActivity toolActivity)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#getToolSessionByLearner(org.lamsfoundation.lams.usermanagement.User, org.lamsfoundation.lams.learningdesign.Activity)
     */
    public ToolSession getToolSessionByLearner(User arg0, Activity arg1) throws LamsToolServiceException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#getToolSessionById(java.lang.Long)
     */
    public ToolSession getToolSessionById(Long arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsToolService#updateToolSession(org.lamsfoundation.lams.tool.ToolSession)
     */
    public void updateToolSession(ToolSession arg0)
    {
        // TODO Auto-generated method stub
        
    }

}
