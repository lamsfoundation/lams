<!DOCTYPE html>>

<%@ include file="/common/taglibs.jsp"%>

<%@ taglib uri="tags-tiles" prefix="tiles"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		
		<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
		<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />

		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.mobile.js"></script>
	</lams:head>
	<body class="large-font">
		<tiles:insert attribute="bodyMobile" />
	</body>
</lams:html>
