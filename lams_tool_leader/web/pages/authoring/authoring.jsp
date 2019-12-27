<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants"%>

<lams:html>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
	<lams:headItems />
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	<lams:headItems/>
		<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
	</lams:head>
	<body class="stripes" onload="init();">
		<form:form action="/lams/tool/lalead11/authoring/updateContent.do"  modelAttribute="authoringForm" id="authoringForm" method="post" >
			<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
			<c:set var="title">
				<fmt:message key="activity.title" />
			</c:set>
			<lams:Page title="${title}" type="navbar" formID="authoringForm">		
				<lams:Tabs control="true" title="${title}" helpToolSignature="<%= LeaderselectionConstants.TOOL_SIGNATURE %>" helpModule="authoring">
					<lams:Tab id="1" key="button.basic" />
				</lams:Tabs>   
				<form:hidden path="currentTab" id="currentTab" />
				<form:hidden path="sessionMapID" />
				<lams:TabBodyArea>
					 <lams:errors/>
					<%-- Page tabs --%>
		            <lams:TabBodys>
						<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
	           		</lams:TabBodys>
					<lams:AuthoringButton formID="authoringForm"
						clearSessionActionUrl="/clearsession.do" toolSignature="lalead11"
						cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
						toolContentID="${sessionMap.toolContentID}"
						accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode=='teacher'}"
						customiseSessionID="${sessionMap.sessionID}" 
						contentFolderID="${sessionMap.contentFolderID}" />
					</lams:TabBodyArea>
				</lams:Page>
			</form:form>
		<div id="footer"></div>
	</body>
</lams:html>




