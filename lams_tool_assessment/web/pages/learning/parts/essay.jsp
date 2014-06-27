<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<table class="question-table">
	<tr>
		<td class="reg-padding">
			<c:choose>
				<c:when test="${question.allowRichEditor && !finishedLock && hasEditRight}">
					<lams:CKEditor id="question${status.index}" value="${question.answerString}" contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner"></lams:CKEditor>
				</c:when>
				<c:when test="${question.allowRichEditor && finishedLock}">
					${question.answerString}
				</c:when>				
				<c:otherwise>
					<lams:STRUTS-textarea property="question${status.index}" rows="7" cols="60" value="${question.answerString}" disabled="${finishedLock || !hasEditRight}" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>

<%@ include file="markandpenaltyarea.jsp"%>