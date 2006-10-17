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
		
		<c:if test="${mcGeneralMonitoringDTO.userExceptionNoToolSessions != 'true'}"> 	
		
			<html:hidden property="selectedToolSessionId"/>							
			<input type="hidden" name="isToolSessionChanged"/>
				<table class="forms">
						<tr> 
						<td NOWRAP  valign=top align=center>  <b> <fmt:message key="label.selectGroup"/> </b>

								<select name="monitoredToolSessionId" onchange="javascript:submitSession(this.value,'submitSession');">
								<c:forEach var="toolSessionName" items="${mcGeneralMonitoringDTO.summaryToolSessions}">
									<c:forEach var="toolSessionId" items="${mcGeneralMonitoringDTO.summaryToolSessionsId}">
										<c:out value="${toolSessionName.key}"/> -<c:out value="${toolSessionId.value}"/>
									
											<c:if test="${toolSessionName.key == toolSessionId.key}"> 			
										
												<c:set var="SELECTED_SESSION" scope="request" value=""/>
												<c:if test="${requestScope.selectionCase == 2}"> 			
													<c:set var="currentMonitoredToolSession" scope="request" value="All"/>
												</c:if>						
												
												<c:if test="${toolSessionId.value == mcGeneralMonitoringDTO.currentMonitoredToolSession}"> 			
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


					
		  	 		<c:set var="queIndex" scope="request" value="0"/>
					<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
					<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			  	 		<tr>
			  	 			<td> &nbsp&nbsp&nbsp</td>
			  	 		</tr>
						<tr>			
							<td NOWRAP valign=top align=left><b>  <fmt:message key="label.question.only"/> <c:out value="${queIndex}"/>:</b>
								<c:out value="${currentDto.question}" escapeXml="false"/> &nbsp (<fmt:message key="label.mark"/> <c:out value="${currentDto.mark}"/> )
							 </td>
						</tr>	
						<tr>					
							<td NOWRAP valign=top align=left>  <b> <fmt:message key="label.mc.options.col"/>  </b> 
								<table align=left>
									<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
										<tr>			
											<td NOWRAP valign=top align=left>
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
		  	 			<td NOWRAP valign=top align=left> <b> 
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
		</c:if>						
		
		
			<c:if test="${noSessionsNotebookEntriesExist == 'true'}"> 							
						<jsp:include page="/monitoring/Reflections.jsp" />
			</c:if>						
		
		
		
		
		
