<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
	
<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	
	<lams:css/>
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
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	<script type="text/javascript">
			//var for jquery.jRating.js
		var pathToImageFolder = "${lams}images/css/",
			//vars for rating.js
			AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
			YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
			MAX_RATES = 0,
			MIN_RATES = 0,
			LAMS_URL = '${lams}',
			COUNT_RATED_ITEMS = true,
			COMMENTS_MIN_WORDS_LIMIT = 0,
			COMMENT_TEXTAREA_TIP_LABEL = '<fmt:message key="label.comment.textarea.tip"/>',
			WARN_COMMENTS_IS_BLANK_LABEL = '<fmt:message key="warning.comment.blank"/>',
			ALLOW_RERATE = true,
			SESSION_ID = ${toolSessionID};
			
		checkNextGateActivity('finish-button', '${toolSessionID}', '', finishSession);
			
	    $(document).ready(function(){
			$('[data-bs-toggle="tooltip"]').bootstrapTooltip();
			
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
</lams:head>
<body class="stripes">

<lams:Page type="learner" title="${mindmapDTO.title}" style="">

	<lams:errors/>
	
	<p><c:out value="${mindmapDTO.instructions}" escapeXml="false" /></p>
	
	<c:if test="${not empty mindmapDTO.galleryWalkInstructions}">
		<hr>
		<p><c:out value="${mindmapDTO.galleryWalkInstructions}" escapeXml="false" /></p>
	</c:if>
		
	<c:if test="${mindmapDTO.galleryWalkFinished and not mindmapDTO.galleryWalkReadOnly}">
		<h4 class="text-center mt-4">
			<fmt:message key="label.gallery.walk.ratings.header" />
		</h4>
		<table id="gallery-walk-rating-table" class="table table-hover table-sm">
		  <thead class="table-light">
		    <tr>
		      <th scope="col"><fmt:message key="monitoring.label.group" /></th>
		      <th scope="col"><fmt:message key="label.rating" /></th>
		    </tr>
		  </thead>
		  <tbody>
			<c:forEach var="mindmapSession" items="${mindmapDTO.sessionDTOs}">
				<tr>
					<td>${mindmapSession.sessionName}</td>
					<td>
						<lams:Rating5 itemRatingDto="${mindmapSession.itemRatingDto}" 
									 isItemAuthoredByUser="true"
									 hideCriteriaTitle="true" />
					</td>
				</tr>
			</c:forEach>
		  </tbody>
		</table>
	</c:if>
	
	<h4 class="text-center mt-4">
		<fmt:message key="label.gallery.walk" />
	</h4>
	
	<c:if test="${mode == 'author'}">
		<lams:Alert5 type="info" id="gallery-walk-preview-info" close="false">
			<fmt:message key="label.gallery.walk.preview" />
		</lams:Alert5>
	</c:if>

	<c:forEach var="mindmapSession" items="${mindmapDTO.sessionDTOs}" varStatus="status">
	    <div class="panel panel-default">
	       <div class="panel-heading" role="tab" id="heading${mindmapSession.sessionID}">
	       	<span class="panel-title collapsable-icon-left">
	       		<button type="button" class="btn btn-secondary collapsed" data-bs-toggle="collapse" data-bs-target="#collapse${mindmapSession.sessionID}" 
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
	       
	       <div id="collapse${mindmapSession.sessionID}" class="panel-collapse collapse mindmap-collapse" 
	       	    role="tabpanel" aria-labelledby="heading${mindmapSession.sessionID}">
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
					<button data-bs-toggle="tooltip" 
							class="btn btn-primary ${mode == 'author' ? '' : 'disabled'}"
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
					<button name="FinishButton" id="finish-button"
							onclick="return continueReflect()" class="btn btn-primary">
						<fmt:message key="button.continue" />
					</button>
				</c:when>
				<c:otherwise>
					<a href="#nogo" name="FinishButton" id="finish-button" class="btn btn-primary na">
						<span class="nextActivity">
							<c:choose>
			 					<c:when test="${sessionMap.isLastActivity}">
			 						<fmt:message key="button.submit" />
			 					</c:when>
			 					<c:otherwise>
			 		 				<fmt:message key="button.finish" />
			 					</c:otherwise>
			 				</c:choose>
						</span>
					</a>
				</c:otherwise>
			</c:choose>
		</div>
	</c:if>
</lams:Page>
</body>
</lams:html>