<%@ include file="/common/taglibs.jsp"%>
<c:set var="dto" value="${mindmapDTO}" />

<link href="${lams}css/rating.css" rel="stylesheet" type="text/css">
<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme5.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">
<style>
	#gallery-walk-panel {
		width: 30%;
		margin: auto;
		margin-bottom: 20px;
		text-align: center;
	}
	
	#gallery-walk-panel.gallery-walk-panel-ratings {
		width: 100%;
	}
	
	#gallery-walk-learner-edit {
		margin-top: 20px;
		margin-bottom: 20px;
	}

	#gallery-walk-skip {
		margin-top: 20px;
	}
	
	#gallery-walk-rating-table th {
		font-weight: bold;
		font-style: normal;
		text-align: center;
	}
	
	#gallery-walk-rating-table td {
		text-align: center;
	}
								
	.mindmap-frame {
		width: calc(100% - 35px);
		margin-left: 17px;
		height: 700px;
		border: none;
	}
</style>

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		submissionDateString: '${submissionDateString}',
		setSubmissionDeadlineUrl: '<c:url value="setSubmissionDeadline.do"/>?<csrf:token/>',
		toolContentID: '<c:out value="${param.toolContentID}" />',
		messageNotification: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.notification" /></spring:escapeBody>',
		messageRestrictionSet: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.set" /></spring:escapeBody>',
		messageRestrictionRemoved: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitor.summary.date.restriction.removed" /></spring:escapeBody>'
	};

	//vars for rating.js
	var	AVG_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message></spring:escapeBody>',
		YOUR_RATING_LABEL = '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message></spring:escapeBody>',
		MAX_RATES = 0,
		MIN_RATES = 0,
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = true,
		ALLOW_RERATE = false;
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script> 
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<lams:JSImport src="includes/javascript/portrait5.js" />
<lams:JSImport src="includes/javascript/rating.js" />

<script type="text/javascript">
	$(document).ready(function(){
		doStatistic();
		initializePortraitPopover("<lams:LAMSURL />");

		// show mindmaps only on Group expand
		$('.mindmap-collapse').on('show.bs.collapse', function(){
			var mindmap = $('.mindmap-frame', this);
			if (mindmap.data('src')) {
				mindmap.attr('src', mindmap.data('src'));
				mindmap.data('src', null);
			}
		});
	});
	
	var evalcomixWindow = null;
	function openEvalcomixWindow(url) {
    	evalcomixWindow=window.open(url, 'evalcomixWindow', 'width=1152,height=648,scrollbars=yes,resizable=yes');
		if (window.focus) {evalcomixWindow.focus()}
	}

	function startGalleryWalk(){
		if (!confirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitoring.summary.gallery.walk.start.confirm" /></spring:escapeBody>')) {
			return;
		}
		
		$.ajax({
			'url' : '<c:url value="/monitoring/startGalleryWalk.do"/>',
			'data': {
				toolContentID : ${dto.toolContentId}
			},
			'success' : function(){
				location.reload();
			}
		});
	}

	function skipGalleryWalk(){
		if (!confirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitoring.summary.gallery.walk.skip.confirm" /></spring:escapeBody>')) {
			return;
		}

		$.ajax({
			'url' : '<c:url value="/monitoring/skipGalleryWalk.do"/>',
			'data': {
				toolContentID : ${dto.toolContentId}
			},
			'success' : function(){
				location.reload();
			}
		});
	}

	function finishGalleryWalk(){
		if (!confirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitoring.summary.gallery.walk.finish.confirm" /></spring:escapeBody>')) {
			return;
		}
		
		$.ajax({
			'url' : '<c:url value="/monitoring/finishGalleryWalk.do"/>',
			'data': {
				toolContentID : ${dto.toolContentId}
			},
			'success' : function(){
				location.reload();
			}
		});
	}

	function enableGalleryWalkLearnerEdit(){
		if (!confirm('<spring:escapeBody javaScriptEscape="true"><fmt:message key="monitoring.summary.gallery.walk.learner.edit.confirm" /></spring:escapeBody>')) {
			return;
		}
		
		$.ajax({
			'url' : '<c:url value="/monitoring/enableGalleryWalkLearnerEdit.do"/>',
			'data': {
				toolContentID : ${dto.toolContentId}
			},
			'success' : function(){
				location.reload();
			}
		});
	}
