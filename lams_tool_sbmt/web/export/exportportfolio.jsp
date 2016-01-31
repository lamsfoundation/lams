<%@include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="report" value="${sessionMap.report}"/>
<c:set var="reflectOn" value="${sessionMap.reflectOn}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css localLinkPath="../"/>
</lams:head>
<body class="stripes">


	<div id="content">

	<h1><fmt:message key="activity.title" /></h1>
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
								</c:forEach>
							</c:otherwise>
							</c:choose>
							
							<c:if test="${reflectOn}">
								<tr>
									<td>
										<fmt:message key="title.reflection" />
										:
									</td>
									<td>
										<lams:out value="${user.reflect}" escapeHtml="true" />
									</td>
								</tr>
							</c:if>
							
							<tr>
								<td>
									&nbsp;
								</td>
								<td>
									&nbsp;
								</td>
							</tr>
							<!--  End user entry -->
							
						</c:forEach>
						
							<%--  End Session loop entry --%>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>

	</div>  <!--closes content-->


	<div id="footer">
	</div><!--closes footer-->

</body>
</lams:html>

