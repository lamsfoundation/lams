<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">
	<c:forEach var="answerEntry" items="${generalLearnerFlowDTO.mapAnswers}">

		<c:if test="${questionEntry.key == answerEntry.key}">

			<div class="card lcard lcard-no-borders shadow my-4" id="question<c:out value='${questionEntry.key}' />" aria-label="question">
				<div class="card-header lcard-header lcard-header-button-border">
					<c:if test="${generalLearnerFlowDTO.mapQuestionContentLearner.size() !=1}"><c:out value="${questionEntry.key}" />. </c:if>  <c:out value="${questionEntry.value.name}" escapeXml="false" />
				</div>

				<div class="card-body">
					<c:if test="${not empty questionEntry.value.description}">
						<div class="" id="questionDescription">
							<c:out value="${questionEntry.value.description}" escapeXml="false" />
						</div>
					</c:if>
				
					<div class="answer-req text-end">
						<c:if test="${questionEntry.value.required}">
							<span class="badge text-bg-danger"><fmt:message key="label.required" /></span>
						</c:if> 

						<c:if test="${questionEntry.value.minWordsLimit != 0}">
						<span class="badge text-bg-primary">
							<fmt:message key="label.words.required" />&nbsp;<span id="words-required-${questionEntry.key}"></span>
						</span>
						</c:if>

					</div>

					<div class="my-2" id="answerResponse">
					<c:choose>
						<c:when test="${hasEditRight}">
							<label id="yourAnswer" class="d-none"><fmt:message key="label.learning.yourAnswer" /></label>
							<c:set var="placeholder"><fmt:message key="label.learning.yourAnswer" />...</c:set>
							<div data-sequence-id="${questionEntry.key}"
								data-is-ckeditor="${generalLearnerFlowDTO.allowRichEditor}"
								data-min-words-limit="${questionEntry.value.minWordsLimit}"
								<c:if test="${questionEntry.value.minWordsLimit != 0}">class="min-words-limit-enabled"</c:if>>
								<c:choose>
									<c:when test="${generalLearnerFlowDTO.allowRichEditor}">
										<lams:CKEditor id="answer${questionEntry.key}"
											value="${answerEntry.value}" 
											contentFolderID="${sessionMap.learnerContentFolder}"
											toolbarSet="DefaultLearner">
										</lams:CKEditor>
									</c:when>

									<c:otherwise>
										<lams:textarea name="answer${questionEntry.key}"
											id="answer${questionEntry.key}" rows="5" placeholder="${placeholder}" aria-labelledby="yourAnswer"
											class="form-control"><c:out value='${answerEntry.value}' escapeXml='false' /></lams:textarea>
									</c:otherwise>
								</c:choose>
							</div>
						</c:when>

						<c:otherwise>
							<lams:textarea name="answer${questionEntry.key}" rows="5"
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
		<button name="btnCombined" type="button" class="btn btn-primary mt-2"
			onclick="javascript:submitMethod('submitAnswersContent');">
			<fmt:message key="button.submitAllContent" />
		</button>
	</div>
</c:if>
