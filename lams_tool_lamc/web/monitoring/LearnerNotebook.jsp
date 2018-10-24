<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css/>
	<title><fmt:message key="activity.title" /></title>
	
</lams:head>

<body class="stripes">
	
<c:set var="title"><fmt:message key="label.view.reflection"/></c:set>
<lams:Page title="${title}" type="learner">

	<h4><c:out value="${mcGeneralLearnerFlowDTO.userName}" escapeXml="true"/></h4>

	<p><lams:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeHtml="true"/></p>

	<a href="javascript:window.close();" class="btn btn-default">
		<fmt:message key="label.close"/>
	</a>
		
	<div id="footer"></div>

</lams:Page>
</body>
</lams:html>
