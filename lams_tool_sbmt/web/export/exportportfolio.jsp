<%@include file="../sharing/share.jsp"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<title>
		Submission Export
	</title>
	<lams:css localLink="true" />
</head>
<body>
	<h1>
		Export Portfolio
	</h1>
	<h2>
		Submit
	</h2>
	<br>
	<div id="datatablecontainer">
		<c:choose>
			<c:when test="${empty report}">
				<h3>
					No files have been submitted
				</h3>
			</c:when>
			<c:otherwise>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach items="${report}" var="mapElement">
						<c:set var="user" value="${mapElement.key}" />
						<c:set var="submissionList" value="${mapElement.value}" />
						<c:set var="first" value="true" />
						<c:forEach items="${submissionList}" var="submission">
							<c:if test="${first}">
								<c:set var="first" value="false" />
								<tr>
									<td colspan="2">
										<c:out value="${user.firstName}" />
										<c:out value="${user.lastName}" />
										(
										<c:out value="${user.login}" />
										) , submitted the following:
									</td>
								</tr>
							</c:if>
							<tr>
								<td>
									File Path:
								</td>
								<td>
									<a href='<c:out value="${submission.exportedURL}"/>'>
										<c:out value="${submission.filePath}" />
									</a>
								</td>

							</tr>
							<tr>
								<td>
									File Description:
								</td>
								<td>
									<c:out value="${submission.fileDescription}" escapeXml="false" />
								</td>
							</tr>
							<tr>
								<td>
									Date of Submission:
								</td>
								<td>
									<c:out value="${submission.dateOfSubmission}" />
								</td>
							</tr>
							<tr>
								<td>
									Marks:
								</td>
								<td>
									<c:choose>
										<c:when test="${empty submission.marks}">
											<c:out value="Not Available" />
										</c:when>
										<c:otherwise>
											<c:out value="${submission.marks}" escapeXml="false" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td>
									Comments:
								</td>
								<td>
									<c:choose>
										<c:when test="${empty submission.comments}">
											<c:out value="Not Available" />
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
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>

	</div>
</body>
</html:html>

