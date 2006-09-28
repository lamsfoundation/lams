<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<html:html locale="true">
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
</head>
<body class="stripes">
			<h1>
				<fmt:message key="page.title.monitoring.view.reflection"/>
			</h1>
			
			<div id="content">
				<table>
					<tr>
						<td>
							<h1>
								${userDTO.fullName}
							</h1>
						</td>
					</tr>
					<tr>
						<td>
							<h1>
								${userDTO.reflectInstrctions}
							</h1>
						</td>
					</tr>
					<tr>
						<td>
							<c:choose>
								<c:when test="${userDTO.finishReflection}">
									<c:out value="${userDTO.reflect}" escapeXml="false"/>
								</c:when>
								<c:otherwise>
									<fmt:message key="message.not.avaliable" />
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
		<div id="footer">
		</div>
		<!--closes footer-->
</body>
</html:html>
