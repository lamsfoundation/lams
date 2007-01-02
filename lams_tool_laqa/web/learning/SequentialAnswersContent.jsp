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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="fck-editor" prefix="FCK"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<!--question content goes here-->

<div class="shading-bg">
	<c:forEach var="questionEntry"
		items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

		<c:if
			test="${questionEntry.key == generalLearnerFlowDTO.currentQuestionIndex}">

			<p>
				<strong><fmt:message key="label.question" /> <c:out
						value="${questionEntry.key}" />:</strong>
				<br>
				<c:out value="${questionEntry.value}" escapeXml="false" />
			</p>

			<p>
				<strong><fmt:message key="label.answer" /> </strong>
			</p>


			<lams:textarea  name="answer" rows="5" cols="60" class="text-area" ><c:out value='${generalLearnerFlowDTO.currentAnswer}' escapeXml='false' /></lams:textarea>
			<html:hidden property="currentQuestionIndex"
				value="${questionEntry.key}" />


		</c:if>
	</c:forEach>
</div>

<div class="space-bottom">
	<!--question content ends here-->
	<c:choose>
		<c:when
			test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount != 1) }">

			<html:button property="btnGetPrevious"
				onclick="javascript:submitMethod('getPreviousQuestion');"
				styleClass="button">
				<fmt:message key="button.getPreviousQuestion" />
			</html:button>

			<div align="right">
				<html:button property="btnDone"
					onclick="javascript:submitMethod('submitAnswersContent');"
					styleClass="button">
					<fmt:message key="button.done" />
				</html:button>
			</div>

		</c:when>

		<c:when
			test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount == 1) }">
			<div align="right">
				<html:button property="btnDone"
					onclick="javascript:submitMethod('submitAnswersContent');"
					styleClass="button">
					<fmt:message key="button.done" />
				</html:button>
			</div>

		</c:when>

		<c:when
			test="${generalLearnerFlowDTO.currentQuestionIndex != generalLearnerFlowDTO.totalQuestionCount && 
				 				  generalLearnerFlowDTO.currentQuestionIndex > 1}">

			<html:button property="btnGetPrevious"
				onclick="javascript:submitMethod('getPreviousQuestion');"
				styleClass="button">
				<fmt:message key="button.getPreviousQuestion" />
			</html:button>
			<html:button property="btnGetNext"
				onclick="javascript:submitMethod('getNextQuestion');"
				styleClass="button">
				<fmt:message key="button.getNextQuestion" />
			</html:button>

		</c:when>

		<c:otherwise>


			<html:button property="btnGetNext"
				onclick="javascript:submitMethod('getNextQuestion');"
				styleClass="button">
				<fmt:message key="button.getNextQuestion" />
			</html:button>


		</c:otherwise>
	</c:choose>

</div>
