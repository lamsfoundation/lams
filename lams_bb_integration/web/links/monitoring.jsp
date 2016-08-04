<%@ page import="java.util.*, java.net.*,
                java.text.SimpleDateFormat,
                blackboard.data.*,
                blackboard.persist.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
				blackboard.data.navigation.*,
                blackboard.persist.course.*,
				blackboard.persist.navigation.*,
                blackboard.data.content.*,
                blackboard.persist.content.*,
                blackboard.db.*,
                blackboard.base.*,
                blackboard.platform.*,
                blackboard.platform.plugin.*,
                org.lamsfoundation.ld.integration.*,
				org.lamsfoundation.ld.integration.util.*" errorPage="error.jsp" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">
<%
	String id = request.getParameter("lsid");
    //check permission
    if (!PlugInUtil.authorizeForCourseControlPanel(request, response)) {
        return;
    }
	
	String monitorURL = LamsSecurityUtil.generateRequestURL(ctx, "monitor", id);
	response.sendRedirect(monitorURL);
%>

</bbData:context>
