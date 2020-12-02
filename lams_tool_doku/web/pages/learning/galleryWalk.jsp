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
	</script>
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
</lams:Page>
</body>
</lams:html>
