<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
	
<lams:PageLearner title="${mindmapDTO.title}" toolSessionID="${toolSessionID}" >
	
	<lams:css suffix="jquery.jRating"/>
	<style>
		#gallery-walk-rating-table {
			width: 60%;
			margin: 50px auto;
		}
		
		.gallery-walk-rating-comment-container {
			width: 60%;
			margin: auto;
		}
		
		.gallery-walk-rating-comment-container textarea {
			margin-bottom: 20px;
		}
		
		#gallery-walk-preview-info {
			margin-bottom: 20px;
		}
							
		.mindmap-frame {
			width: 100%;
			height: 700px;
			border: none;
		}
	</style>
	
	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript">
			//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/",
			//vars for rating.js
			AVG_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message></spring:escapeBody>',
			YOUR_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message></spring:escapeBody>',
			MAX_RATES = 0,
			MIN_RATES = 0,
			COUNT_RATED_ITEMS = true,
			COMMENTS_MIN_WORDS_LIMIT = 0,
			COMMENT_TEXTAREA_TIP_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.comment.textarea.tip"/></spring:escapeBody>',
			WARN_COMMENTS_IS_BLANK_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="warning.comment.blank"/></spring:escapeBody>',
			ALLOW_RERATE = true,
			SESSION_ID = ${toolSessionID};
			
		checkNextGateActivity('finish-button', '${toolSessionID}', '', finishSession);

		$(document).ready(function(){
			$('[data-bs-toggle="tooltip"]').each((i, el) => {
				new bootstrap.Tooltip($(el))
			});
			
			// show mindmaps only on Group expand
			$('.mindmap-collapse').on('show.bs.collapse', function(){
				var mindmap = $('.mindmap-frame', this);
				if (mindmap.data('src')) {
					mindmap.attr('src', mindmap.data('src'));
					mindmap.data('src', null);
				}
			});
		});
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finishActivity.do?toolSessionID=${toolSessionID}"/>';
		}
		
		function continueReflect(){
			document.location.href='<c:url value="/learning/reflect.do?toolSessionID=${toolSessionID}&userUid=${userUid}"/>';
		}
	</script>
	<lams:JSImport src="includes/javascript/rating.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>	
	<%@ include file="websocket.jsp"%>	

<div id="container-main">
	<lams:errors5/>
	
	<div id="instructions" class="instructions">
		<c:out value="${mindmapDTO.instructions}" escapeXml="false" />
		
		<c:if test="${not empty mindmapDTO.galleryWalkInstructions}">
			<div class="mt-3">
				<c:out value="${mindmapDTO.galleryWalkInstructions}" escapeXml="false" />
			</div>
		</c:if>
	</div>

	<c:if test="${mindmapDTO.galleryWalkFinished and not mindmapDTO.galleryWalkReadOnly}">
		<div class="text-center card-subheader my-3">
			<fmt:message key="label.gallery.walk.ratings.header" />
		</div>
		
		<div id="gallery-walk-rating-table" class="ltable table-sm">
			<div class="row">
		    	<div class="col"><fmt:message key="monitoring.label.group" /></div>
		    	<div class="col text-center"><fmt:message key="label.rating" /></div>
			</div>
		  
			<c:forEach var="mindmapSession" items="${mindmapDTO.sessionDTOs}">
				<div class="row">
					<div class="col">
						${mindmapSession.sessionName}
					</div>
					<div class="col">
						<lams:Rating5 itemRatingDto="${mindmapSession.itemRatingDto}" 
									 isItemAuthoredByUser="true"
									 hideCriteriaTitle="true" />
					</div>
				</div>
			</c:forEach>
		</div>
	</c:if>
	
	<div class="text-center card-subheader my-3">
		<fmt:message key="label.gallery.walk" />
	</div>
	
	<c:if test="${mode == 'author'}">
		<lams:Alert5 type="info" id="gallery-walk-preview-info" close="false">
			<fmt:message key="label.gallery.walk.preview" />
		</lams:Alert5>
	</c:if>

	<c:forEach var="mindmapSession" items="${mindmapDTO.sessionDTOs}" varStatus="status">
	    <div class="card lcard">
	       <div class="card-header" id="heading${mindmapSession.sessionID}">
	       	<span class="card-title collapsable-icon-left">
	       		<button type="button" class="btn btn-secondary-darker no-shadow collapsed" data-bs-toggle="collapse" data-bs-target="#collapse${mindmapSession.sessionID}" 
						aria-expanded="false" aria-controls="collapse${mindmapSession.sessionID}"
				>
					<c:choose>
						<c:when test="${toolSessionID == mindmapSession.sessionID}">
							<b><c:out value="${mindmapSession.sessionName}" />&nbsp;<fmt:message key="label.gallery.walk.your.group" /></b>
						</c:when>
						<c:otherwise>
							<c:out value="${mindmapSession.sessionName}" />
						</c:otherwise>
					</c:choose>
				</button>
			</span>
	       </div>
	       
	       <div id="collapse${mindmapSession.sessionID}" class="card-body collapse mindmap-collapse" 
	       	    aria-labelledby="heading${mindmapSession.sessionID}">
				<%-- Do not show rating to own group before Gallery Walk is finished --%>
	       	    <c:if test="${not mindmapDTO.galleryWalkReadOnly and (mindmapDTO.galleryWalkFinished or mode == 'teacher' or toolSessionID != mindmapSession.sessionID)}">
	       	    	<div class="gallery-walk-rating-comment-container">
	       	    		<lams:Rating5 itemRatingDto="${mindmapSession.itemRatingDto}"
								     isItemAuthoredByUser="${mindmapDTO.galleryWalkFinished or mode == 'teacher'}" />
					 </div>
	       	    </c:if>
	 			
				<iframe class="mindmap-frame"
						data-src='<c:url value="/learning/getGalleryWalkMindmap.do?toolSessionID=${mindmapSession.sessionID}"/>'>
				</iframe>
			</div>
		</div>
	</c:forEach>
	
	<c:if test="${mode != 'teacher'}">
		<div class="activity-bottom-buttons">
			<c:choose>
				<c:when test="${not mindmapDTO.galleryWalkFinished}">
					<button type="button" data-bs-toggle="tooltip" 
							class="btn btn-primary na ${mode == 'author' ? '' : 'disabled'}"
							<c:choose>
								<c:when test="${mode == 'author'}">
									title="<fmt:message key='label.gallery.walk.wait.finish.preview' />"
									onClick="javascript:location.href = location.href + '&galleryWalk=forceFinish'"
								</c:when>
								<c:otherwise>
									title="<fmt:message key='label.gallery.walk.wait.finish' />"
								</c:otherwise>
							</c:choose>
					>
						<fmt:message key="button.continue" />
					</button>
				</c:when>
				
				<c:when test="${reflectOnActivity and not finishedActivity}">
					<button type="button" name="FinishButton" id="finish-button"
							onclick="return continueReflect()" class="btn btn-primary na">
						<fmt:message key="button.continue" />
					</button>
				</c:when>
				
				<c:otherwise>
					<button type="button" name="FinishButton" id="finish-button" class="btn btn-primary na">
						<c:choose>
			 				<c:when test="${sessionMap.isLastActivity}">
			 					<fmt:message key="button.submit" />
			 				</c:when>
			 				<c:otherwise>
			 		 			<fmt:message key="button.finish" />
			 				</c:otherwise>
			 			</c:choose>
					</button>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
</div>
</lams:PageLearner>