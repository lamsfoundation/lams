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

		function clearMessage() {
			$("#messageArea2").html("");
			return false;
		}
		
		function closeResultsForLearner() {
			$("#emailPreviewArea").html("");
			$("#emailPreviewArea").hide();
			return false;
		}

		// Prview the email to be sent to the learner
		function previewResultsForLearner(sessionId, userId) {
			$(".btn-disable-on-submit").prop("disabled", true);
			var url = "<c:url value="/monitoring/previewResultsToUser.do"/>";
			clearMessage();
			$("#messageArea2_Busy").show();
			$("#emailPreviewArea").load(
				url,
				{
					sessionMapID: "${sessionMapID}",
					toolContentID: ${sessionMap.toolContentID},
					toolSessionId: sessionId, 
					userID: userId,
					reqID: (new Date()).getTime()
				},
				function() {
					$("#messageArea2_Busy").hide();
					$("#emailPreviewArea").show();
					$(".btn-disable-on-submit").prop("disabled", false);
				}
			);
			return false;
		}

		// Send the previewed email to the learner
		function sendResultsForLearner(sessionId, userId, dateTimeStamp) {
			$(".btn-disable-on-submit").prop("disabled", true);
			var url = "<c:url value="/monitoring/sendPreviewedResultsToUser.do"/>";
			clearMessage();
			$("#messageArea2_Busy").show();
			$("#messageArea2").load(
				url,
				{
					sessionMapID: "${sessionMapID}",
					toolContentID: ${sessionMap.toolContentID},
					toolSessionId: sessionId, 
					dateTimeStamp: dateTimeStamp,
					userID: userId,
					reqID: (new Date()).getTime()
				},
				function() {
					$("#messageArea2_Busy").hide();
					closeResultsForLearner();
					$(".btn-disable-on-submit").prop("disabled", false);
				}
			);
			return false;
		}
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
</lams:head>

<body class="stripes">
	<lams:Page type="monitor" title="${criteria.rubricsStyleRating ? '' : title}">
		<!--For send results feature-->
		<i class="fa fa-spinner" style="display:none" id="messageArea2_Busy"></i>
		<div class="voffset5" id="messageArea2"></div>

		<c:choose>
			<c:when test="${criteria.rubricsStyleRating}">
				<%@ include file="rubricspart.jsp" %>
			</c:when>
			<c:otherwise>
				<%@ include file="criteriapart.jsp" %>
			</c:otherwise>
		</c:choose>
		
		
		<div class="voffset10" id="emailPreviewArea" style="display:none" ></div>

 		<span onclick="window.close()" class="btn btn-default voffset20 pull-right">
 			<fmt:message key="label.close"/>
 		</span>
	</lams:Page>

	<div id="footer"></div>
</body>
</lams:html>