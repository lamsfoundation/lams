<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="whiteboard" value="${sessionMap.whiteboard}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	
<lams:PageLearner title="${whiteboard.title}" toolSessionID="${toolSessionID}">
	<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
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
							
		.whiteboard-frame {
			width: 100%;
			height: 700px;
			border: 1px solid #c1c1c1;
		}
		
		.full-screen-launch-button {
			margin-bottom: 5px;
			margin-top: 5px;
		}
		
		.full-screen-exit-button {
			display: none;
			margin-bottom: 5px;
		}
	
		.full-screen-content-div {
			clear: both;
		}
		
		.full-screen-content-div:fullscreen {
			padding: 20px 0 70px 0;
		}
		
		.full-screen-content-div:fullscreen .full-screen-flex-div,
		.full-screen-content-div:fullscreen .full-screen-main-div,
		.full-screen-content-div:fullscreen .whiteboard-frame {
			height: 100%;
			width: 100%;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/fullscreen.js"></script>
	<script type="text/javascript">
		//vars for rating.js
		var	AVG_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message></spring:escapeBody>',
			YOUR_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message></spring:escapeBody>',
			MAX_RATES = 0,
			MIN_RATES = 0,
			COUNT_RATED_ITEMS = true,
			COMMENTS_MIN_WORDS_LIMIT = 0,
			COMMENT_TEXTAREA_TIP_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.comment.textarea.tip"/></spring:escapeBody>',
			WARN_COMMENTS_IS_BLANK_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="warning.comment.blank"/></spring:escapeBody>',
			SESSION_ID = ${toolSessionID};
			
		checkNextGateActivity('finish-button', '${toolSessionID}', '', finishSession);
			
	    $(document).ready(function(){
			$('[data-bs-toggle="tooltip"]').each((i, el) => {
				new bootstrap.Tooltip($(el))
			});
			
			// show Whiteboards only on Group expand
			$('.whiteboard-collapse').on('show.bs.collapse', function(){
				var whiteboard = $('.whiteboard-frame', this);
				if (whiteboard.data('src')) {
					whiteboard.attr('src', whiteboard.data('src'));
					whiteboard.data('src', null);
				}
			});
			
			setupFullScreenEvents();
		});
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
		}
	</script>
	<lams:JSImport src="includes/javascript/rating5.js" />
	<%@ include file="websocket.jsp"%>	

<div id="container-main">
	<lams:errors5/>
	
	<div id="instructions" class="instructions">
		<c:out value="${whiteboard.instructions}" escapeXml="false" />
	
		<c:if test="${not empty whiteboard.galleryWalkInstructions}">
			<div class="mt-3">
				<c:out value="${whiteboard.galleryWalkInstructions}" escapeXml="false" />
			</div>
		</c:if>
	</div>
		
	<c:if test="${whiteboard.galleryWalkFinished and not whiteboard.galleryWalkReadOnly}">
		<div class="text-center card-subheader my-3">
			<fmt:message key="label.gallery.walk.ratings.header" />
		</div>
		
		<div id="gallery-walk-rating-table" class="ltable table-sm">
			<div class="row">
		    	<div class="col"><fmt:message key="monitoring.label.group" /></div>
		    	<div class="col text-center"><fmt:message key="label.rating" /></div>
		    </div>

			<c:forEach var="groupSummary" items="${summaryList}">
				<div class="row">
					<div class="col">
						${groupSummary.sessionName}
					</div>
					<div class="col">
						<lams:Rating5 itemRatingDto="${groupSummary.itemRatingDto}" 
								isDisplayOnly="true"
								hideCriteriaTitle="true" />
					</div>
				</div>
			</c:forEach>
		</div>
	</c:if>
	
	<div class="card-subheader text-center my-3">
		<fmt:message key="label.gallery.walk" />
	</div>
	
	<c:if test="${mode == 'author'}">
		<lams:Alert5 type="info" id="gallery-walk-preview-info" close="false">
			<fmt:message key="label.gallery.walk.preview" />
		</lams:Alert5>
	</c:if>

	<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
	    <div class="card lcard">
	       <div class="card-header">
	       	<span class="card-title collapsable-icon-left">
	       		<button type="button" class="btn btn-secondary-darker no-shadow collapsed" data-bs-toggle="collapse" data-bs-target="#collapse${groupSummary.sessionId}" 
						aria-expanded="false" aria-controls="collapse${groupSummary.sessionId}">
					<c:choose>
						<c:when test="${toolSessionID == groupSummary.sessionId}">
							<b><c:out value="${groupSummary.sessionName}" />&nbsp;<fmt:message key="label.gallery.walk.your.group" /></b>
						</c:when>
						<c:otherwise>
							<c:out value="${groupSummary.sessionName}" />
						</c:otherwise>
					</c:choose>
				</button>
			</span>
	       </div>
	       
	       <div id="collapse${groupSummary.sessionId}" class="card-body collapse whiteboard-collapse">
				<%-- Do not show rating to own group before Gallery Walk is finished --%>
	       	    <c:if test="${not whiteboard.galleryWalkReadOnly and (whiteboard.galleryWalkFinished or mode == 'teacher' or toolSessionID != groupSummary.sessionId)}">
	       	    	<div class="gallery-walk-rating-comment-container">
	       	    		<lams:Rating5 itemRatingDto="${groupSummary.itemRatingDto}"
								     isDisplayOnly="${whiteboard.galleryWalkFinished or not hasEditRight or mode == 'teacher'}" />
					 </div>
	       	    </c:if>
	 
			 	<div class="full-screen-content-div">
					<div class="full-screen-flex-div">
						<button type="button" class="btn btn-secondary float-end ms-1 full-screen-launch-button" onclick="javascript:launchIntoFullscreen(this)"
								title="<fmt:message key='label.fullscreen.open' />">
							<i class="fa-solid fa-maximize" aria-hidden="true"></i>
						</button>
				       	<button type="button" class="btn btn-secondary float-end ms-1 full-screen-exit-button" onclick="javascript:exitFullscreen()"
								title="<fmt:message key='label.fullscreen.close' />">
				       		<i class="fa fa-compress" aria-hidden="true"></i>
				       	</button>
				       	
				       	<div class="full-screen-main-div">
							<iframe class="whiteboard-frame" title="Whiteboard"
									data-src='${whiteboardServerUrl}/?whiteboardid=${groupSummary.wid}&username=${whiteboardAuthorName}${empty groupSummary.accessToken ? "" : "&accesstoken=".concat(groupSummary.accessToken)}&copyfromwid=${sourceWid}${empty whiteboardCopyAccessToken ? "" : "&copyaccesstoken=".concat(groupSummary.copyAccessToken)}'>
							</iframe>		
						</div>
					</div>
				</div>

			</div>
		</div>
	</c:forEach>
	
	<c:if test="${mode != 'teacher'}">
		<div class="activity-bottom-buttons">
			<c:choose>
				<c:when test="${not whiteboard.galleryWalkFinished}">
					<button type="button" data-bs-toggle="tooltip" class="btn btn-primary na ${mode == 'author' ? '' : 'disabled'}"
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
						<fmt:message key="label.continue" />
					</button>
				</c:when>
				
				<c:otherwise>
					<button type="button" name="FinishButton" id="finish-button" class="btn btn-primary na">
						<c:choose>
			 				<c:when test="${sessionMap.isLastActivity}">
			 					<fmt:message key="label.submit" />
			 				</c:when>
			 				<c:otherwise>
			 		 			<fmt:message key="label.finished" />
			 				</c:otherwise>
			 			</c:choose>
					</button>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
</div>
</lams:PageLearner>