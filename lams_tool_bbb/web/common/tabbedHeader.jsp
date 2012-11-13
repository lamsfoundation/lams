<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:headItems />
	<tiles:insert attribute="headItems" />
</lams:head>
