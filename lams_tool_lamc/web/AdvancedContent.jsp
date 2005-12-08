<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

			<table class="forms">
				<tr>
	      			<td class="formlabel" valign=top>
		      			<bean:message key="radiobox.sln"/>:
					</td>
					<td valign=top>
						<html:radio property="sln"  value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="sln"  value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>
		         
   				<tr>
					<td class="formlabel" valign=top>
		      			<bean:message key="radiobox.onepq"/>:		      			
					</td>
					<td valign=top>
						<html:radio property="questionsSequenced" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="questionsSequenced" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		        </tr>
		         
		         <tr>
					<td class="formlabel" valign=top>
		      			<bean:message key="radiobox.retries"/>:		      			
					</td>
					<td valign=top>
						<html:radio property="retries" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="retries" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>

				</tr>	      			
		        
		         <tr> 
					<td class="formlabel" valign=top>
				 			<bean:message key="label.report.title"/>: 
				 	</td>
	  				<td class="formcontrol" valign=top>
						<FCK:editor id="richTextReportTitle" basePath="/lams/fckeditor/">
							  <c:out value="${sessionScope.richTextReportTitle}" escapeXml="false" />						  
						</FCK:editor>
					</td> 
			  	</tr>
			  	
			  	<tr> 
					<td class="formlabel" valign=top>
			 			<bean:message key="label.report.endLearningMessage"/>: 
			 		</td>
	  				<td class="formcontrol">
						<FCK:editor id="richTextEndLearningMsg" basePath="/lams/fckeditor/">
							  <c:out value="${sessionScope.richTextEndLearningMsg}" escapeXml="false" />						  
						</FCK:editor>
					</td> 
			  	</tr>
			  	
		  		<tr>
 				 	<td colspan=2 align=center valign=top>								
						&nbsp&nbsp
				  	</td>
				</tr>
				<tr>
				 	<td valign=top>								
						&nbsp&nbsp
				  	</td>
					 <td valign=top> 
						 <html:submit onclick="javascript:submitMethod('doneAdvancedTab');" >
							<bean:message key="button.done"/>
						</html:submit>
					</td> 
 				 </tr>
			</table>	  	
		
		

		