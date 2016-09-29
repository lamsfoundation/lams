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

<c:if test="${not empty submissionDeadline}">
	<div class="info">
		<fmt:message key="authoring.info.teacher.set.restriction" >
			<fmt:param><lams:Date value="${submissionDeadline}" /></fmt:param>
		</fmt:message>	
	</div>
</c:if>

<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true' && mcGeneralLearnerFlowDTO.passMark != '0'}">

	<p>
		<fmt:message key="label.learner.message" />
		(
		<c:out value="${mcGeneralLearnerFlowDTO.passMark}" />
		)
	</p>

</c:if>

<c:forEach var="dto" items="${requestScope.learnerAnswersDTOList}">

	<div class="shading-bg">
		<div style="overflow: auto;">
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
	</div>
	
	<table class="indent">
		<tbody>
			<c:forEach var="option" items="${dto.options}" varStatus="status">
			
				<tr>
		
					<td>
						<c:choose>
							<c:when test="${hasEditRight}">		
								<input type="radio" name="checkedCa${dto.questionUid}" class="noBorder" value="${dto.questionUid}-${option.uid}"
									<c:if test="${option.selected}">checked="checked"</c:if>>
							</c:when>
							
							<c:otherwise>
								<input type="radio" name="checkedCa${dto.questionUid}" class="noBorder" value="${dto.questionUid}-${option.uid}" 
									<c:if test="${option.selected}">checked="checked"</c:if> disabled="disabled">
							</c:otherwise>
						</c:choose>
					</td>
		
					<td width="100%">
						<c:if test="${isPrefixAnswersWithLetters}">
							<c:set var="seqLetter" value="${status.index}"/>
							<span class="float-left">
								<%=Character.toChars(97 + (Integer)pageContext.getAttribute("seqLetter"))%>)
							</span>
						</c:if>
								
						<c:out value="${option.mcQueOptionText}" escapeXml="false" />
					</td>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>

</c:forEach>

<div class="space-bottom-top align-right">
	<html:hidden property="continueOptionsCombined" value="Continue" />

	<c:if test="${hasEditRight}">
		<html:button property="continueButton" styleClass="button" onclick="doSubmit();" styleId="continueButton">
			<fmt:message key="button.continue" />
		</html:button>
	</c:if>
</div>

<!--options content ends here-->

