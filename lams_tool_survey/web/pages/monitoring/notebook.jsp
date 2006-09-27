<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<html:html locale="true">
<head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
</head>
<body>
	<div id="page-learner">
			<h1 class="no-tabs-below">
				<fmt:message key="page.title.monitoring.view.reflection"/>
			</h1>
			<div id="header-no-tabs-learner"></div>
			<div id="content-learner">
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
		<div id="footer-learner">
		</div>
		<!--closes footer-->
	</div><!--closes page-->
</body>
</html:html>
