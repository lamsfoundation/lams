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

 
	<div id="basicTabContainer">	
		           <h2><font size=2> <b> <bean:message key="label.authoring.qa.basic"/> </b></font></h2>
		        	<table align=center> 	  
						<tr>   
						<td NOWRAP class=error>
							<c:if test="${sessionScope.sbmtSuccess == 1}"> 			
								<img src="images/success.gif" align="left" width=20 height=20>  <font size=2> <bean:message key="submit.successful"/> </font> </img>
							</c:if> 			
						</td>
						</tr> 
					</table>

			<tr> <td>
					<table align=center> 
						<tr> 
					 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.title"/>: </b></font> </td>
					 		<td NOWRAP width=700> 
                                
                                 <span id="previewTitle" style="visibility: hidden; display: none;">
                                     <div>
                                         <a href="javascript:doWYSWYGEdit('Title','small')"><font size=2> <b> <bean:message key="label.openEditor"/></a>
                                     </div>
                                     <div class="smallPreviewPanel" id="previewTitle.text"></div>
                                 </span>
                                 <span id="txTitle">
                                     <div>
                                         <a href="javascript:doTextToHTML('Title'); doWYSWYGEdit('Title','small')"><font size=2> <b> <bean:message key="label.openEditor"/></a>
                                     </div>
                                     <textarea class="smallTextareaPanel" name="title" id="txTitle.textarea"><c:out value="${QaAuthoringForm.title}" escapeXml="false" /></textarea>
                                 </span>
                                
							</td> 
					  	</tr>
					  	<tr> 
					 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.instructions"/>:  </b></font></td>
					 		<td NOWRAP width=700> 
                                <span id="previewInstructions" style="visibility: hidden; display: none;">
                                    <div>
                                        <a href="javascript:doWYSWYGEdit('Instructions')"><font size=2> <b> <bean:message key="label.openEditor"/></a>
                                    </div>
                                    <div class="previewPanel" id="previewInstructions.text"></div>
                                </span>
                                <span id="txInstructions">
                                    <div>
                                        <a href="javascript:doTextToHTML('Instructions'); doWYSWYGEdit('Instructions')"><font size=2> <b><bean:message key="label.openEditor"/></a>
                                    </div>
                                    <textarea class="textareaPanel" name="instructions" id="txInstructions.textarea"><c:out value="${QaAuthoringForm.instructions}" escapeXml="false" /></textarea>
                                </span>
							</td>
						</tr>
				
			 		<!--default question content, this entry can not be deleted but can be updated -->
				 		<tr> 
						  	<td valign="top"> 
						 		<font size=2> <b> <bean:message key="label.question1"/>:  </b></font>
						 	</td>
						  	<td>
                                <span id="previewQuestion0" style="visibility: hidden; display: none;">
                                    <div>
                                        <a href="javascript:doWYSWYGEdit('Question0')"><bean:message key="label.openEditor"/></a>
                                    </div>
                                    <div class="previewPanel" id="previewQuestion0.text"></div>
                                </span>
                                <span id="txQuestion0">
                                    <div>
                                        <a href="javascript:doTextToHTML('Question0'); doWYSWYGEdit('Question0')"><font size=2> <b><bean:message key="label.openEditor"/></a>
                                    </div>
                                    <textarea class="textareaPanel" name="questionContent0" id="txQuestion0.textarea"><c:out value="${sessionScope.defaultQuestionContent}"/></textarea>
                                </span>
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
								  	<td>
  
                                        <span id="preview<c:out value="Question${queIndex-1}"/>" style="visibility: hidden; display: none;">
                                            <div>
                                                <a href="javascript:doWYSWYGEdit('<c:out value="Question${queIndex-1}"/>')"><font size=2> <b><bean:message key="label.openEditor"/></a>
                                            </div>
                                            <div class="previewPanel" id="preview<c:out value="Question${queIndex-1}"/>.text"></div>
                                        </span>
                                        <span id="tx<c:out value="Question${queIndex-1}"/>">
                                            <div>
                                                <a href="javascript:doTextToHTML('<c:out value="Question${queIndex-1}"/>'); doWYSWYGEdit('<c:out value="Question${queIndex-1}"/>')"><font size=2> <b><bean:message key="label.openEditor"/></a>
                                            </div>
                                            <textarea class="textareaPanel" name="<c:out value="questionContent${queIndex-1}"/>" id="tx<c:out value="Question${queIndex-1}"/>.textarea"><c:out value="${questionEntry.value}"/></textarea>
                                        </span>
                                
		 		 						<html:submit property="removeContent" 
                                                     styleClass="linkbutton"  
                                                     onclick="removeQuestion(${queIndex});"
                                                     onmouseover="pviiClassNew(this,'linkbutton')" 
                                                     onmouseout="pviiClassNew(this,'linkbutton')">
											<bean:message key="button.removeQuestion"/>
										</html:submit>
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
                                             onmouseover="pviiClassNew(this,'linkbutton')" 
                                             onmouseout="pviiClassNew(this,'linkbutton')"
                                             onclick="submitMethod('addNewQuestion');">
                                    <bean:message key="button.addNewQuestion"/>
                                </html:submit>
                            </td>
                        </tr>
					
					</table>
			</td></tr>			
</div>	

<SCRIPT language="JavaScript"> 

	function removeQuestion(questionIndex)
	{
		document.QaAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeQuestion');
	}
	
 </SCRIPT>

		