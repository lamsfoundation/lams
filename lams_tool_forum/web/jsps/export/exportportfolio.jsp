<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="ToolContentTopicList" value="${sessionMap.ToolContentTopicList}"/>
<c:set var="mode" value="${sessionMap.mode}"/>
<c:set var="title" value="${sessionMap.title}"/>

<lams:html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<lams:css localLinkPath="../"/>
</head>

<body class="stripes">


<div id="content">

<h1>
	${title}
</h1>

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
							<c:set var="author" value="${msgDto.author}"/>
							<c:if test="${empty author}">
								<c:set var="author">
									<fmt:message key="label.default.user.name"/>
								</c:set>
							</c:if>
							${author}						
									-
							<lams:Date value="${msgDto.message.created}"/>
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
								<a href="${msgDto.attachmentLocalUrl}"> <c:out value="${msgDto.attachmentName}" /> </a>
							</c:forEach>
						</td>
					</tr>
				</c:if>
				<%-- display mark for teacher --%>
				<c:if test="${(msgDto.released && msgDto.isAuthor)|| mode=='teacher'}">
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



</body>
</lams:html>
