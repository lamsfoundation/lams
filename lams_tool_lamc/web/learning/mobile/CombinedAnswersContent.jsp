<%@ include file="/common/taglibs.jsp"%>

<!--options content goes here-->

<p>
	<c:out value="${mcGeneralLearnerFlowDTO.activityInstructions}" escapeXml="false" />
</p>

	<c:if test="${not empty submissionDeadline}">
		<div class="info">
			<fmt:message key="authoring.info.teacher.set.restriction" >
				<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
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
<c:forEach var="dto" varStatus="status"	items="${requestScope.learnerAnswersDTOList}">

	<li>
		<div>
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
		</div>
		
	<fieldset data-role="controlgroup">
		<legend>							
		</legend>
	
		<c:forEach var="option" varStatus="status" items="${dto.options}">
			<c:choose>
				<c:when test="${hasEditRight}">		
					<input type="radio" name="checkedCa${dto.questionUid}" id="checkedCa${option.uid}" value="${dto.questionUid}-${option.uid}"
						<c:if test="${option.selected}">checked="checked"</c:if>>
				</c:when>
				
				<c:otherwise>
					<input type="radio" name="checkedCa${dto.questionUid}" id="checkedCa${option.uid}" value="${dto.questionUid}-${option.uid}" 
						<c:if test="${option.selected}">checked="checked"</c:if> disabled="disabled">
				</c:otherwise>
			</c:choose>

			<label for="checkedCa${option.uid}">
				<c:if test="${isPrefixAnswersWithLetters}">
					<c:set var="seqLetter" value="${status.index}"/>
					<span class="float-left">
						<%=Character.toChars(97 + (Integer)pageContext.getAttribute("seqLetter"))%>)
					</span>
				</c:if>
						
				<c:out value="${option.mcQueOptionText}" escapeXml="false" />
			</label>
		
		</c:forEach>
	</fieldset>
	</li>
</c:forEach>
</ul>

<div class="space-top button-inside">
	<html:hidden property="continueOptionsCombined" value="Continue" />

	<c:if test="${hasEditRight}">
		<button type="button" name="continueButton" id="continueButton" data-theme="b" onclick="doSubmit();">
			<fmt:message key="button.continue" />
		</button>
	</c:if>
</div>

<!--options content ends here-->

</div><!-- /page div -->

<div data-role="footer" data-theme="b">
	<h2>&nbsp;</h2>
</div>
