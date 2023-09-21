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
<c:set var="displayFinalFinishButton" value="${not peerreview.showRatingsLeftByUser and not peerreview.showRatingsLeftForUser and not peerreview.reflectOnActivity and stepNumber >= numCriteria}" />
<c:set var="isComment" value="<%=RatingCriteria.RATING_STYLE_COMMENT%>"/>
<c:set var="isStar" value="<%=RatingCriteria.RATING_STYLE_STAR%>"/>
<c:set var="isRanking" value="<%=RatingCriteria.RATING_STYLE_RANKING%>"/>
<c:set var="isHedging" value="<%=RatingCriteria.RATING_STYLE_HEDGING%>"/>
<c:set var="isRubrics" value="<%=RatingCriteria.RATING_STYLE_RUBRICS%>"/>
<c:choose>
	<c:when test="${peerreview.showRatingsLeftForUser || peerreview.reflectOnActivity}">
		<c:set var="finishButtonLabel"><fmt:message key="label.continue" /></c:set>
	</c:when>	
	<c:when test="${sessionMap.isLastActivity}">
		<c:set var="finishButtonLabel"><fmt:message key="label.finish" /></c:set>
	</c:when>					
	<c:otherwise>
		<c:set var="finishButtonLabel"><fmt:message key="label.finished" /></c:set>
	</c:otherwise>
</c:choose>

<lams:PageLearner title="${peerreview.title}" toolSessionID="${toolSessionId}">
	<!-- ********************  CSS ********************** -->
	<link rel="stylesheet" href="<lams:WebAppURL/>/includes/css/learning.css">
	
	<!-- ********************  javascript ********************** -->
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript">
		function hideButtons() {
			$("#buttonNextPrevDiv button").prop('disabled', true);
		}	
		function showButtons() {
			$("#buttonNextPrevDiv button").prop('disabled', false);
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

	<div class="container-lg">
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert5 type="danger" id="warn-lock" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked"/>
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" ><fmt:param>${finishButtonLabel}</fmt:param></fmt:message>
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>

		<div id="instructions" class="instructions">
			<c:out value="${peerreview.instructions}" escapeXml="false" />

			<c:if test="${numCriteria > 1}">
				<div>
					<fmt:message key="label.step">
						<fmt:param>${stepNumber}</fmt:param>
						<fmt:param>${numCriteria}</fmt:param>
					</fmt:message>
				</div>
			</c:if>
		</div>
		
			<c:choose>
				<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isComment}">					
					<%@ include file="comment.jsp"%>
				</c:when>
				
				<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isStar}">
					<%@ include file="star.jsp"%>
				</c:when>
				
				<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isRanking}">
					<c:choose>
						<c:when test="${rateAllUsers > 0}">
							<%@ include file="rankall.jsp"%>
						</c:when>
						<c:otherwise>
							<%@ include file="ranking.jsp"%>
						</c:otherwise>
					</c:choose>
				</c:when>
				
				<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isHedging}">
					<%@ include file="hedging.jsp"%>
				</c:when>
				
				<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == isRubrics}">
					<c:choose>
						<c:when test="${peerreview.rubricsView eq 2}">
							<%@ include file="rubricsPivot.jsp"%>
						</c:when>
						<c:otherwise>
							<%@ include file="rubrics.jsp"%>
						</c:otherwise>
					</c:choose>
				</c:when>
			</c:choose>

		<div class="activity-bottom-buttons">
			<div id="buttonNextPrevDiv">
				<c:choose>	
					<c:when test="${finishedLock || mode == 'teacher'}">
						<c:set var="method">nextPrev</c:set>
					</c:when>
					<c:otherwise>
						<c:set var="method">submitEntry</c:set>
					</c:otherwise>
				</c:choose>
				
				<c:if test="${stepNumber > 1}">
					<button type="button" id="prevButton" class="btn btn-secondary btn-icon-previous me-1" onclick="javascript:${method}(false);">
						<fmt:message key="label.previous"/>
					</button>
				</c:if>
				
				<c:choose>
					<c:when test="${stepNumber == numCriteria}">
						<button type="button" id="finishButton" class="btn btn-primary btn-icon-next" onclick="javascript:${method}(true);">
							${finishButtonLabel}
						</button>
					</c:when>
					<c:otherwise>
						<button type="button" id="finishButton" class="btn btn-primary btn-icon-next" onclick="javascript:${method}(true);">
							<fmt:message key="label.next"/>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
						
			<button type="button" id="refreshButton" class="btn btn-secondary btn-icon-refresh me-4" onclick="javascript:refresh();">
				<fmt:message key="label.refresh"/>
			</button>
		</div>			
	
	</div>
</lams:PageLearner>
