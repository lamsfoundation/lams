<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<head>
		<title><c:out value="${notebookDTO.title}" escapeXml="false" />
		</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<lams:css localLinkPath="../" />
	</head>

	<body>
		<div id="page">
			<!--main box 'page'-->

			<h1 class="no-tabs-below">
				<c:out value="${notebookDTO.title}" escapeXml="false" />
			</h1>
			<div id="header-no-tabs">

			</div>
			<!--closes header-->

			<div id="content">

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
										${user.firstName} ${user.lastName }
									</th>
								</tr>
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
										<c:out value="${user.entryDTO.entry}" escapeXml="false"></c:out>
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

		</div>
		<!--closes page-->
	</body>
</html>

