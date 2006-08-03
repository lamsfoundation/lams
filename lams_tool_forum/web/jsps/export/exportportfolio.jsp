<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<lams:css localLinkPath="../"/>
</head>

<body>
<h1 class="no-tabs-below">
	${forum_title}
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">

	<h2>
		<fmt:message key="title.message.view.topic" />
	</h2>
<c:forEach var="entry" items="${ToolContentTopicList}">
	<c:set var="sessionName" value="${entry.key}"/>
	<h2>
		${sessionName}
	</h2>
	
	<c:set var="topicThread" value="${entry.value}"/>
	<c:forEach var="msgDto" items="${topicThread}">
		<c:set var="indentSize" value="${msgDto.level*3}" />
		<c:set var="hidden" value="${msgDto.message.hideFlag}" />
		<div style="margin-left:<c:out value="${indentSize}"/>em;">
			<table cellspacing="0" class="forum">
				<tr>
					<th class="first">
						<c:choose>
							<c:when test='${(mode == "teacher") || (not hidden)}'>
								<b> <c:out value="${msgDto.message.subject}" /> </b>
							</c:when>
							<c:otherwise>
								<fmt:message key="topic.message.subject.hidden" />
							</c:otherwise>
						</c:choose>
					</th>
				</tr>
				<tr>
					<td class="first posted-by">
						<c:if test='${(mode == "teacher") || (not hidden)}'>
							<fmt:message key="lable.topic.subject.by" />
							<c:out value="${msgDto.author}" />
									-
									<fmt:formatDate value="${msgDto.message.created}" type="time" timeStyle="short" />
							<fmt:formatDate value="${msgDto.message.created}" type="date" dateStyle="full" />
						</c:if>
					</td>
				</tr>
				<tr>
					<td>
						<c:if test='${(not hidden) || (hidden && mode == "teacher")}'>
							<c:out value="${msgDto.message.body}" escapeXml="false" />
						</c:if>
						<c:if test='${hidden}'>
							<fmt:message key="topic.message.body.hidden" />
						</c:if>
					</td>
				</tr>
	
				<c:if test="${not empty msgDto.message.attachments}">
					<tr>
						<td>
							<c:forEach var="file" items="${msgDto.message.attachments}">
								<a href="${file.fileName}"> <c:out value="${file.fileName}" /> </a>
							</c:forEach>
						</td>
					</tr>
				</c:if>
				<%-- display mark for teacher --%>
				<c:if test='${mode == "teacher"}'>
					<tr>
						<td>
							<span class="field-name" ><fmt:message key="lable.topic.title.mark"/></span>
							<BR>
							<c:choose>
								<c:when test="${empty msgDto.mark}">
									<fmt:message key="message.not.avaliable"/>
								</c:when>
								<c:otherwise>
									${msgDto.mark}
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td>
							<span class="field-name" ><fmt:message key="lable.topic.title.comment"/></span>
							<BR>
							<c:choose>
								<c:when test="${empty msgDto.comment}">
									<fmt:message key="message.not.avaliable"/>
								</c:when>
								<c:otherwise>
									<c:out value="${msgDto.comment}" escapeXml="false" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
			</table>
		</div>
	</c:forEach>
</c:forEach>


</div>

<div id="footer-learner"></div>

</body>
</html:html>
