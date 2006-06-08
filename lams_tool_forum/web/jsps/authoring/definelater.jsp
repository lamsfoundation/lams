<%@ include file="/includes/taglibs.jsp"%>
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

	<!-- start tabs -->
	<lams:Tabs collection="${tabs}" useKey="true" control="true" />
	<!-- end tab buttons -->
	<div class="tabbody">
		<!-- tab content 1 (Basic) -->
		<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />
		<!-- end of content (Basic) -->

		<!-- Button Row -->
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.toolContentID}" defineLater="yes"/>

		<lams:HTMLEditor />
	</div>

</html:form>
