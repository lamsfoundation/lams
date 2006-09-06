<%@include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="report" value="${sessionMap.report}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css localLinkPath="../"/>
</head>
<body>

<div id="page-learner"><!--main box 'page'-->

	<h1 class="no-tabs-below"><fmt:message key="activity.title" /></h1>
	<div id="header-no-tabs-learner">

	</div><!--closes header-->

	<div id="content-learner">

		<c:choose>
			<c:when test="${empty report}">
				<h3>
					<fmt:message key="label.learner.noUpload" />
				</h3>
			</c:when>
			<c:otherwise>
				<table>
					<c:forEach items="${report}" var="mapElement">
						<c:set var="sessionName" value="${mapElement.key}" />
						<c:set var="session" value="${mapElement.value}" />
							<tr>
								<td colspan="2">
									<h2>
										${sessionName}
									</h2>
								</td>
							</tr>
						<c:forEach items="${session}" var="mapElement">
						
							<!--  Start user entry -->
							<c:set var="user" value="${mapElement.key}" />
							<c:set var="submissionList" value="${mapElement.value}" />
							
							<tr>
								<td colspan="2">
									<c:out value="${user.firstName}" />
									<c:out value="${user.lastName}" />
									(
									<c:out value="${user.login}" />
									) , 
									<fmt:message key="label.submit.file.suffix"/>:
								</td>
							</tr>
							
							<c:choose>
							<c:when test="${empty submissionList}">
								<tr>
									<td colspan="2">
										<fmt:message key="label.learner.noUpload" />
								</td>
							</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${submissionList}" var="submission">
									<tr>
										<td>
											<fmt:message key="label.learner.filePath" />
											:
										</td>
										<td>
											<a href='<c:out value="${submission.exportedURL}"/>'> <c:out value="${submission.filePath}" /> </a>
										</td>
		
									</tr>
									<tr>
										<td>
											<fmt:message key="label.learner.fileDescription" />
											:
										</td>
										<td>
											<c:out value="${submission.fileDescription}" escapeXml="false" />
										</td>
									</tr>
									<tr>
										<td>
											<fmt:message key="label.learner.dateOfSubmission" />
											:
										</td>
										<td>
											<lams:Date value="${submission.dateOfSubmission}" />
										</td>
									</tr>
									<tr>
										<td>
											<fmt:message key="label.learner.marks" />
											:
										</td>
										<td>
											<c:choose>
												<c:when test="${empty submission.marks}">
													<fmt:message key="label.learner.notAvailable" />
												</c:when>
												<c:otherwise>
													<c:out value="${submission.marks}" escapeXml="false" />
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td>
											<fmt:message key="label.learner.comments" />
											:
										</td>
										<td>
											<c:choose>
												<c:when test="${empty submission.comments}">
													<fmt:message key="label.learner.notAvailable" />
												</c:when>
												<c:otherwise>
													<c:out value="${submission.comments}" escapeXml="false" />
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;
										</td>
										<td>
											&nbsp;
										</td>
									</tr>
	
								</c:forEach>
							</c:otherwise>
							</c:choose>
							<!--  End user entry -->
							
						</c:forEach>
						
							<%--  End Session loop entry --%>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>

	</div>  <!--closes content-->


	<div id="footer-learner">
	</div><!--closes footer-->

</div><!--closes page-->
</body>
</html:html>

