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
	<strong> <fmt:message key="label.learner.message" /> ( <c:out
			value="${mcGeneralLearnerFlowDTO.passMark}" /> ) 
	</strong>
</c:if>

<html:hidden property="nextQuestionSelected" />
<html:hidden property="continueOptionsCombined" />
<html:hidden property="questionIndex" value="${mcGeneralLearnerFlowDTO.questionIndex}" />

<c:forEach var="dto" varStatus="status"	items="${requestScope.learnerAnswersDTOList}">
	<c:if test="${dto.displayOrder == mcGeneralLearnerFlowDTO.questionIndex}">
	
		<div class="shading-bg">
			<div style="overflow: auto;">
				<span class="float-left space-right">
					${dto.displayOrder})
				</span>
				<c:out value="${dto.question}" escapeXml="false" />
				
				<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
					[
					<strong> <fmt:message key="label.mark" /> </strong>
					<c:out value="${dto.mark}" />
					]
				</c:if>				
			</div>
		</div>
		
		<table class="indent">
			<tbody>
				<c:forEach var="option" varStatus="status" items="${dto.options}">
					<tr>
					
						<td>
							<input type="radio" name="checkedCa" class="noBorder" value="${dto.questionUid}-${option.uid}"
								<c:if test="${option.selected}">checked="checked"</c:if>>
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

	</c:if>
</c:forEach>

<div class="space-bottom-top">
    <c:if test="${mcGeneralLearnerFlowDTO.totalCountReached != 'true'  &&  mcGeneralLearnerFlowDTO.totalQuestionCount != '1'}"> 
    	<html:button property="continueOptionsCombined" onclick="submitNextQuestionSelected();" styleClass="button"> 
        	<fmt:message key="button.continue" /> 
        </html:button> 
    </c:if>

	<c:if test="${mcGeneralLearnerFlowDTO.totalQuestionCount == '1'}">
		<html:button property="continueOptionsCombined" onclick="doSubmit();" styleClass="button">
			<fmt:message key="button.continue" />
		</html:button>
	</c:if>

	<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached == 'true'}">
		<html:button property="continueOptionsCombined" onclick="submitAllAnswers();" styleClass="button">
			<fmt:message key="button.submit" />
		</html:button>
	</c:if>

	<p>&nbsp;</p>
</div>
<!--options content ends here-->
