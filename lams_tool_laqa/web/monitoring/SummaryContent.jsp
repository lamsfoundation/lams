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

	<html:hidden property="responseId"/>	 
	<html:hidden property="sessionId"/>	 
	
	
		<c:if test="${(qaGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}"> 	
			<c:if test="${notebookEntriesExist != 'true' }"> 			
				<table class="forms">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <bean:message key="error.noLearnerActivity"/> </b>
						</td> 
					<tr>
				</table>
			</c:if>							
		</c:if>			
					
		<c:if test="${(qaGeneralMonitoringDTO.userExceptionNoToolSessions != 'true') }"> 	
		
				<table class="forms">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <bean:message key="label.learners.answers"/> </b>
						</td> 
					<tr>
				</table>
		
		
			<html:hidden property="selectedToolSessionId"/>							
			<input type="hidden" name="isToolSessionChanged"/>
				<table class="forms">

				<c:if test="${(generalLearnerFlowDTO.requestLearningReport != 'true')}"> 	
						<tr> 
							<td NOWRAP width="60" valign=top align=center> <b> <bean:message key="label.selectGroup"/> </b>
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

			<c:if test="${currentMonitoredToolSession != 'All'}"> 							
					<jsp:include page="/monitoring/IndividualSessionSummary.jsp" />					
			</c:if> 	    	  

			<c:if test="${currentMonitoredToolSession == 'All'}"> 							
					<jsp:include page="/monitoring/AllSessionsSummary.jsp" />								
					
					<jsp:include page="/monitoring/Reflections.jsp" />										
			</c:if> 	    	  
		
			</table>  	
		</c:if>						


	