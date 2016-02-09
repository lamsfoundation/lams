<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL/></c:set>
<%@ page import="org.lamsfoundation.lams.tool.survey.SurveyConstants"%>
<lams:html>
<lams:head>
	    <%@ include file="/common/header.jsp" %>
</lams:head>
<body class="stripes">
	<div id="content">
		<h1><fmt:message key="title.reflection"/></h1>

		<%-- Reflection list  --%>
		<c:if test="${empty reflectList}">
			<p><fmt:message key="message.no.reflection.available"/></p>
		</c:if>
		
		<c:forEach var="user" items="${reflectList}" varStatus="refStatus">
			<c:if test="${refStatus.first}">
			<table cellpadding="0"  class="alternative-color">
				<tr>
					<th><fmt:message key="monitoring.user.fullname"/></th>
					<th><fmt:message key="monitoring.label.user.loginname"/></th>
					<th><fmt:message key="monitoring.user.reflection"/></th>
				</tr>
			</c:if>
				<tr>
					<td><c:out value="${user.fullName}" escapeXml="true"/></td>
					<td><c:out value="${user.loginName}" escapeXml="true"/></td>
					<td><c:set var="viewReflection">
							<c:url value="/monitoring/viewReflection.do?toolSessionID=${toolSessionID}&userUid=${user.userUid}"/>
						</c:set>
						<html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="label.view" />
						</html:link>
					</td>
				</tr>
			<c:if test="${refStatus.last}">
			</table>
			</c:if>
		</c:forEach>
		
	</div>
</body>
</lams:html>
