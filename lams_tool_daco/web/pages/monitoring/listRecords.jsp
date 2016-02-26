<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>

<lams:html> 
<lams:head>
		<%-- This page/learning/listRecords.jsp page modifies its content depending on the page it was included from. --%>
		<c:if test="${not empty param.includeMode}">
			<c:set var="includeMode" value="${param.includeMode}" />
		</c:if>
		<c:if test="${empty includeMode}">
			<c:set var="includeMode" value="monitoring" />
		</c:if>
		<c:if test="${not empty param.sessionMapID}">
			<c:set var="sessionMapID" value="${param.sessionMapID}" />
		</c:if>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:set var="userGroup" value="${sessionMap.monitoringSummary}" />
			
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

			<div style="float: right; margin-left: 10px; padding-top: 4px" class="help">
				<%-- Switch between the horizontal and vertical views --%>
				<c:url var="changeViewUrl" value='/monitoring/changeView.do'>
					<c:param name="sessionMapID" value="${sessionMapID}" />
					<c:param name="toolSessionID" value="${toolSessionID}" />
					<c:param name="userId" value="${userId}" />
					<c:param name="sort" value="${sort}" />
				</c:url>
				<img src="${tool}includes/images/uparrow.gif" title="<fmt:message key="label.common.view.change" />"
				 onclick="javascript:document.location.href='${changeViewUrl}'" />
			</div>

			<c:if test="${sessionMap.isGroupedActivity}">
			<h2  style="display: inline"><fmt:message key="label.monitoring.group" />: ${userGroup.sessionName}</h2>
			</c:if>
			<div class="float-right"><a href="#" onclick="javascript:self.close()" class="button"><fmt:message key="label.monitoring.close" /></a></div>
			<c:forEach var="user" items="${userGroup.users}">
					<table cellpadding="0" class="alternative-color">
						<tr>
							<th><fmt:message key="label.monitoring.fullname" /></th>
							<th><fmt:message key="label.monitoring.recordcount" /></th>
						</tr>
						<tr>
							<td style="height: 20px;">
								 <c:out value="${user.fullName}" escapeXml="true"/>
							</td>
							<td>
								 ${user.recordCount}
							</td>
						</tr>
					</table>
					<c:if test="${not empty user.records}">
						<c:set var="recordList" value="${user.records}" />
						<%@ include file="/pages/learning/listRecords.jsp" %>
					</c:if>
			</c:forEach>
	</div>
	<div id="footer">
	</div>
</div>
</body>
</lams:html>
