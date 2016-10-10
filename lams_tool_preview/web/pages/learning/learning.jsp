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
<c:set var="isComment" value="<%=RatingCriteria.RATING_STYLE_COMMENT%>"/>
<c:set var="isStar" value="<%=RatingCriteria.RATING_STYLE_STAR%>"/>
<c:set var="isRanking" value="<%=RatingCriteria.RATING_STYLE_RANKING%>"/>
<c:set var="isHedging" value="<%=RatingCriteria.RATING_STYLE_HEDGING%>"/>
<c:set var="numCriteria"  value="${sessionMap.numCriteria}" />

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
	
	<script type="text/javascript">

		function hideButtons() {
			$("#buttondiv").css("visibility", "hidden");
		}	
		function showButtons() {
			$("#buttondiv").css("visibility", "visible");
		}	

 		function finishSession(){
 			hideButtons();
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionId=${toolSessionId}"/>';
			return false;
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
/*
		function onRatingErrorCallback() {
			alert('<fmt:message key="error.max.ratings.per.user"/>');
			refresh();
		}
 */
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
			<p><fmt:message key="label.step"><fmt:param>${criteriaRatings.ratingCriteria.orderId}</fmt:param><fmt:param>${numCriteria}</fmt:param></fmt:message></p>
		</div>
			
		<div class="panel">
			<h4>${criteriaRatings.ratingCriteria.title}</h4>
		<c:choose>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isComment}">
			<%@ include file="comment.jsp" %>
		</c:when>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isStar}">
			<%@ include file="star.jsp" %>
		</c:when>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isRanking}">
			<c:choose>
			<c:when test="${criteriaRatings.ratingCriteria.maxRating == 0}">
			<%@ include file="rankall.jsp" %>
			</c:when>
			<c:otherwise>
			<%@ include file="ranking.jsp" %>
			</c:otherwise>
			</c:choose>		
		</c:when>
		<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isHedging}">
			<%@ include file="hedging.jsp" %>
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
		
		<div class="pull-right">		
		<c:if test="${criteriaRatings.ratingCriteria.orderId > 1}">
			<span id="prevButton" class="btn btn-default" onclick="javascript:${method}(false);"><fmt:message key="label.previous"/></span>
		</c:if>
		<c:choose>
			<c:when test="${criteriaRatings.ratingCriteria.orderId == numCriteria}">
				<span id="finishButton" class="btn btn-primary" onclick="javascript:${method}(true);">${finishButtonLabel}</span>
			</c:when>
			<c:otherwise>
				<span id="nextButton" class="btn btn-default" onclick="javascript:${method}(true);"><fmt:message key="label.next"/></span>
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
