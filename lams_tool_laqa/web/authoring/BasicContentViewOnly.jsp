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


<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

						<table class="forms">
							<tr> 
	 							<td> <b>  <bean:message key="label.authoring.title"/>: </b> </td>
						 		<td NOWRAP> 
	                                     <c:out value="${qaGeneralAuthoringDTO.activityTitle}" escapeXml="false"/>
								</td> 
						  	</tr>
						  	<tr> 
	 							<td> <b> <bean:message key="label.authoring.instructions"/>:  </b></td>
						 		<td NOWRAP> 
	                                    <c:out value="${qaGeneralAuthoringDTO.activityInstructions}" escapeXml="false"/>
								</td>
							</tr>
					
					 		<tr> 
	 							<td>
									 <b> <bean:message key="label.question1"/>:  </b>
							 	</td>
							  	<td>
										<c:out value="${qaGeneralAuthoringDTO.defaultQuestionContent}"  escapeXml="false"/>
							  	</td>
						  	</tr>
	
				  		<!-- if there is more than just the default content start presenting them -->
				  	 		<c:set var="queIndex" scope="request" value="1"/>
							<c:forEach var="questionEntry" items="${qaGeneralAuthoringDTO.mapQuestionContent}">
						  		<c:if test="${questionEntry.key > 1}"> 			
									<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
									  <tr>
			 							<td> <b> <c:out value="Question ${queIndex}"/>:  </b></td>
									  	<td>
												<c:out value="${questionEntry.value}" escapeXml="false"/>                         
	                                    </td>
									  </tr>
								</c:if> 			
							</c:forEach>
							<html:hidden property="questionIndex"/>
				
						<tr>
							<td colspan=2 align=right>			
				   				<html:submit onclick="javascript:submitMethod('editActivityQuestions');" styleClass="button">
									<bean:message key="label.edit"/>
								</html:submit>	 				 		  					
							</td>
						</tr>

						</table>




		