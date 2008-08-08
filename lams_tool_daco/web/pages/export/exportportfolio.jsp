<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="monitoringSummary" value="${sessionMap.monitoringSummary}" />
<c:set var="includeMode" value="exportportfolio" />
<c:set var="anyRecordsAvailable" value="false" />
<c:set var="daco" value="${sessionMap.daco}" />
<c:set var="reflectEntry" value="${sessionMap.reflectEntry}" />
<lams:html>
<lams:head>
	<title><fmt:message key="label.export.title" /></title>
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	<lams:css localLinkPath="../" />
	<link rel="StyleSheet" href="daco.css" type="text/css" />
	<script type="text/javascript">
		function launchPopup(url,title) {
			var wd = null;
			if(wd && wd.open && !wd.closed){
				wd.close();
			}
			wd = window.open(url,title,'resizable,width=796,height=570,scrollbars');
			wd.window.focus();
		}
	</script>
</lams:head>
<body class="stripes">
<div id="content">
	<table>
		<tr>
			<td>
			<h1>${daco.title}</h1>
			</td>
		</tr>
		<tr>
			<td>${daco.instructions}</td>
		</tr>
	</table>
	<c:choose>
		<c:when test="${mode == 'learner'}">
			<c:set var="recordList" value="${sessionMap.recordList}" />
			<%@ include file="/pages/learning/listRecords.jsp" %>
			<%@ include file="/pages/learning/questionSummaries.jsp" %>
			<c:if test="${daco.reflectOnActivity}">
				<div>
					<h2>${daco.reflectInstructions }</h2>
					<p>
						<c:choose>
							<c:when test="${empty reflectEntry}">
								<fmt:message key="message.no.reflection.available" />
							</c:when>
							<c:otherwise>
								${reflectEntry}
							</c:otherwise>
						</c:choose>
					</p>
				</div>
			</c:if>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${empty monitoringSummary || empty monitoringSummary[0].users}">
					<p class="hint">
						<fmt:message key="message.monitoring.summary.no.session" />
					</p>
				</c:when>
				<c:otherwise>
					<c:forEach var="userGroup" items="${monitoringSummary}">
						<c:forEach var="user" items="${userGroup.users}" varStatus="userStatus">
							<table cellpadding="0" class="alternative-color">
								<tr>
									<th><fmt:message key="label.monitoring.fullname" /></th>
									<th><fmt:message key="label.monitoring.loginname" /></th>
									<th><fmt:message key="label.monitoring.recordcount" /></th>
									<th><fmt:message key="label.monitoring.notebook" /></th>
								</tr>
								<c:if test="${userStatus.first || userUid==user.uid}">
									<tr>
										<td colspan="4" style="font-weight: bold; text-align: center">
											<fmt:message key="label.monitoring.group" />: ${userGroup.sessionName}
										</td>
									</tr>
								</c:if>
								<tr>
									<td>
										 ${user.fullName}
									</td>
									<td>
										 ${user.loginName}
									</td>
									<td style="text-align: center; font-weight: bold;">
										<c:choose>
											<c:when test="${user.recordCount > 0}">
												<c:set var="anyRecordsAvailable" value="true" />
												<a href="#" onclick="javascript:launchPopup('learners/${user.uid}-records.html','RecordList')">
													${user.recordCount}
												</a>
											</c:when>
											<c:otherwise>
												0
											</c:otherwise>
										</c:choose>
									</td>
									<td style="text-align: center;">
										<c:choose>
											<c:when test="${empty user.reflectionEntry}">
												<fmt:message key="label.monitoring.notebook.none" />
											</c:when>
											<c:otherwise>
												<a href="#" onclick="javascript:launchPopup('learners/${user.uid}-reflection.html','Reflection')">
													<fmt:message key="label.monitoring.notebook.view" />
												</a>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</table>
						</c:forEach>
					</c:forEach>
					<c:if  test="${anyRecordsAvailable}">
						<a href="#" onclick="javascript:launchPopup('learners/allRecords.html','RecordList')" class="button space-left">
							<fmt:message key="label.monitoring.viewrecords.all" />
						</a>
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
</div>
<!--closes content-->


<div id="footer"></div>
<!--closes footer-->

</body>
</lams:html>
