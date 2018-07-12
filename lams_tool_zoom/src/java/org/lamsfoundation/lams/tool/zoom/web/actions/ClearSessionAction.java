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


package org.lamsfoundation.lams.tool.zoom.web.actions;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.authoring.web.LamsAuthoringFinishAction;
import org.lamsfoundation.lams.tool.ToolAccessMode;

/**
 * This class give a chance to clear HttpSession when user save/close authoring page.
 *
 * @author Steve.Ni
 *
 *
 *
 *
 *
 * @version $Revision$
 */
public class ClearSessionAction extends LamsAuthoringFinishAction {

    @Override
    public void clearSession(String customiseSessionID, HttpSession session, ToolAccessMode mode) {
	if (mode.isAuthor()) {
	    session.removeAttribute(customiseSessionID);
	}
    }

}
