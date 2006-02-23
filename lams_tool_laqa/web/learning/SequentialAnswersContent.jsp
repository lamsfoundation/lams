<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>

	<!--question content goes here-->
			<tr> <td >
			<table>
				<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
			  		<c:if test="${questionEntry.key == sessionScope.currentQuestionIndex}"> 			
					  <tr>
					  	<td colspan=2 NOWRAP class="input" valign=top> <font size=2> 
					  		<bean:message key="label.question"/> 
						  	<c:out value="${questionEntry.key}" escapeXml="false"/>: <c:out value="${questionEntry.value}" escapeXml="false"/>
					  	</font>
					  </tr>
					  <tr> 
				 		<td NOWRAP class="input" valign=top> <font size=2> <bean:message key="label.answers"/> </font> </td>
				 		<td NOWRAP class="input" valign=top> 
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
					 <td NOWRAP class="input" valign=top> 
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
					 <td NOWRAP class="input" valign=top> 
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
					 <td NOWRAP class="input" valign=top> 
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

