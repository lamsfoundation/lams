<%@ page import="java.util.*, java.net.*,
                 org.lamsfoundation.ld.integration.blackboard.*" errorPage="error.jsp" %>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbData:context id="ctx">
<%
	String authorURL = LamsSecurityUtil.generateRequestURL(ctx, "author", null);
	authorURL = authorURL + "&isPostMessageToParent=true";
%>
<bbNG:learningSystemPage>
	<bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="false">
		<bbNG:breadcrumb>LAMS Author</bbNG:breadcrumb>
	</bbNG:breadcrumbBar>
	
	<bbNG:pageHeader>
		<bbNG:pageTitleBar title="LAMS Author" />
	</bbNG:pageHeader>
	
	To launch LAMS Author, please, click <a href="<%= authorURL %>" target="_blank">here</a>.
</bbNG:learningSystemPage>
</bbData:context>
