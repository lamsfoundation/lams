<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<head>
		<title><c:out value="${chatDTO.title}" escapeXml="false" />
		</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<lams:css localLinkPath="../" />
	</head>

	<body>
		<div id="page-learner">
			<!--main box 'page'-->

			<h1 class="no-tabs-below">
				<c:out value="${chatDTO.title}" escapeXml="false" />
			</h1>
			<div id="header-no-tabs-learner">

			</div>
			<!--closes header-->

			<div id="content-learner">
				<br />
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
						<p>
							<c:forEach var="message" items="${session.messageDTOs}">
								${message.from} : ${message.body}<BR>
							</c:forEach>
						</p>
					</div>

					<h3>
						<fmt:message key="heading.reflection" />
					</h3>
					<c:forEach var="user" items="${session.userDTOs}">
						<c:if test="${user.finishedReflection}">
							<p>
								<span style="font-weight: bold">${user.firstName}
									${user.lastName} </span>
								<br />
								${user.notebookEntry}
							</p>
						</c:if>
					</c:forEach>
				</c:forEach>

			</div>
			<!--closes content-->

			<div id="footer-learner">
			</div>
			<!--closes footer-->

		</div>
		<!--closes page-->
	</body>
</html>

