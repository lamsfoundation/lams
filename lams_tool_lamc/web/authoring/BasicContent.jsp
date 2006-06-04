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


				<c:if test="${sessionScope.activeModule != 'defineLater'}"> 			
					<h2><font size=2> <bean:message key="label.authoring.mc.basic"/> </font> </h2>	
				</c:if> 				
				
		    	<table align=center> 	  
					<tr>   
					<td NOWRAP class=error>
						<c:if test="${sessionScope.sbmtSuccess == 1}"> 			
							<img src="<c:out value="${tool}"/>images/success.gif" align="left" width=20 height=20>  
								<font size=2> <bean:message key="sbmt.successful"/> </font> 
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
					 		<td NOWRAP class="formlabel" valign=top> <b> <font size=2> <bean:message key="label.authoring.title"/>: </font> </b> </td>
							<td NOWRAP valign=top>
								<lams:SetEditor id="richTextTitle" text="${sessionScope.richTextTitle}" small="true"/>								
							</td> 
					  	</tr>
					  	
					  	<tr> 
					 		<td NOWRAP class="formlabel" valign=top> <b> <font size=2> <bean:message key="label.authoring.instructions"/>: </font> </b> </td>
							<td NOWRAP valign=top>
								<lams:SetEditor id="richTextInstructions" text="${sessionScope.richTextInstructions}" small="true"/>																
							</td>
						</tr>
					</c:if> 										
					
				<c:if test="${ (sessionScope.activeModule == 'defineLater') &&  
							   (sessionScope.defineLaterInEditMode != 'true') 
							  }"> 			
 						<tr> 
					 		<td NOWRAP class="formlabel" valign=top align=left> <b> <font size=2> <bean:message key="label.authoring.title"/>: </font> </b> </td>
							<td NOWRAP class="formcontrol" valign=top align=left>
									  <c:out value="${sessionScope.richTextTitle}" escapeXml="false" />						  
							</td> 
					  	</tr>
					  	
					  	<tr> 
					 		<td NOWRAP class="formlabel" valign=top align=left> <b> <font size=2> <bean:message key="label.authoring.instructions"/>: </font> </b> </td>
							<td NOWRAP class="formcontrol" valign=top align=left>
									  <c:out value="${sessionScope.richTextInstructions}" escapeXml="false" />						  
							</td>
						</tr>
					</c:if> 										
					
					<tr>
					 	<td NOWRAP colspan=2 align=center valign=top>								

					  	</td>
					</tr>
				
					<tr> <td NOWRAP colspan=2 align=center valign=top>
					<table align="center" border="1" summary="layout and Content" width="55%">
					     <tr>
								  	<td NOWRAP bgcolor="#A8C7DE" colspan=5 class="input" valign=top align=left>
									  	 <font size=2> <b> <bean:message key="label.mc.questions"/> </b> </font>
								  	</td>
						 </tr>					
					
			  	 		<c:set var="queIndex" scope="session" value="0"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionsContent}">
							<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
							<c:if test="${ (sessionScope.activeModule == 'authoring') ||  
								   (sessionScope.defineLaterInEditMode == 'true') 
								  }"> 			

								  <tr>
								  	<td NOWRAP bgcolor="#EEEEEE" class="input" valign=top>  <font size=2> <b> <c:out value="Question ${queIndex}"/> : </b>  </font> </td>
								  	
							  		<td NOWRAP bgcolor="#EEEEEE" class="input" valign=top width=50> 
								  		<font size=2> <input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>" 
								  		size="50" maxlength="255" > </font>
								  	</td>
	
								  	<td NOWRAP bgcolor="#EEEEEE" class="input"  align=center valign=top>			
								 		<c:if test="${sessionScope.queIndex == 1}"> 		
			   								 <font size=2>  <img src="<c:out value="${tool}"/>images/down.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');"> </font>
		   								</c:if> 			
		   								
		
						  				<c:if test="${sessionScope.maxQuestionIndex == sessionScope.queIndex}"> 			
		     								 <font size=2> <img src="<c:out value="${tool}"/>images/up.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');"> </font>
		     							</c:if> 	    
										
		 				  				<c:if test="${(sessionScope.maxQuestionIndex != sessionScope.queIndex) && (sessionScope.queIndex != 1)}"> 			
			 				  				<font size=2>
			   								    <img src="<c:out value="${tool}"/>images/down.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">		  	   								 
			       								<img src="<c:out value="${tool}"/>images/up.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">		  	
			       							</font>
										</c:if> 	           								 
	                                </td>
	
	                                    <td NOWRAP bgcolor="#EEEEEE" class="input" align=left valign=top>										  	
		                                    <font size=2>
												<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">       
														<c:if test="${questionEntry.key == weightsEntry.key}"> 			
													  			<input type="text" name="questionWeight<c:out value="${queIndex}"/>" value="<c:out value="${weightsEntry.value}"/>"
														  		size="3" maxlength="3">
														  		<bean:message key="label.percent"/>
														</c:if>
												</c:forEach>
											</font>
	                                    </td>
																						
	                                   <td NOWRAP bgcolor="#EEEEEE" class="input" valign=top>								
											<font size=2>	                                   
												<img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','editOptions');">	
												<img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="javascript:submitModifyQuestion('<c:out value="${queIndex}"/>','removeQuestion');">	
											<font>											
									  	</td>
								  </tr>
							</c:if> 																		  								  
							
							<c:if test="${ (sessionScope.activeModule == 'defineLater') &&
										   (sessionScope.defineLaterInEditMode != 'true') 
										  }"> 			

								  <tr>
								  	<td NOWRAP bgcolor="#EEEEEE" class="input" valign=top>  <font size=2> <b> <c:out value="Question ${queIndex}"/> : </b>  </font> </td>
								  	
							  		<td NOWRAP bgcolor="#EEEEEE" class="input" valign=top> 
								  		<font size=2> <c:out value="${questionEntry.value}"/> </font>
								  	</td>
	
								  	<td NOWRAP bgcolor="#EEEEEE" class="input"  align=center valign=top>			
									  	<font size=2>
									 		<c:if test="${sessionScope.queIndex == 1}"> 		
			   								    <img src="<c:out value="${tool}"/>images/down.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
			   								</c:if> 			
			
							  				<c:if test="${sessionScope.maxQuestionIndex == sessionScope.queIndex}"> 			
			     								 <img src="<c:out value="${tool}"/>images/up.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
			     							</c:if> 	    
											
			 				  				<c:if test="${(sessionScope.maxQuestionIndex != sessionScope.queIndex) && (sessionScope.queIndex != 1)}"> 			
			   								    <img src="<c:out value="${tool}"/>images/down.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
			       								<img src="<c:out value="${tool}"/>images/up.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
											</c:if> 	           								 
										</font>
	                                </td>
	
                                    <td NOWRAP bgcolor="#EEEEEE" class="input" align=left valign=top>										  	
	                                    <font size=2>
											<c:forEach var="weightsEntry" items="${sessionScope.mapWeights}">                                            
													<c:if test="${questionEntry.key == weightsEntry.key}"> 			
														<font size=2> <c:out value="${weightsEntry.value}"/><bean:message key="label.percent"/> </font>
													</c:if>
											</c:forEach>
										</font>
                                    </td>
																					
                                   <td NOWRAP bgcolor="#EEEEEE" class="input" valign=top>								
										<font size=2>                                   
											<img src="<c:out value="${tool}"/>images/edit.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
											<img src="<c:out value="${tool}"/>images/delete.gif" align=left onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
										</font>
								  	</td>
								  </tr>
							</c:if> 																		  								  
						</c:forEach>

						<c:if test="${ (sessionScope.activeModule == 'authoring') ||  
							   			(sessionScope.defineLaterInEditMode == 'true') 
							  }"> 			
							 <tr>
								  	<td NOWRAP bgcolor="#EEEEEE" colspan=5 class="input" valign=top align=right>
									  	<font size=2>
											<html:submit onclick="javascript:submitMethod('addNewQuestion');" styleClass="button">
												<bean:message key="label.add.question"/>
											</html:submit>	 				 		  										  		
										</font>
								  	</td>
							 </tr>							
						</c:if> 							


							<c:if test="${ (sessionScope.activeModule == 'defineLater') &&  
										   (sessionScope.defineLaterInEditMode != 'true') 
										  }"> 			
							 <tr>
								  	<td NOWRAP bgcolor="#EEEEEE" colspan=5 class="input" valign=top align=right>
									  	<font size=2>
											<html:submit styleClass="button" disabled="true" onclick="javascript:alert('This button is disabled in this [view-only] mode.');" >
												<bean:message key="label.add.question"/>
											</html:submit>	 				 		  										  		
										</font>
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
						  	<td NOWRAP class="formlabel" valign=top><b> <font size=2> <bean:message key="radiobox.passmark"/>: </font> </b> </td>
							<td NOWRAP class="input" valign=top> 
								<font size=2>
								  <html:text property="passmark"  size="3" maxlength="3"/><bean:message key="label.percent"/>
								 </font>
							</td>
							<td NOWRAP valign=top> </td> 
							<td NOWRAP valign=top> </td>
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
						  	<td NOWRAP class="formlabel" valign=top align=left><b> <bean:message key="radiobox.passmark"/>: </b></td>
							<td NOWRAP class="input" valign=top align=left> 
								<font size=2>
										<c:out value="${sessionScope.passMark}"/> <bean:message key="label.percent"/>
								</font>
							</td>
							<td NOWRAP valign=top> </td> 
							<td NOWRAP valign=top> </td>
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
	 						<td NOWRAP valign=top> </td> 
							<td NOWRAP class="input" colspan=3 align=left valign=top>								
								<font size=2>
									<html:submit onclick="javascript:submitMethod('editActivityQuestions');" styleClass="buttonLeft">
										<bean:message key="label.edit"/>
									</html:submit>	 				 		  					
								</font>
			 			  	</td>
							<td NOWRAP valign=top> </td> 
							<td NOWRAP valign=top> </td>
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
 				