<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>


			<tr> <td>
			<table>
				<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
						  <tr>
						  	<td colspan=2 NOWRAP class="input" valign=top> <font size=2> Question <c:out value="${questionEntry.key}" escapeXml="false"/>: 
						  		<c:out value="${questionEntry.value}" escapeXml="false"/> 
						  	</font> </td>
						  </tr>
						  <tr> 
					 		<td NOWRAP class="input" valign=top > <font size=2> <bean:message key="label.answers"/>  </font> </td>
					 		<td  NOWRAP class="input" valign=top>
					 			<input type="text" name="answer<c:out value="${questionEntry.key}"  escapeXml="false"/>" size="60" maxlength="255" value=""/> 
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
			
