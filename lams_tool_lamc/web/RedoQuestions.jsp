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
							  	<font size=3> <b>  <bean:message key="label.individual.results.withRetries"/> </b> </font>
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${sessionScope.isRetries == 'false'}"> 							  
						  <tr>
						  	<td align=center class="input" valign=top colspan=2> 
							  	<font size=3> <b>  <bean:message key="label.individual.results.withoutRetries"/> </b> </font>
						  	</td>
						  </tr>
					</c:if> 			


					  <tr>
					  	<td align=center class="input" valign=top colspan=2> 
						  	<font size=3>  <bean:message key="label.learner.redo"/> </font>
					  	</td>
					  </tr>	

					  <tr>
					  	<td align=right class="input" valign=top colspan=2> 
							&nbsp
					  	</td>
					  </tr>	


					  <tr>
					  	<td align=center class="input" valign=top colspan=2> 
						  	<font size=2>  <bean:message key="label.learner.bestMark"/>
						  	<b>   <c:out value="${sessionScope.learnerBestMark}"/> </b> 
						  	<bean:message key="label.outof"/> 
						  	<b> <c:out value="${sessionScope.totalQuestionCount}"/> </b> </font>
					  	</td>
					  </tr>	
					  
					<tr> 
				 		<td colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>
					  
					  
				  <tr>
				  	<td colspan=2 align=center class="input" valign=top> 
			  			<html:submit property="viewAnswers" styleClass="a.button">
							<bean:message key="label.view.answers"/>
						</html:submit>	 		
   						&nbsp&nbsp&nbsp&nbsp&nbsp
   						<html:submit property="redoQuestionsOk" styleClass="a.button">
							<bean:message key="label.redo.questions"/>
						</html:submit>	 				 		  					
				  	 </td>
				  </tr>
				
					  
		</html:form>

