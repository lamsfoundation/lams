<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Set"%>

<%Set tabs = new HashSet();
			tabs.add("authoring.tab.basic");
			pageContext.setAttribute("tabs", tabs);

			%>
<html:form action="authoring/update" method="post" styleId="authoringForm">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<html:hidden property="toolContentID" />
	<html:hidden property="mode" value="teacher" />
	<html:hidden property="sessionMapID"/>
	<input type="hidden" name="mode" value="teacher">
	
	<div id="header">
		<lams:Tabs collection="${tabs}" useKey="true" control="true" />
	</div>
	
	<div id="content">
		<table>
			<tr><td>
			<%@ include file="/common/messages.jsp"%>
			</td></tr>
		</table>
		<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />

		<!-- Button Row -->
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
			toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.toolContentID}" 
			accessMode="teacher" defineLater="yes" customiseSessionID="${formBean.sessionMapID}" />
		<lams:HTMLEditor />
	</div>
</html:form>
