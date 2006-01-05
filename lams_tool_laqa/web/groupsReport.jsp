<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>


		 		<c:set var="sessionIndex" scope="request" value="0"/>
				<c:forEach var="sessionsEntry" items="${sessionScope.mapToolSessions}">
				<c:set var="sessionIndex" scope="request" value="${sessionIndex +1}"/>
					<c:set var="sectionSessionId" scope="request" value="${sessionsEntry.key}"/>
				
				<tr>  
			  		<td> <fmt:message key="group.label"/> 
			  	</tr>
					<tr> <td>
					<table>
				 		<c:set var="queIndex" scope="request" value="0"/>
						<c:forEach var="mainEntry" items="${sessionsEntry.value}">
							<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
		
								<c:forEach var="monitoringQuestion" items="${sessionScope.mapMonitoringQuestions}">
									<c:if test="${monitoringQuestion.key == requestScope.queIndex}"> 			
										  	<tr>
										  		<td colspan=2> <fmt:message key="label.question"/> <c:out value="${queIndex}"/> : <c:out value="${monitoringQuestion.value}"/>
										  	</tr>
									</c:if>							  	
								</c:forEach>		  	
								  
				  				 	<tr> 
										 <td> &nbsp&nbsp&nbsp <fmt:message key="label.learning.user"/>	</td>  
				  						 <td> &nbsp&nbsp&nbsp <fmt:message key="label.learning.attemptTime"/></td>
			  	  						 <td> &nbsp&nbsp&nbsp <fmt:message key="label.learning.timezone"/></td>
				  						 <td> &nbsp&nbsp&nbsp <fmt:message key="label.learning.response"/> 	</td>
						  			</tr>				 
						  			
										<c:set var="ansIndex" scope="request" value="0"/>
								  		<c:forEach var="subEntry" items="${mainEntry.value}">
											<c:set var="ansIndex" scope="request" value="${ansIndex +1}"/>
										  	<%
										  	  String sectionSessionId	=(String)request.getAttribute("sectionSessionId");
										  	  String fullName			="fullName" + sectionSessionId + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
										  	  String aTime				="aTime" + sectionSessionId + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
										  	  String timeZoneId			="timeZoneId" + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
											  String answer				="answer" + sectionSessionId + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
		  									  String responseId			="responseId" + sectionSessionId + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
		  									  String responseHidden		="responseHidden" + sectionSessionId + request.getAttribute("queIndex") + request.getAttribute("ansIndex");
											  
											  fullName = (String) request.getSession().getAttribute(fullName);
											  request.setAttribute("fullName", fullName);

									 	   	  java.util.Date attemptTime= (java.util.Date) request.getSession().getAttribute(aTime);
									 	   	  request.setAttribute("attemptTime", attemptTime);

	  	    						 	   	  timeZoneId= (String) request.getSession().getAttribute(timeZoneId);
						 	   	  			  request.setAttribute("timeZoneId", timeZoneId);									 	   	  
						 	   	  			  
									 	   	  answer= (String) request.getSession().getAttribute(answer);
									 	   	  request.setAttribute("answer", answer);
									 	   	  Long longResponseId= (Long) request.getSession().getAttribute(responseId);
									 	   	  request.setAttribute("responseId", longResponseId);
									 	   	  Boolean booleanResponseHidden= (Boolean) request.getSession().getAttribute(responseHidden);
									 	   	  request.setAttribute("responseHidden", booleanResponseHidden);
									 	   	  
											%>
											<c:if test="${requestScope.answer != ''}"> 			
												<tr> 
													<c:if test="${requestScope.fullName != ''}"> 			
														<td>  
															  		&nbsp&nbsp&nbsp  <c:out value="${requestScope.fullName}"/>
														</td>  
													</c:if>							  	
													
													<c:if test="${requestScope.attemptTime != ''}"> 														
														<td>  
															  		&nbsp&nbsp&nbsp  <c:out value="${requestScope.attemptTime}"/>
														</td>  
													</c:if>							  													

													<c:if test="${requestScope.timeZoneId != ''}"> 														
														<td>  
															  		&nbsp&nbsp&nbsp  <c:out value="${requestScope.timeZoneId}"/>
														</td>  
													</c:if>							  													

													<c:if test="${requestScope.responseId == sessionScope.dataMapEditableResponseId}"> 			
															<td>  
																	&nbsp&nbsp&nbsp <html:text property="updatedResponse" value="${requestScope.answer}" 
																							   size="8" maxlength="255"/>
															</td>  
															<td>  
																  	&nbsp&nbsp&nbsp  
													  				<html:submit property="updateReport" styleClass="linkbutton" onclick="submitMethod('updateReport');">
																		<fmt:message key="label.update"/>
																	</html:submit>
															</td>  
													</c:if>							  	
													
													<c:if test="${requestScope.responseId != sessionScope.dataMapEditableResponseId}"> 			
																<c:if test="${requestScope.responseId != sessionScope.dataMapHiddenResponseId}"> 			
																	<c:if test="${requestScope.responseHidden == 'true'}"> 			
																		<td>  
																			  		&nbsp&nbsp&nbsp <fmt:message key="label.hidden"/> : 
																			  		<c:out value="${requestScope.answer}"/>
																		</td>  
																		<td>  
																			<c:if test="${requestScope.portfolioRequest != 'true'}"> 			
																			  	&nbsp&nbsp&nbsp  
															  					<html:submit property="editReport" styleClass="linkbutton"  
															  											onclick="selectResponse(${requestScope.responseId})">
																					<fmt:message key="label.edit"/>
																				</html:submit>
																			</c:if>							  																					
																		</td>  
	
																		<td>  
																		  	&nbsp&nbsp&nbsp  
															  				<html:submit property="unhideReport" styleClass="linkbutton"  
															  											onclick="unHideResponse(${requestScope.responseId});">
																				<fmt:message key="label.unHide"/>
																			</html:submit>
																		</td>  
																	</c:if>																
	
																	<c:if test="${requestScope.responseHidden == 'false'}"> 			
																		<td>  
																			  		&nbsp&nbsp&nbsp 
																			  		<c:out value="${requestScope.answer}"/>
																		</td>  
																		<td>  
																			<c:if test="${requestScope.portfolioRequest != 'true'}">																		
																		  		&nbsp&nbsp&nbsp  
															  					<html:submit property="editReport" styleClass="linkbutton"  
															  											onclick="selectResponse(${requestScope.responseId})">
																					<fmt:message key="label.edit"/>
																				</html:submit>
																			</c:if>							  																																									
																		</td>  
	
																	<td>  
														  				<c:if test="${requestScope.portfolioRequest != 'true'}">																	
																		  	&nbsp&nbsp&nbsp  
															  				<html:submit property="hideReport" styleClass="linkbutton"  
														  											onclick="hideResponse(${requestScope.responseId})">
																				<fmt:message key="label.hide"/>
																			</html:submit>																			
																		</c:if>							  																																																												
																	</td>  
																	</c:if>																
																</c:if>	
															
																<c:if test="${requestScope.responseId == sessionScope.dataMapHiddenResponseId}"> 			
																	<td>  
																				&nbsp&nbsp&nbsp <fmt:message key="label.hidden"/> : <c:out value="${requestScope.answer}"/> 
																	</td>  
			
																	<td>  
														  				<c:if test="${requestScope.portfolioRequest != 'true'}"> 																		  																												
																		  	&nbsp&nbsp&nbsp  
															  				<html:submit property="editReport" styleClass="linkbutton"  
															  											onclick="selectResponse(${requestScope.responseId})">
																				<fmt:message key="label.edit"/>
																			</html:submit>																			
																		</c:if>																																			
		
																	</td>  
																	<td>  
																	  	&nbsp&nbsp&nbsp  
														  				<html:submit property="unhideReport" styleClass="linkbutton"  
														  											onclick="unHideResponse(${requestScope.responseId});">
														  											
																			<fmt:message key="label.unHide"/>
																		</html:submit>
																	</td>  
																</c:if>	
													</c:if>	
																			  	
											  		</tr>	
				  								</c:if>							  		
									  		</c:forEach>
								  	  <tr><td> &nbsp </td> </tr>
							</c:forEach>
					</table>
					</td> </tr>
				</c:forEach>
