<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>

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
		<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
		<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />

		<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script src="${lams}includes/javascript/jquery.js"></script>
		<script src="${lams}includes/javascript/jquery.mobile.js"></script>		
		
	</lams:head>
	<body class="large-font">
		<div data-role="page" data-dom-cache="false">
			<tiles:insert attribute="bodyMobile" />
		</div>
	</body>
</lams:html>
