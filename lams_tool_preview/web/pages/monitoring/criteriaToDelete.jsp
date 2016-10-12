<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionId" value="${sessionMap.toolSessionId}" />
<c:set var="peerreview" value="${sessionMap.peerreview}" />

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="<html:rewrite page='/includes/css/learning.css'/>">

</lams:head>
<body class="stripes">

	<lams:Page type="monitor" title="${criteriaRatings.ratingCriteria.title}">

${criteriaRatings}

	<lams:StyledRating criteriaRatings="${criteriaRatings}" showJustification="false" alwaysShowAverage="true" currentUserDisplay="true"/>

	<span onclick="window.close()" class="btn btn-default voffset5 pull-right">Close</span>

	</lams:Page>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
