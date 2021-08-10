<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="dto" value="${mindmapDTO}" />

<lams:css suffix="jquery.jRating"/>
<link type="text/css" href="${lams}/css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
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
	}
	
	#gallery-walk-rating-table th {
		font-weight: bold;
		font-style: normal;
		text-align: center;
	}
	
	#gallery-walk-rating-table td {
		text-align: center;
	}
	
	#gallery-walk-rating-table th:first-child,
	#gallery-walk-rating-table td:first-child {
		text-align: right;
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
		toolContentID: '${param.toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};


	//var for jquery.jRating.js
	var pathToImageFolder = "${lams}images/css/",
		//vars for rating.js
		AVG_RATING_LABEL = '<fmt:message key="label.average.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param></fmt:message>',
		YOUR_RATING_LABEL = '<fmt:message key="label.your.rating"><fmt:param>@1@</fmt:param><fmt:param>@2@</fmt:param><fmt:param>@3@</fmt:param></fmt:message>',
		MAX_RATES = 0,
		MIN_RATES = 0,
		LAMS_URL = '${lams}',
		COUNT_RATED_ITEMS = true,
		ALLOW_RERATE = false;
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script> 
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}includes/javascript/portrait.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/rating.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
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
		if (!confirm('<fmt:message key="monitoring.summary.gallery.walk.start.confirm" />')) {
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
	
	function finishGalleryWalk(){
		if (!confirm('<fmt:message key="monitoring.summary.gallery.walk.finish.confirm" />')) {
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
		if (!confirm('<fmt:message key="monitoring.summary.gallery.walk.learner.edit.confirm" />')) {
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

<div class="panel">
	<h4>
	  <c:out value="${mindmapDTO.title}" escapeXml="true"/>
	</h4>
	
	<div class="instructions voffset5">
	  <c:out value="${mindmapDTO.instructions}" escapeXml="false"/>
	</div>
	
	<c:if test="${empty dto.sessionDTOs}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			<fmt:message key="label.nogroups" />
		</lams:Alert>
	</c:if>
</div>
	
<c:if test="${not empty dto.sessionDTOs and mindmapDTO.galleryWalkEnabled}">
	<div class="panel panel-default ${mindmapDTO.galleryWalkFinished and not mindmapDTO.galleryWalkReadOnly ? 'gallery-walk-panel-ratings' : ''}"
		 id="gallery-walk-panel">
	  <div class="panel-heading">
	    <h3 class="panel-title">
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
		</h3>
	  </div>
	  <div class="panel-body">
	   		<button id="gallery-walk-start" type="button"
			        class="btn btn-primary
			        	   ${not mindmapDTO.galleryWalkStarted and not mindmapDTO.galleryWalkFinished ? '' : 'hidden'}"
			        onClick="javascript:startGalleryWalk()">
				<fmt:message key="monitoring.summary.gallery.walk.start" /> 
			</button>
			
			<button id="gallery-walk-finish" type="button"
			        class="btn btn-primary ${mindmapDTO.galleryWalkStarted and not mindmapDTO.galleryWalkFinished ? '' : 'hidden'}"
			        onClick="javascript:finishGalleryWalk()">
				<fmt:message key="monitoring.summary.gallery.walk.finish" /> 
			</button>
			
			<br>
						
			<button id="gallery-walk-learner-edit" type="button"
			        class="btn btn-default ${not mindmapDTO.galleryWalkEditEnabled and (mindmapDTO.galleryWalkStarted or mindmapDTO.galleryWalkFinished) ? '' : 'hidden'}"
			        onClick="javascript:enableGalleryWalkLearnerEdit()">
				<fmt:message key="monitoring.summary.gallery.walk.learner.edit" /> 
			</button>
			
			<c:if test="${mindmapDTO.galleryWalkFinished and not mindmapDTO.galleryWalkReadOnly}">
				<h4 style="text-align: center"><fmt:message key="label.gallery.walk.ratings.header" /></h4>
				<table id="gallery-walk-rating-table" class="table table-hover table-condensed">
				  <thead class="thead-light">
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
											 isItemAuthoredByUser="true"
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

	<c:if test="${isGroupedActivity}">	
	    <div class="panel panel-default" >
	        <div class="panel-heading" id="heading${session.sessionID}">
	        	<span class="panel-title collapsable-icon-left">
	        		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${session.sessionID}" 
							aria-expanded="false" aria-controls="collapse${session.sessionID}" >
						<fmt:message key="heading.group" >
							<fmt:param><c:out value="${session.sessionName}" /></fmt:param>
						</fmt:message>
					</a>
				</span>
	        </div>
        
			<div id="collapse${session.sessionID}" class="panel-collapse collapse mindmap-collapse" 
        			role="tabpanel" aria-labelledby="heading${session.sessionID}">
	</c:if>
	
	<div class="loffset10">
		<fmt:message key="heading.totalLearners" />&nbsp;${session.numberOfLearners}
	</div>
	
	<c:if test="${dto.multiUserMode}">
		<iframe class="mindmap-frame"
				data-src='<c:url value="/learning/getGalleryWalkMindmap.do?allowPrinting=true&toolSessionID=${session.sessionID}"/>'>
		</iframe>
	</c:if>

	<c:if test="${not dto.multiUserMode or dto.reflectOnActivity == true}">
		<table class="table table-striped table-condensed voffset10">
	
			<tr>
				<th width="40%">
					<fmt:message key="heading.learner" />
				</th>
				<c:if test="${not dto.multiUserMode}">
					<th width="40%">
						<fmt:message key="heading.mindmapEntry" />
					</th>
				</c:if>
				<c:if test="${dto.reflectOnActivity == true}">
				<th>
					<fmt:message key="label.notebookEntry" />
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
								<a href="reflect.do?userUID=${user.uid}&toolContentID=${dto.toolContentId}">
									<fmt:message key="label.view" />
								</a>
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
										<a href="showMindmap.do?allowPrinting=true&userUID=${user.uid}&toolContentID=${dto.toolContentId}&toolSessionID=${session.sessionID}">
											<fmt:message key="label.view" />
										</a>									
									</c:otherwise>
								</c:choose>
							</td>
							
							<c:if test="${dto.reflectOnActivity}">
							<td>
								<a href="reflect.do?userUID=${user.uid}&toolContentID=${dto.toolContentId}"><fmt:message key="label.view" />	</a>
							</td>
							</c:if>
							
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
	${ !isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>

<c:if test="${isGroupedActivity}">
	</div> <!--  end panel group -->
</c:if>

<%@ include file="advanceOptions.jsp"%>

<%@include file="dateRestriction.jsp"%>

