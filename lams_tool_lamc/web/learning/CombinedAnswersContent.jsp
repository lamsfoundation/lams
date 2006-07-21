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
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

	<!--options content goes here-->
				<table class="forms">
					  <tr>
					  	<th scope="col" valign=top colspan=2> 
						  	 <bean:message key="label.assessment"/> 
					  	</td>
					  </tr>
					  
					  <tr>
					  	<td NOWRAP align=left valign=top colspan=2> 
						  	<c:out value="${mcGeneralLearnerFlowDTO.activityInstructions}" escapeXml="false" /> 
					  	</td>
					  </tr>
					  
			
			 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 		
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	  <b> <bean:message key="label.withRetries"/> </b>
					  	</td>
					  </tr>
					</c:if> 			
				
					<c:if test="${mcGeneralLearnerFlowDTO.retries == 'false'}"> 		
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						    <b> <bean:message key="label.withoutRetries"/> </b>
					  	</td>
					  </tr>
					</c:if> 			

			 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true' && mcGeneralLearnerFlowDTO.passMark != '0'}"> 		
					  <tr>
					  	<td NOWRAP align=left valign=top colspan=2> 
						  	 <b>  <bean:message key="label.learner.message"/> (<c:out value="${mcGeneralLearnerFlowDTO.passMark}"/><bean:message key="label.percent"/> )  </b>
					  	</td>
					  </tr>
					</c:if>

				  <tr>						 
					  	<td NOWRAP align=left valign=top colspan=2> 
						<table align=left>
								<c:forEach var="dto" varStatus="status" items="${requestScope.listQuestionCandidateAnswersDto}">
									  <tr>
									  	<td NOWRAP align=left valign=top> 
										  		<c:out value="${dto.question}"/> &nbsp[ <b> <bean:message key="label.weight"/> : </b>
										  		<c:out value="${dto.weight}"/> ]
									  	</td>
									  </tr>
									
									  <tr>
									  	<td NOWRAP align=left valign=top> 
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
								</c:forEach>								
						</table>
						</td>
					</tr>


			  	   	<tr> 
				 		<td NOWRAP colspan=2 valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>
			  	   
			  	<html:hidden property="donePreview"/>						   
	  	   		  <tr>
				  	<td NOWRAP colspan=2 align=left valign=top> 
				  			<html:submit property="continueOptionsCombined" styleClass="button">
								<bean:message key="button.continue"/>
							</html:submit>	 				 		  					
				  	 </td>
				  </tr>
		</table>
	<!--options content ends here-->

