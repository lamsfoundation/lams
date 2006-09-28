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
	<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css/>	
	<tiles:insert attribute="headItems" />
</head>
