<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

	<!--question content goes here-->
			<tr> <td>
			<table>
				<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
			  		<c:if test="${questionEntry.key == sessionScope.currentQuestionIndex}"> 			
					  <tr>
					  	<td colspan=2> <fmt:message key="label.question"/> <c:out value="${questionEntry.key}"/>: <c:out value="${questionEntry.value}"/>
					  </tr>
					  <tr> 
				 		<td> <fmt:message key="label.answers"/> </td>
				 		<td> 
				 			<html:text property="answer" size="60" maxlength="255" value="${sessionScope.currentAnswer}"/>
				 			<html:hidden property="currentQuestionIndex" value="${questionEntry.key}"/>
				 		</td>
				  	  </tr>
 					</c:if> 			
				</c:forEach>
			</table>

	<!--question content ends here-->

			<hr>
			<table>
				<tr>
				<c:choose>  
				  <c:when test="${sessionScope.currentQuestionIndex == sessionScope.totalQuestionCount}"> 
					 <td> 
					 	<html:submit property="getPreviousQuestion" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.getPreviousQuestion"/>
						</html:submit>
					 		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
					 	 <html:submit property="submitAnswersContent" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.submitAllContent"/>
						</html:submit>
					</td> 
				  </c:when> 
 				  <c:when test="${sessionScope.currentQuestionIndex != sessionScope.totalQuestionCount && sessionScope.currentQuestionIndex > 1}"> 
					 <td> 
						<html:submit property="getPreviousQuestion" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.getPreviousQuestion"/>
						</html:submit>
						&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
						 <html:submit property="getNextQuestion" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.getNextQuestion"/>
						</html:submit>
					</td> 
  				  </c:when> 
				  <c:otherwise>
					 <td> 
						 <html:submit property="getNextQuestion" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.getNextQuestion"/>
						</html:submit>
					</td> 
				  </c:otherwise>
				</c:choose> 
				</tr>
			</table>
		</td>
		</tr>

