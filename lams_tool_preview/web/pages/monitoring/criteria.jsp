<!DOCTYPE html>
        
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>

<lams:html>
<lams:head>
	<title><fmt:message key="label.monitoring.heading" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<style media="screen,projection" type="text/css">
		 .ui-jqgrid {
			border-left-style: none !important;
			border-right-style: none !important;
			border-bottom-style: none !important;
		}
		
		.ui-jqgrid tr {
			border-left-style: none !important;
		}
		
		.ui-jqgrid td {
			border-style: none !important;
		}
	</style>

	<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
	<link type="text/css" href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
	<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
	<link rel="stylesheet" href="<html:rewrite page='/includes/css/learning.css'/>">

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
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script>

</lams:head>
<body class="stripes">

	<lams:Page type="monitor" title="${criteria.title}">
		<%@ include file="criteriapart.jsp" %>
 		<span onclick="window.close()" class="btn btn-default voffset5 pull-right"><fmt:message key="label.close"/></span>
	</lams:Page>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>