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
					  	<td align=right class="input" valign=top colspan=2> 
						  	<font size=2 color="#000000"> <b>  <bean:message key="label.mark"/> </b> </font>
					  	</td>
					  </tr>	

					<tr>
						<td align=right class="input" valign=top colspan=2> 
							<hr>
						</td> 
					</tr>
					
  		  	 		<c:set var="mainQueIndex" scope="session" value="0"/>
					<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
					<c:set var="mainQueIndex" scope="session" value="${mainQueIndex +1}"/>
						  <tr>
						  	<td align=left class="input" valign=top bgColor="#999966" colspan=2> 
							  	<font color="#FFFFFF"> 
							  		<c:out value="${questionEntry.value}"/> 
							  	</font> 
						  	</td>
						  </tr>

								  								  
						  <tr>						 
							<td align=left>
							<table align=left>
			  		  	 		<c:set var="queIndex" scope="session" value="0"/>
								<c:forEach var="mainEntry" items="${sessionScope.mapGeneralOptionsContent}">
									<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
										<c:if test="${sessionScope.mainQueIndex == sessionScope.queIndex}"> 		
									  		<c:forEach var="subEntry" items="${mainEntry.value}">
				  								<tr> 
													<td align=left class="input" valign=top> 
					   								    <img src="images/dot.jpg" align=left>
													</td> 
													<td align=left class="input" valign=top> 
														<font size=2 color="#006666">	<c:out value="${subEntry.value}"/> </font>
													</td>
												</tr>	
											</c:forEach>

												<tr> 
													<td align=left class="input" valign=top> 
					   								    <bean:message key="label.you.answered"/>
													</td> 
													<td align=left class="input" valign=top> 											
														<table align=left>
													  		<c:forEach var="subEntry" items="${mainEntry.value}">
	
					  											<c:forEach var="selectedMainEntry" items="${sessionScope.mapGeneralCheckedOptionsContent}">
																	<tr> 
																			<c:if test="${selectedMainEntry.key == sessionScope.queIndex}"> 		
																		  		<c:forEach var="selectedSubEntry" items="${selectedMainEntry.value}">
					
																					<c:if test="${subEntry.key == selectedSubEntry.key}"> 		
	
																							<td align=left class="input" valign=top> 
																								<b> <c:out value="${subEntry.value}"/> </b>
																							</td> 
									  												</c:if> 			
																				</c:forEach>																						
						  													</c:if> 			
																	</tr>			
																</c:forEach>		
																
												  		</c:forEach>											
														</table>
													</td>
												</tr>
											
										</c:if> 			
								</c:forEach>
							</table>
							</td>
						</tr>
					</c:forEach>

			  	   	<tr> 
				 		<td colspan=2 class="input" valign=top> 
				 		&nbsp
				 		</td>
			  	   </tr>

			  	   
	  	   		  <tr>
				  	<td colspan=2 align=center class="input" valign=top> 
			  			<html:submit property="redoQuestions" styleClass="a.button">
							<bean:message key="label.redo.question"/>
						</html:submit>	 		
   						&nbsp&nbsp&nbsp&nbsp&nbsp
   						<html:submit property="viewSummary" styleClass="a.button">
							<bean:message key="label.view.summary"/>
						</html:submit>	 				 		  					
				  	 </td>
				  </tr>
				</table>
	</html:form>

