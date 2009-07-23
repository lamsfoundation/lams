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
	<link href="${tool}pages/learning/notebook_style.css" rel="stylesheet" type="text/css">
	<lams:css/>

	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
</lams:head>
