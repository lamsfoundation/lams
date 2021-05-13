<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="whiteboard" value="${sessionMap.whiteboard}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	
	<%@ include file="/common/header.jsp"%>
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
							
		.whiteboard-frame {
			width: 100%;
			height: 700px;
			border: 1px solid #c1c1c1;
		}
	</style>

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
			
	    // avoid name clash between bootstrap and jQuery UI
	    $.fn.bootstrapTooltip = $.fn.tooltip.noConflict();
		
	    $(document).ready(function(){
			$('[data-toggle="tooltip"]').bootstrapTooltip();
			
			// show Whiteboards only on Group expand
			$('.whiteboard-collapse').on('show.bs.collapse', function(){
				var whiteboard = $('.whiteboard-frame', this);
				if (whiteboard.data('src')) {
					whiteboard.attr('src', whiteboard.data('src'));
					whiteboard.data('src', null);
				}
			});
		});
		
		function finishSession(){
			document.getElementById("finish-button").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
	</script>
	<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
	
	<%@ include file="websocket.jsp"%>	
</lams:head>
<body class="stripes">

<lams:Page type="learner" title="${whiteboard.title}" style="">

	<lams:errors/>
	
	<p><c:out value="${whiteboard.instructions}" escapeXml="false" /></p>
	
	<c:if test="${not empty whiteboard.galleryWalkInstructions}">
		<hr>
		<p><c:out value="${whiteboard.galleryWalkInstructions}" escapeXml="false" /></p>
	</c:if>
		
	<c:if test="${whiteboard.galleryWalkFinished and not whiteboard.galleryWalkReadOnly}">
		<h4 class="voffset20" style="text-align: center"><fmt:message key="label.gallery.walk.ratings.header" /></h4>
		<table id="gallery-walk-rating-table" class="table table-hover table-condensed">
		  <thead class="thead-light">
		    <tr>
		      <th scope="col"><fmt:message key="monitoring.label.group" /></th>
		      <th scope="col"><fmt:message key="label.rating" /></th>
		    </tr>
		  </thead>
		  <tbody>
			<c:forEach var="groupSummary" items="${summaryList}">
				<tr>
					<td>${groupSummary.sessionName}</td>
					<td>
						<lams:Rating itemRatingDto="${groupSummary.itemRatingDto}" 
									 isItemAuthoredByUser="true"
									 hideCriteriaTitle="true" />
					</td>
				</tr>
			</c:forEach>
		  </tbody>
		</table>
	</c:if>
	
	<h4 class="voffset20" style="text-align: center"><fmt:message key="label.gallery.walk" /></h4>
	
	<c:if test="${mode == 'author'}">
		<div class="row no-gutter" id="gallery-walk-preview-info">
			<div class="col-xs-12 col-sm-offset-2 col-sm-8">
				<div class="alert alert-info leader-display">
					<fmt:message key="label.gallery.walk.preview" />
				</div>
			</div>
		</div>
	</c:if>
	

	<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
	    <div class="panel panel-default">
	       <div class="panel-heading" role="tab" id="heading${groupSummary.sessionId}">
	       	<span class="panel-title collapsable-icon-left">
	       		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}" 
						aria-expanded="false" aria-controls="collapse${groupSummary.sessionId}">
					<c:choose>
						<c:when test="${toolSessionID == groupSummary.sessionId}">
							<b><c:out value="${groupSummary.sessionName}" />&nbsp;<fmt:message key="label.gallery.walk.your.group" /></b>
						</c:when>
						<c:otherwise>
							<c:out value="${groupSummary.sessionName}" />
						</c:otherwise>
					</c:choose>
				</a>
			</span>
	       </div>
	       <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse whiteboard-collapse" 
	       	    role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
				<%-- Do not show rating to own group before Gallery Walk is finished --%>
	       	    <c:if test="${not whiteboard.galleryWalkReadOnly and (whiteboard.galleryWalkFinished or mode == 'teacher' or toolSessionID != groupSummary.sessionId)}">
	       	    	<div class="gallery-walk-rating-comment-container">
	       	    		<lams:Rating itemRatingDto="${groupSummary.itemRatingDto}"
								     isItemAuthoredByUser="${whiteboard.galleryWalkFinished or not hasEditRight or mode == 'teacher'}" />
					 </div>
	       	    </c:if>
	 
				<iframe class="whiteboard-frame"
						data-src='${whiteboardServerUrl}/?whiteboardid=${groupSummary.wid}&username=${whiteboardAuthorName}${empty groupSummary.accessToken ? "" : "&accesstoken=".concat(groupSummary.accessToken)}&copyfromwid=${whiteboard.contentId}${empty whiteboardCopyAccessToken ? "" : "&copyaccesstoken=".concat(groupSummary.copyAccessToken)}'>
				</iframe>	
			</div>
		</div>
	</c:forEach>
	
	<c:if test="${mode != 'teacher'}">
		<div>
			<c:choose>
				<c:when test="${not whiteboard.galleryWalkFinished}">
					<button data-toggle="tooltip" 
							class="btn btn-default voffset5 pull-right ${mode == 'author' ? '' : 'disabled'}"
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
					<button name="FinishButton" id="finish-button"
							onclick="return continueReflect()" class="btn btn-default voffset5 pull-right">
						<fmt:message key="label.continue" />
					</button>
				</c:when>
				<c:otherwise>
					<a href="#nogo" name="FinishButton" id="finish-button"
							onclick="return finishSession()" class="btn btn-primary voffset5 pull-right na">
						<span class="nextActivity">
							<c:choose>
			 					<c:when test="${sessionMap.isLastActivity}">
			 						<fmt:message key="label.submit" />
			 					</c:when>
			 					<c:otherwise>
			 		 				<fmt:message key="label.finished" />
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
