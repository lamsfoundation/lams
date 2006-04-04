<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

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

		           <h2><font size=2> <b> <bean:message key="label.authoring.qa.basic"/> </b></font></h2>
		        	<table align=center> 	  
						<tr>   
						<td NOWRAP class=error>
							<c:if test="${sessionScope.sbmtSuccess == 1}"> 			
								<img src="images/success.gif" align="left" width=20 height=20>  <font size=2> <bean:message key="submit.successful"/> </font> </img>
							</c:if> 			
						</td>
						</tr> 

						<tr>   
						<td NOWRAP class=error>
							<c:if test="${userExceptionQuestionsDuplicate == 'true'}"> 			
								<img src="images/error.jpg" align="left" width=20 height=20>  <font size=2> <bean:message key="error.questions.duplicate"/> </font> </img>
							</c:if> 			
						</td>
						</tr> 
					</table>


					<table align=center> 
						<tr> 
					 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.title"/>: </b></font> </td>
							<td NOWRAP valign=top>
								<lams:SetEditor id="title" text="${activityTitle}" small="true"/>								
							</td> 
							
					  	</tr>

					  	<tr> 
					 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.instructions"/>:  </b></font></td>
							<td NOWRAP valign=top>
								<lams:SetEditor id="instructions" text="${activityInstructions}" small="true"/>								
							</td> 
						</tr>
				
			 		<!--default question content, this entry can not be deleted but can be updated -->
				 		<tr> 
						  	<td valign="top"> 
						 		<font size=2> <b> <bean:message key="label.question1"/>:  </b></font>
						 	</td>

							<td NOWRAP valign=top>
								<lams:SetEditor id="questionContent0" text="${sessionScope.defaultQuestionContent}" small="true"/>								
							</td> 
					  	</tr>
		
				  	<!--end of default question content -->
				  	
			  		<!-- if there is more than just the default content start presenting them -->
			  	 		<c:set var="queIndex" scope="session" value="1"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContent}">
					  		<c:if test="${questionEntry.key > 1}"> 			
								<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
								  <tr>
								  	<td valign="top"> <font size=2> <b> <c:out value="Question ${queIndex}"/>:  </b></font></td>

									<td NOWRAP valign=top>

										<lams:SetEditor id="questionContent${queIndex-1}" text="${questionEntry.value}" small="true"/>								
                                
	                                	<c:if test="${ (sessionScope.activeModule != 'monitoring') }"> 			
			 		 						<html:submit property="removeContent" 
	                                                     styleClass="linkbutton"  
	                                                     onclick="removeQuestion(${queIndex});">
												<bean:message key="button.removeQuestion"/>
											</html:submit>
										</c:if> 			
	                                	<c:if test="${ (sessionScope.activeModule == 'monitoring') }"> 			
			 		 						<html:submit property="removeContent" 
	                                                     styleClass="linkbutton"  
	                                                     onclick="removeMonitoringQuestion(${queIndex});">
												<bean:message key="button.removeQuestion"/>
											</html:submit>
										</c:if> 													
										
                                    </td>
								  </tr>
							</c:if> 			
						</c:forEach>
						<html:hidden property="toolContentId" value="${QaAuthoringForm.toolContentId}"/>
						<html:hidden property="questionIndex"/>
                        
                        <tr>
                            <td></td>
                            <td align="right">
                                <html:submit property="addContent" 
                                             styleClass="linkbutton" 
                                             onclick="submitMethod('addNewQuestion');">
                                    <bean:message key="button.addNewQuestion"/>
                                </html:submit>
                            </td>
                        </tr>
					
					</table>

	<HR>
		<p align="right">
	        <a href="javascript:submitMethod('submitAllContent')" class="button"><bean:message key="label.save"/></a>
	        <a href="javascript:window.close()" class="button"><bean:message key="label.cancel"/></a>
		</p>

<SCRIPT language="JavaScript"> 

	function removeQuestion(questionIndex)
	{
		document.QaAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeQuestion');
	}

	function removeMonitoringQuestion(questionIndex)
	{
		document.QaMonitoringForm.questionIndex.value=questionIndex;
        submitMonitoringMethod('removeQuestion');
	}
	
 </SCRIPT>

		