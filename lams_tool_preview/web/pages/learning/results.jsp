<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionId" value="${sessionMap.toolSessionId}" />
<c:set var="peerreview" value="${sessionMap.peerreview}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="isCommentsEnabled" value="${sessionMap.isCommentsEnabled}" />
<c:set var="finishImmediately" value="${peerreview.lockWhenFinished and not peerreview.showRatingsLeftByUser and not peerreview.showRatingsLeftForUser}" />

<lams:PageLearner title="${peerreview.title}" toolSessionID="${toolSessionId}">
	<!-- ********************  CSS ********************** -->
	<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager5.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css">
	<link rel="stylesheet" href="<lams:WebAppURL/>includes/css/learning.css">

	<!-- ********************  javascript ********************** -->
	<script type="text/javascript">
		//vars for rating.js
		var MAX_RATES = 0,
		MIN_RATES = 0,
		COMMENTS_MIN_WORDS_LIMIT = 0,
		MAX_RATINGS_FOR_ITEM = 0,
		LIMIT_BY_CRITERIA = "true";
		COUNT_RATED_ITEMS = 0,
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="warning.comment.blank"/>',
		WARN_MIN_NUMBER_WORDS_LABEL = "<fmt:message key="warning.minimum.number.words"><fmt:param value="${criteriaRatings.ratingCriteria.commentsMinWordsLimit}"/></fmt:message>";
	</script>
	<lams:JSImport src="includes/javascript/jquery.tablesorter.js" />
	<lams:JSImport src="includes/javascript/jquery.tablesorter-widgets.js" />
	<lams:JSImport src="includes/javascript/jquery.tablesorter-pager.js" />
	<lams:JSImport src="includes/javascript/common.js" />
	<lams:JSImport src="includes/javascript/rating5.js" />
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
			initializeStarability();
		 });
	
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionId=${toolSessionId}"/>';
		}
		
		function redoRatings(){
			document.location.href='<c:url value="/learning/start.do?toolSessionID=${toolSessionId}&mode=${mode}&isRedo=true"/>';
		}
    </script>

<div id="container-main">
	<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
		<lams:Alert5 type="danger" id="warn-lock" close="false">
			<c:choose>
				<c:when test="${sessionMap.userFinished}">
					<fmt:message key="message.activityLocked"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="message.warnLockOnFinish" ><fmt:param><fmt:message key="label.finished" /></fmt:param></fmt:message>
				</c:otherwise>
			</c:choose>
		</lams:Alert5>
	</c:if>

	<c:if test="${peerreview.showRatingsLeftByUser and not empty allCriteriaRatings}">
		<c:forEach var="criteriaRatings" items="${allCriteriaRatings}" varStatus="status">
			<div class="card lcard">
				<c:if test="${not criteriaRatings.ratingCriteria.rubricsStyleRating}">
					<div class="card-header">
						<c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/>
					</div>
				</c:if>
				
				<lams:StyledRating criteriaRatings="${criteriaRatings}" showJustification="true" alwaysShowAverage="false"
					   currentUserDisplay="false" rubricsInBetweenColumns="${peerreview.rubricsInBetweenColumns}"
					   rubricsPivotView="${peerreview.rubricsView eq 2}" />
			</div>
		</c:forEach>
	</c:if>
	
	<c:if test="${peerreview.showRatingsLeftForUser}">
		<div class="card lcard">
			<div class="card-header">
				<fmt:message key="label.ratings.by.others" />
			</div>
			
			<div class="card-body">
				<lams:Alert5 type="info" id="warn-lock" close="false">
					<fmt:message key="label.no.ratings.out.of.possible.ratings" ><fmt:param>${numberRatings}</fmt:param><fmt:param>${numberPotentialRatings}</fmt:param></fmt:message>
				</lams:Alert5>

				<c:forEach var="criteriaRatings" items="${userRatings}" varStatus="status">
					<div class="card mb-3">
					<c:if test="${not criteriaRatings.ratingCriteria.rubricsStyleRating}">
						<div class="card-header">
							<c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/>
						</div>
					</c:if>
					
					<div class="card-body">
			 		<lams:StyledRating criteriaRatings="${criteriaRatings}" showJustification="false" alwaysShowAverage="true"
			 						   currentUserDisplay="true" rubricsInBetweenColumns="${peerreview.rubricsInBetweenColumns}" />
			 		</div>
			 		</div>
				</c:forEach>
			</div>
		</div>
	</c:if>

	<c:if test="${mode != 'teacher'}">
		<div class="activity-bottom-buttons">
			<c:choose>
				<c:when test="${sessionMap.isLastActivity}">
					<button type="button" id="finishButton" class="btn btn-primary na">
						<fmt:message key="label.finish" />
					</button>
				</c:when>		
				<c:otherwise>
					<button type="button" id="finishButton" class="btn btn-primary na">
						<fmt:message key="label.finished" />
					</button>
				</c:otherwise>
			</c:choose>

			<c:if test="${!peerreview.lockWhenFinished}">
				<button type="button" class="btn btn-secondary btn-icon-return me-2" onclick="redoRatings();">
					<fmt:message key="label.redo" />
				</button>
			</c:if>	
		</div>
	</c:if>
	
</div>
</lams:PageLearner>
