<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
		<%-- This page modifies its content depending on the page it was included from. --%>
		<c:if test="${not empty param.includeMode}">
			<c:set var="includeMode" value="${param.includeMode}" />
		</c:if>
		<lams:css localLinkPath="../../" />
		<link rel="StyleSheet" href="../daco.css" type="text/css" />
		<c:if test="${not empty param.sessionMapID}">
			<c:set var="sessionMapID" value="${param.sessionMapID}" />
		</c:if>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
			
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/prototype.js"></script>					
		<%@ include file="/common/header.jsp"%>

		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoMonitoring.js'/>"></script>
		<script type="text/javascript">
			function checkCheckbox(checkboxName){
				var checkbox = document.getElementById(checkboxName);
				checkbox.checked=true;
			}	
		</script> 
		<title>
			<fmt:message key="title.monitoring.recordlist" />
		</title>
</lams:head> 
<body class="stripes">
<div id="page">
	<div id="header-no-tabs">
	</div>
	<div id="content">
		<c:forEach var="userGroup" items="${monitoringSummary}">
			<c:forEach var="user" items="${userGroup.users}" varStatus="userStatus">
				<c:if test="${empty userUid || userUid==user.uid}">
					<table cellpadding="0" class="alternative-color">
						<tr>
							<th><fmt:message key="label.monitoring.fullname" /></th>
							<th><fmt:message key="label.monitoring.recordcount" /></th>
						</tr>
						<c:if test="${userStatus.first || userUid==user.uid}">
							<tr>
								<td colspan="3" style="height: 20px; font-weight: bold; text-align: center">
									<fmt:message key="label.monitoring.group" />: ${userGroup.sessionName}
								</td>
							</tr>
						</c:if>
						<tr>
							<td style="height: 20px;">
								 <c:out value="${user.fullName}" escapeXml="true"/>
							</td>
							<td>
								 ${user.recordCount}
							</td>
						</tr>
					</table>
					<%-- for each user his record list is displayed --%>
					<c:if test="${not empty user.records}">
						<c:set var="recordList" value="${user.records}" />
						<%@ include file="/pages/learning/listRecords.jsp" %>
					</c:if>
				</c:if>
			</c:forEach>
		</c:forEach>
	</div>
	<div id="footer">
	</div>
</div>
</body>
</lams:html>
