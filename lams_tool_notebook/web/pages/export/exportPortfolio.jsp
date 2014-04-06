<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><c:out value="${notebookDTO.title}" escapeXml="true" />
		</title>
		<lams:css localLinkPath="../" />
	</lams:head>

	<body class="stripes">

			<div id="content">

			<h1>
				<c:out value="${notebookDTO.title}" escapeXml="true" />
			</h1>

				<p>
					<c:out value="${notebookDTO.instructions}" escapeXml="false" />
				</p>

				<c:if test='${mode == "teacher"}'>
					<div id="sessionContents">
						<ul>
							<c:forEach var="session" items="${notebookDTO.sessionDTOs}">
								<li>
									<a href="#sid-${session.sessionID}">${session.sessionName}</a>
								</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>

				<c:forEach var="session" items="${notebookDTO.sessionDTOs}">
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
										<c:out value="${user.firstName} ${user.lastName}"/>
									</th>
								</tr>
								<c:if test="${not empty user.entryDTO}">
									<tr>
										<td class="field-name" width="20%">
											<fmt:message key="label.created" />
										</td>
										<td>
											<lams:Date value="${user.entryDTO.createDate }"></lams:Date>
										</td>
									</tr>
									<tr>
										<td class="field-name" width="20%">
											<fmt:message key="label.lastModified" />
										</td>
										<td>
											<lams:Date value="${user.entryDTO.lastModified }"></lams:Date>
										</td>
									</tr>
	
									<tr>
										<td class="field-name">
											<fmt:message key="label.notebookEntry" />
										</td>
										<td>
											<lams:out value="${user.entryDTO.entry}" escapeHtml="true"/>
										</td>
									</tr>
								</c:if>
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

