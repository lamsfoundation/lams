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

<!--question content goes here-->

<div class="shading-bg">
	<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

		<c:if test="${questionEntry.key == generalLearnerFlowDTO.currentQuestionIndex}">

			<p>
				<strong>
					<fmt:message key="label.question" /> 
					<c:out value="${questionEntry.key}" />:
				</strong>
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

			<p>
				<strong><fmt:message key="label.answer" /> </strong>
			</p>

			<div data-sequence-id="${questionEntry.key}" data-is-ckeditor="${generalLearnerFlowDTO.allowRichEditor}"
				 data-min-words-limit="${questionEntry.value.minWordsLimit}"
				<c:if test="${questionEntry.value.minWordsLimit != 0}">class="min-words-limit-enabled"</c:if>
			>
				<c:choose>
					<c:when test="${generalLearnerFlowDTO.allowRichEditor}">
						<lams:CKEditor id="answer${questionEntry.key}" value="${generalLearnerFlowDTO.currentAnswer}"
							contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner">
						</lams:CKEditor>
					</c:when>
		
					<c:otherwise>
						<lams:textarea  name="answer" id="answer${questionEntry.key}" rows="5" cols="60" class="text-area" ><c:out value='${generalLearnerFlowDTO.currentAnswer}' escapeXml='false' /></lams:textarea>
					</c:otherwise>
				</c:choose>
			</div>
			
			<html:hidden property="currentQuestionIndex"value="${questionEntry.key}" />

		</c:if>
	</c:forEach>
</div>

<div class="space-bottom">
	<!--question content ends here-->
	<c:choose>
		<c:when test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount != 1) }">

			<html:button property="btnGetPrevious" onclick="javascript:submitMethod('getPreviousQuestion');" styleClass="button">
				<fmt:message key="button.getPreviousQuestion" />
			</html:button>

			<div align="right">
				<html:button property="btnDone" onclick="javascript:submitMethod('submitAnswersContent');" styleClass="button">
					<fmt:message key="button.done" />
				</html:button>
			</div>

		</c:when>

		<c:when test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount == 1) }">
			<div align="right">
				<html:button property="btnDone" onclick="javascript:submitMethod('submitAnswersContent');" styleClass="button">
					<fmt:message key="button.done" />
				</html:button>
			</div>

		</c:when>

		<c:when test="${generalLearnerFlowDTO.currentQuestionIndex != generalLearnerFlowDTO.totalQuestionCount && 
				 				  generalLearnerFlowDTO.currentQuestionIndex > 1}">

			<html:button property="btnGetPrevious" onclick="javascript:submitMethod('getPreviousQuestion');" styleClass="button">
				<fmt:message key="button.getPreviousQuestion" />
			</html:button>
			<html:button property="btnGetNext"
				onclick="javascript:submitMethod('getNextQuestion');"
				styleClass="button">
				<fmt:message key="button.getNextQuestion" />
			</html:button>

		</c:when>

		<c:otherwise>
			<html:button property="btnGetNext" onclick="javascript:submitMethod('getNextQuestion');" styleClass="button">
				<fmt:message key="button.getNextQuestion" />
			</html:button>
		</c:otherwise>
	</c:choose>

</div>
