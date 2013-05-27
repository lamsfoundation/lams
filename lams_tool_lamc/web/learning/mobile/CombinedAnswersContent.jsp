<%@ include file="/common/taglibs.jsp"%>

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

	<p>
		<fmt:message key="label.learner.message" />
		(
		<c:out value="${mcGeneralLearnerFlowDTO.passMark}" />
		)
	</p>


</c:if>


<ul data-role="listview" data-inset="true" data-theme="d" >
<c:forEach var="dto" varStatus="status"	items="${requestScope.listQuestionCandidateAnswersDto}">

	<li>
	<fieldset data-role="controlgroup">
		<legend>
			<span class="float-left space-right">
				${dto.displayOrder})
			</span>
			<c:out value="${dto.question}" escapeXml="false" />

			<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">			
				[
				<strong><fmt:message key="label.mark" /></strong>
				<c:out value="${dto.mark}" />
				]
			</c:if>							
		</legend>
	
		<c:forEach var="ca" varStatus="status" items="${dto.candidateAnswerUids}">

			<input type="radio" name="checkedCa${dto.questionUid}" id="checkedCa${ca.key}" value="${dto.questionUid}-${ca.value}">

			<c:forEach var="caText" varStatus="status" items="${dto.candidateAnswers}">
				<c:if test="${ca.key == caText.key}">
					<label for="checkedCa${ca.key}"><c:out value="${caText.value}" escapeXml="false" /></label>
				</c:if>
			</c:forEach>
		
		</c:forEach>
	</fieldset>
	</li>
</c:forEach>
</ul>

<html:hidden property="donePreview" />

<div class="space-top button-inside">
	<html:hidden property="continueOptionsCombined" value="Continue" />

	<button type="button" name="continueButton" id="continueButton" data-theme="b" onclick="doSubmit();">
		<fmt:message key="button.continue" />
	</button>
</div>

<!--options content ends here-->

</div><!-- /page div -->

<div data-role="footer" data-theme="b">
	<h2>&nbsp;</h2>
</div>
