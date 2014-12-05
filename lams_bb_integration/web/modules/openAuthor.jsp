<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page import="blackboard.platform.plugin.PlugInUtil"%>
<%@ page import="blackboard.platform.plugin.PlugInException"%>
<%@ page import="org.lamsfoundation.ld.integration.blackboard.LamsSecurityUtil"%>
<%@ page errorPage="/error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:genericPage title="Open Author" ctxId="ctx">

<%
	// Authorise current user for Course Control Panel (automatic redirect)
	try{
	    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
	        return;
	} catch(PlugInException e) {
	    throw new RuntimeException(e);
	}

	// Get the Login Request URL for authoring LAMS Lessons
	String authorUrl = LamsSecurityUtil.generateRequestURL(ctx, "author", null);
	authorUrl = authorUrl + "&isPostMessageToParent=true";
		
    response.sendRedirect(authorUrl);
%>

</bbNG:genericPage>
