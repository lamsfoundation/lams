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

			<table class="forms">
				<tr>
					<td class="field-name">							
			      			<bean:message key="radiobox.synchInMonitor"/>:
					</td>
					<td>							
		      			<html:checkbox property="synchInMonitor" value="1"
							styleClass="noBorder" styleId="synchInMonitor">
						</html:checkbox>
	      			</td>
		         </tr>
		         
				<tr>
					<td class="field-name">							
			      			<bean:message key="radiobox.usernameVisible"/>:
					</td>
					<td>							
		      			<html:checkbox property="usernameVisible" value="1"
							styleClass="noBorder" styleId="usernameVisible">
						</html:checkbox>

	      			</td>
		         </tr>
		         
   				<tr>
					<td class="field-name">							
			      			<bean:message key="label.reflect"/>:
					</td>
					<td>							
		      			<html:checkbox property="reflect" value="1"
							styleClass="noBorder" styleId="reflect">
						</html:checkbox>
	      			</td>
		        </tr>

   				<tr>
					<td class="field-name">							
							&nbsp
					</td>
					<td>							
 						<html:textarea cols="30" rows="3" property="reflectionSubject"></html:textarea>
	      			</td>
		        </tr>
		        

   				<tr>
					<td class="field-name">							
			      			<bean:message key="radiobox.questionsSequenced"/>:
					</td>
					<td>							
		      			<html:checkbox property="questionsSequenced" value="1"
							styleClass="noBorder" styleId="questionsSequenced">
						</html:checkbox>
	      			</td>
		        </tr>
		        
		        
		         
		         <tr> 
					<td class="field-name">							
				 		<bean:message key="label.report.title"/>: 
			 		</td>
					<td>							
			 			<html:text property="reportTitle" value="${qaGeneralAuthoringDTO.reportTitle}" size="60" maxlength="100"/>
			 		</td>
			  	</tr>
			  	
			  	<tr> 
					<td class="field-name">							
				 		<bean:message key="label.monitoringReport.title"/>: </td>
					<td>							
			 			<html:text property="monitoringReportTitle" value="${qaGeneralAuthoringDTO.monitoringReportTitle}" size="60" maxlength="100"/>
			 		</td>
			  	</tr>
			  	
			</table>	  	



		
		
		

		