</script>

<c:if test="${empty dto.sessionDTOs}">
	<lams:Alert5 type="info" id="no-session-summary">
		<fmt:message key="label.nogroups" />
	</lams:Alert5>
</c:if>

<h1>
  <c:out value="${mindmapDTO.title}" escapeXml="true"/>
</h1>
	
<div class="instructions voffset5">
  <c:out value="${mindmapDTO.instructions}" escapeXml="false"/>
</div>
	
<c:if test="${not empty dto.sessionDTOs and mindmapDTO.galleryWalkEnabled}">
	<div class="card shadow mb-5 ${mindmapDTO.galleryWalkFinished and not mindmapDTO.galleryWalkReadOnly ? 'gallery-walk-panel-ratings' : ''}"
			id="gallery-walk-panel">
		<div class="card-header">
			<fmt:message key="label.gallery.walk" />&nbsp;
			
			<b>
				<c:choose>
					<c:when test="${not mindmapDTO.galleryWalkStarted and not mindmapDTO.galleryWalkFinished}">
						<fmt:message key="label.gallery.walk.state.not.started" />
					</c:when>
					<c:when test="${mindmapDTO.galleryWalkStarted and not mindmapDTO.galleryWalkFinished}">
						<fmt:message key="label.gallery.walk.state.started" />
					</c:when>
					<c:when test="${mindmapDTO.galleryWalkFinished}">
						<fmt:message key="label.gallery.walk.state.finished" />
					</c:when>
				</c:choose>
				<c:if test="${mindmapDTO.galleryWalkEditEnabled}">
					<fmt:message key="label.gallery.walk.state.learner.edit.enabled" />
				</c:if>
			</b>
		</div>

		<div class="card-body">
			<button id="gallery-walk-start" type="button"
			        class="btn btn-primary ${not mindmapDTO.galleryWalkStarted and not mindmapDTO.galleryWalkFinished ? '' : 'd-none'}"
			        onClick="startGalleryWalk()">
			    <i class="fa-solid fa-play me-1"></i>
				<fmt:message key="monitoring.summary.gallery.walk.start" /> 
			</button>
			
			<button id="gallery-walk-finish" type="button"
			        class="btn btn-primary ${mindmapDTO.galleryWalkStarted and not mindmapDTO.galleryWalkFinished ? '' : 'd-none'}"
			        onClick="finishGalleryWalk()">
				<i class="fa-regular fa-circle-stop me-1"></i>
				<fmt:message key="monitoring.summary.gallery.walk.finish" /> 
			</button>
			
			<br>

			<button id="gallery-walk-skip" type="button"
					  class="btn btn-light ${not mindmapDTO.galleryWalkStarted and not mindmapDTO.galleryWalkFinished ? '' : 'd-none'}"
					  onClick="skipGalleryWalk()">
				<i class="fa-solid fa-forward me-1"></i>
				<fmt:message key="monitoring.summary.gallery.walk.skip" />
			</button>

			<button id="gallery-walk-learner-edit" type="button"
			        class="btn btn-light ${not mindmapDTO.galleryWalkEditEnabled and (mindmapDTO.galleryWalkStarted or mindmapDTO.galleryWalkFinished) ? '' : 'd-none'}"
			        onClick="enableGalleryWalkLearnerEdit()">
			    <i class="fa-regular fa-pen-to-square me-1"></i>
				<fmt:message key="monitoring.summary.gallery.walk.learner.edit" /> 
			</button>
			
			<c:if test="${mindmapDTO.galleryWalkFinished and not mindmapDTO.galleryWalkReadOnly}">
				<h4 style="text-align: center">
					<fmt:message key="label.gallery.walk.ratings.header" />
				</h4>
				
				<table id="gallery-walk-rating-table" class="table table-hover table-condensed mb-0">
				  <thead class="text-bg-light">
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
								<lams:Rating itemRatingDto="${mindmapSession.itemRatingDto}" 
											 isDisplayOnly="true"
											 hideCriteriaTitle="true" />
							</td>
						</tr>
					</c:forEach>
				  </tbody>
				</table>
			</c:if>
	  </div>
	</div>
