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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp"%>
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
			<fmt:message key="label.learner.answer" />
		</td>
		<td>
			<c:choose>
				<c:when test="${content.showOtherAnswers}">
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
			<fmt:message key="label.show.names" />
		</td>
		<td>	
			<c:choose>
				<c:when test="${content.usernameVisible}">
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
			<fmt:message key="label.authoring.allow.rate.answers" />
		</td>
		<td>
			<c:choose>
				<c:when test="${content.allowRateAnswers}">
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
				<c:when test="${content.reflect}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	 		
	<c:choose>
		<c:when test="${content.reflect}">
			<tr>
				<td>
					<fmt:message key="monitor.summary.td.notebookInstructions" />
				</td>	
				<td>
					${content.reflectionSubject}
				</td>
			</tr>
		</c:when>
	</c:choose>
	
	<tr>
		<td>
			<fmt:message key="radiobox.questionsSequenced" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.questionsSequenced}">
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
			<fmt:message key="label.lockWhenFinished" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.lockWhenFinished}">
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
			<fmt:message key="label.allowRichEditor" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.allowRichEditor}">
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
			<fmt:message key="label.use.select.leader.tool.output" />
		</td>
		
		<td>
			<c:choose>
				<c:when test="${content.useSelectLeaderToolOuput}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>

</table>
</div>	

	<%@include file="dateRestriction.jsp"%>
		
	<html:hidden property="responseId"/>	 
	<html:hidden property="sessionId"/>	 
	
	
		<c:if test="${(qaGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}"> 	
			<c:if test="${notebookEntriesExist != 'true' }"> 			
				<table class="forms">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <fmt:message key="error.noLearnerActivity"/> </b>
						</td> 
					<tr>
				</table>
			</c:if>							
		</c:if>			
		
					
		<c:if test="${(qaGeneralMonitoringDTO.userExceptionNoToolSessions != 'true') }"> 	
		
				<table class="forms">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <fmt:message key="label.learners.answers"/> </b>
						</td> 
					<tr>
				</table>
		
		
			<html:hidden property="selectedToolSessionId"/>							
			<input type="hidden" name="isToolSessionChanged"/>
				<table class="forms">
				
				<c:if test="${fn:length(requestScope.summaryToolSessions) > 2 }">
					<%-- When grouping is not enabled, we have only 2 items in summaryToolSessions.  The main toolSession and 'All' --%>

					<c:if test="${(generalLearnerFlowDTO.requestLearningReport != 'true')}"> 	
						<tr> 
							<td NOWRAP width="60" valign=top align=center> <b> <fmt:message key="label.selectGroup"/> </b>
								<select name="monitoredToolSessionId" onchange="javascript:submitSession(this.value,'submitSession');">
									<c:forEach var="toolSessionName" items="${requestScope.summaryToolSessions}">
										<c:forEach var="toolSessionId" items="${requestScope.summaryToolSessionsId}">
											<c:out value="${toolSessionName.key}"/> -<c:out value="${toolSessionId.value}"/>
										
												<c:if test="${toolSessionName.key == toolSessionId.key}"> 			
											
													<c:set var="SELECTED_SESSION" scope="request" value=""/>
													<c:if test="${requestScope.selectionCase == 2}"> 			
														<c:set var="currentMonitoredToolSession" scope="request" value="All"/>
													</c:if>						
													
													<c:if test="${toolSessionId.value == requestScope.currentMonitoredToolSession}"> 			
															<c:set var="SELECTED_SESSION" scope="request" value="SELECTED"/>
													</c:if>						
													
													<c:if test="${toolSessionId.value != 'All'}"> 		
														<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>> <c:out value="${toolSessionName.value}"/>  </option>						
													</c:if>						
													
													<c:if test="${toolSessionId.value == 'All'}"> 	
														<option value="<c:out value="${toolSessionId.value}"/>"  <c:out value="${requestScope.SELECTED_SESSION}"/>>  All  </option>						
													</c:if>						
											</c:if>							
										</c:forEach>		  	
									</c:forEach>		  	
								</select>
							</td> 
						<tr>
					</c:if>
				</c:if>			

			<c:if test="${currentMonitoredToolSession != 'All'}"> 							
					<jsp:include page="/monitoring/IndividualSessionSummary.jsp" />					
			</c:if> 	    	  

			<c:if test="${currentMonitoredToolSession == 'All'}"> 							
					<jsp:include page="/monitoring/AllSessionsSummary.jsp" />								
					
					<jsp:include page="/monitoring/Reflections.jsp" />										
			</c:if> 	    	  
		
			</table>  	
		</c:if>						
		
			<c:if test="${noSessionsNotebookEntriesExist == 'true'}"> 							
						<jsp:include page="/monitoring/Reflections.jsp" />
			</c:if>						
		
		



	