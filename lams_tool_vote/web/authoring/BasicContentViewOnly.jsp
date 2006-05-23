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
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
	
				<tr> <td>
						<table align=center> 
							<tr> 
						 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.title"/>: </b></font> </td>
						 		<td NOWRAP width=700> 
	                                     <c:out value="${activityTitle}" escapeXml="false"/>
								</td> 
						  	</tr>
						  	<tr> 
						 		<td valign="top"><font size=2> <b> <bean:message key="label.authoring.instructions"/>:  </b></font></td>
						 		<td NOWRAP width=700> 
	                                    <c:out value="${activityInstructions}" escapeXml="false"/>
								</td>
							</tr>
					
					 		<tr> 
							  	<td valign="top"> 
							 		<font size=2> <b> <bean:message key="label.nomination1"/>:  </b></font>
							 	</td>
							  	<td>
										<c:out value="${defaultOptionContent}"  escapeXml="false"/>
							  	</td>
						  	</tr>
	
			  		<!-- if there is more than just the default content start presenting them -->
			  	 		<c:set var="optIndex" scope="session" value="1"/>
						<c:forEach var="optionEntry" items="${sessionScope.mapOptionsContent}">
					  		<c:if test="${optionEntry.key > 1}"> 			
								<c:set var="optIndex" scope="session" value="${optIndex +1}"/>
								  <tr>
								  	<td NOWRAP valign="top">  <font size=2> <b> <c:out value="Nomination ${optIndex}"/>:  </b> </font></td>

									<td NOWRAP valign=top>
										<c:out value="${optionEntry.value}"  escapeXml="false"/>										
                                    </td>
								  </tr>
							</c:if> 			
						</c:forEach>
						<html:hidden property="toolContentId" value="${VoteAuthoringForm.toolContentId}"/>
						<html:hidden property="optIndex"/>
                        
				
						<tr>
							<td colspan=2>			
				   				<html:submit onclick="javascript:submitMethod('editActivityQuestions');" styleClass="button">
									<bean:message key="label.edit"/>
								</html:submit>	 				 		  					
							</td>
						</tr>

						</table>
				</td></tr>			



		