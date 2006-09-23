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
	
					<c:forEach var="groupDto" items="${listAllGroupsDTO}">
			  	 		<c:set var="sessionId" scope="request" value="${groupDto.sessionId}"/>
			  	 		<c:set var="sessionName" scope="request" value="${groupDto.sessionName}"/>
			  	 		<c:set var="groupData" scope="request" value="${groupDto.groupData}"/>
			  	 		
			  	 		<tr>
			  	 			<td> <b> <bean:message key="group.label"/> : </b> <c:out value="${sessionName}"/> </td>
			  	 		</tr>
			  	 		
									  	 		
			  	 		
			  	 		
					<c:forEach var="currentDto" items="${groupData}">
			  	 		<c:set var="currentQuestionId" scope="request" value="${currentDto.questionUid}"/>

			  	 		
						<tr>			
							<td valign=top align=left><b>  <bean:message key="label.question"/> : </b> 
								<c:out value="${currentDto.question}" escapeXml="false"/>
							 </td>
						</tr>	
						
						<tr> 
							<td valign=top>
								<table align=center>

		  							<c:forEach var="questionAttemptData" items="${currentDto.questionAttempts}">
										<c:forEach var="sData" items="${questionAttemptData.value}">
								  	 		<c:set var="userData" scope="request" value="${sData.value}"/>
								  	 		<c:set var="responseUid" scope="request" value="${userData.uid}"/>
								  	 		<c:set var="userSessionId" scope="request" value="${userData.sessionId}"/>
								  	 		
								  	 		
	  	 									<c:if test="${sessionId == userSessionId}"> 											  	 		

		  	 									<c:if test="${currentQuestionId == userData.questionUid}"> 			
														<c:if test="${qaGeneralMonitoringDTO.editResponse != 'true'}">	  	 									 			
															<tr> 
																 <td  valign=top>  <c:out value="${userData.userName}"/>   </td>  
																 <td  valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>														 
																 
																  <td align=right valign=top width="150"> 
																	<b> <bean:message key="label.edit"/> </b>
																	<a title="<bean:message key='label.tooltip.edit'/>" href="javascript:;" onclick="javascript:submitEditGroupResponse('<c:out value="${sessionId}"/>','<c:out value="${userData.uid}"/>','editGroupResponse');">
								                                                <img src="<c:out value="${tool}"/>images/edit.gif" border="0">
																	</a> 
																  </td>
	
			   													 <td align=right valign=top> 				 
																	<c:if test="${userData.visible == 'true' }"> 			
													                            <html:submit property="hideResponse" 
													                                         styleClass="linkbutton" 
													                                         onclick="submitGroupResponse(${sessionId}, ${responseUid}, 'hideGroupResponse');">						                                             
													                                <bean:message key="label.hide"/>
													                            </html:submit>
																	</c:if> 													
													
																	<c:if test="${userData.visible != 'true' }"> 			
													                            <html:submit property="showResponse" 
													                                         styleClass="linkbutton" 
													                                         onclick="submitGroupResponse(${sessionId}, ${responseUid}, 'showGroupResponse');">						                                             
													                                <bean:message key="label.show"/>
													                            </html:submit>
																	</c:if> 						
															 	</td>	  	 
															</tr>	 
															
															<tr>
																 <td colspan=4  valign=top>   
																	<c:if test="${userData.visible != 'true' }"> 			
														                         <i><bean:message key="label.response.hidden"/> </i> 
																	</c:if> 								
																	 <c:out value="${userData.responsePresentable}" escapeXml="false"/> 
																 </td>
															</tr>
														</c:if>
														
														
	
														<c:if test="${qaGeneralMonitoringDTO.editResponse == 'true'}">	  	
	
	
	
																<c:if test="${editableResponseId == responseUid}">	  	 									 			
																	<tr> 
																		 <td valign=top>  <c:out value="${userData.userName}"/>   </td>  
																		 <td valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>														 
															
																		  <td align=right valign=top width="150">  
																		  	<b> <bean:message key="label.save"/> </b>
		
																			<a title="<bean:message key='label.tooltip.tick'/>" href="javascript:;" onclick="javascript:submitEditGroupResponse('<c:out value="${sessionId}"/>','<c:out value="${userData.uid}"/>','updateGroupResponse');">
										                                                <img src="<c:out value="${tool}"/>images/tick.gif" border="0">
																			</a> 
																		  </td>	  	 																	
															
		
					   													 <td align=right valign=top> 				 
																			<c:if test="${userData.visible == 'true' }"> 			
															                            <html:submit property="hideResponse" 
															                                         styleClass="linkbutton" 
															                                         onclick="submitGroupResponse(${sessionId}, ${responseUid}, 'hideGroupResponse');">						                                             
															                                <bean:message key="label.hide"/>
															                            </html:submit>
																			</c:if> 													
															
																			<c:if test="${userData.visible != 'true' }"> 			
															                            <html:submit property="showResponse" 
															                                         styleClass="linkbutton" 
															                                         onclick="submitGroupResponse(${sessionId}, ${responseUid}, 'showGroupResponse');">						                                             
															                                <bean:message key="label.show"/>
															                            </html:submit>
																			</c:if> 						
																	 	</td>	  	 
																	</tr>
		
																	<tr>
																		 <td colspan=4 valign=top>   
																			<c:if test="${userData.visible != 'true' }"> 			
																                         <i><bean:message key="label.response.hidden"/> </i> 
																			</c:if> 								
											 						 			<textarea name="updatedResponse" rows=6 cols=60><c:out value="${userData.response}" escapeXml="false"/></textarea>
																		 </td>
																	</tr>
																	
																</c:if>													
	
	
	
															<c:if test="${editableResponseId != responseUid}">	  	 									 			
																<tr> 
																	 <td valign=top>  <c:out value="${userData.userName}"/>   </td>  
																	 <td valign=top>   <lams:Date value="${userData.attemptTime}"/> </td>														 
	
																	  <td align=right valign=top width="150"> 
																		 <b> <bean:message key="label.edit"/> </b>
																		<a title="<bean:message key='label.tooltip.edit'/>" href="javascript:;" onclick="javascript:submitEditGroupResponse('<c:out value="${sessionId}"/>','<c:out value="${userData.uid}"/>','editGroupResponse');">
									                                                <img src="<c:out value="${tool}"/>images/edit.gif" border="0">
																		</a> 
																	  </td>	  	 
	
				   													 <td align=right valign=top> 				 
																		<c:if test="${userData.visible == 'true' }"> 			
														                            <html:submit property="hideResponse" 
														                                         styleClass="linkbutton" 
														                                         onclick="submitGroupResponse(${sessionId}, ${responseUid}, 'hideGroupResponse');">						                                             
														                                <bean:message key="label.hide"/>
														                            </html:submit>
																		</c:if> 													
														
																		<c:if test="${userData.visible != 'true' }"> 			
														                            <html:submit property="showResponse" 
														                                         styleClass="linkbutton" 
														                                         onclick="submitGroupResponse(${sessionId}, ${responseUid}, 'showGroupResponse');">						                                             
														                                <bean:message key="label.show"/>
														                            </html:submit>
																		</c:if> 						
																 	</td>	  	 
																</tr>
	
																<tr>
																	 <td colspan=4 valign=top>   
																		<c:if test="${userData.visible != 'true' }"> 			
															                         <i><bean:message key="label.response.hidden"/> </i> 
																		</c:if> 								
																		 <c:out value="${userData.responsePresentable}" escapeXml="false"/> 
																	 </td>
																</tr>
																
															</c:if>					
													</c:if>													
														
												</c:if>														  					 									  													  			
											</c:if>														  					 									  													  														

	 									</c:forEach>		
	 									
								  	 		<tr>
								  	 			<td> &nbsp&nbsp&nbsp</td>
								  	 		</tr>
	 									  	
									</c:forEach>		  	
								</table>
							</td>  
			  			</tr>
					</c:forEach>		  	
				</c:forEach>		  	



