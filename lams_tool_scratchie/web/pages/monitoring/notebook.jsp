<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
        

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
</lams:head>
<body class="stripes">

	<c:set var="title"><fmt:message key="page.title.monitoring.view.reflection"/></c:set>
	<lams:Page type="learner" title="${title}">

		<h4>${userDTO.fullName}</h4>

		<p><lams:out value="${userDTO.reflectInstrctions}"/></p>

		<c:choose>
			<c:when test="${userDTO.finishReflection}">
				<p><lams:out value="${userDTO.reflect}"/></p>
			</c:when>
			<c:otherwise>
				<p><fmt:message key="message.no.reflection.available" /></p>
			</c:otherwise>
		</c:choose>

		<a href="javascript:window.close();" class="btn btn-default"><fmt:message key="button.close"/></a>

		<div id="footer"></div>
	</lams:Page>

</body>
</lams:html>
