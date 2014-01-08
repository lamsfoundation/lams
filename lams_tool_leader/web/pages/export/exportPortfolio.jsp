<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><c:out value="${leaderselectionDTO.title}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />
	</lams:head>

	<body class="stripes">

		<div id="content">

			<h1>
				<c:out value="${leaderselectionDTO.title}" escapeXml="false" />
			</h1>

			<p>
				<c:out value="${leaderselectionDTO.instructions}" escapeXml="false" />
			</p>

			<c:forEach var="session" items="${leaderselectionDTO.sessionDTOs}">
				<div id="sid-${session.sessionID}">
					<p>
						&nbsp;
					</p>
					<b>
						<fmt:message key="heading.group" >
							<fmt:param>
								${session.sessionName}
							</fmt:param>
						</fmt:message>
					</b>
					<div>
						<fmt:message key="heading.leader" /> 
						<c:choose>
							<c:when test="${session.groupLeader != null}">
								${session.groupLeader.firstName} ${session.groupLeader.lastName}
							</c:when>
							<c:otherwise>
								<fmt:message key="label.no.leader.yet" />
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:forEach>
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->

	</body>
</html>

