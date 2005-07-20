<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<tr>
<td>
	<c:if test="${requestScope.userExceptionNumberFormat == 'true' || 
			 	requestScope.userExceptionDefaultContentNotAvailable == 'true' || 
			 	requestScope.userExceptionUserIdNotAvailable == 'true' || 
			 	requestScope.userExceptionUserIdNotNumeric == 'true' || 
			 	requestScope.userExceptionDefaultContentNotSetup == 'true' || 
			 	requestScope.userExceptionDefaultQuestionContentNotAvailable == 'true'}"> 	
		<table align=center> <!-- Dave to take off-->
			<tr> <td class="error">   
				<html:errors/>
			</td></tr>
		</table>
	</c:if>						

	<c:if test="${requestScope.userExceptionNumberFormat != 'true'  && 
				  requestScope.userExceptionDefaultContentNotAvailable != 'true'  && 
				  requestScope.userExceptionUserIdNotAvailable != 'true'  && 
				  requestScope.userExceptionDefaultContentNotSetup != 'true'  && 				  
  				  requestScope.userExceptionUserIdNotNumeric != 'true'  && 
				  requestScope.userExceptionDefaultQuestionContentNotAvailable != 'true'}"> 	
		<!-- Dave to do the styling-->
		<!-- this form is exists temporarily to represent tool icon, remove this form once the tool is deployed into authoring environment -->
		<c:if test="${requestScope.startMonitoringSummaryRequest == 'true'}"> 			
			<html:form  action="/authoring?method=loadMonitoring&validate=false" enctype="multipart/form-data" method="POST" target="_self">
				<table align=center> <!-- Dave to take off--> 
				<tr>
						<td>
							<html:submit property="summaryMonitoring" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.summary"/>
							</html:submit> 
						</td><td>
							<html:submit property="instructionsMonitoring" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.instructions"/>
							</html:submit> 
						</td><td>
							<html:submit property="editActivityMonitoring" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.editActivity"/>
							</html:submit> 
						</td><td>			
							<html:submit property="statsMonitoring" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.stats"/>
							</html:submit> 
						</td>
				</tr>
				</table>
			</html:form>		
		</c:if>						
		
		<html:form  action="/authoring?method=loadQ&validate=false" enctype="multipart/form-data" method="POST" target="_self">
		<table align=center> <!-- Dave to take off--> 
			<c:if test="${requestScope.stopRenderingQuestions != 'true'}"> 			
				<tr><td>
					<fmt:message key="label.authoring.qa"/>
				</td></tr>
			</c:if>								
			
			<c:if test="${sessionScope.isDefineLater == 'true'}"> 			
				<c:if test="${requestScope.startMonitoringSummaryRequest != 'true'}"> 			
					<tr><td class="error">
						<fmt:message key="error.content.beingModified"/>
					</td></tr>
				</c:if>						
			</c:if>						

			<c:if test="${sessionScope.isDefineLater != 'true' || sessionScope.isMonitoringDefineLater == 'true' }"> 			
				<c:if test="${sessionScope.contentLocked == 'true'}"> 			
					<tr><td class="error">
						<fmt:message key="error.content.inUse"/>
					</td></tr>
				</c:if>						
				<c:if test="${sessionScope.contentLocked == 'false'}"> 			
					<tr> <td class="error">
						<html:errors/>
					</td></tr>
					
					<c:if test="${requestScope.startMonitoringSummaryRequest != 'true'}"> 			
						<tr><td>
								<html:submit property="choiceBasic" styleClass="longButton" onmouseover="pviiClassNew(this,'longButtonover')" onmouseout="pviiClassNew(this,'longButton')">
									<bean:message key="button.basic"/>
								</html:submit>
								<html:submit property="choiceAdvanced" styleClass="longButton" onmouseover="pviiClassNew(this,'longButtonover')" onmouseout="pviiClassNew(this,'longButton')">
									<bean:message key="button.advanced"/>
								</html:submit>
								<html:submit property="choiceInstructions" styleClass="longButton" onmouseover="pviiClassNew(this,'longButtonover')" onmouseout="pviiClassNew(this,'longButton')">
									<bean:message key="button.instructions"/>
								</html:submit>
						</td></tr>
					</c:if>						
				</c:if>					
			</c:if>											
			</hl> 
	
			<c:if test="${sessionScope.isDefineLater != 'true' || sessionScope.isMonitoringDefineLater == 'true' }"> 			
				<c:if test="${sessionScope.contentLocked != 'true'}"> 			
						<c:choose> 
						  <c:when test="${sessionScope.choice == sessionScope.choiceTypeBasic}" > 
								<jsp:include page="BasicContent.jsp" />
						  </c:when> 
						  <c:when test="${sessionScope.choice == sessionScope.choiceTypeAdvanced}" > 
								<jsp:include page="AdvancedContent.jsp" />
						  </c:when> 
						  <c:when test="${sessionScope.choice == sessionScope.choiceTypeInstructions}" > 
								<jsp:include page="InstructionsContent.jsp" />
						  </c:when> 
						  <c:otherwise> 
							  	<jsp:include page="BasicContent.jsp" />
						  </c:otherwise>
						</c:choose> 
				</c:if>											
			</c:if>															
			</table>
			</html:form>
	</c:if>						
	
 <td> </tr>
	