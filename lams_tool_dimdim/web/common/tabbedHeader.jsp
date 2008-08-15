<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<link href="${tool}pages/learning/dimdim_style.css" rel="stylesheet"
		type="text/css">
	<lams:headItems />
	<tiles:insert attribute="headItems" />
</lams:head>
