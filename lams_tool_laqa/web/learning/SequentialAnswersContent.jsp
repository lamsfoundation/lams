<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<!--question content goes here-->
			<tr> <td >
			<table>
				<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
			  		<c:if test="${questionEntry.key == sessionScope.currentQuestionIndex}"> 			
					  <tr>
					  	<td colspan=2 NOWRAP class="input" valign=top> <font size=2> 
					  		<b> <bean:message key="label.question"/> 
						  	<c:out value="${questionEntry.key}" escapeXml="false"/>:</b> <c:out value="${questionEntry.value}" escapeXml="false"/>
					  	</font>
					  </tr>
					  <tr> 
				 		<td NOWRAP class="input" valign=top> <font size=2> <b> <bean:message key="label.answer"/> </b> </font> </td>
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
				  <c:when test="${(sessionScope.currentQuestionIndex == sessionScope.totalQuestionCount) && 
				  				  (sessionScope.totalQuestionCount !=1) }"> 
					 <td NOWRAP class="input" valign=top> 
						<html:submit onclick="javascript:submitMethod('getPreviousQuestion');" styleClass="button">
									<bean:message key="button.getPreviousQuestion"/>
						</html:submit>	 				 		  					
					 		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
						<html:submit onclick="javascript:submitMethod('submitAnswersContent');" styleClass="button">
									<bean:message key="button.done"/>
						</html:submit>	 				 		  					
					</td> 
				  </c:when> 

  				  <c:when test="${(sessionScope.currentQuestionIndex == sessionScope.totalQuestionCount) && 
				  				  (sessionScope.totalQuestionCount == 1) }"> 
					 <td NOWRAP class="input" valign=top> 
						<html:submit onclick="javascript:submitMethod('submitAnswersContent');" styleClass="button">
									<bean:message key="button.done"/>
						</html:submit>	 				 		  					
					</td> 
				  </c:when> 
				  
				  
 				  <c:when test="${sessionScope.currentQuestionIndex != sessionScope.totalQuestionCount && sessionScope.currentQuestionIndex > 1}"> 
					 <td NOWRAP class="input" valign=top> 
						<html:submit onclick="javascript:submitMethod('getPreviousQuestion');" styleClass="button">
									<bean:message key="button.getPreviousQuestion"/>
						</html:submit>	 				 		  					
						&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
						<html:submit onclick="javascript:submitMethod('getNextQuestion');" styleClass="button">
									<bean:message key="button.getNextQuestion"/>
						</html:submit>	 				 		  					
					</td> 
  				  </c:when> 
  				  
				  <c:otherwise>
					 <td NOWRAP class="input" valign=top> 
						<html:submit onclick="javascript:submitMethod('getNextQuestion');" styleClass="button">
									<bean:message key="button.getNextQuestion"/>
						</html:submit>	 				 		  					
					</td> 
				  </c:otherwise>
				</c:choose> 
				</tr>
			</table>
		</td>
		</tr>

