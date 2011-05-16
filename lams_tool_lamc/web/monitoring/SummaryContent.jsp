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
<c:set var="lams">
 		<lams:LAMSURL />
</c:set>

<link type="text/css" href="${lams}/css/jquery-ui-1.8.11.flick-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui-timepicker-addon.css" rel="stylesheet">
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui-1.8.11.custom.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  

<script type="text/javascript">
<!--

	$(function(){
		$("#datetime").datetimepicker();

		var submissionDeadline = '${mcGeneralMonitoringDTO.submissionDeadline}';
		if (submissionDeadline != "") {
			var date = new Date(eval(submissionDeadline));

			$("#dateInfo").html( formatDate(date) );
			
			//open up date restriction area
			toggleAdvancedOptionsVisibility(document.getElementById('restrictUsageDiv'), document.getElementById('restrictUsageTreeIcon'),'${lams}');
		
		}
		
	});

	function formatDate(date) {
		var currHour = "" + date.getHours();
		if (currHour.length == 1) {
			currHour = "0" + currHour;
		}  
		var currMin = "" + date.getMinutes();
		if (currMin.length == 1) {
			currMin = "0" + currMin;
		}
		return $.datepicker.formatDate( 'mm/dd/yy', date ) + " " + currHour + ":" + currMin;
	}

	function setSubmissionDeadline() {
		//get the timestamp in milliseconds since midnight Jan 1, 1970
		var date = $("#datetime").datetimepicker('getDate');
		if (date == null) {
			return;
		}

		var reqIDVar = new Date();
		var url = "<c:url value="/monitoring.do"/>?dispatch=setSubmissionDeadline&toolContentID=${param.toolContentID}&submissionDeadline="
					+ date.getTime() + "&reqID=" + reqIDVar.getTime();

		$.ajax({
			url : url,
			success : function() {
				$.growlUI('<fmt:message key="monitor.summary.notification" />', '<fmt:message key="monitor.summary.date.restriction.set" />');
				$("#datetimeDiv").hide();
				$("#dateInfo").html(formatDate(date) );
				$("#dateInfoDiv").show();
			}
		});
	}
	function removeSubmissionDeadline() {
		var reqIDVar = new Date();
		var url = "<c:url value="/monitoring.do"/>?dispatch=setSubmissionDeadline&toolContentID=${param.toolContentID}&submissionDeadline="
					+ "&reqID=" + reqIDVar.getTime();

		$.ajax({
			url : url,
			success : function() {
				$.growlUI('<fmt:message key="monitor.summary.notification" />', '<fmt:message key="monitor.summary.date.restriction.removed" />');
				$("#dateInfoDiv").hide();
				
				$("#datetimeDiv").show();
				$("#datetime").val("");
			}
		});
	}
			
	
