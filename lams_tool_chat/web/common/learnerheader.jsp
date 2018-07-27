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
	<c:choose>
		<c:when test="${page_direction != 'RTL'}">
			<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		</c:when>
		<c:otherwise>
			<link href="${tool}includes/css/chat_rtl.css" rel="stylesheet" type="text/css">
		</c:otherwise>
	</c:choose>
	<lams:css/>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<tiles:insert attribute="headItems" />
</lams:head>
