package org.lamsfoundation.lams.tool.peerreview.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dto.GroupSummary;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tblmonitoring")
public class TblMonitoringController {

    @Autowired
    @Qualifier("peerreviewService")
    private IPeerreviewService peerreviewService;

    /**
     * Shows tra page
     */
    @RequestMapping("/peerreview")
    public String peerreview(HttpServletRequest request) {
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
	
	return "/pages/monitoring/summary";
    }
    
}