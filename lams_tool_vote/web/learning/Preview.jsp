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


<html:html locale="true">
<head>
	<jsp:include page="/learning/learningHeader.jsp" />
</head>
<body>

<html:form  action="/learning?validate=false" enctype="multipart/form-data"method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolContentID"/>

				<table width="80%" cellspacing="8" align="CENTER" class="forms">
	  					  <tr>
						  	<td NOWRAP align=center class="input" valign=top colspan=2> 
								  <c:out value="${VoteLearningForm.activityTitle}" escapeXml="false" />					 	  		
						  	</td>
						  </tr>
						  
						  <tr>
						  	<td  NOWRAP align=left class="input" valign=top colspan=2> 
								  <c:out value="${VoteLearningForm.activityInstructions}" escapeXml="false" />					 	  						 	  							  	
						  	</td>
						  </tr>
	
	
							  <tr>						 
								<td NOWRAP align=left>
								<table align=left>
					  		  	 		<c:set var="queIndex" scope="session" value="0"/>
											  		<c:forEach var="subEntry" items="${sessionScope.mapQuestionContentLearner}">
												  		<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
															<tr> 
																<td NOWRAP align=left class="input" valign=top> 
																	<font size=2>
																		<input type="checkbox" 
																		name=optionCheckBox<c:out value="${sessionScope.queIndex}"/>-<c:out value="${subEntry.key}"/>> 
																			  <c:out value="${subEntry.value}" escapeXml="false" />																			
																	</font>
																</td> 
															</tr>	
													</c:forEach>
														<tr> 
															<td NOWRAP align=left class="input" valign=top  colspan=2> 
												      			<font size=2> <b>
															 		<bean:message key="label.other"/>: 
														      		</b> 
														 			<html:text property="userEntry" size="30" maxlength="100"/>
																</font>									 			
													 		</td>
													  	</tr>
								</table>
								</td>
							</tr>
							  

				
					  	   	<html:hidden property="optionCheckBoxSelected"/>
							<html:hidden property="questionIndex"/>
							<html:hidden property="optionIndex"/>
							<html:hidden property="optionValue"/>						
							<html:hidden property="checked"/>
				  	   
				  	<html:hidden property="donePreview"/>						   

				</table>
</html:form>

</body>
</html:html>

