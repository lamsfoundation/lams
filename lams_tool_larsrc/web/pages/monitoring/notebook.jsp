<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<lams:html>
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
</head>
<body class="stripes">
		
			<div id="content">
				<h1>
					<fmt:message key="page.title.monitoring.view.reflection"/>
				</h1>
				${userDTO.fullName}<BR><BR>
				<table class="alternative-color" cellspacing="0">
					<tr>
						<th class="first">
							<lams:out value="${userDTO.reflectInstrctions}"/>
						</th>
					</tr>
					<tr>
						<td>
							<c:choose>
								<c:when test="${userDTO.finishReflection}">
									<lams:out value="${userDTO.reflect}"/>
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
</lams:html>