</c:if>

<c:if test="${isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="session" items="${dto.sessionDTOs}" varStatus="status">
	<c:set var="totalLearnersHtml">
		<div class="badge text-bg-light m-2 float-end">
			<fmt:message key="heading.totalLearners" />&nbsp;${session.numberOfLearners}
		</div>
	</c:set>

	<c:choose>
		<c:when test="${isGroupedActivity}">	
		    <div class="lcard" >
		        <div class="card-header" id="heading${session.sessionID}">
					${totalLearnersHtml}
		
		        	<span class="card-title collapsable-icon-left">
		        		<button class="btn btn-secondary-darker no-shadow collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${session.sessionID}" 
								aria-expanded="false" aria-controls="collapse${session.sessionID}" >
							<fmt:message key="heading.group" >
								<fmt:param><c:out value="${session.sessionName}" /></fmt:param>
							</fmt:message>
						</button>
					</span>
		        </div>
	        
				<div id="collapse${session.sessionID}" class="card-collapse collapse mindmap-collapse">
		</c:when>
		<c:otherwise>
			${totalLearnersHtml}
		</c:otherwise>
	</c:choose>
	
	<c:if test="${dto.multiUserMode}">
		<iframe class="mindmap-frame ${isGroupedActivity ? '' : 'shadow'} p-1"
			<c:choose>
				<c:when test="${isGroupedActivity}">
					<%-- iframe is loaded on collapse panel open --%>
					data-src
				</c:when>
				<c:otherwise>
					<%-- iframe is loaded on page open --%>
					src
				</c:otherwise>
			</c:choose>
			='<c:url value="/learning/getGalleryWalkMindmap.do?allowPrinting=true&toolSessionID=${session.sessionID}"/>'>
		</iframe>
	</c:if>

	<c:if test="${not dto.multiUserMode}">
		<table class="table table-striped table-condensed mt-2">
			<tr>
				<th width="40%">
					<fmt:message key="heading.learner" />
				</th>
				
				<c:if test="${not dto.multiUserMode}">
					<th width="40%">
						<fmt:message key="heading.mindmapEntry" />
					</th>
				</c:if>
			</tr>
	
			<c:choose>
				<c:when test="${dto.multiUserMode}">
					<c:forEach var="user" items="${session.userDTOs}">
						<tr>
							<td>
								<lams:Portrait userId="${user.userId}" hover="true"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/></lams:Portrait>
							</td>
					
							<td width="30%">
							</td>
						</tr>
					</c:forEach>
				</c:when>
				
				<c:otherwise>
					<c:forEach var="user" items="${session.userDTOs}">
						<tr>
							<td>
								<lams:Portrait userId="${user.userId}" hover="true"><c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/></lams:Portrait>
							</td>
							
							<td>
								<c:choose>
									<c:when test="${user.finishedActivity != true}">
										<fmt:message key="label.notAvailable" />
									</c:when>
									
									<c:otherwise>
										<a href="showMindmap.do?allowPrinting=true&userUID=${user.uid}&toolContentID=${dto.toolContentId}&toolSessionID=${session.sessionID}"
												class="btn btn-sm btn-light">
											<i class="fa-regular fa-eye me-1"></i>
											<fmt:message key="label.view" />
										</a>									
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
	</c:if>
	
	<c:if test="${isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
</c:forEach>

<c:if test="${isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

<h2 class="card-subheader fs-4 mt-3" id="header-statistics">
	<fmt:message key="button.statistics" />
</h2>
<%@ include file="statistics.jsp"%>

<h2 class="card-subheader fs-4" id="header-settings">
	Settings
</h2>
<%@ include file="editActivity.jsp"%>
<lams:RestrictedUsageAccordian submissionDeadline="${submissionDeadline}"/>
