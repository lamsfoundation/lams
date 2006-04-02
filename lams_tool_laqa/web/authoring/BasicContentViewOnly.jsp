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

	
				<tr> <td>
						<table align=center> 
							<tr> 
						 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.title"/>: </b></font> </td>
						 		<td NOWRAP width=700> 
	 				      			<font size=2> 
	                                     <c:out value="${activityTitle}" escapeXml="false"/>
	    							</font>
								</td> 
						  	</tr>
						  	<tr> 
						 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.instructions"/>:  </b></font></td>
						 		<td NOWRAP width=700> 
					      			<font size=2> 
	                                    <c:out value="${activityInstructions}" escapeXml="false"/>
	    							</font>									
								</td>
							</tr>
					
					 		<tr> 
							  	<td valign="top"> 
							 		<font size=2> <b> <bean:message key="label.question1"/>:  </b></font>
							 	</td>
							  	<td>
					      			<font size=2> 
										<c:out value="${sessionScope.defaultQuestionContent}"  escapeXml="false"/>
									</font>																		
							  	</td>
						  	</tr>
	
				  		<!-- if there is more than just the default content start presenting them -->
				  	 		<c:set var="queIndex" scope="session" value="1"/>
							<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContent}">
						  		<c:if test="${questionEntry.key > 1}"> 			
									<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
									  <tr>
									  	<td valign="top"> <font size=2> <b> <c:out value="Question ${queIndex}"/>:  </b></font></td>
									  	<td>
							      			<font size=2> 								  	
												<c:out value="${questionEntry.value}" escapeXml="false"/>                         
											</font>																													       
	                                    </td>
									  </tr>
								</c:if> 			
							</c:forEach>
							<html:hidden property="toolContentId" value="${QaAuthoringForm.toolContentId}"/>
							<html:hidden property="questionIndex"/>
				
						<tr>
							<td colspan=2>			
				   				<html:submit onclick="javascript:submitMethod('editActivityQuestions');" styleClass="button">
									<bean:message key="label.edit"/>
								</html:submit>	 				 		  					
							</td>
						</tr>

						</table>
				</td></tr>			



		