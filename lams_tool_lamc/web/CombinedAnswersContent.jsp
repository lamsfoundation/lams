<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!--options content goes here-->
				<table align=center>
					  <tr>
					  	<td align=left class="input" valign=top bgColor="#333366" colspan=2> 
						  	<font size=2 color="#FFFFFF"> <b>  <bean:message key="label.assessment"/> </b> </font>
					  	</td>
					  </tr>
					  

			 		<c:if test="${sessionScope.isRetries == 'true'}"> 		
					  <tr>
					  	<td align=center class="input" valign=top colspan=2> 
						  	<font size=3> <b>  <bean:message key="label.withRetries"/> </b> </font>
					  	</td>
					  </tr>
					</c:if> 			
				
					<c:if test="${sessionScope.isRetries == 'false'}"> 		
					  <tr>
					  	<td align=center class="input" valign=top colspan=2> 
						  	<font size=3> <b>  <bean:message key="label.withoutRetries"/> </b> </font>
					  	</td>
					  </tr>
					</c:if> 			

			 		<c:if test="${sessionScope.isRetries == 'true'}"> 		
					  <tr>
					  	<td align=left class="input" valign=top colspan=2> 
						  	<font size=2> <b>  <bean:message key="label.learner.message"/> (<c:out value="${sessionScope.passMark}"/><bean:message key="label.percent"/> ) 
						  	</b> </font>
					  	</td>
					  </tr>
					</c:if> 								  
				
					<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
						  <tr>
						  	<td align=left class="input" valign=top bgColor="#009999" colspan=2> 
							  	<font color="#FFFFFF"> 
							  		<c:out value="${questionEntry.value}"/> 
							  	</font> 
						  	</td>
						  </tr>
					</c:forEach>
					

		  	 		<c:set var="queIndex" scope="session" value="0"/>
					<c:forEach var="optionEntry" items="${sessionScope.mapOptionsContent}">
						<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
						<tr> 
							<td align=left class="input" valign=top> 
								<input type="checkbox" name=optionCheckBox<c:out value="${queIndex}"/>> 
							</td> 
							<td align=left class="input" valign=top> 
								<c:out value="${optionEntry.value}"/>
							</td>
						</tr>
					</c:forEach>

			  	   	<tr> 
				 		<td colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>
			  	   
	  	   		  <tr>
				  	<td colspan=2 align=right class="input" valign=top> 
			  			<html:submit property="continueOptions" styleClass="a.button">
							<bean:message key="button.continue"/>
						</html:submit>	 				 		  					
				  	 </td>
				  </tr>
				</table>
	<!--options content ends here-->

