<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
        

<lams:html>
<lams:head>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="daco" value="${sessionMap.daco}"/>
<c:set var="user" value="${sessionMap.user}"/>

<%@ include file="/common/header.jsp"%>
<title><fmt:message key="label.learning.title" /></title>
</lams:head>
<body class="stripes">
		
			<div id="content">
				<h1>
					<fmt:message key="label.monitoring.reflection.heading"/>
				</h1>
				<c:out value="${user.fullName}" escapeXml="true"/><BR><BR>
				<table class="alternative-color" cellspacing="0">
					<tr>
						<th class="first">
							<lams:out value="${daco.reflectInstructions}" escapeHtml="true"/>
						</th>
					</tr>
					<tr>
						<td>
							<c:choose>
								<c:when test="${not empty user.reflectionEntry}">
									<lams:out value="${user.reflectionEntry}" escapeHtml="true"/>
								</c:when>
								<c:otherwise>
									<fmt:message key="message.no.reflection.available" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
				<a href="javascript:window.close();" class="button"><fmt:message key="label.monitoring.close"/></a>
			</div>
		<div id="footer">
		</div>
		<!--closes footer-->
</body>
</lams:html>
