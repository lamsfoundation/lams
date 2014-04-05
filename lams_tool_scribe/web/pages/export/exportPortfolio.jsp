<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><c:out value="${scribeDTO.title}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />

		<style>
			<jsp:include flush="true" page="/includes/css/scribe.css"></jsp:include>			
		</style>
	</lams:head>

	<body class="stripes">

		<div id="content">
			<h1>
				<c:out value="${scribeDTO.title}" escapeXml="false" />
			</h1>


			<div class="space-left space-right">

				<p>
					<c:out value="${scribeDTO.instructions}" escapeXml="false" />
				</p>
				<c:if test="${fn:length(scribeDTO.sessionDTOs) > 1}">
					<div id="sessionContents">
						<ul>
							<c:forEach var="session" items="${scribeDTO.sessionDTOs}">
								<li>
									<a href="#sid-${session.sessionID}">${session.sessionName}</a>
								</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>

				<c:forEach var="session" items="${scribeDTO.sessionDTOs}">
					<div id="sid-${session.sessionID}">
						<h2>
							${session.sessionName}
						</h2>

						<div class="field-name">
							<fmt:message key="heading.appointedScribe" />							
						</div>

						<p>
							<c:out value="${session.appointedScribe}" escapeXml="true"/>
						</p>

						<c:set var="scribeSessionDTO" value="${session}" scope="request"> </c:set>
						<%@include file="/pages/parts/voteDisplay.jsp" %>						

						<div class="field-name">
							<fmt:message key="heading.report" />
						</div>
						<hr />
						<c:forEach var="report" items="${session.reportDTOs}">
							<p>
								<lams:out value="${report.headingDTO.headingText}" />
							</p>
							<p>
								<lams:out value="${report.entryText}" />
							</p>
							<hr />
						</c:forEach>
					</div>

					<c:if test="${scribeDTO.reflectOnActivity}">
						<h3>
							<fmt:message key="heading.reflection" />
						</h3>
						<c:forEach var="user" items="${session.userDTOs}">
							<c:if test="${user.finishedReflection}">

								<p>
									<span style="font-weight: bold"><c:out value="${user.firstName}
										${user.lastName}" escapeXml="true"/> </span>
									<br />
									<lams:out value="${user.notebookEntry}" escapeHtml="true"/>
								</p>
							</c:if>
						</c:forEach>
					</c:if>
				</c:forEach>
			</div>
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->

	</body>
</lams:html>
