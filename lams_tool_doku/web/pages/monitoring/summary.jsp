<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="dokumaran" value="${sessionMap.dokumaran}" />
<%@ page import="org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants"%>

<script type="text/javascript" src="<html:rewrite page='/includes/javascript/etherpad.js'/>"></script>
<script type="text/javascript" src="${lams}includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
		
			$('#etherpad-container-${groupSummary.sessionId}').pad({
				'padId':'${groupSummary.padId}',
				'host':'${etherpadServerUrl}',
				//'lang':'',
				'showControls':'true',
				'showChat':'${dokumaran.showChat}',
				'showLineNumbers':'${dokumaran.showLineNumbers}',
				'height':'200',
				'userName':'<lams:user property="firstName" />&nbsp;<lams:user property="lastName" />'
			});
			
		</c:forEach>
		
	});

</script>

<div class="panel">
	<h4>
	    <c:out value="${sessionMap.dokumaran.title}" escapeXml="true"/>
	</h4>
	
	<c:if test="${empty summaryList}">
		<lams:Alert type="info" id="no-session-summary" close="false">
			 <fmt:message key="message.monitoring.summary.no.session" />
		</lams:Alert>
	</c:if>
	
	<!--For release marks feature-->
	<i class="fa fa-spinner" style="display:none" id="message-area-busy"></i>
	<div id="message-area"></div>
</div>

<c:if test="${sessionMap.isGroupedActivity}">
	<div class="panel-group" id="accordionSessions" role="tablist" aria-multiselectable="true"> 
</c:if>

<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
	
	<c:if test="${sessionMap.isGroupedActivity}">	
	    <div class="panel panel-default" >
        <div class="panel-heading" id="heading${groupSummary.sessionId}">
        	<span class="panel-title collapsable-icon-left">
        	<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${groupSummary.sessionId}" 
					aria-expanded="false" aria-controls="collapse${groupSummary.sessionId}" >
			<fmt:message key="monitoring.label.group" />&nbsp;${groupSummary.sessionName}</a>
			</span>
        </div>
        
        <div id="collapse${groupSummary.sessionId}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading${groupSummary.sessionId}">
	</c:if>

	<div id="etherpad-container-${groupSummary.sessionId}"></div>
	
	<c:if test="${sessionMap.isGroupedActivity}">
		</div> <!-- end collapse area  -->
		</div> <!-- end collapse panel  -->
	</c:if>
	${ !sessionMap.isGroupedActivity || ! status.last ? '<div class="voffset5">&nbsp;</div>' :  ''}
	
</c:forEach>

<c:if test="${sessionMap.isGroupedActivity}">
	</div> <!--  end accordianSessions --> 
</c:if>
	
<c:if test="${sessionMap.dokumaran.reflectOnActivity}">
	<%@ include file="reflections.jsp"%>
</c:if>

<%@ include file="advanceoptions.jsp"%>
