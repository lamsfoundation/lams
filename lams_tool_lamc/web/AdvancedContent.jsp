<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<div id="advancedTabContainer">
		<tr> <td>
			<table>
				<tr>
	      			<td>
		      			<fmt:message key="radiobox.synchInMonitor"/>:
					</td>
					<td>
						<html:radio property="synchInMonitor" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="synchInMonitor" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>
		         
				<tr>
	      			<td>
		      			<fmt:message key="radiobox.usernameVisible"/>:
					</td>
					<td>
						<html:radio property="usernameVisible" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="usernameVisible" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>
		         
   				<tr>
	      			<td>
		      			<fmt:message key="radiobox.questionsSequenced"/>:
					</td>
					<td>
						<html:radio property="questionsSequenced" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="questionsSequenced" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		        </tr>
		         
		         <tr> 
			 		<td> <fmt:message key="label.report.title"/>: </td>
			 		<td> 
			 			<html:text property="reportTitle" size="60" maxlength="100"/>
			 		</td>
			  	</tr>
			  	
			  	<tr> 
			 		<td> <fmt:message key="label.monitoringReport.title"/>: </td>
			 		<td> 
			 			<html:text property="monitoringReportTitle" size="60" maxlength="100"/>
			 		</td>
			  	</tr>
			  	
			  	<tr> 
			 		<td> <fmt:message key="label.report.endLearningMessage"/>: </td>
			 		<td> 
			 			<html:text property="endLearningMessage" size="60" maxlength="100"/>
			 		</td>
			  	</tr>
			</table>	  	

			<hr>
			<table>
				<tr>
					 <td class="body" colspan=2> 
						 <html:submit property="submitTabDone" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.done"/>
						</html:submit>
					</td> 
				</tr>
  			</table>
		
		</td></tr>
</div>


		
		
		

		