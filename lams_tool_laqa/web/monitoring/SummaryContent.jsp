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

<%@ include file="/common/taglibs.jsp" %>

	<html:hidden property="responseId"/>	 
	
		<c:if test="${(qaGeneralMonitoringDTO.userExceptionNoToolSessions == 'true')}"> 	
				<table class="forms">
					<tr> 
						<td NOWRAP valign=top align=center> 
							<b> <bean:message key="error.noLearnerActivity"/> </b>
						</td> 
					<tr>
				</table>
		</c:if>			
					
		<c:if test="${(qaGeneralMonitoringDTO.userExceptionNoToolSessions != 'true') }"> 	
			<html:hidden property="selectedToolSessionId"/>							
			<input type="hidden" name="isToolSessionChanged"/>
				<table class="forms">

				<c:if test="${(generalLearnerFlowDTO.requestLearningReport != 'true')}"> 	
						<tr> 
							<td NOWRAP  valign=top align=center> <b> <bean:message key="label.selectGroup"/> </b>
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

					<c:forEach var="currentDto" items="${generalLearnerFlowDTO.listMonitoredAnswersContainerDTO}">
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>
			  	 		<tr>
			  	 			<td> &nbsp&nbsp&nbsp</td>
			  	 		</tr>
						<tr>			
							<td NOWRAP valign=top align=left><b>  <bean:message key="label.question"/> : </b> 
								<c:out value="${currentDto.question}" escapeXml="false"/>
							 </td>
						</tr>	
						
						<tr> 
							<td NOWRAP valign=top>
								<table align=center>
									<tr> 
										 <td NOWRAP valign=top> <b>  <bean:message key="label.user"/>  </b> </td>  
				  						 <td NOWRAP valign=top> <b>  <bean:message key="label.attemptTime"/> </b></td>
				  						 <td NOWRAP valign=top> <b>  <bean:message key="label.response"/> 	</b></td>
						  			</tr>				 
		  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
								  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
								  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>

	  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			
												<tr> 
													<c:if test="${qaGeneralMonitoringDTO.editResponse != 'true'}">	  	 									 			
														 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
														 <td NOWRAP valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>														 
														 <td NOWRAP valign=top>   <c:out value="${userData.response}"/> 
											
														<c:if test="${userData.visible != 'true' }"> 			
											                         <i><bean:message key="label.response.hidden"/> </i> 
														</c:if> 								
														 </td>
												
														
															 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','editResponse');">	</td>	  	 
															 <td NOWRAP valign=top> 				 
															<c:if test="${userData.visible == 'true' }"> 			
											                            <html:submit property="hideResponse" 
											                                         styleClass="linkbutton" 
											                                         onclick="submitResponse(${responseUid}, 'hideResponse');">						                                             
											                                <bean:message key="label.hide"/>
											                            </html:submit>
															</c:if> 													
											
															<c:if test="${userData.visible != 'true' }"> 			
											                            <html:submit property="showResponse" 
											                                         styleClass="linkbutton" 
											                                         onclick="submitResponse(${responseUid}, 'showResponse');">						                                             
											                                <bean:message key="label.show"/>
											                            </html:submit>
															</c:if> 						
														 	</td>	  	 
													</c:if>
													
													<c:if test="${qaGeneralMonitoringDTO.editResponse == 'true'}">	  	
														<c:if test="${editableResponseId == responseUid}">	  	 									 			
															 <td NOWRAP valign=top>  <c:out value="${userData.userName}"/>   </td>  
															 <td NOWRAP valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>															 
															 <td NOWRAP valign=top>  <input type="text" name="updatedResponse" value='<c:out value="${userData.response}"/>'> 
											
															<c:if test="${userData.visible != 'true' }"> 			
												                         <i><bean:message key="label.response.hidden"/> </i> 
															</c:if> 								
															 </td>
													
												
																 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/tick.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','updateResponse');">	</td>	  	 
																 <td NOWRAP valign=top> 
																	<c:if test="${userData.visible == 'true' }"> 			
													                            <html:submit property="hideResponse" 
													                                         styleClass="linkbutton" 
													                                         onclick="submitResponse(${responseUid}, 'hideResponse');">						                                             
													                                <bean:message key="label.hide"/>
													                            </html:submit>
																	</c:if> 													
													
																	<c:if test="${userData.visible != 'true' }"> 			
													                            <html:submit property="showResponse" 
													                                         styleClass="linkbutton" 
													                                         onclick="submitResponse(${responseUid}, 'showResponse');">						                                             
													                                <bean:message key="label.show"/>
													                            </html:submit>
																	</c:if> 						
															 	</td>	  	 
															</c:if>
													
													<c:if test="${editableResponseId != responseUid}">	  	 									 			
															 <td NOWRAP valign=top>   <c:out value="${userData.userName}"/>   </td>  
															 <td NOWRAP valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>															 
															 <td NOWRAP valign=top>   <c:out value="${userData.response}"/> 
																<c:if test="${userData.visible != 'true' }"> 			
													                         <i><bean:message key="label.response.hidden"/> </i> 
																</c:if> 								
															 </td>
													
													
																 <td NOWRAP valign=top> <img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:submitEditResponse('<c:out value="${userData.uid}"/>','editResponse');">	</td>	  	 
																 <td NOWRAP valign=top> 
																	<c:if test="${userData.visible == 'true' }"> 			
													                            <html:submit property="hideResponse" 
													                                         styleClass="linkbutton" 
													                                         onclick="submitResponse(${responseUid}, 'hideResponse');">						                                             
													                                <bean:message key="label.hide"/>
													                            </html:submit>
																	</c:if> 													
													
																	<c:if test="${userData.visible != 'true' }"> 			
													                            <html:submit property="showResponse" 
													                                         styleClass="linkbutton" 
													                                         onclick="submitResponse(${responseUid}, 'showResponse');">						                                             
													                                <bean:message key="label.show"/>
													                            </html:submit>
																	</c:if> 						
																 	</td>	  	 
														</c:if>														  					 									  													  			
													</c:if>														  					 									  													  			
												</tr>		




											</c:if>														  					 
	 									</c:forEach>		  	
									</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
					</c:forEach>		  	

				</table>
		</c:if>						


	