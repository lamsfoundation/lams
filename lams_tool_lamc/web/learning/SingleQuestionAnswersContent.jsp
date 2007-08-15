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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<!--options content goes here-->

<p>
	<c:out value="${mcGeneralLearnerFlowDTO.activityInstructions}" escapeXml="false" />
</p>

<c:if
	test="${mcGeneralLearnerFlowDTO.retries == 'true' && mcGeneralLearnerFlowDTO.passMark != '0'}">

	<strong> <fmt:message key="label.learner.message" /> ( <c:out
			value="${mcGeneralLearnerFlowDTO.passMark}" /> ) </strong>
	</c:if>

<html:hidden property="nextQuestionSelected" />
<html:hidden property="continueOptionsCombined" />
<html:hidden property="questionIndex"
	value="${mcGeneralLearnerFlowDTO.questionIndex}" />



<c:forEach var="dto" varStatus="status"
	items="${requestScope.listQuestionCandidateAnswersDto}">
	<c:if
		test="${dto.displayOrder == mcGeneralLearnerFlowDTO.questionIndex}">
		<div class="shading-bg">
			<div style="overflow: auto;">
				<c:out value="${dto.question}" escapeXml="false" />
				
				<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
					[
					<strong> <fmt:message key="label.mark" /> </strong>
					<c:out value="${dto.mark}" />
					]
				</c:if>				
			</div>
		</div>
		<c:forEach var="ca" varStatus="status"
			items="${dto.candidateAnswerUids}">
			<div class="indent">
				<input type="radio" name="checkedCa" class="noBorder"
					value="${dto.questionUid}-${ca.value}">

				<c:forEach var="caText" varStatus="status"
					items="${dto.candidateAnswers}">
					<c:if test="${ca.key == caText.key}">
						<c:out value="${caText.value}" escapeXml="false" />
					</c:if>
				</c:forEach>
			</div>
		</c:forEach>

	</c:if>

</c:forEach>



<html:hidden property="donePreview" />

<div class="space-bottom-top">


	<c:if
		test="${mcGeneralLearnerFlowDTO.totalCountReached != 'true'  &&  mcGeneralLearnerFlowDTO.totalQuestionCount != '1'}">
		<img src="<c:out value="${tool}"/>images/green_arrow_down_right.gif"
			align=left onclick="javascript:if (verifyCandidateSelected()){ submitNextQuestionSelected();}">
	</c:if>


	<c:if test="${mcGeneralLearnerFlowDTO.totalQuestionCount == '1'}">
		<html:submit property="continueOptionsCombined" onclick="javascript:verifyCandidateSelected()" styleClass="button">
			<fmt:message key="button.continue" />
		</html:submit>
	</c:if>

	<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached == 'true'}">
		<html:button property="continueOptionsCombined" onclick="javascript:if (verifyCandidateSelected()){ submitAllAnswers();}" styleClass="button">
			<fmt:message key="button.submit" />
		</html:button>
	</c:if>

	<p>&nbsp;</p>
</div>
<!--options content ends here-->
