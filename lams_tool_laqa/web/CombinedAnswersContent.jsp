<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

			<tr> <td>
			<table>
				<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
						  <tr>
						  	<td colspan=2> Question <c:out value="${questionEntry.key}"/>: <c:out value="${questionEntry.value}"/>
						  </tr>
						  <tr> 
					 		<td> <fmt:message key="label.answers"/> </td>
					 		<td>
					 			<input type="text" name="answer<c:out value="${questionEntry.key}"/>" size="60" maxlength="255" value=""/> 
					 		</td>
					  	  </tr>
					  	  <tr><td> &nbsp </td> </tr>
				</c:forEach>
			</table>
			
			<hr>
			<table>
			<tr>
				 <td> 
				 	 <html:submit property="submitAnswersContent" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
						<bean:message key="button.submitAllContent"/>
					</html:submit>
				</td> 
			</tr>
			</table>
			
