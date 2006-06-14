<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<head>
		<title><fmt:message key="titleHeading.exportPortfolio" /></title>
		<lams:headItems />
	</head>

	<body>
		<h1>
			${chatDTO.title}
		</h1>

		<p>
			${chatDTO.instructions}
		</p>

		<c:if test='${mode == "teacher"}'>
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
					<div>
						${message.from} : ${message.body}
					</div>
				</c:forEach>
			</div>
		</c:forEach>
	</body>
</html>

