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

	<html:hidden property="responseId"/>	 
	<html:hidden property="selectedToolSessionId"/>							
	<input type="hidden" name="isToolSessionChanged"/>
	
		<c:if test="${(userExceptionNoToolSessions == 'true')}"> 	
				<table align="center">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <font size=2> <bean:message key="error.noLearnerActivity"/> </font></b>
						</td> 
					<tr>
				</table>
		</c:if>			


		<c:if test="${(userExceptionNoToolSessions != 'true') }"> 	
		
			<c:if test="${currentMonitoredToolSession == 'All'}"> 				
				<table align="left">
				<tr>
			 		<td NOWRAP> <font size=2> <bean:message key="label.select.session"/>  </td>
			 		<td NOWRAP align=right> <font size=2> 
						<jsp:include page="/monitoring/PullDownMenu.jsp" />
			 		</td>
				</tr>
				</table>
			</c:if> 	    

			<c:if test="${currentMonitoredToolSession != 'All'}"> 							

				<table align="left">
						<tr> 
					 		<td NOWRAP> </td>
							<td NOWRAP align=right>
								<jsp:include page="/monitoring/PullDownMenu.jsp" />					
							</td> 
						</tr>

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

									<c:if test="${VoteMonitoringForm.showOpenVotesSection == 'false'}"> 										
		                                <html:submit property="viewOpenVotes" 
		                                             styleClass="linkbutton" 
		                                             onclick="submitMethod('viewOpenVotes');">
		                                    <bean:message key="label.view.openVotes"/>
		                                </html:submit>
									</c:if> 					                                
		                                
									<c:if test="${VoteMonitoringForm.showOpenVotesSection == 'true'}"> 										
		                                <html:submit property="closeOpenVotes" 
		                                             styleClass="linkbutton" 
		                                             onclick="submitMethod('closeOpenVotes');">
		                                    <bean:message key="label.view.closeVotes"/>
		                                </html:submit>
									</c:if> 					                                		                                
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
											 <font size=2>	<bean:message key="label.view.chart"/>  </font>
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

						<c:if test="${VoteMonitoringForm.showOpenVotesSection == 'true'}"> 										
							<HR>
							<tr>
						 		<td NOWRAP colspan=2 > &nbsp&nbsp </td>
							</tr>
							<tr>
						 		<td NOWRAP colspan=2 > &nbsp&nbsp </td>
							</tr>
	
							<tr>
						 		<td NOWRAP colspan=2>
					 			<table align=left border=1>
					 					<tr>
									 		<td NOWRAP align=center> 
												<b> <font size=2> <bean:message key="label.openVotes"/> </b>
											</td>
										</tr>
					 			
									<c:forEach var="currentDto" items="${sessionScope.listUserEntries}">
							  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
										<tr>			
											<td NOWRAP valign=top align=left><b> <font size=2> <bean:message key="label.vote"/>: </b>
												<c:out value="${currentDto.question}" escapeXml="false"/>
											</font> </td>
										</tr>	
										
										<tr> 
											<td NOWRAP valign=top>
												<table align=center>
													<tr> 
														 <td NOWRAP valign=top> <b> <font size=2> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<bean:message key="label.user"/> </font> </b> </td>  
								  						 <td NOWRAP valign=top> <b> <font size=2> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<bean:message key="label.attemptTime"/></font> </b></td>
								  						 <td NOWRAP valign=top> <b> <font size=2> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<bean:message key="label.timezone"/> </font>	</b></td>
										  			</tr>				 
				
						  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
											  	 		<c:set var="userData" scope="request" value="${questionAttemptData.value}"/>
														<tr> 
																 <td NOWRAP valign=top>  &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
																 <td NOWRAP valign=top>  &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
																 <td NOWRAP valign=top>  &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<font size=2> <c:out value="${userData.timeZone}"/> </font> </td>
														</tr>		
													</c:forEach>		  	
												</table>
											</td>  
							  			</tr>
									</c:forEach>		
								<table> 
								</td>
							</tr>
							
						</c:if> 			

				</table>
			</c:if> 	    	  
		</c:if>						


