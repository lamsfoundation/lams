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
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

 				
		<c:if test="${(userExceptionNoToolSessions == 'true')}"> 	
				<table class="forms" align="center">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <bean:message key="error.noLearnerActivity"/> </b>
						</td> 
					<tr>
				</table>
		</c:if>			

		<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
		
			<table class="forms">
				<c:if test="${(portfolioExportMode != 'learner')}">
		  	 		<tr>
					  	<th scope="col" valign=top> 
						    <bean:message key="label.mcqSummary"/> 
					  	</th>
	
		  	 		</tr>
					
					
		  	 		<c:set var="queIndex" scope="request" value="0"/>
					<c:forEach var="currentDto" items="${sessionScope.listMonitoredAnswersContainerDto}">
					<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			  	 		<tr>
			  	 			<td> &nbsp&nbsp&nbsp</td>
			  	 		</tr>
						<tr>			
							<td NOWRAP valign=top align=left><b>  <bean:message key="label.question.only"/> <c:out value="${queIndex}"/>:</b>
								<c:out value="${currentDto.question}"/> &nbsp (<bean:message key="label.weight"/> 
								<c:out value="${currentDto.weight}"/>  <bean:message key="label.percent"/>)
							 </td>
						</tr>	
						<tr>					
							<td NOWRAP valign=top align=left>  <b> <bean:message key="label.mc.options.col"/>  </b> 
								<table align=left>
									<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
										<tr>			
											<td NOWRAP valign=top align=left>
												&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
												<c:out value="${answersData.candidateAnswer}"/> 
												
												<c:if test="${answersData.correct == 'true'}"> 		
													&nbsp (<bean:message key="label.correct"/>)
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
		  	 				<bean:message key="label.passingMark"/> </b> <c:out value="${passMark}"/> <bean:message key="label.percent"/>  
		  	 			</td>
		  	 		</tr>
				</table>
	
	
				 <h2>    <bean:message key="label.studentMarks"/>  </h2>
						<jsp:include page="/monitoring/SummaryAllSessions.jsp" />			  	 		
				</c:if>										
				
				<c:if test="${(portfolioExportMode == 'learner')}">
					<table width="80%" cellspacing="8" align="center" class="forms">
			  	 		<c:set var="mainQueIndex" scope="request" value="0"/>
						<c:forEach var="currentDto" items="${sessionScope.listMonitoredAnswersContainerDto}">
						<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}"/>
				  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
				  	 		<tr>
				  	 			<td> &nbsp&nbsp&nbsp</td>
				  	 		</tr>
							<tr>			
								<td NOWRAP valign=top align=left><b>  <bean:message key="label.question.only"/> <c:out value="${mainQueIndex}"/>:</b>
									<c:out value="${currentDto.question}"/> &nbsp (<bean:message key="label.weight"/> 
									<c:out value="${currentDto.weight}"/>  <bean:message key="label.percent"/>)
								 </td>
							</tr>	
							<tr>					
								<td NOWRAP valign=top align=left>  <b> <bean:message key="label.mc.options.col"/>  </b> 
									<table align=left>
										<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
											<tr>			
												<td NOWRAP valign=top align=left>
													&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
													<c:out value="${answersData.candidateAnswer}"/> 
													
													<c:if test="${answersData.correct == 'true'}"> 		
														&nbsp (<bean:message key="label.correct"/>)
													</c:if>																		
												</td>	
											</tr>
										</c:forEach>		  	
									</table>
								</td>  
							</tr>			
									
							<tr>
								<td  NOWRAP align=left valign=top> 											
								<table align=left>
									<c:forEach var="attemptEntry" items="${sessionScope.mapQueAttempts}">
										<c:if test="${mainQueIndex == attemptEntry.key}"> 		
								  		  	 		<c:set var="aIndex" scope="session" value="1"/>
									  		  	 		<tr>
															<td NOWRAP align=left valign=top> 
																		<b> <bean:message key="label.yourAnswers"/>  </b>
															</td>
														 </tr>
								  		  	 		
													 <c:forEach var="i" begin="1" end="30" step="1">
						 								<c:forEach var="distinctAttemptEntry" items="${attemptEntry.value}">
															<c:if test="${distinctAttemptEntry.key == i}"> 	
																<tr>
																	<c:set var="aIndex" scope="session" value="${sessionScope.aIndex +1}"/>
			 														<td align=left valign=top> 																					
				 														<table align=left>
						 													<c:forEach var="singleAttemptEntry" items="${distinctAttemptEntry.value}">
																				<tr>
																					<td NOWRAP align=left valign=top>
								 															&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
								 															<c:out value="${singleAttemptEntry.value}"/> 
							 														</td>
																				</tr>	
																			</c:forEach>
																		</table>	
			 														</td>
																</tr>		
															</c:if> 																																						
														</c:forEach>
													</c:forEach>
										</c:if> 																		
										
									</c:forEach>
								</table>

									<c:forEach var="responseEntry" items="${mapResponses}">
											<c:if test="${mainQueIndex == responseEntry.key}"> 		
												(<c:out value="${responseEntry.value}"/> <bean:message key="label.percent"/>)
											</c:if> 																													
									</c:forEach>
								</td>
						  </tr>								
	
						</c:forEach>
	
			  	 		<tr>
			  	 			<td NOWRAP valign=top align=left> <b> 
			  	 				<bean:message key="label.passingMark"/> </b> <c:out value="${passMark}"/> <bean:message key="label.percent"/>  
			  	 			</td>
			  	 		</tr>
	
	
			  	 		<tr>
			  	 			<td NOWRAP valign=top align=left> <b> 
			  	 				<bean:message key="label.yourMark"/> </b> <c:out value="${learnerMark}"/> <bean:message key="label.percent"/>  
			  	 			</td>
			  	 		</tr>
					</table>					
				</c:if>								 
		</c:if>				

