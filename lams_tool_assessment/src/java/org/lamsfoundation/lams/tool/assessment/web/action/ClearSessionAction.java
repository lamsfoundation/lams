package org.lamsfoundation.lams.tool.assessment.web.action;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.web.action.LamsAuthoringFinishAction;

/**
 * This class give a chance to clear HttpSession when user save/close authoring page.
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class ClearSessionAction extends LamsAuthoringFinishAction {

    @Override
    public void clearSession(String customiseSessionID, HttpSession session, ToolAccessMode mode) {
	session.removeAttribute(customiseSessionID);
    }

}