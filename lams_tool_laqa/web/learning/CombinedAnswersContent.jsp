<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">
	<c:set var="questionIndex" value="${questionEntry.key}"/>
	<c:forEach var="answerEntry" items="${generalLearnerFlowDTO.mapAnswers}">

		<c:if test="${questionIndex == answerEntry.key}">

			<div class="card lcard mt-4" id="question${questionIndex}">
				<div class="card-header">
					<c:if test="${generalLearnerFlowDTO.mapQuestionContentLearner.size() !=1}">${questionIndex}. </c:if>  <c:out value="${questionEntry.value.name}" escapeXml="false" />
				</div>

				<div class="card-body">
					<c:if test="${not empty questionEntry.value.description}">
						<div>
							<c:out value="${questionEntry.value.description}" escapeXml="false" />
						</div>
					</c:if>

					<div class="answer-req text-end">
						<c:if test="${questionEntry.value.required}">
							<span class="badge text-bg-danger"><fmt:message key="label.required" /></span>
						</c:if>

						<c:if test="${questionEntry.value.minWordsLimit != 0}">
						<span class="badge text-bg-primary">
							<fmt:message key="label.words.required" />&nbsp;<span id="words-required-${questionIndex}"></span>
						</span>
						</c:if>
					</div>

					<div class="my-2">
						<label class="visually-hidden" id="your-answer-label${questionIndex}" for="answer${questionIndex}">
							<fmt:message key="label.learning.yourAnswer" />
						</label>
						
						<c:choose>
							<c:when test="${hasEditRight}">
								<div data-sequence-id="${questionIndex}"
									 data-is-ckeditor="${generalLearnerFlowDTO.allowRichEditor}"
									 data-min-words-limit="${questionEntry.value.minWordsLimit}"
									 <c:if test="${questionEntry.value.minWordsLimit != 0}">class="min-words-limit-enabled"</c:if>>
									<c:choose>
										<c:when test="${generalLearnerFlowDTO.allowRichEditor}">
											<lams:CKEditor id="answer${questionIndex}"
														   value="${answerEntry.value}"
														   contentFolderID="${sessionMap.learnerContentFolder}"
														   toolbarSet="DefaultLearner"
														   ariaLabelledby="your-answer-label${questionIndex}">
											</lams:CKEditor>
										</c:when>

										<c:otherwise>
											<c:set var="placeholder"><fmt:message key="label.learning.yourAnswer" />...</c:set>
											<lams:textarea name="answer${questionIndex}"
														   id="answer${questionIndex}" rows="5" placeholder="${placeholder}"
														   class="form-control"><c:out value='${answerEntry.value}' escapeXml='false' /></lams:textarea>
										</c:otherwise>
									</c:choose>
								</div>
							</c:when>

							<c:otherwise>
								<lams:textarea name="answer${questionIndex}" rows="5"
											   class="form-control" disabled="disabled">
									<c:out value='${answerEntry.value}' escapeXml='false' />
								</lams:textarea>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>

		</c:if>

	</c:forEach>
</c:forEach>

<c:if test="${hasEditRight}">
	<div class="activity-bottom-buttons">
		<button name="btnCombined" type="button" class="btn btn-primary na"
				onclick="javascript:submitMethod('submitAnswersContent');">
			<fmt:message key="button.submitAllContent" />
		</button>
	</div>
</c:if>