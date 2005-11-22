<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
				<table align=center bgcolor="#FFFFFF">
					  <tr>
					  	<td align=left class="input" valign=top bgColor="#333366" colspan=2> 
						  	<font size=2 color="#FFFFFF"> <b>  <bean:message key="label.assessment"/> </b> </font>
					  	</td>
					  </tr>
				
			 		<c:if test="${sessionScope.isRetries == 'true'}"> 		
						  <tr>
						  	<td align=center class="input" valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.withRetries.results.summary"/> </b> </font>
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${sessionScope.isRetries != 'true'}"> 							  
						  <tr>
						  	<td align=center class="input" valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.withoutRetries.results.summary"/> </b> </font>
						  	</td>
						  </tr>
					</c:if> 			


					  <tr>
					  	<td align=left class="input" valign=top colspan=2> 
						  	<font size=2>  <c:out value="${sessionScope.countSessionComplete}"/> <bean:message key="label.learnersFinished"/> 4
						  	</font>
					  	</td>
					  </tr>	

					<tr>
						<td align=right class="input" valign=top colspan=2> 
							<hr>
						</td> 
					</tr>


					 <tr>
					 <td> 
		 				<table align=center bgcolor="#FFFFFF">
						  <tr>
						  	<td align=left class="input" valign=top> 
							  	<font size=2> <b>  <bean:message key="label.topMark"/> </b> </font> 
							 </td> 
							 <td align=right>	
							  	 <c:out value="${sessionScope.topMark}"/>
						  	</td>
						  </tr>	
	
						  <tr>
						  	<td align=left class="input" valign=top colspan=2> 
							  	<font size=2> <b>  <bean:message key="label.avMark"/> </b>  </font>
						  	</td>
						  	<td align=right>
							  	<c:out value="${sessionScope.averageMark}"/>
						  	</td>
						  </tr>	
	
						  <tr>
						  	<td align=left class="input" valign=top colspan=2> 
							  	<font size=2> <b>  <bean:message key="label.loMark"/> </b> </font>
						  	</td>
						  	<td align=right>
							  	<c:out value="${sessionScope.lowestMark}"/>
						  	</td>
						  </tr>	
						 </table>
					</td>
					</tr>

			  	   	<tr> 
				 		<td colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>

			  	   

			 		<c:if test="${sessionScope.isRetries == 'true'}"> 					  	   
		  	   		  <tr>
					  	<td colspan=2 align=center class="input" valign=top> 
				  			<html:submit property="redoQuestions" styleClass="a.button">
								<bean:message key="label.redo.questions"/>
							</html:submit>	 		
		       
					  	   <html:submit property="learnerFinished" styleClass="a.button">
								<bean:message key="label.finished"/>
						   </html:submit>

					  	 </td>
					  </tr>
					</c:if> 																		

					<c:if test="${sessionScope.isRetries != 'true'}"> 							  
		  	   		  <tr>
		  	   		    <td colspan=2 align=right class="input" valign=top>
					  	   <html:submit property="learnerFinished" styleClass="a.button">
										<bean:message key="label.finished"/>
						   </html:submit>
					  	 </td>
					  </tr>
					</c:if> 																		
					
				</table>
	</html:form>

