<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
		<%-- This page modifies its content depending on the page it was included from. --%>
		<c:if test="${not empty param.includeMode}">
			<c:set var="includeMode" value="${param.includeMode}" />
		</c:if>
		<c:if test="${empty includeMode}">
			<c:set var="includeMode" value="monitoring" />
		</c:if>
		<%-- The style sheet file is either on the server (monitoring)
			or in the parent directory (export portfolio) --%>
		<c:if test="${includeMode=='exportportfolio'}">
			<lams:css localLinkPath="../../" />
			<link rel="StyleSheet" href="../daco.css" type="text/css" />
		</c:if>
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
		<c:if test="${includeMode=='monitoring'}">
			<div style="float: right; margin-left: 10px; padding-top: 4px" class="help">
				<%-- Switch between the horizontal and vertical views --%>
				<c:url var="changeViewUrl" value='/monitoring/changeView.do'>
					<c:param name="sessionMapID" value="${sessionMapID}" />
					<c:param name="userUid" value="${userUid}" />
				</c:url>
				<img src="${tool}includes/images/uparrow.gif" title="<fmt:message key="label.common.view.change" />"
				 onclick="javascript:document.location.href='${changeViewUrl}'" />
			</div>
			<table>
				<tr>
					<td class="button-cell">
						<div style="float: left">
							<%-- Users in the dropdown menu are divided into groups.
								 Currently displayed user is selected.
								 There is also an option to display all records of all users. --%>
							<select id="userDropdown" style="margin-top: -4px;">
								<c:forEach var="userGroup" items="${monitoringSummary}">
									<option value="-1" disabled="disabled">--- ${userGroup.sessionName} ---</option>
									<c:forEach var="nextUser" items="${userGroup.users}">
										<option value="${nextUser.uid}" 
										<c:if test="${userUid==nextUser.uid}">
											selected="selected"
										</c:if>
										><c:out value="${nextUser.fullName}" escapeXml="true"/></option>
									</c:forEach>
								</c:forEach>
								<option value="-1" disabled="disabled">----------</option>
								<option value="SHOW_ALL" 
									<c:if test="${empty userUid}">
										selected="selected"
									</c:if>
								>
									<fmt:message key="label.monitoring.viewrecords.all" />
								</option>
							</select>
						</div>
						<c:url var="showRecordsUrl" value='/monitoring/listRecords.do'>
							<c:param name="sessionMapID" value="${sessionMapID}" />
						</c:url>
						<a href="#" onclick="javascript:refreshPage('${showRecordsUrl}');" class="button space-left" style="margin-top: 60px;">
							<fmt:message key="label.monitoring.chooseuser" />
						</a>
					</td>
					<td class="button-cell">
						<a href="#" onclick="javascript:self.close()" class="button"><fmt:message key="label.monitoring.close" /></a>
					</td>
				</tr>
			</table>
		</c:if>
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
