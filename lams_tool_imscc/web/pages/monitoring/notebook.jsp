<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>        

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
</lams:head>

<body class="stripes">

	<c:set var="title"><fmt:message key="page.title.monitoring.view.reflection" /></c:set>
	<lams:Page type="monitor" title="${title}">
		
		<c:out value="${userDTO.fullName}" escapeXml="true"/><BR><BR>
		<table class="table">
			<tr>
				<th>
					<lams:out value="${userDTO.reflectInstrctions}" escapeHtml="true"/>
				</th>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${userDTO.finishReflection}">
							<lams:out value="${userDTO.reflect}" escapeHtml="true"/>
						</c:when>
						<c:otherwise>
							<fmt:message key="message.no.reflection.available" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</table>
		
		<a href="javascript:window.close();" class="btn btn-primary">
			<fmt:message key="button.close"/>
		</a>
	</lams:Page>
			
	<div id="footer"></div>
</body>
</lams:html>
