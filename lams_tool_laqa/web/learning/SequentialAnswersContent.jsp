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

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>


	<!--question content goes here-->
			<tr> <td>
			<table class="forms">
				<c:forEach var="questionEntry" items="${mcGeneralLearnerFlowDTO.mapQuestionContentLearner}">
			  		<c:if test="${questionEntry.key == mcGeneralLearnerFlowDTO.currentQuestionIndex}"> 			
					  <tr>
						<td colspan=2 NOWRAP> 
					  		<b> <bean:message key="label.question"/> 
						  	<c:out value="${questionEntry.key}" escapeXml="false"/>:  </b> 
						  	<c:out value="${questionEntry.value}" escapeXml="false"/>
						</td>
					  </tr>

					  <tr> 
						<td NOWRAP>  <b> <bean:message key="label.answer"/> </b> </td>
						<td NOWRAP>
				 			<html:text property="answer" size="60" maxlength="255" value="${mcGeneralLearnerFlowDTO.currentAnswer}"/>
				 			<html:hidden property="currentQuestionIndex" value="${questionEntry.key}"/>
				 		</td>
				  	  </tr>
 					</c:if> 			
				</c:forEach>
			</table>

	<!--question content ends here-->
			<table class="forms">
				<tr>
				<c:choose>  
				  <c:when test="${(mcGeneralLearnerFlowDTO.currentQuestionIndex == mcGeneralLearnerFlowDTO.totalQuestionCount) && 
				  				  (sessionScope.totalQuestionCount != 1) }"> 
					<td NOWRAP>
						<html:submit onclick="javascript:submitMethod('getPreviousQuestion');" styleClass="button">
									<bean:message key="button.getPreviousQuestion"/>
						</html:submit>	 				 		  					
						<html:submit onclick="javascript:submitMethod('submitAnswersContent');" styleClass="button">
									<bean:message key="button.done"/>
						</html:submit>	 				 		  					
					</td> 
				  </c:when> 

  				  <c:when test="${(mcGeneralLearnerFlowDTO.currentQuestionIndex == mcGeneralLearnerFlowDTO.totalQuestionCount) && 
				  				  (mcGeneralLearnerFlowDTO.totalQuestionCount == 1) }"> 
					 <td NOWRAP valign=top> 
						<html:submit onclick="javascript:submitMethod('submitAnswersContent');" styleClass="button">
									<bean:message key="button.done"/>
						</html:submit>	 				 		  					
					</td> 
				  </c:when> 
				  
				  
 				  <c:when test="${mcGeneralLearnerFlowDTO.currentQuestionIndex != mcGeneralLearnerFlowDTO.totalQuestionCount && 
				 				  mcGeneralLearnerFlowDTO.currentQuestionIndex > 1}"> 
					 <td NOWRAP valign=top> 
						<html:submit onclick="javascript:submitMethod('getPreviousQuestion');" styleClass="button">
									<bean:message key="button.getPreviousQuestion"/>
						</html:submit>	 				 		  					
						<html:submit onclick="javascript:submitMethod('getNextQuestion');" styleClass="button">
									<bean:message key="button.getNextQuestion"/>
						</html:submit>	 				 		  					
					</td> 
  				  </c:when> 
  				  
				  <c:otherwise>
					 <td NOWRAP valign=top> 
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

