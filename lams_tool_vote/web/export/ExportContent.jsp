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

 				
			<table width="80%" cellspacing="8" align="CENTER" class="forms">
						<tr>
							<td NOWRAP valign=top align=left>
								<table>

									<c:if test="${(portfolioExportMode == 'learner')}">
										<tr>
									 		<th NOWRAP colspan=2>  <bean:message key="label.class.summary"/>  </th>
										</tr>
										
										<tr>
									 		<td NOWRAP> <b> <font size=2> <bean:message key="label.nomination"/> </b> </td>
											<td NOWRAP> <b> <font size=2> <bean:message key="label.total.votes"/> </b> </td>
										</tr>
										
										
										<c:forEach var="currentNomination" items="${mapStandardNominationsContent}">
								  	 		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}"/>
								  	 		 <tr>
					  	 						<td NOWRAP valign=top align=left>
								  	 		  		<c:forEach var="currentVoteVisibility" items="${mapStandardNominationsVisibility}">
												  	 		<c:set var="currentVisibilityKey" scope="request" value="${currentVoteVisibility.key}"/>
											  				<c:if test="${currentNominationKey == currentVisibilityKey}"> 				
												  				<c:if test="${currentVoteVisibility.value == 'true'}"> 				
																	<c:out value="${currentNomination.value}" escapeXml="false"/>		
																</c:if> 	    
												  				<c:if test="${currentVoteVisibility.value != 'true'}"> 				
																	<font size=2> <bean:message key="label.hiddenLearner"/> </font>
																</c:if> 	    
															</c:if> 	  
													</c:forEach>		  
												 </td>
				
												<td NOWRAP valign=top align=left>				  	 		
										  	 		<c:forEach var="currentUserCount" items="${mapStandardUserCount}">
											  	 		<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}"/>
										  				<c:if test="${currentNominationKey == currentUserKey}"> 				
																	<font size=2> <c:out value="${currentUserCount.value}"/>  </font>
														</c:if> 	    
													</c:forEach>		  
				
										  	 		<c:forEach var="currentRate" items="${mapStandardRatesContent}">
											  	 		<c:set var="currentRateKey" scope="request" value="${currentRate.key}"/>
										  				<c:if test="${currentNominationKey == currentRateKey}"> 				
																	<font size=2> &nbsp(<c:out value="${currentRate.value}"/> <bean:message key="label.percent"/>) </font>
														</c:if> 	    
													</c:forEach>		  
												</td>								
											</tr>	
										</c:forEach>	
									</c:if>											


									<c:if test="${(portfolioExportMode != 'learner')}">
										<tr>
									 		<th NOWRAP colspan=2>  <bean:message key="label.class.summaryAll"/>  </th>
										</tr>
										
										<tr>
									 		<td NOWRAP> <b> <font size=2> <bean:message key="label.nomination"/> </b> </td>
											<td NOWRAP> <b> <font size=2> <bean:message key="label.total.votes"/> </b> </td>
										</tr>
										
										<c:forEach var="currentNomination" items="${mapStandardNominationsContent}">
								  	 		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}"/>
								  	 		 <tr>
					  	 						<td NOWRAP valign=top align=left>
													<c:out value="${currentNomination.value}" escapeXml="false"/>
												 </td>
				
												<td NOWRAP valign=top align=left>				  	 		
										  	 		<c:forEach var="currentUserCount" items="${mapStandardUserCount}">
											  	 		<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}"/>
										  				<c:if test="${currentNominationKey == currentUserKey}"> 				
																	<font size=2> <c:out value="${currentUserCount.value}"/>  </font>
														</c:if> 	    
													</c:forEach>		  
				
										  	 		<c:forEach var="currentRate" items="${mapStandardRatesContent}">
											  	 		<c:set var="currentRateKey" scope="request" value="${currentRate.key}"/>
										  				<c:if test="${currentNominationKey == currentRateKey}"> 				
																	<font size=2> &nbsp(<c:out value="${currentRate.value}"/> <bean:message key="label.percent"/>) </font>
														</c:if> 	    
													</c:forEach>		  
												</td>								
											</tr>	
										</c:forEach>	
									</c:if>											

								</table>
							</td>
						</tr>


						<tr> 
							<td NOWRAP valign=top align=left >
								Pie Chart
							</td>
						</tr>

						<tr> 
							<td NOWRAP valign=top align=left >
								Bar Chart
							</td>
						</tr>


							<tr>
						 		<td NOWRAP> &nbsp&nbsp </td>
							</tr>
						
							<tr>
						 		<td NOWRAP> <HR> </td>
							</tr>

							<tr>
						 		<td NOWRAP> &nbsp&nbsp </td>
							</tr>



							<tr>
								<td NOWRAP valign=top align=left>
					 			<table align=left>
					 			
									<c:if test="${(portfolioExportMode == 'learner')}">
										<tr>
									 		<th NOWRAP>  <bean:message key="label.individual.learnerVotes"/>  </th>
										</tr>
									</c:if>											
	
									<c:if test="${(portfolioExportMode != 'learner')}">
										<tr>
									 		<th NOWRAP>  <bean:message key="label.all.learnerVotes"/>  </th>
										</tr>
									</c:if>											
					 			

								<c:forEach var="currentDto" items="${sessionScope.listMonitoredAnswersContainerDto}">
						  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
						  	 		<tr>
						  	 			<td> &nbsp&nbsp</td>
						  	 		</tr>
									<tr>			
										<td NOWRAP valign=top align=left><b> <font size=2> <bean:message key="label.nomination"/>: </b>
											<c:out value="${currentDto.question}" escapeXml="false"/>
										</font> </td>
									</tr>	
									
									<tr> 
										<td NOWRAP class="formlabel" valign=top>
											<table align=center>
												<tr> 
													 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.user"/> </font> </b> </td>  
							  						 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.attemptTime"/></font> </b></td>
									  			</tr>				 
					  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
													<c:forEach var="sData" items="${questionAttemptData.value}">
											  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
											  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>
			
				  	 									<c:if test="${currentQuestionId == userData.questionUid}">
					  	 									<c:if test="${sessionScope.currentMonitoredToolSession == 'All'}"> 			
																		<jsp:include page="/monitoring/UserResponses.jsp" />
															</c:if>														  					 									  			
															
					  	 									<c:if test="${sessionScope.currentMonitoredToolSession != 'All'}"> 			
					  	 										<c:if test="${sessionScope.currentMonitoredToolSession == userData.sessionId}"> 			
																		<jsp:include page="/monitoring/UserResponses.jsp" />										
																</c:if>														  					 									  													  			
															</c:if>														  					 									  													  			
														</c:if>														  					 
				 									</c:forEach>		  	
												</c:forEach>		  	
											</table>
										</td>  
						  			</tr>
								</c:forEach>		  
									
								
							<c:forEach var="currentDto" items="${sessionScope.listUserEntries}">
						  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
						  	 		<tr>
						  	 			<td> &nbsp&nbsp</td>
						  	 		</tr>
									<tr>			
										<td NOWRAP valign=top align=left><b> <font size=2> <bean:message key="label.nomination"/>: </b>
											<c:out value="${currentDto.question}" escapeXml="false"/>
										</font> </td>
									</tr>	
									
									<tr> 
										<td NOWRAP class="formlabel" valign=top>
											<table align=center>
												<tr> 
													 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.user"/> </font> </b> </td>  
							  						 <td NOWRAP valign=top> <b> <font size=2> <bean:message key="label.attemptTime"/></font> </b></td>
									  			</tr>				 
			
					  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
											  	 		<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
															<jsp:include page="/monitoring/UserResponses.jsp" />										
												</c:forEach>		  	
									  			
											</table>
										</td>  
						  			</tr>
							</c:forEach>		  	

							</table>
					 		</td>
						</tr>
			</table>

