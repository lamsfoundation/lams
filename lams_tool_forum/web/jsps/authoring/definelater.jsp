<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.forum.util.ForumConstants"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Set"%>

<script type="text/javascript" src="${tool}includes/javascript/message.js"></script>

<html:form action="authoring/update" method="post" styleId="authoringForm">
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<html:hidden property="toolContentID" />
	<html:hidden property="sessionMapID"/>
	<html:hidden property="mode" value="teacher" />
	<html:hidden property="contentFolderID" />
	
<c:set var="title"><fmt:message key="activity.title" /></c:set>
<lams:Page title="${title}" type="navbar">
	
	<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ForumConstants.TOOL_SIGNATURE %>" helpModule="authoring">
		<lams:Tab id="1" key="authoring.tab.basic" />
	</lams:Tabs>    

	<lams:TabBodyArea>
		<%@ include file="/common/messages.jsp"%>
	   
	    <!--  Set up tabs  -->
	     <lams:TabBodys>
			<lams:TabBody id="1" titleKey="authoring.tab.basic" page="basic.jsp" />
	    </lams:TabBodys>
	
		<!-- Button Row -->
		<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" 
			toolSignature="<%=ForumConstants.TOOL_SIGNATURE%>" toolContentID="${formBean.toolContentID}" 
			accessMode="teacher" defineLater="yes" customiseSessionID="${formBean.sessionMapID}"  contentFolderID="${formBean.contentFolderID}"/>

	</lams:TabBodyArea>
           		
    <div id="footer"></div>
    
</lams:Page>

</html:form>
