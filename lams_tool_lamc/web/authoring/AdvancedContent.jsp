<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

			<table class="forms">
				<tr>
	      			<td NOWRAP class="formlabel" valign=top>
		      			<font size=2> 
			      			<b> <bean:message key="radiobox.sln"/>: </b>
		      			</font>
					</td>
					<td NOWRAP valign=top>
						<font size=2>
							<html:radio property="sln"  value="ON">
							 <bean:message key="option.on"/>
							</html:radio>
	
							<html:radio property="sln"  value="OFF">
							 <bean:message key="option.off"/>
							</html:radio>
						</font>
	      			</td>
		         </tr>
		         
   				<tr>
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
			      			<b> <bean:message key="radiobox.onepq"/>: </b>
		      			</font>
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
						<font size=2>
			      			<b> <bean:message key="radiobox.retries"/>: </b>
		      			</font>
					</td>
					<td NOWRAP valign=top>
						<font size=2>
							<html:radio property="retries" value="ON">
							 <bean:message key="option.on"/>
							</html:radio>
	
							<html:radio property="retries" value="OFF">
							 <bean:message key="option.off"/>
							</html:radio>
						</font>
	      			</td>

				</tr>	      			
		        
		         <tr> 
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
					 		<b>	<bean:message key="label.report.title"/>: </b>
				 		</font>
				 	</td>
	  				<td NOWRAP class="formcontrol" valign=top>
		  				<font size=2>
							<FCK:editor id="richTextReportTitle" basePath="/lams/fckeditor/">
								  <c:out value="${sessionScope.richTextReportTitle}" escapeXml="false" />						  
							</FCK:editor>
						</font>
					</td> 
			  	</tr>
			  	
			  	<tr> 
					<td NOWRAP class="formlabel" valign=top>
						<font size=2>
					 		<b>	<bean:message key="label.report.endLearningMessage"/>: </b>
			 			</font>
			 		</td>
	  				<td NOWRAP class="formcontrol">
		  				<font size=2>
							<FCK:editor id="richTextEndLearningMsg" basePath="/lams/fckeditor/">
								  <c:out value="${sessionScope.richTextEndLearningMsg}" escapeXml="false" />						  
							</FCK:editor>
						</font>
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
		 			 <font size=2>
						 <html:submit onclick="javascript:submitMethod('doneAdvancedTab');" styleClass="button">
								<bean:message key="button.done"/>
						</html:submit>
					 </font>
					</td> 
 				 </tr>
			</table>	  	
		
			