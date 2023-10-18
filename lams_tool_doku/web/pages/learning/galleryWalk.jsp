<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	
<lams:PageLearner title="${dokumaran.title}" toolSessionID="${toolSessionID}">
	<lams:css suffix="jquery.jRating"/>
	<style>
		#gallery-walk-rating-table {
			width: 60%;
			margin: 50px auto;
			border-bottom: 1px solid #ddd;
		}
		
		#gallery-walk-rating-table th {
			font-weight: bold;
			font-style: normal;
			text-align: center;
		}
		
		#gallery-walk-rating-table td {
			text-align: center;
		}
		
		#gallery-walk-rating-table th:first-child, #gallery-walk-rating-table td:first-child {
			text-align: right;
		}
		
		#gallery-walk-preview-info {
			margin-bottom: 20px;
		}

		.comment-textarea {
			height: 100px !important;
		}
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/etherpad.js"></script>
	<script type="text/javascript">
			//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/",
			//vars for rating.js
			AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
			YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
			MAX_RATES = 0,
			MIN_RATES = 0,
			COMMENTS_MIN_WORDS_LIMIT = 0,
			COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip" />',
			WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="warning.comment.blank"/>',
			COUNT_RATED_ITEMS = true,
			ALLOW_RERATE = true,
			SESSION_ID = ${toolSessionID};
			
		checkNextGateActivity('finish-button', '${toolSessionID}', '', finishSession);
		
	    $(document).ready(function(){
			// show etherpads only on Group expand
			$('.etherpad-collapse').on('show.bs.collapse', function(){
				var etherpad = $('.etherpad-container', this);
				if (!etherpad.hasClass('initialised')) {
					var id = etherpad.attr('id'),
						groupId = id.substring('etherpad-container-'.length);
					etherpadInitMethods[groupId]();
				}
			});
			
			$('[data-bs-toggle="tooltip"]').each((i, el) => {
				new bootstrap.Tooltip($(el))
			});
		});
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
		}
		
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
	</script>
	<lams:JSImport src="includes/javascript/rating.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	<%@ include file="websocket.jsp"%>	

<div id="container-main">
	<lams:errors/>
	
	<div id="instructions" class="instructions">
		<c:out value="${dokumaran.description}" escapeXml="false" />
		
		<c:if test="${not empty dokumaran.galleryWalkInstructions}">
			<div class="card-subheader mt-3">
				<fmt:message key="label.gallery.walk.instructions.header" />
			</div>
			<div>
				<c:out value="${dokumaran.galleryWalkInstructions}" escapeXml="false" />
			</div>
		</c:if>
	</div>
		
	<c:if test="${dokumaran.galleryWalkFinished and not dokumaran.galleryWalkReadOnly}">
		<div class="text-center card-subheader my-3">
			<fmt:message key="label.gallery.walk.ratings.header" />
		</div>
		
		<div id="gallery-walk-rating-table" class="ltable table-sm">
			<div class="row">
		    	<div class="col"><fmt:message key="monitoring.label.group" /></div>
		    	<div class="col"><fmt:message key="label.rating" /></div>
		    </div>

			<c:forEach var="groupSummary" items="${summaryList}">
				<div class="row">
					<div class="col">
						${groupSummary.sessionName}
					</div>
					<div class="col">
						<lams:Rating5 itemRatingDto="${groupSummary.itemRatingDto}" 
									 isItemAuthoredByUser="true"
									 hideCriteriaTitle="true" />
					</div>
				</div>
			</c:forEach>
		  </tbody>
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
			<div class="card-header" id="heading${groupSummary.sessionId}">
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
			
			<div id="collapse${groupSummary.sessionId}" class="card-body collapse etherpad-collapse" 
		       	    aria-labelledby="heading${groupSummary.sessionId}">
				<%-- Do not show rating to own group before Gallery Walk is finished --%>
		       	<c:if test="${not dokumaran.galleryWalkReadOnly and (dokumaran.galleryWalkFinished or mode == 'teacher' or toolSessionID != groupSummary.sessionId)}">
		       	   	<lams:Rating5 itemRatingDto="${groupSummary.itemRatingDto}" 
		       	   			showComments="true"
							isItemAuthoredByUser="${dokumaran.galleryWalkFinished or not hasEditRight or mode == 'teacher'}" />
		       	</c:if>
		 
				<lams:Etherpad groupId="${groupSummary.sessionId}"
						padId="${hasEditRight and dokumaran.galleryWalkEditEnabled and toolSessionID == groupSummary.sessionId ? groupSummary.padId : groupSummary.readOnlyPadId}"
						showControls="${hasEditRight and ((dokumaran.galleryWalkEditEnabled and toolSessionID == groupSummary.sessionId) or (not dokumaran.galleryWalkFinished and not dokumaran.galleryWalkReadOnly))}"
						showOnDemand="true" 
						height="600" />	
			</div>
		</div>
	</c:forEach>
	
	<c:if test="${mode != 'teacher'}">
		<div class="activity-bottom-buttons">
			<c:choose>
				<c:when test="${not dokumaran.galleryWalkFinished}">
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
						<fmt:message key="label.continue" />
					</button>
				</c:when>
				
				<c:when test="${sessionMap.reflectOn and not sessionMap.userFinished}">
					<button type="button" name="FinishButton" id="finish-button"
							onclick="return continueReflect()" class="btn btn-primary na">
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