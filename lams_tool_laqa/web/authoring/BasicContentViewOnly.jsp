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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp"%>

			<html:hidden property="questionIndex"/>
			<table cellpadding="0">

						<tr>
							<td colspan="2">
								<div class="field-name">
									<fmt:message key="label.authoring.title.col"></fmt:message>
								</div>
						  			<c:out value="${qaGeneralAuthoringDTO.activityTitle}" escapeXml="true"/> 
							</td>
						</tr>
						

						<tr>
							<td colspan="2">
								<div class="field-name">
									<fmt:message key="label.authoring.instructions.col"></fmt:message>
								</div>
						  			<c:out value="${qaGeneralAuthoringDTO.activityInstructions}" escapeXml="false"/> 																	
							</td>
						</tr>
	
				 		<tr>
						<td colspan="2">
							<div id="resourceListArea">
									<%@ include file="/authoring/itemlistViewOnly.jsp"%>									
							</div>
						</td>
						</tr>

						<tr>
							<td colspan=2 class="align-right">			
				   				<html:submit onclick="javascript:submitMethod('editActivityQuestions');" styleClass="button">
									<fmt:message key="label.edit"/>
								</html:submit>	 				 		  					
							</td>
						</tr>
			 </table>			
				 


				
							
							
				






		
