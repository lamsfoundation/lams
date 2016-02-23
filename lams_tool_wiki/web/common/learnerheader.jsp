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
	<link href="${tool}/includes/css/wiki_style.css" rel="stylesheet" type="text/css">
	<lams:css/>

	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/wikiCommon.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
</lams:head>
