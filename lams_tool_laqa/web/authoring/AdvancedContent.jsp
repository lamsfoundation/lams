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

<div id="advancedTabContainer">
			<table align=center>
				<tr>
	      			<td NOWRAP class="formlabel" valign=top>
		      			<font size=2> <b>
			      			<bean:message key="radiobox.synchInMonitor"/>:
			      		</b> </font>
					</td>
					<td NOWRAP valign=top>
						<font size=2>
						<html:radio property="synchInMonitor" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="synchInMonitor" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
						</font>						
	      			</td>
		         </tr>
		         
				<tr>
	      			<td NOWRAP class="formlabel" valign=top>
		      			<font size=2> <b>
			      			<bean:message key="radiobox.usernameVisible"/>:
			      		</b> </font>		      			
					</td>
					<td NOWRAP valign=top>
						<font size=2>
						<html:radio property="usernameVisible" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="usernameVisible" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
						</font>												
	      			</td>
		         </tr>
		         
   				<tr>
	      			<td NOWRAP class="formlabel" valign=top>
		      			<font size=2> <b>
			      			<bean:message key="radiobox.questionsSequenced"/>:
			      		</b> </font>		      			
					</td>
					<td NOWRAP valign=top>
					<font size=2>
						<html:radio property="questionsSequenced" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="questionsSequenced" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
					</font>												
	      			</td>
		        </tr>
		         
		         <tr> 
	      			<td NOWRAP class="formlabel" valign=top>
	      			<font size=2> <b>
				 		<bean:message key="label.report.title"/>: 
		      		</b> </font>		      							 		
				 		</td>
					<td NOWRAP valign=top>
						<font size=2>
			 			<html:text property="reportTitle" size="60" maxlength="100"/>
						</font>									 			
			 		</td>
			  	</tr>
			  	
			  	<tr> 
	      			<td NOWRAP class="formlabel" valign=top>
	      			<font size=2> <b>
				 		<bean:message key="label.monitoringReport.title"/>: </td>
		      		</b> </font>		      							 					 		
					<td NOWRAP valign=top>
					<font size=2>
			 			<html:text property="monitoringReportTitle" size="60" maxlength="100"/>
					</font>									 			
			 		</td>
			  	</tr>
			  	
			  	<tr> 
	      			<td NOWRAP class="formlabel" valign=top>
	      			<font size=2> <b>	      			
				 		<bean:message key="label.report.endLearningMessage"/>: 
		      		</b> </font> </td>
				 		
					<td NOWRAP valign=top>
					<font size=2>
			 			<html:text property="endLearningMessage" size="60" maxlength="100"/>
					</font>									 			
			 		</td>
			  	</tr>
			</table>	  	
</div>


		
		
		

		