package org.lamsfoundation.lams.tool.leaderselection.web.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.tool.leaderselection.service.LeaderselectionServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class TblMonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(TblMonitoringAction.class);

    private ILeaderselectionService leaderselectionService;
    
    /**
     * Save selected user as a leader
     * @throws IOException 
     * @throws JSONException 
     */
    public ActionForward changeLeader(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	initService();
	
	Long leaderUserId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	LeaderselectionUser user = leaderselectionService.getUserByUserIdAndContentId(leaderUserId, toolContentId);

	// save selected user as a leader
	boolean isSuccessful = false;
	if (user != null) {
	    Long toolSessionId = user.getLeaderselectionSession().getSessionId();
	    log.info("Changing group leader for toolSessionId=" + toolSessionId + ". New leader's userUid="
		    + leaderUserId);
	    isSuccessful = leaderselectionService.setGroupLeader(user.getUid(), toolSessionId);
	}

	// build JSON
	JSONObject responseJSON = new JSONObject();
	responseJSON.put("isSuccessful", isSuccessful);
	writeResponse(response, "text/json", LamsDispatchAction.ENCODING_UTF8, responseJSON.toString());
	return null;
    }

    /**
     * set up service
     */
    private void initService() {
	if (leaderselectionService == null) {
	    leaderselectionService = LeaderselectionServiceProxy.getLeaderselectionService(this.getServlet().getServletContext());
	}
    }
}