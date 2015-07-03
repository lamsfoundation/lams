<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<ul data-role="listview" data-theme="c" id="qaQuestionsSequential">
	<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">
		<c:if test="${questionEntry.key == generalLearnerFlowDTO.currentQuestionIndex}">
		
			<li>
				<p class="big-space-top">
					<strong><fmt:message key="label.question" /> <c:out
							value="${questionEntry.key}" />:</strong>
					<c:if test="${questionEntry.value.required}">
						<fmt:message key="label.required" />
					</c:if>
					<c:if test="${questionEntry.value.minWordsLimit != 0}">
						<fmt:message key="label.minimum.number.words" >
							<fmt:param>&nbsp;${questionEntry.value.minWordsLimit}</fmt:param>
						</fmt:message>
					</c:if>					
					<br>
					<c:out value="${questionEntry.value.question}" escapeXml="false" />
				</p>
	
				<p class="small-space-top">
					<strong><fmt:message key="label.answer" /> </strong>
				</p>
	
				<div data-sequence-id="${questionEntry.key}" data-is-ckeditor="${generalLearnerFlowDTO.allowRichEditor}"
					 data-min-words-limit="${questionEntry.value.minWordsLimit}"
					class="space-bottom <c:if test="${questionEntry.value.minWordsLimit != 0}">min-words-limit-enabled</c:if>"
				>
					<lams:textarea  name="answer" id="answer" rows="5" cols="60" class="text-area" ><c:out value='${generalLearnerFlowDTO.currentAnswer}' escapeXml='false' /></lams:textarea>
				</div>
				
				<html:hidden property="currentQuestionIndex"value="${questionEntry.key}" />
			</li>
			
		</c:if>
	</c:forEach>
</ul>

</div><!-- /page div -->

<div data-role="footer" data-theme="b" class="ui-bar">

	<c:choose>
		<c:when	test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  (generalLearnerFlowDTO.totalQuestionCount != 1) }">
			<button name="btnGetPrevious" onclick="javascript:submitMethod('getPreviousQuestion');" data-icon="arrow-l" data-theme="b">
				<fmt:message key="button.getPreviousQuestion" />
			</button>
			
			<span class="ui-finishbtn-right">
				<button name="btnDone"	onclick="javascript:submitMethod('submitAnswersContent');" data-icon="arrow-r" data-theme="b">
					<fmt:message key="button.done" />
				</button>
			</span>
		</c:when>

		<c:when	test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  (generalLearnerFlowDTO.totalQuestionCount == 1) }">
			<span class="ui-finishbtn-right">
				<button name="btnDone"	onclick="javascript:submitMethod('submitAnswersContent');" data-icon="arrow-r" data-theme="b">
					<fmt:message key="button.done" />
				</button>
			</span>
		</c:when>

		<c:when	test="${generalLearnerFlowDTO.currentQuestionIndex != generalLearnerFlowDTO.totalQuestionCount && 
				 generalLearnerFlowDTO.currentQuestionIndex > 1}">
			<button name="btnGetPrevious" onclick="javascript:submitMethod('getPreviousQuestion');" data-icon="arrow-l" data-theme="b">
				<fmt:message key="button.getPreviousQuestion" />
			</button>

			<span class="ui-finishbtn-right">
				<button name="btnGetNext" onclick="javascript:submitMethod('getNextQuestion');" data-icon="arrow-r" data-theme="b">
					<fmt:message key="button.getNextQuestion" />
				</button>
			</span>
		</c:when>

		<c:otherwise>
			<span class="ui-finishbtn-right">
				<button name="btnGetNext" onclick="javascript:submitMethod('getNextQuestion');" class="button" data-theme="b" data-icon="arrow-r">
					<fmt:message key="button.getNextQuestion" />
				</button>
			</span>
		</c:otherwise>
	</c:choose>

</div><!-- /footer -->