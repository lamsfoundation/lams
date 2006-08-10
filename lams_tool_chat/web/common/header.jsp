<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<head>
	<title>
		<fmt:message key="activity.title" />
	</title>
	<link href="${tool}pages/learning/chat_style.css" rel="stylesheet" type="text/css">
	<lams:headItems />
	<tiles:insert attribute="headItems" />
</head>
