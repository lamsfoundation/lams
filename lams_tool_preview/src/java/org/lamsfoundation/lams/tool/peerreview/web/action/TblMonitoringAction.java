package org.lamsfoundation.lams.tool.peerreview.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dto.GroupSummary;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TblMonitoringAction extends LamsDispatchAction {
    private static Logger log = Logger.getLogger(TblMonitoringAction.class);

    private static IPeerreviewService peerreviewService;

    /**
     * Shows tra page
     */
    public ActionForward peerreview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	initializePeerreviewService();

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<GroupSummary> groupList = peerreviewService.getGroupSummaries(contentId);

	Peerreview peerreview = peerreviewService.getPeerreviewByContentId(contentId);

	// cache into sessionMap
	sessionMap.put(PeerreviewConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(PeerreviewConstants.PAGE_EDITABLE, peerreview.isContentInUse());
	sessionMap.put(PeerreviewConstants.ATTR_PEERREVIEW, peerreview);
	sessionMap.put(PeerreviewConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(PeerreviewConstants.ATTR_IS_GROUPED_ACTIVITY, peerreviewService.isGroupedActivity(contentId));
	sessionMap.put("tblMonitoring", true);
	
	List<RatingCriteria> criterias = peerreviewService.getRatingCriterias(contentId);
	request.setAttribute(PeerreviewConstants.ATTR_CRITERIAS, criterias);
	
	return mapping.findForward("summary");
    }
    
    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private void initializePeerreviewService() {
	if (peerreviewService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    peerreviewService = (IPeerreviewService) wac.getBean(PeerreviewConstants.PEERREVIEW_SERVICE);
	}
    }
}