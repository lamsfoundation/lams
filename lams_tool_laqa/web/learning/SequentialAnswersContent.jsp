<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 

<!--question content goes here-->
<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

	<c:if test="${questionEntry.key == generalLearnerFlowDTO.currentQuestionIndex}">
		
		<div class="card lcard mt-4" id="question<c:out value='${questionEntry.key}' />">
			<div class="card-header">
				<div class="card-title">
					<c:if test="${generalLearnerFlowDTO.mapQuestionContentLearner.size() != 1}"><c:out value="${questionEntry.key}" />.&nbsp;</c:if> <c:out value="${questionEntry.value.name}" escapeXml="false" />
				</div>
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
							<fmt:message key="label.words.required" />&nbsp;<span id="words-required-"></span>
						</span>				
					</c:if>
				</div>

				<div class="my-2" id="answerResponse">
					<label class="visually-hidden" for="answer">
						<fmt:message key="label.learning.yourAnswer" />
					</label>
					
					<div data-sequence-id="${questionEntry.key}" data-is-ckeditor="${generalLearnerFlowDTO.allowRichEditor}"
						data-min-words-limit="${questionEntry.value.minWordsLimit}"
						<c:if test="${questionEntry.value.minWordsLimit != 0}">class="min-words-limit-enabled"</c:if>>
						<c:set var="placeholder"><fmt:message key="label.learning.yourAnswer" />...</c:set>
						<c:choose>
							<c:when test="${generalLearnerFlowDTO.allowRichEditor}">
								<lams:CKEditor id="answer" value="${generalLearnerFlowDTO.currentAnswer}"
								 contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner">
								</lams:CKEditor>
							</c:when>

							<c:otherwise>
								<lams:textarea name="answer" id="answer" rows="5" placeholder="${placeholder}" class="form-control">
									<c:out value='${generalLearnerFlowDTO.currentAnswer}' escapeXml='false' />
								</lams:textarea>
							</c:otherwise>
						</c:choose>
					</div>

					<input type="hidden" name="currentQuestionIndex" value="${questionEntry.key}" />
				</div>
			</div>
		</div>
	</c:if>
</c:forEach>

<div class="activity-bottom-buttons">
	<!--question content ends here-->
	<c:choose>
		<c:when test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount != 1) }">
			<button id="btnDone" type="button" onclick="javascript:submitMethod('submitAnswersContent');"
					class="btn btn-primary">
				<fmt:message key="button.done" />
				&nbsp; <i class="fa fa-arrow-right"></i>
			</button>
			
			<button id="btnGetPrevious" type="button" onclick="javascript:submitMethod('getPreviousQuestion');"
					class="btn btn-secondary me-2">
				<i class="fa fa-arrow-left"></i>&nbsp; 
				<fmt:message key="button.getPreviousQuestion" />
			</button>
		</c:when>

		<c:when test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount == 1) }">
			<button id="btnDone" type="button" onclick="javascript:submitMethod('submitAnswersContent');"
					class="btn btn-primary na">
				<fmt:message key="button.done" />
			</button>
		</c:when>

		<c:when test="${generalLearnerFlowDTO.currentQuestionIndex != generalLearnerFlowDTO.totalQuestionCount && 
				 				  generalLearnerFlowDTO.currentQuestionIndex > 1}">
			<button id="btnGetPrevious" type="button" onclick="javascript:submitMethod('getPreviousQuestion');"
					class="btn btn-secondary me-2">
				<i class="fa fa-arrow-left"></i>&nbsp;
				<fmt:message key="button.getPreviousQuestion" />
			</button>
			
			<button id="btnGetNext" type="button" onclick="javascript:submitMethod('getNextQuestion');"
					class="btn btn-primary">
				<fmt:message key="button.getNextQuestion" />
				&nbsp; <i class="fa fa-arrow-right"></i>
			</button>
		</c:when>

		<c:otherwise>
			<button id="btnGetNext" type="button" onclick="javascript:submitMethod('getNextQuestion');"
					class="btn btn-primary">
				<fmt:message key="button.getNextQuestion" />
				&nbsp; <i class="fa fa-arrow-right"></i>
			</button>
		</c:otherwise>
	</c:choose>

</div>
<!-- End pane body -->
