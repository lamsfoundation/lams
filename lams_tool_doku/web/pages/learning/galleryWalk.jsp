<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />
<c:set var="hasEditRight" value="${sessionMap.hasEditRight}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	
	<%@ include file="/common/header.jsp"%>
	<lams:css suffix="jquery.jRating"/>
	
	<style>
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
			LAMS_URL = '${lams}',
			COUNT_RATED_ITEMS = true,
			ALLOW_RERATE = true,
			SESSION_ID = ${toolSessionID};

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

<lams:Page type="learner" title="${dokumaran.title}" style="">

	<lams:errors/>
	
	<c:out value="${dokumaran.description}" escapeXml="false" />
	
	<div id="doku-group-panels" class="panel-group" role="tablist" aria-multiselectable="true"> 
		<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
		
		    <div class="panel panel-default doku-group-panel">
		       <div class="panel-heading" role="tab" id="heading${groupSummary.sessionId}">
		       	<span class="panel-title collapsable-icon-left">
		       		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}" 
							aria-expanded="false" aria-controls="collapse${groupSummary.sessionId}" data-parent="#doku-group-panels">
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
		       <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse etherpad-collapse" 
		       	    role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
					<%-- Do not show rating to own group before Gallery Walk is finished --%>
		       	    <c:if test="${not dokumaran.galleryWalkReadOnly and (dokumaran.galleryWalkFinished or mode == 'teacher' or toolSessionID != groupSummary.sessionId)}">
		       	    	<lams:Rating itemRatingDto="${groupSummary.itemRatingDto}"
								     isItemAuthoredByUser="${dokumaran.galleryWalkFinished or not hasEditRight or mode == 'teacher'}" />
		       	    </c:if>
		 
					<lams:Etherpad groupId="${groupSummary.sessionId}" padId="${groupSummary.readOnlyPadId}"
								   showControls="${not dokumaran.galleryWalkFinished and not dokumaran.galleryWalkReadOnly and hasEditRight}"
								   showOnDemand="true" height="600" />	
				</div>
			</div>
		</c:forEach>
	</div>
	
	<c:if test="${mode != 'teacher' and dokumaran.galleryWalkFinished}">
		<div>
			<c:choose>
				<c:when test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
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
