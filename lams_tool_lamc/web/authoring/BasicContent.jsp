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

				<table class="forms">
					<tr>   
					<td NOWRAP class=error>
						<c:if test="${sessionScope.sbmtSuccess == 1}"> 			
							<img src="<c:out value="${tool}"/>images/success.gif" align="left" width=20 height=20>  
								<bean:message key="sbmt.successful"/> 
							</img>
						</c:if> 			
					</td>
					</tr> 
					<tr> <td>
						<jsp:include page="/McErrorBox.jsp" />
					</td> </tr>
				</table>
				
				<table class="forms">
					
				<c:if test="${ (sessionScope.activeModule == 'authoring') ||  
							   (sessionScope.defineLaterInEditMode == 'true') 
							  }"> 			
 						<tr> 
							<td colspan=2 NOWRAP valign=top>
								<lams:SetEditor id="richTextTitle" text="${sessionScope.richTextTitle}" small="true" key="label.authoring.title.col"/>																
							</td> 
					  	</tr>
					  	
					  	<tr> 
							<td colspan=2 NOWRAP valign=top>
								<lams:SetEditor id="richTextInstructions" text="${sessionScope.richTextInstructions}" key="label.authoring.instructions.col"/>																
							</td>
						</tr>
					</c:if> 										
					
				<c:if test="${ (sessionScope.activeModule == 'defineLater') &&  
							   (sessionScope.defineLaterInEditMode != 'true') 
							  }"> 			
 						<tr> 
					 		<td NOWRAP valign=top align=left> <b> <bean:message key="label.authoring.title"/>: </b> </td>
							<td NOWRAP valign=top align=left>
									  <c:out value="${sessionScope.richTextTitle}" escapeXml="false" />						  
							</td> 
					  	</tr>
					  	
					  	<tr> 
					 		<td NOWRAP  valign=top align=left> <b> <bean:message key="label.authoring.instructions"/>:  </b> </td>
							<td NOWRAP  valign=top align=left>
									  <c:out value="${sessionScope.richTextInstructions}" escapeXml="false" />						  
							</td>
						</tr>
					</c:if> 										
					
					<tr>
					 	<td NOWRAP colspan=2 align=center valign=top>								

					  	</td>
					</tr>
				
					<tr> <td NOWRAP colspan=2 align=center valign=top>
					<table width="40%">
					     <tr>
								  	<td NOWRAP bgcolor="#A8C7DE" colspan=5 valign=top align=left>
									  	 <b> <bean:message key="label.mc.questions"/> </b> 
								  	</td>
						 </tr>					
					
			  	 		<c:set var="queIndex" scope="session" value="0"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionsContent}">
							<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
							<c:if test="${ (sessionScope.activeModule == 'authoring') ||  
								   (sessionScope.defineLaterInEditMode == 'true') 
								  }"> 			

								  <tr>
								  	<td NOWRAP bgcolor="#EEEEEE"  valign=top>  <b> <bean:message key="label.question.only"/> ${queIndex} : </b>   </td>
								  	
							  		<td NOWRAP bgcolor="#EEEEEE"  valign=top width=50> 
								  		 <input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>" 
								  		size="30" maxlength="255" > 
								  	</td>
	
								  	<td NOWRAP bgcolor="#EEEEEE"   align=center valign=top>			
									 		<c:if test="${sessionScope.queIndex == 1}"> 		
												<a title="<bean:message key='label.tip.moveQuestionDown'/>" href="javascript:;" onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">
			                                                <img src="<c:out value="${tool}"/>images/down.gif" border="0">
												</a> 
			   								</c:if> 			
			   								
							  				<c:if test="${(sessionScope.maxQuestionIndex == sessionScope.queIndex) && (sessionScope.queIndex != 1)}"> 			
												<a title="<bean:message key='label.tip.moveQuestionUp'/>" href="javascript:;" onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">
			                                                <img src="<c:out value="${tool}"/>images/up.gif" border="0">
												</a> 
			     							</c:if> 	    
											
			 				  				<c:if test="${(sessionScope.maxQuestionIndex != sessionScope.queIndex) && (sessionScope.queIndex != 1)}"> 			
												<a title="<bean:message key='label.tip.moveQuestionDown'/>" href="javascript:;" onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">
			                                                <img src="<c:out value="${tool}"/>images/down.gif" border="0">
												</a> 
												<a title="<bean:message key='label.tip.moveQuestionUp'/>" href="javascript:;" onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">
			                                                <img src="<c:out value="${tool}"/>images/up.gif" border="0">
												</a> 
											</c:if>
	                                </td>
	
	                                    <td NOWRAP bgcolor="#EEEEEE"  align=left valign=top>										  	
												<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">       
														<c:if test="${questionEntry.key == weightsEntry.key}"> 			
													  			<input type="text" name="questionWeight<c:out value="${queIndex}"/>" value="<c:out value="${weightsEntry.value}"/>"
														  		size="3" maxlength="3">
														  		<bean:message key="label.percent"/>
														</c:if>
												</c:forEach>
	                                    </td>
																						
	                                   <td NOWRAP bgcolor="#EEEEEE"  valign=top>								
												<a title="<bean:message key='label.tip.editOptions'/>" href="javascript:;" onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','editOptions');">
			                                                <img src="<c:out value="${tool}"/>images/edit.gif" border="0">
												</a> 

												<a title="<bean:message key='label.tip.removeQuestion'/>" href="javascript:;" onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','removeQuestion');">
			                                                <img src="<c:out value="${tool}"/>images/delete.gif" border="0">
												</a> 
									  	</td>
								  </tr>
							</c:if> 																		  								  
							
							<c:if test="${ (sessionScope.activeModule == 'defineLater') &&
										   (sessionScope.defineLaterInEditMode != 'true') 
										  }"> 			

								  <tr>
								  	<td NOWRAP bgcolor="#EEEEEE"  valign=top>   <b> <bean:message key="label.question.only"/> ${queIndex} : </b>   </td>
								  	
							  		<td NOWRAP bgcolor="#EEEEEE"  valign=top> 
								  		 <c:out value="${questionEntry.value}"/> 
								  	</td>
	
								  	<td NOWRAP bgcolor="#EEEEEE"  align=center valign=top>			
									 		<c:if test="${sessionScope.queIndex == 1}"> 		
			   								    <img src="<c:out value="${tool}"/>images/down.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
			   								</c:if> 			
			
							  				<c:if test="${(sessionScope.maxQuestionIndex == sessionScope.queIndex) && (sessionScope.queIndex != 1)}"> 										  				
			     								 <img src="<c:out value="${tool}"/>images/up.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
			     							</c:if> 	    
											
			 				  				<c:if test="${(sessionScope.maxQuestionIndex != sessionScope.queIndex) && (sessionScope.queIndex != 1)}"> 			
			   								    <img src="<c:out value="${tool}"/>images/down.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
			       								<img src="<c:out value="${tool}"/>images/up.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
											</c:if> 	           								 
	                                </td>
	
                                    <td NOWRAP bgcolor="#EEEEEE" align=left valign=top>										  	
											<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">                                            
													<c:if test="${questionEntry.key == weightsEntry.key}"> 			
														 <c:out value="${weightsEntry.value}"/><bean:message key="label.percent"/> 
													</c:if>
											</c:forEach>
                                    </td>
																					
                                   <td NOWRAP bgcolor="#EEEEEE"  valign=top>								
											<img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
											<img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
								  	</td>
								  </tr>
							</c:if> 																		  								  
						</c:forEach>

						<c:if test="${ (sessionScope.activeModule == 'authoring') ||  
							   			(sessionScope.defineLaterInEditMode == 'true') 
							  }"> 			
							 <tr>
								  	<td NOWRAP bgcolor="#EEEEEE" colspan=5  valign=top align=right>
											<html:submit onclick="javascript:submitMethod('addNewQuestion');" styleClass="button">
												<bean:message key="label.add.question"/>
											</html:submit>	 				 		  										  		
								  	</td>
							 </tr>							
						</c:if> 							


							<c:if test="${ (sessionScope.activeModule == 'defineLater') &&  
										   (sessionScope.defineLaterInEditMode != 'true') 
										  }"> 			
							 <tr>
								  	<td NOWRAP bgcolor="#EEEEEE" colspan=5  valign=top align=right>
											<html:submit styleClass="button" disabled="true" onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
												<bean:message key="label.add.question"/>
											</html:submit>	 				 		  										  		
								  	</td>
							 </tr>													
						</c:if> 													
						
					</table> </td> </tr>
							<html:hidden property="questionIndex"/>
					<tr>
	 				 	<td colspan=5 align=center>
					  	</td>
					</tr>		
					
					<c:if test="${ (sessionScope.activeModule == 'authoring') ||  
						   			(sessionScope.defineLaterInEditMode == 'true') 
						  }"> 			

						<tr>
						  	<td colspan=5 NOWRAP valign=top><b> <bean:message key="radiobox.passmark"/>:  </b> 
								  <html:text property="passmark"  size="3" maxlength="3"/><bean:message key="label.percent"/>
							</td>
						</tr>
						
						<tr>
		 				 	<td NOWRAP colspan=5 align=center valign=top>
						  	</td>
						</tr>
							
						<tr>
		 				 	<td NOWRAP colspan=5 align=center valign=top>								
						  	</td>
						</tr>
	
					</c:if> 													
					
					<c:if test="${ (sessionScope.activeModule == 'defineLater') &&
								   (sessionScope.defineLaterInEditMode != 'true') 
								  }"> 			

						<tr>
						  	<td NOWRAP colspan=5 valign=top align=left><b>  <bean:message key="radiobox.passmark"/>:  </b>
										<c:out value="${sessionScope.passMark}"/> <bean:message key="label.percent"/>
							</td>
						</tr>
						<tr>
		 				 	<td NOWRAP colspan=5 align=center valign=top>
						  	</td>
						</tr>
							
						<tr>
		 				 	<td NOWRAP colspan=5 align=center valign=top>								
						  	</td>
						</tr>
						
						
						 <tr>
							<td NOWRAP colspan=5 align=right valign=top>								
									<html:submit onclick="javascript:submitMethod('editActivityQuestions');" styleClass="buttonLeft">
										<bean:message key="label.edit"/>
									</html:submit>	 				 		  					
			 			  	</td>
						</tr>

						
					</c:if> 																																						
 				</table> 	 


	      	<c:if test="${(activeModule == 'defineLater') && (defineLaterInEditMode == 'true')}"> 					
				<p align="right">
				    <a href="javascript:submitMethod('submitQuestions')" class="button">
			        	<bean:message key="label.save"/></a>
				</p>
			</c:if> 					

	      	<c:if test="${activeModule == 'monitoring' }"> 					
				<p align="right">
				    <a href="javascript:submitMethod('submitQuestions')" class="button">
			        	<bean:message key="label.save"/></a>
				</p>
			</c:if> 					

			<c:if test="${ activeModule == 'authoring' }"> 					
					<p align="right">
					<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
					<lams:AuthoringButton formID="authoringForm" clearSessionActionUrl="/clearsession.do" toolSignature="lamc11" 
					cancelButtonLabelKey="label.cancel" saveButtonLabelKey="label.save" toolContentID="${formBean.toolContentID}" />		
				</p>
			
			</c:if> 					
 				