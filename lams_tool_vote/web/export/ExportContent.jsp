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

 				
			<table  class="forms">
						<tr>
							<td NOWRAP valign=top align=left>
								<table>

									<c:if test="${(exportPortfolioDto.portfolioExportMode == 'learner')}">
										<tr>
									 		<th NOWRAP colspan=2>  <bean:message key="label.class.summary"/>  </th>
										</tr>
										
										<tr>
									 		<td NOWRAP> <b>  <bean:message key="label.nomination"/> </b> </td>
											<td NOWRAP> <b>  <bean:message key="label.total.votes"/> </b> </td>
										</tr>

										<c:forEach var="currentNomination" items="${exportPortfolioDto.mapStandardNominationsHTMLedContent}">
								  	 		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}"/>
								  	 		 <tr>
					  	 						<td NOWRAP valign=top align=left>
													<c:out value="${currentNomination.value}" escapeXml="false"/>
												 </td>
				
												<td NOWRAP valign=top align=left>				  	 		
										  	 		<c:forEach var="currentUserCount" items="${exportPortfolioDto.mapStandardUserCount}">
											  	 		<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}"/>
										  				<c:if test="${currentNominationKey == currentUserKey}"> 				
																	 <c:out value="${currentUserCount.value}"/>  
														</c:if> 	    
													</c:forEach>		  
				
										  	 		<c:forEach var="currentRate" items="${exportPortfolioDto.mapStandardRatesContent}">
											  	 		<c:set var="currentRateKey" scope="request" value="${currentRate.key}"/>
										  				<c:if test="${currentNominationKey == currentRateKey}"> 				
																	 &nbsp(<c:out value="${currentRate.value}"/> <bean:message key="label.percent"/>) 
														</c:if> 	    
													</c:forEach>		  
												</td>								
											</tr>	
										</c:forEach>	

										
										<tr>
									 		<td NOWRAP colspan=2>
								 			<table align=left>
														<tr>
													 		<th NOWRAP colspan=2>  <bean:message key="label.openVotes"/>  </th>
														</tr>
			
														<tr> 
															 <td NOWRAP valign=top align=left> <b>   <bean:message key="label.vote"/>  </b> </td>  														 
									  						 <td NOWRAP valign=top align=left> <b>   <bean:message key="label.attemptTime"/> </b></td>
											  			</tr>				 
													
								 			
													<c:forEach var="currentDto" items="${exportPortfolioDto.listUserEntries}">
								  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
													  	 		<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
																<tr> 
																		<td NOWRAP valign=top align=left> 
																				<c:out value="${currentDto.question}" escapeXml="false"/> 
																		</td>
																		 <td NOWRAP valign=top align=left>   
																			<lams:Date value="${userData.attemptTime}"/> 
																		 </td>
																</tr>		
															</c:forEach>		  	
													</c:forEach>		
											</table> 
											</td>
										</tr>
										
									</c:if>											


									<c:if test="${(exportPortfolioDto.portfolioExportMode != 'learner')}">
										<tr>
									 		<th NOWRAP colspan=2>  <bean:message key="label.class.summaryAll"/>  </th>
										</tr>
										
										<tr>
									 		<td NOWRAP> <b>  <bean:message key="label.nomination"/> </b> </td>
											<td NOWRAP> <b>  <bean:message key="label.total.votes"/> </b> </td>
										</tr>
										
										<c:forEach var="currentNomination" items="${exportPortfolioDto.mapStandardNominationsHTMLedContent}">
								  	 		<c:set var="currentNominationKey" scope="request" value="${currentNomination.key}"/>
								  	 		 <tr>
					  	 						<td NOWRAP valign=top align=left>
													<c:out value="${currentNomination.value}" escapeXml="false"/>
												 </td>
				
												<td NOWRAP valign=top align=left>				  	 		
										  	 		<c:forEach var="currentUserCount" items="${exportPortfolioDto.mapStandardUserCount}">
											  	 		<c:set var="currentUserKey" scope="request" value="${currentUserCount.key}"/>
										  				<c:if test="${currentNominationKey == currentUserKey}"> 				
																	 <c:out value="${currentUserCount.value}"/>  
														</c:if> 	    
													</c:forEach>		  
				
										  	 		<c:forEach var="currentRate" items="${exportPortfolioDto.mapStandardRatesContent}">
											  	 		<c:set var="currentRateKey" scope="request" value="${currentRate.key}"/>
										  				<c:if test="${currentNominationKey == currentRateKey}"> 				
																	 &nbsp(<c:out value="${currentRate.value}"/> <bean:message key="label.percent"/>) 
														</c:if> 	    
													</c:forEach>		  
												</td>								
											</tr>	
										</c:forEach>	
										
										
								<tr>
							 		<td NOWRAP colspan=2>
						 			<table align=left>
												<tr>
											 		<th NOWRAP colspan=3>  <bean:message key="label.openVotes"/>  </th>
												</tr>
	
												<tr> 
													 <td NOWRAP valign=top align=left> <b>   <bean:message key="label.vote"/>  </b> </td>  														 
													 <td NOWRAP valign=top align=left> <b>   <bean:message key="label.user"/>  </b> </td>  
							  						 <td NOWRAP valign=top align=left> <b>   <bean:message key="label.attemptTime"/> </b></td>
									  			</tr>				 
											
						 			
											<c:forEach var="currentDto" items="${exportPortfolioDto.listUserEntries}">
						  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
											  	 		<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
		  	 									  	 		<c:set var="currentUid" scope="request" value="${userData.uid}"/>
														<tr> 
																<td NOWRAP valign=top align=left> 
																	<c:out value="${currentDto.question}" escapeXml="false"/> 
																	<c:if test="${userData.visible != 'true' }"> 			
												                                 <i><bean:message key="label.hidden"/> </i> 
																	</c:if> 								
																</td>
																 
																 <td NOWRAP valign=top align=left>    <c:out value="${userData.userName}"/>   </td>  
																 <td NOWRAP valign=top align=left>    <lams:Date value="${userData.attemptTime}"/>  </td>																 
																 
														</tr>		
													</c:forEach>		  	
											</c:forEach>		
									</table> 
									</td>
								</tr>

										
									</c:if>											

								</table>
							</td>
						</tr>


						 <tr> 
					       <td NOWRAP valign=top align=left >
					        <IMG SRC="pie.png" ALT="Pie Chart"/>
					       </td>
					      </tr>
					
					      <tr> 
					       <td NOWRAP valign=top align=left >
					        <IMG SRC="bar.png" ALT="Bar Chart"/>
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
					 			
									<c:if test="${(exportPortfolioDto.portfolioExportMode == 'learner')}">
										<tr>
									 		<th NOWRAP>  <bean:message key="label.individual.learnerVotes"/>  </th>
										</tr>
									</c:if>											
	
					 			
					 			
									<c:if test="${(exportPortfolioDto.portfolioExportMode != 'learner')}">
										<tr>
									 		<th NOWRAP>  <bean:message key="label.all.learnerVotes"/>  </th>
										</tr>
									</c:if>											
					 			

								<c:forEach var="currentDto" items="${exportPortfolioDto.listMonitoredAnswersContainerDto}">
						  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
						  	 		<tr>
						  	 			<td> &nbsp&nbsp</td>
						  	 		</tr>
									<tr>			
										<td NOWRAP valign=top align=left><b>  <bean:message key="label.nomination"/>: </b>
											<c:out value="${currentDto.question}" escapeXml="false"/>
										 </td>
									</tr>	
									
									<tr> 
										<td NOWRAP class="formlabel" valign=top>
											<table align=center>
												<tr> 
													 <td NOWRAP valign=top> <b>  <bean:message key="label.user"/>  </b> </td>  
							  						 <td NOWRAP valign=top> <b>  <bean:message key="label.attemptTime"/> </b></td>
									  			</tr>				 
					  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
													<c:forEach var="sData" items="${questionAttemptData.value}">
											  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
											  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>
			
				  	 									<c:if test="${currentQuestionId == userData.questionUid}">
																<tr> 
																		 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
																		 <td NOWRAP valign=top>   <c:out value="${userData.attemptTime}"/>  </td>
																</tr>		
														</c:if>														  					 
				 									</c:forEach>		  	
												</c:forEach>		  	
											</table>
										</td>  
						  			</tr>
								</c:forEach>		  
									
								
							<c:forEach var="currentDto" items="${exportPortfolioDto.listUserEntries}">
						  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
						  	 		<tr>
						  	 			<td> &nbsp&nbsp</td>
						  	 		</tr>
									<tr>			
										<td NOWRAP valign=top align=left><b>  <bean:message key="label.nomination"/>: </b>
											<c:out value="${currentDto.question}" escapeXml="false"/>
										 </td>
									</tr>	
									
									<tr> 
										<td NOWRAP class="formlabel" valign=top>
											<table align=center>
												<tr> 
													 <td NOWRAP valign=top> <b>  <bean:message key="label.user"/>  </b> </td>  
							  						 <td NOWRAP valign=top> <b> <bean:message key="label.attemptTime"/> </b></td>
									  			</tr>				 
			
					  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
											  	 		<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
																<tr> 
																	 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
																	 <td NOWRAP valign=top >    <lams:Date value="${userData.attemptTime}"/>  </td>																		 
																</tr>		
												</c:forEach>		  	
									  			
											</table>
										</td>  
						  			</tr>
							</c:forEach>		  	

							</table>
					 		</td>
						</tr>
			</table>
			
			
			
			<table  class="forms">
				
						<tr>			
							<td valign=top align=left>
								&nbsp
							</td>
						</tr>
				
					<tr>			
							<td valign=top align=left>
								<table align=left>
								
										<tr>			
											<td colspan=3 valign=top align=center>
												<b>  <bean:message key="label.reflection"/>  </b> 
											 </td>
										</tr>	
								
								
									<c:forEach var="currentDto" items="${reflectionsContainerDTO}">
							  	 		<c:set var="userName" scope="request" value="${currentDto.userName}"/>
							  	 		<c:set var="userId" scope="request" value="${currentDto.userId}"/>
							  	 		<c:set var="sessionId" scope="request" value="${currentDto.sessionId}"/>
							  	 		<c:set var="reflectionUid" scope="request" value="${currentDto.reflectionUid}"/>
							  	 		<c:set var="entry" scope="request" value="${currentDto.entry}"/>
										<tr>			
											<td valign=top align=left>
												<b> <bean:message key="label.user"/>:</b>
											 </td>
										
											<td valign=top align=left>
												 <c:out value="${userName}" escapeXml="false"/> 
											 </td>

											<td valign=top align=left>
												 <c:out value="${entry}" escapeXml="false"/> 
											 </td>
										</tr>	
									</c:forEach>		
								</table>  	
							 </td>
						</tr>	
				</table>		  	 								
			

