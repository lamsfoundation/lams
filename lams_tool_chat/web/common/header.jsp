<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<head>
	<title>
		<bean:message key="activity.title" />
	</title>
	<link href="${tool}chat_client/chat_style.css" rel="stylesheet" type="text/css">
	<lams:headItems />
	<tiles:insert attribute="headItems" />
</head>
