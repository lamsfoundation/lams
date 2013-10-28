<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Set"%>

<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>

<%Set tabs = new HashSet();
			tabs.add("authoring.tab.basic");
			pageContext.setAttribute("tabs", tabs);

			%>
<html:form action="authoring/update" method="post" styleId="authoringForm">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<html:hidden property="toolContentID" />
	<html:hidden property="sessionMapID"/>
	<html:hidden property="mode" value="teacher" />
	<html:hidden property="contentFolderID" />
	
	<div id="header">
		<lams:Tabs collection="${tabs}" useKey="true" control="true" />
	</div>
	
	<div id="content">

		<%@ include file="/common/messages.jsp"%>
		
		<lams:help toolSignature="<%= ForumConstants.TOOL_SIGNATURE %>" module="authoring"/>
		<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />

		<!-- Button Row -->
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
			toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.toolContentID}" 
			accessMode="teacher" defineLater="yes" customiseSessionID="${formBean.sessionMapID}"  contentFolderID="${formBean.contentFolderID}"/>
	</div>
</html:form>
