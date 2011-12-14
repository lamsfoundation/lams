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
	
	<lams:css/>
	<link href="${tool}pages/learning/bbb_style.css" rel="stylesheet" type="text/css">
	
	<%-- TODO is this the best place to import these scripts ?	--%>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	
</lams:head>
