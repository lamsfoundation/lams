<!DOCTYPE html>
            
<%@include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<%@ page import="org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants"%>
<lams:html>
<lams:head>
	
	<lams:headItems />
	<title><fmt:message key="activity.title" /></title>
	
	<script type="text/javascript">
        function doSelectTab(tabId) {
	    	selectTab(tabId);
        } 
    </script>

</lams:head>
<body class="stripes">
<html:form action="/authoring" styleId="authoringForm" method="post" enctype="multipart/form-data">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<c:set var="title"><fmt:message key="activity.title" /></c:set>
	
	<html:hidden property="mode" value="${sessionMap.mode}" />
	<html:hidden property="dispatch" value="updateContent" />
	<html:hidden property="sessionMapID" />
	<html:hidden property="toolContentID" />
	<html:hidden property="contentFolderID" />
	
	<lams:Page title="${title}" type="navbar">
		
		<lams:Tabs control="true" title="${title}" helpToolSignature="<%= SbmtConstants.TOOL_SIGNATURE %>" helpModule="authoring">
			<lams:Tab id="1" key="label.authoring.heading.basic" />
			<lams:Tab id="2" key="label.authoring.heading.advance" />
		</lams:Tabs> 
		
		<lams:TabBodyArea>
			<%@ include file="/common/messages.jsp"%>
	
		     <lams:TabBodys>
				<lams:TabBody id="1" titleKey="label.authoring.heading.basic.desc" page="basic.jsp" />
				<lams:TabBody id="2" titleKey="label.authoring.heading.advance.desc" page="advance.jsp" />
		    </lams:TabBodys>
	
			<lams:AuthoringButton formID="authoringForm"
				clearSessionActionUrl="/clearsession.do"
				toolSignature="<%=SbmtConstants.TOOL_SIGNATURE%>"
				accessMode="${sessionMap.mode}"
				defineLater="${sessionMap.mode == 'teacher'}"
				toolContentID="${formBean.toolContentID}"
				customiseSessionID="${formBean.sessionMapID}" 
				contentFolderID="${formBean.contentFolderID}" />
		</lams:TabBodyArea>
		
		<div id="footer"></div>
		
	</lams:Page>
</html:form>
</body>
</lams:html>