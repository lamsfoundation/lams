<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">
	<c:forEach var="answerEntry" items="${generalLearnerFlowDTO.mapAnswers}">

		<c:if test="${questionEntry.key == answerEntry.key}">
			<div class="row no-gutter">
				<div class="col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div class="panel-title">
								<c:if test="${generalLearnerFlowDTO.mapQuestionContentLearner.size() !=1}"><c:out value="${questionEntry.key}" />.</c:if>  <c:out value="${questionEntry.value.name}" escapeXml="false" />
							</div>
						</div>
						
						<div class="panel-body">
							<c:if test="${not empty questionEntry.value.description}">
								<div class="panel">
									<c:out value="${questionEntry.value.description}" escapeXml="false" />
								</div>
							</c:if>
						
							<div class="answer-req">
								<c:if test="${questionEntry.value.required}">
									<span class="label label-danger pull-right"><fmt:message key="label.required" /></span>
								</c:if> 
								
								<c:if test="${questionEntry.value.minWordsLimit != 0}">
									<button class="btn btn-xs btn-primary" type="button">
									<strong><fmt:message key="label.words.required" />&nbsp;</strong><span id="words-required-${questionEntry.key}" class="badge"></span>
									</button>
									<div class="voffset5">&nbsp;</div>
								</c:if>
							</div>

							<c:choose>
								<c:when test="${hasEditRight}">
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
													id="answer${questionEntry.key}" rows="5" placeholder="${placeholder}"
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
			</div>
		</c:if>

	</c:forEach>
</c:forEach>

<c:if test="${hasEditRight}">
	<div class="right-buttons">
		<button name="btnCombined" type="button" class="btn btn-default pull-right voffset5"
			onclick="javascript:submitMethod('submitAnswersContent');">
			<fmt:message key="button.submitAllContent" />
		</button>
	</div>
</c:if>
