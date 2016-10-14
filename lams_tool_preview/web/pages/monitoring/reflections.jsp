<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

</lams:head>
<body class="stripes">

	<c:set var="title"><fmt:message key="title.reflection"/></c:set>

	<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
		<c:if test="${groupSummary.sessionId eq toolSessionId}" >
			<c:set var="title">${groupSummary.sessionName}</c:set>
		</c:if>
	</c:forEach>

	<lams:Page type="monitor" title="${title}">

		<c:if test="${empty reflectList}">
			<fmt:message key="message.no.reflection.available"></fmt:message>
		</c:if>
		
		<table class="table table-condensed table-striped">
	
			<c:forEach var="reflectDTO" items="${reflectList}">
				<tr>
					<td>
						<strong><c:out value="${reflectDTO.fullName}" escapeXml="true"/></strong> - <lams:Date value="${reflectDTO.date}"/>
						<br>
						<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
					</td>
				</tr>
			</c:forEach>
		
		</table>
	
		<span onclick="window.close()" class="btn btn-default voffset5 pull-right"><fmt:message key="label.close"/></span>
	
	</lams:Page>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>

</lams:html>