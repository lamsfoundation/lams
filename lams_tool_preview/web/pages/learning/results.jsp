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

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<link type="text/css" href="${lams}css/jquery.jRating.css" rel="stylesheet"/>
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="<html:rewrite page='/includes/css/learning.css'/>">

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
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-widgets.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/common.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script>	
	<script type="text/javascript">
	
		$(document).ready(function(){
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
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionId=${toolSessionId}"/>';
			return false;
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
			<div class="panel-heading panel-title">
				<c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/>
			</div>
			<div class="panel-body">
			<lams:StyledRating criteriaRatings="${criteriaRatings}" showJustification="true" alwaysShowAverage="false" currentUserDisplay="false" />
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
				<h4><c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/></h4>
		 		<lams:StyledRating criteriaRatings="${criteriaRatings}" showJustification="false" alwaysShowAverage="true" currentUserDisplay="true"/>
			</c:forEach>
		</div>
		</div>
	</c:if>
				
	<!-- Reflection -->
	<c:if test="${sessionMap.isSessionCompleted and sessionMap.reflectOn}">
		<%@ include file="notebookdisplay.jsp"%>
	</c:if>
	<!-- End Reflection -->

	<c:if test="${!peerreview.lockWhenFinished}">
		<html:button property="redoRatings" styleClass="btn btn-default voffset5 pull-left" onclick="redoRatings();">
			<fmt:message key="label.redo" />
		</html:button>
	</c:if>	

	<c:if test="${mode != 'teacher'}">
		<div>
			<c:choose>			
				<c:when test="${(not sessionMap.isSessionCompleted) and sessionMap.reflectOn}">
					<html:button property="FinishButton" onclick="return continueReflect()" styleClass="btn btn-default voffset5 pull-right">
						<fmt:message key="label.continue" />
					</html:button>
				</c:when>
				<c:otherwise>
					<html:link href="#nogo" property="FinishButton" styleId="finishButton" onclick="return finishSession()" styleClass="btn btn-primary voffset5 pull-right na">
						<fmt:message key="label.finished" />
					</html:link>
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
