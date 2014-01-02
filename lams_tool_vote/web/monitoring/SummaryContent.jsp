<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">

<script type="text/javascript"> 
	var chartDataUrl = '<lams:WebAppURL />chartGenerator.do';

	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring.do?dispatch=setSubmissionDeadline"/>',
		toolContentID: '${toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};	
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/raphael.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.raphael.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.pie.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/g.bar.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/raphael/chart.js"></script>
	
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="label.use.select.leader.tool.output" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${useSelectLeaderToolOuput}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.vote.lockedOnFinish" />
		</td>
		<td>
			<c:choose>
				<c:when test="${lockOnFinish}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.allowText" />
		</td>
		<td>
			<c:choose>
				<c:when test="${allowText}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.maxNomCount" />
		</td>
		<td>	
			${maxNominationCount}
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.minNomCount" />
		</td>
		<td>	
			${minNominationCount}
		</td>
	</tr>	
	
	<tr>
		<td>
			<fmt:message key="label.show.results" />
		</td>	
		<td>
			<c:choose>
				<c:when test="${showResults}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="monitor.summary.td.addNotebook" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${reflect}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<c:choose>
		<c:when test="${reflect}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>
				<td>	
					${reflectionSubject}
				</td>
			</tr>
		</c:when>
	</c:choose>
</table>
</div>


<%@include file="daterestriction.jsp"%>


		<c:if test="${(voteGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}"> 	
			<c:if test="${notebookEntriesExist != 'true' }"> 			
				<table align="center">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b>  <fmt:message key="error.noLearnerActivity"/> </b>
						</td> 
					<tr>
				</table>
			</c:if>							
		</c:if>			


		<c:if test="${(voteGeneralMonitoringDTO.userExceptionNoToolSessions != 'true') }"> 	
			<c:if test="${voteGeneralMonitoringDTO.currentMonitoredToolSession != 'All'}"> 							
					<jsp:include page="/monitoring/IndividualSessionSummary.jsp" />					
			</c:if> 	    	  

			<c:if test="${voteGeneralMonitoringDTO.currentMonitoredToolSession == 'All'}"> 							
					<jsp:include page="/monitoring/AllSessionsSummary.jsp" />								
					
				<jsp:include page="/monitoring/Reflections.jsp" />										
			</c:if> 	    	  
		</c:if>								
		
			<c:if test="${noSessionsNotebookEntriesExist == 'true'}"> 							
						<jsp:include page="/monitoring/Reflections.jsp" />
			</c:if>						
		
		
