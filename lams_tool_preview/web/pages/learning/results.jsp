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
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme-blue.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css">
	<link rel="stylesheet" href="<html:rewrite page='/includes/css/learning.css'/>">
	<style media="screen,projection" type="text/css">
		.tablesorter {width: 80%;}
	</style>

	<script type="text/javascript">
		//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/";
		
		//vars for rating.js
		var MAX_RATES = ${peerreview.maximumRates},
		MIN_RATES = ${peerreview.minimumRates},
		COMMENTS_MIN_WORDS_LIMIT = ${sessionMap.commentsMinWordsLimit},
		MAX_RATINGS_FOR_ITEM = ${peerreview.maximumRatesPerUser},
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = ${sessionMap.countRatedItems},
		COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
		WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="warning.comment.blank"/>',
		WARN_MIN_NUMBER_WORDS_LABEL = "<fmt:message key="warning.minimum.number.words"><fmt:param value="${sessionMap.commentsMinWordsLimit}"/></fmt:message>";
	</script>
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.jRating.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/jquery.tablesorter-pager.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/common.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/rating.js" type="text/javascript" ></script>	
	<script type="text/javascript">
	
		$(document).ready(function(){
			
			$(".tablesorter").tablesorter({
				theme: 'blue',
			    widthFixed: true,
			    widgets: ['zebra'],
		        headers: { 
		            1: { 
		                sorter: false 
		            }, 
		            2: {
		                sorter: false 
		            } 
		        } 
			});
			
			//initializeJRating();
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
				<fmt:message key="message.activityLocked" />
			</lams:Alert>
		</c:if>
		
		<c:if test="${not empty itemRatingDtos}">

		<!-- Ratings UI -->
		<div class="panel panel-default">
		<div class="panel-heading panel-title">
			<fmt:message key="label.ratings" />
		</div>
		<div class="panel-body">
	
		<table class="tablesorter">
			<thead>
				<tr>
					<th title="<fmt:message key='label.sort.by.user.name'/>" >
						<fmt:message key="label.user.name" />
					</th>
					<th>
						<fmt:message key="label.rating" />
					</th>
	
			<table class="tablesorter">
				<thead>
					<tr>
						<th title="<fmt:message key='label.sort.by.user.name'/>" >
							<fmt:message key="label.user.name" />
						</th>
						<th>
							<fmt:message key="label.rating" />
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="itemRatingDto" items="${itemRatingDtos}">
						<tr>
							<td>
								<c:out value="${userNameMap[itemRatingDto.itemId]}" escapeXml="true"/>
							</td>
							
							<td style="width:50%; text-align: right;">
								<lams:Rating itemRatingDto="${itemRatingDto}" disabled="true" isItemAuthoredByUser="false"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		</div>
		</c:if>
		
		<c:if test="${peerreview.showRatingsLeftForUser}">
		<div class="panel panel-default">
		<div class="panel-heading panel-title">
			<fmt:message key="label.ratings.by.others" />
		</div>
		<div class="panel-body">
			<lams:Rating itemRatingDto="${itemRatingDto}" disabled="true" isItemAuthoredByUser="true"/>
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
				<c:choose>			
					<c:when test="${sessionMap.reflectOn && (not sessionMap.isSessionCompleted)}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="btn btn-primary voffset5 pull-right">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:link href="#nogo" property="FinishButton" styleId="finishButton" onclick="return finishSession()" styleClass="btn btn-primary voffset5 pull-right na">
								<c:choose>
				 					<c:when test="${sessionMap.activityPosition.last}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
				 				</c:choose>
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
