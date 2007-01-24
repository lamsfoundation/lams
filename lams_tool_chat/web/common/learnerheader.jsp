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
	<c:choose>
		<c:when test="${page_direction != 'RTL'}">
			<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		</c:when>
		<c:otherwise>
			<link href="${tool}includes/css/chat_rtl.css" rel="stylesheet" type="text/css">
		</c:otherwise>
	</c:choose>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css/>	
	<tiles:insert attribute="headItems" />
</head>
