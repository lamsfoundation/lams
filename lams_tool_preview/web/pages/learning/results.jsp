<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionId" value="${sessionMap.toolSessionId}" />
<c:set var="peerreview" value="${sessionMap.peerreview}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="isCommentsEnabled" value="${sessionMap.isCommentsEnabled}" />
<c:set var="finishImmediately" value="${peerreview.lockWhenFinished and not peerreview.showRatingsLeftByUser and not peerreview.showRatingsLeftForUser and not peerreview.reflectOnActivity}" />
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<lams:css suffix="jquery.jRating"/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>includes/css/learning.css">

	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
		
		//vars for rating.js
		var MAX_RATES = 0,
		MIN_RATES = 0,
		COMMENTS_MIN_WORDS_LIMIT = 0,
		MAX_RATINGS_FOR_ITEM = 0,
		LIMIT_BY_CRITERIA = "true";
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = 0,
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="warning.comment.blank"/>',
		WARN_MIN_NUMBER_WORDS_LABEL = "<fmt:message key="warning.minimum.number.words"><fmt:param value="${criteriaRatings.ratingCriteria.commentsMinWordsLimit}"/></fmt:message>";
	</script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/common.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script>	
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionId}', '', finishSession);
	
		$(document).ready(function(){
			var finishImmediately = ${finishImmediately};

			if (finishImmediately) {
				$('#finishButton').click();
				return;
			}
		
			$(".tablesorter").tablesorter({
				theme: 'bootstrap',
			    widthFixed: true,
			    sortInitialOrder: 'desc',
			    headerTemplate : '{content} {icon}',
			    widgets: ['uitheme', 'zebra'],
		        headers: { 
		            1: { 
		                sorter: false 
		            }, 
		            2: {
		                sorter: false 
		            } 
		        } 
			});
			initializeJRating();
		 });
	
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionId=${toolSessionId}"/>';
		}
		
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function redoRatings(){
			document.location.href='<c:url value="/learning/start.do?toolSessionID=${toolSessionId}&mode=${mode}&isRedo=true"/>';
		}

    </script>
</lams:head>
<body class="stripes">

	<lams:Page type="learner" title="${peerreview.title}">

	<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
		<lams:Alert type="danger" id="warn-lock" close="false">
			<c:choose>
				<c:when test="${sessionMap.userFinished}">
					<fmt:message key="message.activityLocked"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" ><fmt:param><fmt:message key="label.finished" /></fmt:param></fmt:message>
				</c:otherwise>
			</c:choose>
		</lams:Alert>
	</c:if>

	<c:if test="${peerreview.showRatingsLeftByUser}">

		<c:if test="${not empty allCriteriaRatings}">
		<c:forEach var="criteriaRatings" items="${allCriteriaRatings}" varStatus="status">
			<div class="panel panel-default">
			<c:if test="${not criteriaRatings.ratingCriteria.rubricsStyleRating}">
				<div class="panel-heading panel-title">
					<c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/>
				</div>
			</c:if>
			<div class="panel-body">
			<lams:StyledRating criteriaRatings="${criteriaRatings}" showJustification="true" alwaysShowAverage="false"
							   currentUserDisplay="false" rubricsInBetweenColumns="${peerreview.rubricsInBetweenColumns}"
							   rubricsPivotView="${peerreview.rubricsView eq 2}" />
			</div>
			</div>
		</c:forEach>
		</c:if>
	</c:if>
	
	<c:if test="${peerreview.showRatingsLeftForUser}">

		<div class="panel panel-default">
		<div class="panel-heading panel-title">
			<fmt:message key="label.ratings.by.others" />
		</div>
		<div class="panel-body">
		
			<lams:Alert type="info" id="warn-lock" close="false">
				<fmt:message key="label.no.ratings.out.of.possible.ratings" ><fmt:param>${numberRatings}</fmt:param><fmt:param>${numberPotentialRatings}</fmt:param></fmt:message>
			</lams:Alert>
		
			<c:forEach var="criteriaRatings" items="${userRatings}" varStatus="status">
				<c:if test="${not criteriaRatings.ratingCriteria.rubricsStyleRating}">
					<h4><c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/></h4>
				</c:if>
		 		<lams:StyledRating criteriaRatings="${criteriaRatings}" showJustification="false" alwaysShowAverage="true"
		 						   currentUserDisplay="true" rubricsInBetweenColumns="${peerreview.rubricsInBetweenColumns}" />
			</c:forEach>
		</div>
		</div>
	</c:if>
				
	<!-- Reflection -->
	<c:if test="${sessionMap.reflectOn and not empty sessionMap.reflectEntry}">
		<%@ include file="notebookdisplay.jsp"%>
	</c:if>
	<!-- End Reflection -->

	<c:if test="${!peerreview.lockWhenFinished}">
		<a href="#nogo" class="btn btn-default voffset5 float-start voffset20" onclick="redoRatings();">
			<fmt:message key="label.redo" />
		</a>
	</c:if>	

	<c:if test="${mode != 'teacher'}">
		<div class="activity-bottom-buttons">
			<c:choose>			
				<c:when test="${sessionMap.reflectOn and empty sessionMap.reflectEntry}">
					<a href="#nogo" id="continueButton" onclick="return continueReflect()" class="btn btn-default">
						<fmt:message key="label.continue" />
					</a>
				</c:when>
				<c:when test="${sessionMap.isLastActivity}">
					<a href="#nogo" id="finishButton" class="btn btn-primary na">
						<fmt:message key="label.finish" />
					</a>
				</c:when>		
				<c:otherwise>
					<a href="#nogo" id="finishButton" class="btn btn-primary na">
						<fmt:message key="label.finished" />
					</a>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>

	</lams:Page>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
