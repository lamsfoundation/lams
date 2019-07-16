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
		<c:forEach var="option" items="${question.optionDtos}"  varStatus="answerStatus">
			<tr>
				<td class="${question.prefixAnswersWithLetters?'has-radio-button-prefix':'has-radio-button'}">
					<c:choose>
						<c:when test="${question.multipleAnswersAllowed}">
							<input type="checkbox" name="question${status.index}_${option.uid}" value="${true}"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
								<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
							/>
						</c:when>
						<c:otherwise>
							<input type="radio" name="question${status.index}" value="${option.uid}"
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
		 						<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
							/>
						</c:otherwise>
					</c:choose>
			 		<c:if test="${question.prefixAnswersWithLetters}">
			 			&nbsp;${option.formatPrefixLetter(answerStatus.index)}
 	                </c:if>				
				</td>
				
				<td ${question.prefixAnswersWithLetters?'class="has-radio-button-prefix-answer"':''}">
					<c:out value="${option.name}" escapeXml="false" />
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
