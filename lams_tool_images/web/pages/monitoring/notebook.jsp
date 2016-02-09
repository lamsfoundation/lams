<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
</lams:head>
<body class="stripes">
		
	<div id="content">
		<h1>
			<fmt:message key="page.title.monitoring.view.reflection"/>
		</h1>
		
		<c:out value="${userDTO.fullName}" escapeXml="true"/><BR><BR>
		<table class="alternative-color" cellspacing="0">
			<tr>
				<th class="first">
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
		<table cellpadding="0">
			<tr>
				<td>
					<a href="javascript:window.close();" class="button"><fmt:message key="button.close"/></a>
				</td>
			</tr>
		</table>
	</div>
			
	<div id="footer"></div><!--closes footer-->
</body>
</lams:html>
