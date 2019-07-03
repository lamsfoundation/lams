<!DOCTYPE html>
        
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<lams:html>
<lams:head>
	<title><fmt:message key="label.monitoring.heading" /></title>
	<%@ include file="/common/header.jsp"%>

	<lams:css suffix="jquery.jRating"/>
	<link type="text/css" href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link type="text/css" href="<lams:WebAppURL/>includes/css/learning.css" rel="stylesheet" >
	<style>
		.tablediv {
		    padding: 5px;
		}
	</style>

	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
			
		//vars for rating.js
		var MAX_RATES = 0,
		MIN_RATES = 0,
		COMMENTS_MIN_WORDS_LIMIT = 0,
		MAX_RATINGS_FOR_ITEM = 0,
		LAMS_URL = '',
		COUNT_RATED_ITEMS = 0,
		COMMENT_TEXTAREA_TIP_LABEL = '',
		WARN_COMMENTS_IS_BLANK_LABEL = '',
		WARN_MIN_NUMBER_WORDS_LABEL = '';
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
</lams:head>

<body class="stripes">
	<lams:Page type="monitor" title="${criteria.title}">
		<%@ include file="criteriapart.jsp" %>
		
 		<span onclick="window.close()" class="btn btn-default voffset20 pull-right">
 			<fmt:message key="label.close"/>
 		</span>
	</lams:Page>

	<div id="footer"></div>
</body>
</lams:html>