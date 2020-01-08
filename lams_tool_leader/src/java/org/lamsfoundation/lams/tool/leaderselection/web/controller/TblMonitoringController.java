package org.lamsfoundation.lams.tool.leaderselection.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/tblmonitoring")
public class TblMonitoringController {
    private static Logger log = Logger.getLogger(TblMonitoringController.class);

    @Autowired
    private ILeaderselectionService leaderselectionService;

    /**
     * Save selected user as a leader.
     */
    @RequestMapping(path = "/changeLeader", method = RequestMethod.POST)
    @ResponseBody
    public String changeLeader(HttpServletRequest request, HttpServletResponse response) {
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
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("isSuccessful", isSuccessful);
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

}