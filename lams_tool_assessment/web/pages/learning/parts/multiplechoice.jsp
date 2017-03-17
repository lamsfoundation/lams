<%@ include file="/common/taglibs.jsp"%>

<div class="question-type">
	<c:choose>
		<c:when test="${question.multipleAnswersAllowed}">
			<fmt:message key="label.learning.choose.at.least.one.answer" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.learning.choose.one.answer" />
		</c:otherwise>
	</c:choose>
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:forEach var="option" items="${question.optionDtos}">
			<tr>
				<td class="has-radio-button">
					<c:choose>
						<c:when test="${question.multipleAnswersAllowed}">
							<input type="checkbox" name="question${status.index}_${option.sequenceId}" value="${true}"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
								<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
							/>
						</c:when>
						<c:otherwise>
							<input type="radio" name="question${status.index}" value="${option.sequenceId}"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
		 						<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
							/>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:out value="${option.optionString}" escapeXml="false" />
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
