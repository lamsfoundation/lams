<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

			<table class="forms">
				<tr>
	      			<td NOWRAP class="formlabel" valign=top>
		      			<bean:message key="radiobox.sln"/>:
					</td>
					<td NOWRAP valign=top>
						<html:radio property="sln"  value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="sln"  value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>
		         
   				<tr>
					<td NOWRAP class="formlabel" valign=top>
		      			<bean:message key="radiobox.onepq"/>:		      			
					</td>
					<td NOWRAP valign=top>
						<html:radio property="questionsSequenced" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="questionsSequenced" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		        </tr>
		         
		         <tr>
					<td NOWRAP class="formlabel" valign=top>
		      			<bean:message key="radiobox.retries"/>:		      			
					</td>
					<td NOWRAP valign=top>
						<html:radio property="retries" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="retries" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>

				</tr>	      			
		        
		         <tr> 
					<td NOWRAP class="formlabel" valign=top>
				 			<bean:message key="label.report.title"/>: 
				 	</td>
	  				<td NOWRAP class="formcontrol" valign=top>
						<FCK:editor id="richTextReportTitle" basePath="/lams/fckeditor/">
							  <c:out value="${sessionScope.richTextReportTitle}" escapeXml="false" />						  
						</FCK:editor>
					</td> 
			  	</tr>
			  	
			  	<tr> 
					<td NOWRAP class="formlabel" valign=top>
			 			<bean:message key="label.report.endLearningMessage"/>: 
			 		</td>
	  				<td NOWRAP class="formcontrol">
						<FCK:editor id="richTextEndLearningMsg" basePath="/lams/fckeditor/">
							  <c:out value="${sessionScope.richTextEndLearningMsg}" escapeXml="false" />						  
						</FCK:editor>
					</td> 
			  	</tr>
			  	
		  		<tr>
 				 	<td NOWRAP colspan=2 align=center valign=top>								
						&nbsp&nbsp
				  	</td>
				</tr>
				<tr>
				 	<td NOWRAP valign=top>								
						&nbsp&nbsp
				  	</td>
					 <td NOWRAP valign=top> 
						 <html:submit onclick="javascript:submitMethod('doneAdvancedTab');" >
							<bean:message key="button.done"/>
						</html:submit>
					</td> 
 				 </tr>
			</table>	  	
		
		

		