<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.rating.model.RatingCriteria" %>

<c:set var="lams"><lams:LAMSURL/></c:set>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="peerreview" value="${sessionMap.peerreview}" />
<c:set var="toolSessionId" value="${sessionMap.toolSessionId}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="userId" value="${sessionMap.user.userId }"/>
<c:set var="displayFinalFinishButton" value="${not peerreview.showRatingsLeftForUser and not peerreview.reflectOnActivity and stepNumber >= numCriteria}" />
<c:set var="isComment" value="<%=RatingCriteria.RATING_STYLE_COMMENT%>"/>
<c:set var="isStar" value="<%=RatingCriteria.RATING_STYLE_STAR%>"/>
<c:set var="isRanking" value="<%=RatingCriteria.RATING_STYLE_RANKING%>"/>
<c:set var="isHedging" value="<%=RatingCriteria.RATING_STYLE_HEDGING%>"/>
<c:set var="isRubrics" value="<%=RatingCriteria.RATING_STYLE_RUBRICS%>"/>

<c:choose>
	<c:when test="${peerreview.showRatingsLeftForUser || peerreview.reflectOnActivity}">
		<c:set var="finishButtonLabel"><fmt:message key="label.continue" /></c:set>
	</c:when>				
	<c:otherwise>
		<c:set var="finishButtonLabel"><fmt:message key="label.finished" /></c:set>
	</c:otherwise>
</c:choose>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	
	<lams:css/>
	<link rel="stylesheet" href="<lams:WebAppURL/>/includes/css/learning.css">
	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script src="${lams}includes/javascript/jquery.js" type="text/javascript"></script>
	<script src="${lams}includes/javascript/bootstrap.min.js" type="text/javascript"></script>
	<script src="${lams}learning/includes/javascript/gate-check.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		<c:if test="${displayFinalFinishButton}">
			checkNextGateActivity('finishButton', '${toolSessionId}', '', finishSession);
		</c:if>
		
		function hideButtons() {
			$("#buttonNextPrevDiv").css("visibility", "hidden");
		}	
		function showButtons() {
			$("#buttonNextPrevDiv").css("visibility", "visible");
		}	

 		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionId=${toolSessionId}"/>';
		}

		function showResults(){
			hideButtons();
			document.location.href ='<c:url value="/learning/showResults.do?sessionMapID=${sessionMapID}"/>';
			return false;
		}
		
		function continueReflect(){
			hideButtons();
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function refresh(page){
			hideButtons();
			document.location.href='<c:url value="/learning/refresh.do?sessionMapID=${sessionMapID}&criteriaId=${criteriaRatings.ratingCriteria.ratingCriteriaId}"/>';
		}
		function nextprev(next){
			hideButtons();
			document.location.href='<c:url value="/learning/nextPrev.do?sessionMapID=${sessionMapID}&criteriaId=${criteriaRatings.ratingCriteria.ratingCriteriaId}&next='+next+'"/>';
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
						<fmt:message key="message.warnLockOnFinish" ><fmt:param>${finishButtonLabel}</fmt:param></fmt:message>
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>

		<div class="panel">
			<p><c:out value="${peerreview.instructions}" escapeXml="false"/><p>
			<c:if test="${numCriteria > 1}">
				<p><fmt:message key="label.step"><fmt:param>${stepNumber}</fmt:param><fmt:param>${numCriteria}</fmt:param></fmt:message></p>
		 	</c:if> 
		</div>
		<div class="panel">
		<c:choose>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isComment}">
			<h4><c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/></h4>
			<%@ include file="comment.jsp" %>
		</c:when>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isStar}">
			<h4><c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/></h4>
			<%@ include file="star.jsp" %>
		</c:when>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isRanking}">
			<h4><c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/></h4>
			<c:choose>
			<c:when test="${rateAllUsers > 0}">
			<%@ include file="rankall.jsp" %>
			</c:when>
			<c:otherwise>
			<%@ include file="ranking.jsp" %>
			</c:otherwise>
			</c:choose>		
		</c:when>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isHedging}">
			<h4><c:out value="${criteriaRatings.ratingCriteria.title}" escapeXml="true"/></h4>
			<%@ include file="hedging.jsp" %>
		</c:when>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isRubrics}">
			<%@ include file="rubrics.jsp" %>
		</c:when>
		</c:choose>		
		</div>
	
		<c:choose>	
			<c:when test="${finishedLock || mode == 'teacher'}">
				<c:set var="method">nextPrev</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="method">submitEntry</c:set>
			</c:otherwise>
		</c:choose>

		<div class="voffset5" id="buttondiv">
		
		<div class=" pull-left">		
		<span id="refreshButton" class="btn btn-default" onclick="javascript:refresh();"><fmt:message key="label.refresh"/></span>
		</div>
		
		<div class="pull-right" id="buttonNextPrevDiv">		
		<c:if test="${stepNumber > 1}">
			<span id="prevButton" class="btn btn-default" onclick="javascript:${method}(false);"><fmt:message key="label.previous"/></span>
		</c:if>
		<c:choose>
			<c:when test="${displayFinalFinishButton}">
				<span id="finishButton" class="btn btn-primary">${finishButtonLabel}</span>
			</c:when>
			<c:when test="${stepNumber == numCriteria}">
				<span id="finishButton" class="btn btn-primary" onclick="javascript:${method}(true);">${finishButtonLabel}</span>
			</c:when>
			<c:otherwise>
				<span id="finishButton" class="btn btn-default" onclick="javascript:${method}(true);"><fmt:message key="label.next"/></span>
			</c:otherwise>
		</c:choose>
		</div>
		</div>				
	
	</lams:Page>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
