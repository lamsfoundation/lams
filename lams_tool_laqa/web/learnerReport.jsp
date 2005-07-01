<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!-- this form is exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
	<html:form  action="/learning?method=displayQ&validate=false" method="POST" target="_self">
	<br>
	<center>

	<table align=center>
		<tr><td>
			<c:out value="${sessionScope.reportTitleLearner}"/>
		</td></tr>

		<tr><td> &nbsp
		</td></tr>

		<tr> <td>
		<table>
	 		<c:set var="queIndex" scope="request" value="0"/>
			<c:forEach var="mainEntry" items="${sessionScope.mapMainReport}">
				<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
					  	<tr>
					  		<td colspan=2> <fmt:message key="label.question"/> <c:out value="${queIndex}"/> : <c:out value="${mainEntry.key}"/>
					  	</tr>
					  
	  				 	<tr> 
							 <td> &nbsp&nbsp&nbsp <fmt:message key="label.learning.user"/>	</td>  
	  						 <td> &nbsp&nbsp&nbsp <fmt:message key="label.learning.attemptTime"/></td>
 	  						 <td> &nbsp&nbsp&nbsp <fmt:message key="label.learning.timezone"/></td>
	  						 <td> &nbsp&nbsp&nbsp <fmt:message key="label.learning.response"/></td>
			  			</tr>				 
			  			
							<c:set var="ansIndex" scope="request" value="0"/>
					  		<c:forEach var="subEntry" items="${mainEntry.value}">
								<c:set var="ansIndex" scope="request" value="${ansIndex +1}"/>
								
							  	<%String fullName	="fullName" + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
							  	  String aTime		="aTime" + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
							  	  String formattedAtime="formattedAtime" + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
							  	  String timeZone	="timeZone" + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
								  String answer		="answer" + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
							   	  String currentLearnerFullname=(String) request.getSession().getAttribute("currentLearnerFullname");
							   	  								  
								  fullName			= (String) request.getSession().getAttribute(fullName);
						 	   	  java.util.Date attemptTime= (java.util.Date) request.getSession().getAttribute(aTime);
						 	   	  formattedAtime 	=(String) request.getSession().getAttribute(formattedAtime);
  						 	   	  timeZone			= (String) request.getSession().getAttribute(timeZone);
						 	   	  answer			= (String) request.getSession().getAttribute(answer);
						 	   	  
								  request.setAttribute("fullName", fullName);
						 	   	  request.setAttribute("attemptTime", attemptTime);
						 	   	  request.setAttribute("formattedAtime", formattedAtime);
						 	   	  request.setAttribute("timeZone", timeZone);
						 	   	  request.setAttribute("answer", answer);
						 	   	  request.setAttribute("currentLearnerFullname", currentLearnerFullname);
								%>
									<tr> 
								   		<c:if test="${sessionScope.targetMode == 'Learning'}"> 			

											 <%								   		
											   if ((fullName != null) && (!fullName.equalsIgnoreCase(currentLearnerFullname)))
											   {
											  %>
												<td>  
												  		<c:if test="${sessionScope.isUsernameVisible == 'false'}"> 			
													  		&nbsp&nbsp&nbsp ( ) 
												  		</c:if>
												  		<c:if test="${sessionScope.isUsernameVisible == 'true'}"> 			
															&nbsp&nbsp&nbsp  <c:out value="${requestScope.fullName}"/> 
												  		</c:if>
												  		
												</td>  
												<%}
												else  if ((fullName != null) && (fullName.equalsIgnoreCase(currentLearnerFullname)))
												 {%>
												<td> 
													&nbsp&nbsp&nbsp  <c:out value="${requestScope.fullName}"/> 
												</td>  
											 <%}
											 	else  if ((fullName == null))
												 {%>
												<td>  
												  		&nbsp&nbsp&nbsp ( )
												</td>  
											 <%}%>
										</c:if>							
								   		
										
										 <% if (fullName != null)
										 	{
										 %>
										<c:if test="${sessionScope.targetMode == 'Monitoring'}"> 			
											<td>  
												&nbsp&nbsp&nbsp  <c:out value="${requestScope.fullName}"/> 
											</td>  
										</c:if>		
										<%}%>					

										

										 <% if (formattedAtime != null)
										 	{
										 %>
											<td>  
												&nbsp&nbsp&nbsp  
												 <!-- <fmt:formatDate value="${requestScope.attemptTime}" type="both" timeStyle="long"/> -->
												 <c:out value="${requestScope.formattedAtime}"/> 
											</td>  
										<%}%>					

										 <% if (timeZone != null)
										 	{
										 %>
											<td>  
												&nbsp&nbsp&nbsp  <c:out value="${requestScope.timeZone}"/> 
											</td>  
										<%}%>					


										 <% if (answer != null)
										 	{
										 %>
											<td>  
												&nbsp&nbsp&nbsp  <c:out value="${requestScope.answer}"/> 
											</td>  
										<%}%>					
							  		</tr>	
					  		</c:forEach>
				  	  <tr><td> &nbsp </td> </tr>
			</c:forEach>
	</table>
	</td> </tr>

	<tr> <td> 
		<c:out value="${sessionScope.endLearningMessage}"/>
	</td> </tr>

	<tr> <td>
	<hr>
	<table>
		<tr>
			 <td> 
			 	 <html:submit property="endLearning" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
					<bean:message key="button.endLearning"/>
				</html:submit>
			</td> 
		</tr>
	</table>
	</td> </tr>	


</table>
</html:form>
	
	
	
	
	
	