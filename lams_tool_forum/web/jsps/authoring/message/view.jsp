<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />
	</head>
	<body>

		<table class="forum" cellspacing="0">
			<!-- Basic Info Form-->
			<tr>
				<th>
					${topic.message.subject}
				</th>
			</tr>
			<tr>
				<td class="posted-by">
					<fmt:message key="lable.topic.subject.by" />
					<c:set var="author" value="${topic.author}"/>
					<c:if test="${empty author}">
						<c:set var="author">
							<fmt:message key="label.default.user.name"/>
						</c:set>
					</c:if>
					${author}
					-
					<lams:Date value="${topic.message.created}" />
				</td>
			</tr>
			<tr>
				<td>
					<c:out value="${topic.message.body}" escapeXml="false" />
				</td>
			</tr>

			<c:if test="${not empty topic.message.attachments}">
				<tr>
					<td>
						<ul>
							<c:forEach var="file" items="${topic.message.attachments}">
								<li>
									<c:set var="downloadURL">
										<html:rewrite
											page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
									</c:set>
									<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
										<c:out value="${file.fileName}" /> </a>
								</li>
							</c:forEach>
						</ul>
					</td>
				</tr>
			</c:if>
			<tr>
				<td align="center">
					<html:link href="javascript:window.parent.hideMessage()"
						styleClass="button space-left">
						<fmt:message key="button.cancel" />
					</html:link>
					<c:set var="deletetopic">
						<html:rewrite
							page="/authoring/deleteTopic.do?sessionMapID=${sessionMapID}&topicIndex=${topicIndex}" />
					</c:set>
					<html:link href="${deletetopic}" styleClass="button space-left">
						<fmt:message key="label.delete" />
					</html:link>
					<c:set var="edittopic">
						<html:rewrite
							page="/authoring/editTopic.do?sessionMapID=${sessionMapID}&topicIndex=${topicIndex}&create=${topic.message.created.time}" />
					</c:set>
					<html:link href="${edittopic}" styleClass="button space-left">
						<fmt:message key="label.edit" />
					</html:link>

				</td>
			</tr>
		</table>

	</body>
</html>

