<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><c:out value="${chatDTO.title}" escapeXml="true" /></title>
		<lams:css localLinkPath="../" />

		<style>
			<jsp:include flush="true" page="/includes/css/chat.css"></jsp:include>			
		</style>
	</lams:head>

	<body class="stripes">

			<div id="content">
			<h1>
				<c:out value="${chatDTO.title}" escapeXml="true" />
			</h1>

				<div class="space-left space-right">

					<p>
						<c:out value="${chatDTO.instructions}" escapeXml="false" />
					</p>
					<c:if test="${fn:length(chatDTO.sessionDTOs) > 1}">
						<div id="sessionContents">
							<ul>
								<c:forEach var="session" items="${chatDTO.sessionDTOs}">
									<li>
										<a href="#sid-${session.sessionID}">${session.sessionName}</a>
									</li>
								</c:forEach>
							</ul>
						</div>
					</c:if>

					<c:forEach var="session" items="${chatDTO.sessionDTOs}">
						<div id="sid-${session.sessionID}">
							<h2>
								${session.sessionName}
							</h2>

							<c:forEach var="message" items="${session.messageDTOs}">
								<div class="message">
									<span class="messageFrom"> ${message.from} </span>
									<br />
									<lams:out escapeHtml="true" value="${message.body}"></lams:out>
								</div>
							</c:forEach>

						</div>

						<c:if test="${chatDTO.reflectOnActivity}">
							<h3>
								<fmt:message key="heading.reflection" />
							</h3>
							<c:forEach var="user" items="${session.userDTOs}">
								<c:if test="${user.finishedReflection}">

									<p>
										<span style="font-weight: bold"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/> </span>
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
