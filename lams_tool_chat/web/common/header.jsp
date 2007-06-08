<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:head>
	<title>
		<fmt:message key="activity.title" />
	</title>
	<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
	<lams:headItems />
	<tiles:insert attribute="headItems" />
</lams:head>
