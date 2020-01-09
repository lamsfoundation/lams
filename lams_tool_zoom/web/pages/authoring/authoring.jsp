<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.zoom.util.ZoomConstants"%>

<lams:html>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:head>
	<title><fmt:message key="activity.title" /></title>
	
	<lams:css/>
	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
</lams:head>

<body class="stripes" onload="init();">
	<lams:Page title="pageTitle.authoring" type="navbar">
		<form:form action="updateContent.do" modelAttribute="authoringForm" method="post">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<c:set var="title">
				<fmt:message key="activity.title" />
			</c:set>
		
			<!--  TITLE KEY PAGE GOES HERE -->
			<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ZoomConstants.TOOL_SIGNATURE %>" helpModule="authoring">
				<lams:Tab id="1" key="button.basic" />
				<lams:Tab id="2" key="button.advanced" />
			</lams:Tabs>
			<!--closes header-->
		
			<form:hidden path="currentTab" id="currentTab" />
			<form:hidden path="sessionMapID" />
		
			<lams:TabBodyArea>
		 		<lams:errors/>
		
				<%-- Page tabs --%>
				<lams:TabBodys>
					<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
					<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
				</lams:TabBodys>
		
				<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lazoom10"
					cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save" toolContentID="${sessionMap.toolContentID}"
					accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode == 'teacher'}" customiseSessionID="${sessionMap.sessionID}"
					contentFolderID="${sessionMap.contentFolderID}" />
		
			</lams:TabBodyArea>
			<div id="footer"></div>
		</form:form>
	</lams:Page>
</body>
</lams:html>
