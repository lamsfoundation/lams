<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<head>
	<title>
		<bean:message key="activity.display.name" />
	</title>
	
	<lams:css />
	<link href="${tool}includes/css/fckeditor_style.css" rel="stylesheet" type="text/css">	
	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/fckcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/tabcontroller.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/xmlrequest.js"></script>
	
	<tiles:insert attribute="script" />
</head>