//-->
</script>

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
			<fmt:message key="radiobox.onepq" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${questionsSequenced}">
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
			<fmt:message key="label.showMarks" />
		</td>
		<td>
			<c:choose>
				<c:when test="${showMarks}">
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
			<fmt:message key="label.randomize" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${randomize}">
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
			<fmt:message key="label.displayAnswers" />
		</td>	
		<td>
			<c:choose>
				<c:when test="${displayAnswers}">
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
			<fmt:message key="radiobox.retries" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${retries}">
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
			<fmt:message key="radiobox.passmark" />
		</td>
		<td>	
			${passMark}
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

		<c:if test="${(mcGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}"> 	
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

	<c:choose>
		<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'true'}">
			<p>
				<fmt:message key="label.monitoring.yesDisplayAnswers"/>
			</p>
		</c:when>
		<c:when test="${mcGeneralMonitoringDTO.displayAnswers == 'false'}">
			<p>
				<fmt:message key="label.monitoring.noDisplayAnswers1"/><br>
				<quote>
				<fmt:message key="label.monitoring.noDisplayAnswers2"/>
				<input onclick="javascript:submitChangeDisplayAnswers(this.value, 'displayAnswers');" class="button" name="displayAnswers" class="button" value="<fmt:message key='button.monitoring.noDisplayAnswers'/>" type="button">	
				</quote>
			</p>
		</c:when>
	</c:choose>
		
		<c:if test="${mcGeneralMonitoringDTO.userExceptionNoToolSessions != 'true'}"> 	
		
			<html:hidden property="selectedToolSessionId"/>							
			<input type="hidden" name="isToolSessionChanged"/>
				<table class="forms">
				
					<c:choose>
					<c:when test="${fn:length(mcGeneralMonitoringDTO.summaryToolSessions) > 2 }">
						<%-- When grouping is not enabled, we have only 2 items in summaryToolSessions.  The main toolSession and 'All' --%>
								
						<tr> 
							<td NOWRAP  valign=top align=center>  <b> <fmt:message key="label.selectGroup"/> </b>

								<select name="monitoredToolSessionId" onchange="javascript:submitSession(this.value,'submitSession');">
								<c:forEach var="toolSessionEntry" items="${mcGeneralMonitoringDTO.summaryToolSessions}">
										<c:set var="SELECTED_SESSION" scope="request" value=""/>
										<c:if test="${toolSessionEntry.key == mcGeneralMonitoringDTO.currentMonitoredToolSession}"> 			
												<c:set var="SELECTED_SESSION" scope="request" value="SELECTED"/>
										</c:if>						
										<c:choose>										
										<c:when test="${toolSessionId.value == 'All'}"> 	
											<option value="<c:out value="${toolSessionEntry.key}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>>All</option>						
										</c:when>		
										<c:otherwise> 		
											<option value="<c:out value="${toolSessionEntry.key}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>><c:out value="${toolSessionEntry.value}"/></option>						
										</c:otherwise>						
										</c:choose>				
								</c:forEach>		  	
								</select>									
							</td> 
						</tr>
					</c:when>
					<c:otherwise>
						<tr><td><input type="hidden" name="monitoredToolSessionId" value="All" /></td></tr>
					</c:otherwise>
					</c:choose>
					
		  	 		<c:set var="queIndex" scope="request" value="0"/>
					<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
					<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>

						<tr>			
							<td NOWRAP valign=top class="align-left"><b>  <fmt:message key="label.question.only"/> <c:out value="${queIndex}"/>:</b>
								<c:out value="${currentDto.question}" escapeXml="false"/> &nbsp (<fmt:message key="label.mark"/> <c:out value="${currentDto.mark}"/> )
							 </td>
						</tr>	
						<tr>					
							<td NOWRAP valign=top class="align-left">  <b> <fmt:message key="label.mc.options.col"/>  </b> 
								<table class="align-left">
									<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
										<tr>			
											<td NOWRAP valign=top class="align-left">
												&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
												<c:out value="${answersData.candidateAnswer}" escapeXml="false"/> 
												
												<c:if test="${answersData.correct == 'true'}"> 		
													&nbsp (<fmt:message key="label.correct"/>)
												</c:if>																		
											</td>	
										</tr>
									</c:forEach>		  	
								</table>
							</td>  
						</tr>			

					</c:forEach>		  	

		  	 		<tr>
		  	 			<td NOWRAP valign=top class="align-left"> <b> 
		  	 				<fmt:message key="label.passingMark"/>: </b> <c:out value="${passMark}"/> 
		  	 			</td>
		  	 		</tr>
				</table>

			 <h2>    <fmt:message key="label.studentMarks"/>  </h2>


				<c:if test="${mcGeneralMonitoringDTO.currentMonitoredToolSession =='All'}"> 						 
					<jsp:include page="/monitoring/AllSessionsSummary.jsp" />
					<jsp:include page="/monitoring/Reflections.jsp" />										
				</c:if>						
				<c:if test="${mcGeneralMonitoringDTO.currentMonitoredToolSession !='All'}"> 						 
					<jsp:include page="/monitoring/IndividualSessionSummary.jsp" />
				</c:if>
				
				<html:link href="#" onclick="javascript:submitMonitoringMethod('downloadMarks');" styleClass="button">
					<fmt:message key="label.monitoring.downloadMarks.button" />
				</html:link>					
		</c:if>						
		
		
			<c:if test="${noSessionsNotebookEntriesExist == 'true'}"> 							
						<jsp:include page="/monitoring/Reflections.jsp" />
			</c:if>						
		
		
		
		
		
