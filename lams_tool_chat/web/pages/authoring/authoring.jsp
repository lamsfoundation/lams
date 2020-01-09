<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.chat.util.ChatConstants"%>

<lams:html>

	<c:set var="lams"> <lams:LAMSURL /> </c:set>
	<c:set var="tool"> <lams:WebAppURL /> </c:set>

	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		<lams:headItems />
		<script type="text/javascript" src="<lams:WebAppURL />includes/javascript/authoring.js"></script>
	</lams:head>
	
	<body class="stripes" onload="init();">
		<form:form action="updateContent.do" modelAttribute="authoringForm" id="authoringForm" method="post">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>

		<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
	
		<form:hidden path="currentTab" id="currentTab" />
		<form:hidden path="sessionMapID" />
	
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
			<lams:Page title="${title}" type="navbar" formID="authoringForm">
		
				<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ChatConstants.TOOL_SIGNATURE %>" helpModule="authoring">
					<lams:Tab id="1" key="button.basic" />
					<lams:Tab id="2" key="button.advanced" />
					<lams:Tab id="3" key="button.conditions" />
				</lams:Tabs>
		
				<lams:TabBodyArea>
				
					<lams:errors/>
				
					<%-- Page tabs --%>
					<lams:TabBodys>
						<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
						<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
						<lams:TabBody id="3" titleKey="button.conditions" page="conditions.jsp" />
					</lams:TabBodys>
					
					<lams:AuthoringButton formID="authoringForm"
						clearSessionActionUrl="/clearsession.do" toolSignature="lachat11"
						cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
						toolContentID="${sessionMap.toolContentID}"
						accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode=='teacher'}"
						customiseSessionID="${sessionMap.sessionID}"
						contentFolderID="${sessionMap.contentFolderID}" />
						
				</lams:TabBodyArea>
		
				<div id="footer"></div>
		
			</lams:Page>

		</form:form>
	</body>
</lams:html>

