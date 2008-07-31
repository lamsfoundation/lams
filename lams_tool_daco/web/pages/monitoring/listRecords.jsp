<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
		<%@ include file="/common/header.jsp"%>
		<c:set var="learningMode" value="false" />
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
		<c:url var="showRecordsUrl" value='/monitoring/listRecords.do?sessionMapID=${sessionMapID}' />
	
		
		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoMonitoring.js'/>"></script> 
		
		<title>
			<fmt:message key="title.monitoring.recordlist" />
		</title>
</lams:head> 
<body class="stripes">
<div id="page">
	<div id="header-no-tabs">
	</div>
	<div id="content">
		<table>
			<tr>
				<td class="button-cell">
					<div style="float: left">
						<select id="userDropdown" style="margin-top: -4px; height: 30px">
							<c:forEach var="userGroup" items="${monitoringSummary}">
								<option value="-1" disabled="disabled">--- ${userGroup.sessionName} ---</option>
								<c:forEach var="nextUser" items="${userGroup.users}">
									<option value="${nextUser.uid}" 
									<c:if test="${userUid==nextUser.uid}">
										selected="selected"
									</c:if>
									>${nextUser.fullName}</option>
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
					<a href="#" onclick="javascript:refreshPage('${showRecordsUrl}');" class="button space-left" style="margin-top: 60px;">
						<fmt:message key="label.monitoring.chooseuser" />
					</a>
				</td>
				<td class="button-cell">
					<a href="#" onclick="javascript:self.close()" class="button"><fmt:message key="button.close" /></a>
				</td>
			</tr>
		</table>
		<c:forEach var="userGroup" items="${monitoringSummary}">
			<c:forEach var="user" items="${userGroup.users}" varStatus="userStatus">
				<c:if test="${empty userUid || userUid==user.uid}">
					<table cellpadding="0" class="alternative-color">
						<tr>
							<th><fmt:message key="label.monitoring.fullname" /></th>
							<th><fmt:message key="label.monitoring.loginname" /></th>
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
								 ${user.fullName}
							</td>
							<td>
								 ${user.loginName}
							</td>
							<td>
								 ${user.recordCount}
							</td>
						</tr>
					</table>
					<c:set var="recordList" value="${user.records}" />
					<c:if test="${not empty recordList}">
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