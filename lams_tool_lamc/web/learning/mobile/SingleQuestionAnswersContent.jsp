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

<c:if test="${not empty mcLearnerStarterDTO.submissionDeadline}">
	<div class="info">
		<fmt:message key="authoring.info.teacher.set.restriction" >
			<fmt:param><lams:Date value="${mcLearnerStarterDTO.submissionDeadline}" /></fmt:param>
		</fmt:message>	
	</div>
</c:if>

<c:if
	test="${mcGeneralLearnerFlowDTO.retries == 'true' && mcGeneralLearnerFlowDTO.passMark != '0'}">

	<strong> <fmt:message key="label.learner.message" /> ( <c:out
			value="${mcGeneralLearnerFlowDTO.passMark}" /> ) </strong>
</c:if>
<br/><br/>

<html:hidden property="nextQuestionSelected" />
<html:hidden property="continueOptionsCombined" />
<html:hidden property="questionIndex"
	value="${mcGeneralLearnerFlowDTO.questionIndex}" />


<ul data-role="listview" data-inset="true" data-theme="d">
<c:forEach var="dto" varStatus="status"
	items="${requestScope.listQuestionCandidateAnswersDto}">
	<c:if
		test="${dto.displayOrder == mcGeneralLearnerFlowDTO.questionIndex}">
		
		<li>
		<fieldset data-role="controlgroup">
			<legend>
				<c:out value="${dto.question}" escapeXml="false" />
				
				<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
					[
					<strong> <fmt:message key="label.mark" /> </strong>
					<c:out value="${dto.mark}" />
					]
				</c:if>				
			</legend>
		
			<c:forEach var="ca" varStatus="status"
				items="${dto.candidateAnswerUids}">
				<div class="indent">
					<input type="radio" name="checkedCa" class="noBorder"
						value="${dto.questionUid}-${ca.value}" id="checkedCa${ca.key}">
	
					<c:forEach var="caText" varStatus="status"
						items="${dto.candidateAnswers}">
						<c:if test="${ca.key == caText.key}">
							<label for="checkedCa${ca.key}"><c:out value="${caText.value}" escapeXml="false" /></label>
						</c:if>
					</c:forEach>
				</div>
			</c:forEach>

		</fieldset>
		</li>
	</c:if>

</c:forEach>
</ul>


<html:hidden property="donePreview" />

<div class="space-top button-inside">
        <c:if test="${mcGeneralLearnerFlowDTO.totalCountReached != 'true'  &&  mcGeneralLearnerFlowDTO.totalQuestionCount != '1'}"> 
        	<button type="button" name="continueOptionsCombined" id="continueOptionsCombined" onclick="submitNextQuestionSelected();" data-theme="b"> 
        		<fmt:message key="button.continue" /> 
            </button> 
        </c:if>

	<c:if test="${mcGeneralLearnerFlowDTO.totalQuestionCount == '1'}">
		<button type="button" name="continueOptionsCombined" id="continueOptionsCombined" onclick="doSubmit();" data-theme="b">
			<fmt:message key="button.continue" />
		</button>
	</c:if>

	<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached == 'true'}">
		<button type="button" name="continueOptionsCombined" id="continueOptionsCombined" onclick="submitAllAnswers();" data-theme="b">
			<fmt:message key="button.submit" />
		</button>
	</c:if>
</div>
<!--options content ends here-->

</div><!-- /page div -->

<div data-role="footer" data-theme="b">
	<h2>&nbsp;</h2>
</div>
