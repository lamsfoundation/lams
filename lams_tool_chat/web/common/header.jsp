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
	<lams:headItems />
	<tiles:insert attribute="script" />
</head>
