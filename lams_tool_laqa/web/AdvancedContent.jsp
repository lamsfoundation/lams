<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<div id="advancedTabContainer">
		<tr> <td>
			<table align=center>
				<tr>
	      			<td NOWRAP class="formlabel" valign=top>
		      			<font size=2> <b>
			      			<fmt:message key="radiobox.synchInMonitor"/>:
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
			      			<fmt:message key="radiobox.usernameVisible"/>:
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
			      			<fmt:message key="radiobox.questionsSequenced"/>:
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
				 		<fmt:message key="label.report.title"/>: 
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
				 		<fmt:message key="label.monitoringReport.title"/>: </td>
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
				 		<fmt:message key="label.report.endLearningMessage"/>: 
		      		</b> </font> </td>
				 		
					<td NOWRAP valign=top>
					<font size=2>
			 			<html:text property="endLearningMessage" size="60" maxlength="100"/>
					</font>									 			
			 		</td>
			  	</tr>
			</table>	  	
		
		</td></tr>
</div>


		
		
		

		