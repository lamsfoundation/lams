<!DOCTYPE html>
            
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.scribe.util.ScribeConstants"%>

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
		<c:set var="tool">
			<lams:WebAppURL />
		</c:set>

		<script type="text/javascript"
			src="${tool}includes/javascript/authoring.js">
		</script>
	</lams:head>
	<body class="stripes" onload="init();">
		<c:set var="csrfToken"><csrf:token/></c:set>
		<form:form action="/lams/tool/lascrb11/authoring/updateContent.do?${csrfToken}" modelAttribute="authoringForm" id="authoringForm" method="post">
		
			<c:set var="sessionMap" value="${sessionScope[authoringForm.sessionMapID]}" />
		
			<c:set var="title"><fmt:message key="activity.title" /></c:set>
			<lams:Page title="${title}" type="navbar">
		
				<lams:Tabs control="true" title="${title}" helpToolSignature="<%= ScribeConstants.TOOL_SIGNATURE %>" helpModule="authoring">
					<lams:Tab id="1" key="button.basic" />
					<lams:Tab id="2" key="button.advanced" />
				</lams:Tabs>
		
				<form:hidden path="currentTab" id="currentTab" />
				<form:hidden path="sessionMapID" />
		
				
		        <lams:TabBodyArea>
					<div id="message" style="text-align: center;">
						 <c:set var="messageKey" value="MESSAGE" /> 
						 <c:if test="${not empty infoMap and not empty infoMap[messageKey]}"> 
						     <lams:Alert id="error" type="danger" close="false"> 
						         <c:forEach var="message" items="${infoMap[messageKey]}"> 
						             <c:out value="${message}" /><br /> 
						         </c:forEach> 
						     </lams:Alert> 
						</c:if>			
					</div>
			
					<%-- Page tabs --%>
					<lams:TabBodys>
						<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
						<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
					</lams:TabBodys>
		
					<lams:AuthoringButton formID="authoringForm"
						clearSessionActionUrl="/clearsession.do" toolSignature="lascrb11"
						cancelButtonLabelKey="button.cancel" saveButtonLabelKey="button.save"
						toolContentID="${sessionMap.toolContentID}"
						accessMode="${sessionMap.mode}" defineLater="${sessionMap.mode == 'teacher'}"
						customiseSessionID="${sessionMap.sessionID}"
						contentFolderID="${sessionMap.contentFolderID}" />
				</lams:TabBodyArea>
		
				<div id="footer"></div>
		
			</lams:Page>
		
		</form:form>

	</body>
</lams:html>
<%@ include file="/common/taglibs.jsp"%>


