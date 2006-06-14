<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@ include file="/common/header.jsp"%>
</head>
<body>
	<c:if test="${empty report}">
		<div align="center"><b><fmt:message key="message.not.avaliable"/></b></div>
	</c:if>
	<c:forEach items="${report}" var="userList">
		<table width="90%" border="1" cellspacing="3" cellpadding="3" align="center">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="3" cellpadding="3">
						<c:set var="user" value="${userList.key}" />
						<c:set var="markList" value="${userList.value}" />
						<c:set var="first" value="true" />
						<c:set var="postNum" value="${fn:length(markList)}" />
						<c:forEach items="${markList}" var="topic" varStatus="status">
							<c:if test="${status.index == 0}">
								<tr>
									<td colspan="2">
										<c:out value="${user.firstName}" />
										<c:out value="${user.lastName}" />
										,
										<c:out value="${user.loginName}" />
										,
										<fmt:message key="monitoring.user.post.topic" />
									</td>
								<tr>
							</c:if>
							<tr>
								<td width="150">
									<b><fmt:message key="lable.topic.title.subject" /></b>
								</td>
								<td>
									<c:out value="${topic.message.subject}" />
								</td>
							</tr>
							<tr>
								<td>
									<b><fmt:message key="button.on" /></b>
								</td>
								<td>
									<fmt:formatDate value="${topic.message.updated}" type="time" timeStyle="short" />
									<fmt:formatDate value="${topic.message.updated}" type="date" dateStyle="full" />
								</td>
							</tr>
							<c:forEach var="file" items="${topic.message.attachments}">
								<tr>
									<td>
										<b><fmt:message key="message.label.attachment" /></b>
									</td>
									<td>
										<c:set var="downloadURL">
											<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
										</c:set>
										<a href="<c:out value='${downloadURL}' escapeXml='false'/>"> <c:out value="${file.fileName}" /> </a>
									</td>
								</tr>
							</c:forEach>
							<tr>
								<td>
									<b><fmt:message key="lable.topic.title.body" /></b>
								</td>
								<td>
									<c:out value="${topic.message.body}" escapeXml="false" />
								</td>
							</tr>
							<tr>
								<td>
									<b><fmt:message key="lable.topic.title.mark" /></b>
								</td>
								<td>
									<c:choose>
										<c:when test="${empty topic.message.report.mark}">
											<fmt:message key="message.not.avaliable" />
										</c:when>
										<c:otherwise>
											<c:out value="${topic.message.report.mark}" escapeXml="false" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td>
									<b><fmt:message key="lable.topic.title.comment" /></b>
								</td>
								<td>
									<c:choose>
										<c:when test="${empty topic.message.report.comment}">
											<fmt:message key="message.not.avaliable" />
										</c:when>
										<c:otherwise>
											<c:out value="${topic.message.report.comment}" escapeXml="false" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<c:if test="${status.count != postNum}">
								<tr>
									<td colspan="2">
										<hr>
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</table>
				</td>
			</tr>
		</table>
	</c:forEach>
</body>
</html:html>
