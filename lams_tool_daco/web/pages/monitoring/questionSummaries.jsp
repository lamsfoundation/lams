<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>

<lams:html>
<lams:head>
		<%@ include file="/common/header.jsp"%>
		<title>
			<fmt:message key="label.common.summary" />
		</title>
</lams:head> 
<body class="stripes">
<c:set var="title"><fmt:message key="label.common.summary" /></c:set>
<lams:Page type="monitor" title="${title}">

	<c:if test="${not empty param.includeMode}">
		<c:set var="includeMode" value="${param.includeMode}" />
	</c:if>
	<c:if test="${empty includeMode}">
		<c:set var="includeMode" value="monitoring" />
	</c:if>
	<%@ include file="/pages/learning/questionSummaries.jsp" %>

	<div id="footer">
	</div>

</lams:Page>
</body>
</lams:html>
	