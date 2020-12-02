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
	<style>
	</style>

	<script type="text/javascript" src="${lams}includes/javascript/etherpad.js"></script>
	<script type="text/javascript">
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
						<c:out value="${groupSummary.sessionName}" />
					</a>
				</span>
		       </div>
		       
		       <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse etherpad-collapse" 
		       	 role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
					
					<lams:Etherpad groupId="${groupSummary.sessionId}" padId="${groupSummary.readOnlyPadId}"
								   showControls="${hasEditRight}" showOnDemand="true" height="600" />	
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
