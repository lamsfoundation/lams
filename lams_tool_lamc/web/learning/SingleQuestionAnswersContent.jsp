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

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<!--options content goes here-->
				<table class="forms">
					  <tr>
					  	<td align=left valign=top colspan=2> 
						  	<c:out value="${mcGeneralLearnerFlowDTO.activityInstructions}" escapeXml="false" /> 
					  	</td>
					  </tr>
					  

			 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true' && mcGeneralLearnerFlowDTO.passMark != '0'}"> 		
					  <tr>
					  	<td align=left valign=top colspan=2> 
						  	 <b>  <fmt:message key="label.learner.message"/> ( <c:out value="${mcGeneralLearnerFlowDTO.passMark}"/> )  </b>
						  	 
					  	</td>
					  </tr>
					</c:if>

				  <tr>						 
					  	<td align=left valign=top colspan=2> 
						<table align=left>
						
								<html:hidden property="nextQuestionSelected"/>						
								<html:hidden property="questionIndex" value="${mcGeneralLearnerFlowDTO.questionIndex}"/>						
								<c:forEach var="dto" varStatus="status" items="${requestScope.listQuestionCandidateAnswersDto}">
									<c:if test="${dto.displayOrder == mcGeneralLearnerFlowDTO.questionIndex}"> 										
									  <tr>
									  	<td align=left valign=top> 
											<div style="overflow: auto;">									  	
										  		<c:out value="${dto.question}" escapeXml="false"/> &nbsp[ <b> <fmt:message key="label.mark"/> </b>
										  		<c:out value="${dto.mark}"/> ]
											</div>																			  		
									  	</td>
									  </tr>
									
									  <tr>
									  	<td align=left valign=top> 
											<c:forEach var="ca" varStatus="status" items="${dto.candidateAnswerUids}">
												<input type="checkbox" name="checkedCa" value="${dto.questionUid}-${ca.value}"> 
													
													<c:forEach var="caText" varStatus="status" items="${dto.candidateAnswers}">
														<c:if test="${ca.key == caText.key}"> 		
																<c:out value="${caText.value}" escapeXml="false"/><BR>
														</c:if>															
													</c:forEach>
											</c:forEach>
									  	</td>
									  </tr>
									</c:if>																								  
								</c:forEach>								
						</table>
						</td>
					</tr>


			  	   	<tr> 
				 		<td colspan=2 valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>
			  	   
			  	<html:hidden property="donePreview"/>						   
		  		<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached != 'true'  &&  mcGeneralLearnerFlowDTO.totalQuestionCount != '1'}"> 			
	  	   		  <tr>
				  	<td colspan=2 valign=top> 
						  <img src="<c:out value="${tool}"/>images/green_arrow_down_right.gif" align=left onclick="javascript:submitNextQuestionSelected();"> 							
				  	 </td>
				  </tr>
				</c:if> 								  

		 		<c:if test="${mcGeneralLearnerFlowDTO.totalQuestionCount == '1'}"> 		
	  	   		  <tr>
				  	<td colspan=2 valign=top> 
				  			<html:submit property="continueOptionsCombined" styleClass="button">
								<fmt:message key="button.continue"/>
							</html:submit>	 				 		  					
				  	 </td>
				  </tr>
				</c:if> 								  
				
		 		<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached == 'true'}"> 		
	  	   		  <tr>
				  	<td colspan=2 valign=top> 
				  			<html:submit property="continueOptionsCombined" styleClass="button">
								<fmt:message key="button.continue"/>
							</html:submit>	 				 		  					
				  	 </td>
				  </tr>
				</c:if> 								  
		</table>
	<!--options content ends here-->

