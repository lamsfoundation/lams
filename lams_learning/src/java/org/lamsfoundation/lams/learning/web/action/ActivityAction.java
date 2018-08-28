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


package org.lamsfoundation.lams.learning.web.action;

import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.web.action.LamsAction;

/**
 * Base class for all activity action classes. Each subclass should call
 * super.execute() to set up the progress data in the ActivityForm.
 */
public abstract class ActivityAction extends LamsAction {

    public static final String RELEASED_LESSONS_REQUEST_ATTRIBUTE = "releasedLessons";

    private ILearnerFullService learnerService = null;

    protected ILearnerFullService getLearnerService() {
	if (learnerService == null) {
	    learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	}
	return learnerService;
    }

}