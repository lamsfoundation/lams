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

			<c:if test="${currentMonitoredToolSession != 'All'}"> 							

				<table align="left">
				
						<c:if test="${statsTabActive != 'true'}"> 							
							<tr> 
						 		<td NOWRAP> </td>
								<td NOWRAP align=right>
									<jsp:include page="/monitoring/PullDownMenu.jsp" />					
								</td> 
							</tr>
						</c:if> 
						<c:if test="${statsTabActive == 'true'}"> 							
							<tr> 
						 		<td NOWRAP> </td>
								<td NOWRAP align=right>
									<jsp:include page="/monitoring/PullDownMenuStats.jsp" />					
								</td> 
							</tr>
						</c:if> 

						<tr>
					 		<td NOWRAP colspan=2 > &nbsp&nbsp </td>
						</tr>

	  					<tr>
					 		<td NOWRAP> <b> <font size=2> <bean:message key="label.total.students"/> </b> </td>
					 		<td> <font size=2> <c:out value="${VoteMonitoringForm.sessionUserCount}"/> </font></td>
						</tr>

	  					<tr>
					 		<td NOWRAP> <b> <font size=2> <bean:message key="label.total.completed.students"/> </b> </td> 
					 		<td> <font size=2> <c:out value="${VoteMonitoringForm.completedSessionUserCount}"/> </font></td>
						</tr>

						<tr>
					 		<td NOWRAP colspan=2 > &nbsp&nbsp </td>
						</tr>
	  				
						<tr>
					 		<td NOWRAP colspan=2> 
		                            <c:out value="${activityInstructions}" escapeXml="false"/> &nbsp
							</td>
						</tr>
						

						<tr>
					 		<td NOWRAP colspan=2 > &nbsp&nbsp </td>
						</tr>
						
						<tr>
							<td NOWRAP valign=top align=left>
							<table align=center>
								<tr> 
									<td> </td>
									<td NOWRAP valign=top align=left >
										<c:set var="viewURL">
											<html:rewrite page="/chartGenerator?type=pie"/>
										</c:set>
										<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
											 <font size=2>	<bean:message key="label.view.piechart"/>  </font>
										</a>
									</td>
								</tr>
								<tr> 
									<td> </td>
									<td NOWRAP valign=top align=left >
										<c:set var="viewURL">
											<html:rewrite page="/chartGenerator?type=bar"/>
										</c:set>
										<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
											 <font size=2>	<bean:message key="label.view.barchart"/>  </font>
										</a>
									</td>
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
							</table>
							</td>
						</tr>


							<tr>
						 		<td NOWRAP colspan=2 > &nbsp&nbsp </td>
							</tr>
						
							<tr>
						 		<td NOWRAP colspan=2 > <HR> </td>
							</tr>

							<tr>
						 		<td NOWRAP colspan=2 > &nbsp&nbsp </td>
							</tr>
	
							<tr>
						 		<td NOWRAP colspan=2>
					 			<table align=left>
					 					<tr>
									 		<td NOWRAP align=center> 
												<b> <font size=2> <bean:message key="label.openVotes"/> </b>
											</td>
										</tr>
					 			
									<c:forEach var="currentDto" items="${sessionScope.listUserEntries}">

										<tr> 
											<td NOWRAP valign=top>
												<table align=center>
													<tr> 
														 <td NOWRAP valign=top align=left> <b> <font size=2>  <bean:message key="label.vote"/> </font> </b> </td>  														 
														 <td NOWRAP valign=top align=left> <b> <font size=2>  <bean:message key="label.user"/> </font> </b> </td>  
								  						 <td NOWRAP valign=top align=left> <b> <font size=2>  <bean:message key="label.attemptTime"/></font> </b></td>
								 						 <c:if test="${statsTabActive != 'true'}"> 															  						 
									  						 <td NOWRAP valign=top align=left> <b> <font size=2>  <bean:message key="label.visible"/> </font> </b></td>								  						 
														 </c:if> 																					  						 
										  			</tr>				 
				
						  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
											  	 		<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
	  	 									  	 		<c:set var="currentUid" scope="request" value="${userData.uid}"/>
														<tr> 
																<td NOWRAP valign=top align=left> 
																	<c:out value="${currentDto.question}" escapeXml="false"/> 
																	<c:if test="${userData.visible != 'true' }"> 			
												                                <font size=2> <i><bean:message key="label.hidden"/> </i> </font>											                                
																	</c:if> 								
																</td>
																 
																 <td NOWRAP valign=top align=left>   <font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
																 <td NOWRAP valign=top align=left>   <font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
																 
										 						<c:if test="${statsTabActive != 'true'}"> 							
																	 <td NOWRAP valign=top align=left>
												 						<c:if test="${userData.visible == 'true' }"> 			
													                                <html:submit property="hideOpenVote" 
													                                             styleClass="linkbutton" 
											                                                     onclick="submitOpenVote(${currentUid}, 'hideOpenVote');">						                                             
													                                    <bean:message key="label.hide"/>
													                                </html:submit>
																		</c:if> 													
							
																		<c:if test="${userData.visible != 'true' }"> 			
													                                <html:submit property="showOpenVote" 
													                                             styleClass="linkbutton" 
											                                                     onclick="submitOpenVote(${currentUid}, 'showOpenVote');">						                                             
													                                    <bean:message key="label.show"/>
													                                </html:submit>
																		</c:if> 						
																	</td>																			
															</c:if> 	    																	
																 
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
			</c:if> 	    	  
