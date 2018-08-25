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

package org.lamsfoundation.lams.tool.vote.web.action;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.web.action.LamsAuthoringFinishAction;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.util.CommonConstants;

/**
 * This class give a chance to clear HttpSession when user save/close authoring page.
 *
 * @author Steve.Ni, Ozgur Demirtas
 */
public class ClearSessionAction extends LamsAuthoringFinishAction {
    private static Logger logger = Logger.getLogger(ClearSessionAction.class.getName());

    @Override
    public void clearSession(String customiseSessionID, HttpSession session, ToolAccessMode mode) {
	session.removeAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG);
	if (mode.isAuthor()) {
	    ClearSessionAction.logger.debug("In Author mode");
	    session.removeAttribute(customiseSessionID);
	}
    }

}
