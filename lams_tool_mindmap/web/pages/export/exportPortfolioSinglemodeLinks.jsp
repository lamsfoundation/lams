<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><c:out value="${mindmapDTO.title}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />
	</lams:head>

	<body class="stripes">
		<div id="content">
		<h1>
			<c:out value="${mindmapDTO.title}" escapeXml="false" />
		</h1>

		<p>
			<c:out value="${mindmapDTO.instructions}" escapeXml="false" />
		</p>

		<c:forEach var="session" items="${mindmapDTO.sessionDTOs}">
			<div id="sid-${session.sessionID}">
				<h2>
					${session.sessionName}
				</h2>
				<p>
					&nbsp;
				</p>
				
				<c:forEach var="user" items="${session.userDTOs}">
					<table>
						<tr>
							<th colspan="2">
								<c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
							</th>
						</tr>
								
						<tr>
							<td class="field-name" width="20%">
								<fmt:message key="label.mindmapEntry" />
							</td>
							<td>
								<a href="${user.firstName}_${user.lastName}_${user.uid}.html">
									<fmt:message key="label.view" />
								</a>							
							</td>
						</tr>
								
					</table>
				</c:forEach>
				
			</div>
		</c:forEach>
				
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->

	</body>
</html>
