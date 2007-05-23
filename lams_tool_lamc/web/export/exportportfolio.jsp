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


	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

	<lams:html>
	<head>
	<title> <fmt:message key="label.exportPortfolio"/> </title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<lams:css localLinkPath="../"/>
	</head>

	<body class="stripes">
	
	    <html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
		<html:hidden property="method"/>
		<html:hidden property="toolContentID"/>
	
			<div id="content">
		
			<h1>
			<c:if test="${(portfolioExportMode == 'learner')}"><fmt:message key="label.export.learner"/></c:if>			
			<c:if test="${(portfolioExportMode != 'learner')}"><fmt:message key="label.export.teacher"/></c:if>			
	        </h1>

				<c:if test="${(userExceptionNoToolSessions == 'true')}"> 	
				<c:if test="${(portfolioExportMode != 'learner')}">
						<h2> <fmt:message key="error.noLearnerActivity"/> </h2>
				</c:if>			
				</c:if>			
		
				<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	

				<c:if test="${(portfolioExportMode != 'learner')}">
					<table>				
			  	 		<tr>
						  	<th scope="col" valign=top> 
							    <fmt:message key="label.mcqSummary"/> 
						  	</th>
			  	 		</tr>
						
						
			  	 		<c:set var="queIndex" scope="request" value="0"/>
						<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
						<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
				  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
				  	 		<tr>
				  	 			<td>&nbsp;</td>
				  	 		</tr>
							<tr>			
								<td NOWRAP valign=top class="align-left"><b>  <fmt:message key="label.question.only"/> <c:out value="${queIndex}"/>:</b>
									<c:out value="${currentDto.question}" escapeXml="false"/> &nbsp; (<fmt:message key="label.mark"/> 
									<c:out value="${currentDto.mark}"/>)
								 </td>
							</tr>	
							<tr>					
								<td NOWRAP valign=top class="align-left">  <b> <fmt:message key="label.mc.options.col"/>  </b> 
									<table class="align-left">
										<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
											<tr>			
												<td NOWRAP valign=top class="align-left">
													<c:out value="${answersData.candidateAnswer}" escapeXml="false"/> 
													
													<c:if test="${answersData.correct == 'true'}"> 		
														&nbsp; (<fmt:message key="label.correct"/>)
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
			  	 				<fmt:message key="label.passingMark"/> </b> <c:out value="${passMark}"/>  
			  	 			</td>
			  	 		</tr>
					</table>
		
		
					 <h2>    <fmt:message key="label.studentMarks"/>  </h2>

						<table class="forms">
							<c:forEach var="sessionMarksDto" items="${listMonitoredMarksContainerDto}">
					  	 		<c:set var="currentSessionId" scope="request" value="${sessionMarksDto.sessionId}"/>
					  	 		<c:set var="mapUserMarksDto" scope="request" value="${sessionMarksDto.userMarks}"/>
		
								<tr>
							 		<td NOWRAP colspan=2 > <b> <fmt:message key="group.label"/> : </b>
							 		<c:out value="${sessionMarksDto.sessionName}"/>  </td>
								</tr>
		
		
					  	 		<tr>
									 <td NOWRAP valign=top class="align-left"> <b> <fmt:message key="label.user"/> </b> </td>  
						  	 		<c:set var="queIndex" scope="request" value="0"/>
									<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
									<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
										<td NOWRAP valign=top class="align-left"> <b>  <fmt:message key="label.question.only"/> 
											<c:out value="${queIndex}"/></b>
											 &nbsp (<fmt:message key="label.mark"/> <c:out value="${currentDto.mark}"/>)
										</td>
									</c:forEach>		  	
									 
									 <td NOWRAP valign=top class="align-left"> <b> <fmt:message key="label.total"/>  </b> </td>  
						  	 		</tr>						 
		
											<c:forEach var="markData" items="${mapUserMarksDto}">						
								  	 		<c:set var="data" scope="request" value="${markData.value}"/>
								  	 		<c:set var="currentUserSessionId" scope="request" value="${data.sessionId}"/>
		
												<c:if test="${currentUserSessionId == currentSessionId}"> 	
													<tr>									  	 		
									  	 				<td NOWRAP valign=top class="align-left"> 
																<c:out value="${data.userName}"/> 
														</td>	
					
														<c:forEach var="mark" items="${data.marks}">
															<td NOWRAP valign=top class="align-left"> 
																	<c:out value="${mark}"/> 								
															</td>
														</c:forEach>		  										
					
														<td NOWRAP valign=top class="align-left"> 
																	<c:out value="${data.totalMark}"/> 																
														</td>							
													</tr>													
												</c:if>																

											</c:forEach>		  	
					
		
				 					<tr> <td NOWRAP colspan="<c:out value='${hrColumnCount}'/>">  </td>
									</tr>
				 					<tr> <td NOWRAP colspan="<c:out value='${hrColumnCount}'/>"> <hr size="2"></td>
									</tr>
				 					<tr> <td NOWRAP colspan="<c:out value='${hrColumnCount}'/>">  </td>
									</tr>
							</c:forEach>		  	
					</table>		  	 		

					</c:if>										
					
					<c:if test="${(portfolioExportMode == 'learner')}">
					<c:if test="${not empty  listMonitoredAnswersContainerDto}"> 
						
						<table width="80%" cellspacing="8" align="center">
				  	 		<c:set var="mainQueIndex" scope="request" value="0"/>
							<c:forEach var="currentDto" items="${listMonitoredAnswersContainerDto}">
							<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}"/>
					  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
					  	 		<tr>
					  	 			<td> &nbsp;</td>
					  	 		</tr>
								<tr>			
									<td NOWRAP valign=top class="align-left"><b>  <fmt:message key="label.question.only"/> <c:out value="${mainQueIndex}"/>:</b>
										<c:out value="${currentDto.question}"  escapeXml="false"/> &nbsp; (<fmt:message key="label.mark"/> 
										<c:out value="${currentDto.mark}"/>)
									 </td>
								</tr>	
								<tr>					
									<td NOWRAP valign=top class="align-left">  <b> <fmt:message key="label.mc.options.col"/>  </b> 
										<table class="align-left">
											<c:forEach var="answersData" items="${currentDto.candidateAnswersCorrect}">
												<tr>			
													<td NOWRAP valign=top class="align-left">
														<c:out value="${answersData.candidateAnswer}"  escapeXml="false"/> 
														
														<c:if test="${answersData.correct == 'true'}"> 		
															&nbsp; (<fmt:message key="label.correct"/>)
														</c:if>																		
													</td>	
												</tr>
											</c:forEach>		  	
										</table>
									</td>  
								</tr>			
										
								<tr>
									<td  NOWRAP class="align-left" valign=top> 											
									<table class="align-left">
										<c:forEach var="attemptEntry" items="${mapQueAttempts}">
											<c:if test="${mainQueIndex == attemptEntry.key}"> 		
									  		  	 		<c:set var="aIndex" scope="request" value="1"/>
										  		  	 		<tr>
																<td NOWRAP class="align-left" valign=top> 
																			<b> <fmt:message key="label.yourAnswers"/>  </b>
																</td>
																<td align=right valign=top>
																	<c:forEach var="responseEntry" items="${mapResponses}">
																			<c:if test="${mainQueIndex == responseEntry.key}"> 		
																				(<c:out value="${responseEntry.value}"/>)
																			</c:if> 																							
																	</c:forEach>
																</td>
															 </tr>
									  		  	 		
														 <c:forEach var="i" begin="1" end="30" step="1">
							 								<c:forEach var="distinctAttemptEntry" items="${attemptEntry.value}">
																<c:if test="${distinctAttemptEntry.key == i}"> 	
																	<tr>
																		<c:set var="aIndex" scope="request" value="${aIndex +1}"/>
				 														<td class="align-left" valign=top colspan=2> 																					
					 														<table class="align-left">
							 													<c:forEach var="singleAttemptEntry" items="${distinctAttemptEntry.value}">
																					<tr>
																						<td NOWRAP class="align-left" valign=top>
									 															<c:out value="${singleAttemptEntry.value}"  escapeXml="false"/> 
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
	
									</td>
							  </tr>								
		
							</c:forEach>
		
				  	 		<tr>
				  	 			<td NOWRAP valign=top class="align-left"> <b> 
				  	 				<fmt:message key="label.passingMark"/>: </b> <c:out value="${passMark}"/> 
				  	 			</td>
				  	 		</tr>
		
		
				  	 		<tr>
				  	 			<td NOWRAP valign=top class="align-left"> <b> 
				  	 				<fmt:message key="label.studentMark"/>: </b> <c:out value="${learnerMark}"/> 
				  	 			</td>
				  	 		</tr>
						</table>					
				</c:if>								 
				
				
				<table  class="forms">
				
						<tr>			
							<td valign=top class="align-left">
								&nbsp
							</td>
						</tr>
				
					<tr>			
							<td valign=top class="align-left">
								<table class="align-left">
								
										<tr>			
											<td colspan=3 valign=top align=center>
												<b>  <fmt:message key="label.reflection"/>  </b> 
											 </td>
										</tr>	
								
								
									<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
							  	 		<c:set var="userName" scope="request" value="${currentDto.userName}"/>
							  	 		<c:set var="userId" scope="request" value="${currentDto.userId}"/>
							  	 		<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
							  	 		<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
							  	 		<c:set var="entry" scope="request" value="${currentDto.entry}"/>
										<tr>			
											<td valign=top class="align-left">
												<b> <fmt:message key="label.user"/>:</b>
											 </td>
										
											<td valign=top class="align-left">
												 <c:out value="${userName}" escapeXml="false"/> 
											 </td>

											<td valign=top class="align-left">
												 <c:out value="${entry}" escapeXml="false"/> 
											 </td>
										</tr>	
									</c:forEach>		
								</table>  	
							 </td>
						</tr>	
				</table>		  	 								
				
				
				
				
				
			</c:if>						
			</c:if>						
		
			</div>  <!--closes content-->
		
		
			<div id="footer">
			</div><!--closes footer-->
		
		</html:form>	

	</body>
</lams:html>



