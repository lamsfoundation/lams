<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 

<!--question content goes here-->
<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

	<c:if test="${questionEntry.key == generalLearnerFlowDTO.currentQuestionIndex}">

		<div class="row no-gutter">
			<div class="col-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title">
							<c:if test="${generalLearnerFlowDTO.mapQuestionContentLearner.size() != 1}"><c:out value="${questionEntry.key}" />.&nbsp;</c:if> <c:out value="${questionEntry.value.name}" escapeXml="false" />
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
									<strong><fmt:message key="label.words.required" />&nbsp;</strong><span id="words-required-" class="badge"></span>
								</button>
								<div class="voffset5">&nbsp;</div>
							</c:if>
						</div>

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
		</div>
	</c:if>
</c:forEach>

<div class="">
	<!--question content ends here-->
	<c:choose>
		<c:when test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount != 1) }">
			<button id="btnGetPrevious" type="button" onclick="javascript:submitMethod('getPreviousQuestion');"
				class="btn btn-sm btn-default voffset10">
				<i class="fa fa-arrow-left"></i>
				<fmt:message key="button.getPreviousQuestion" />
			</button>

			<div align="right-buttons">
				<button id="btnDone" type="button" onclick="javascript:submitMethod('submitAnswersContent');"
					class="btn btn-primary voffset10 pull-right">
					<fmt:message key="button.done" />
				</button>
			</div>
		</c:when>

		<c:when test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount == 1) }">
			<div align="right-buttons">
				<button id="btnDone" type="button" onclick="javascript:submitMethod('submitAnswersContent');"
					class="btn btn-primary pull-right voffset10">
					<fmt:message key="button.done" />
				</button>
			</div>
		</c:when>

		<c:when test="${generalLearnerFlowDTO.currentQuestionIndex != generalLearnerFlowDTO.totalQuestionCount && 
				 				  generalLearnerFlowDTO.currentQuestionIndex > 1}">
			<button id="btnGetPrevious" type="button" onclick="javascript:submitMethod('getPreviousQuestion');"
				class="btn btn-sm btn-default voffset10">
				<i class="fa fa-arrow-left"></i>&nbsp;
				<fmt:message key="button.getPreviousQuestion" />
			</button>
			<button id="btnGetNext" type="button" onclick="javascript:submitMethod('getNextQuestion');"
				class="btn btn-sm btn-default voffset10 pull-right">
				<fmt:message key="button.getNextQuestion" />
				&nbsp; <i class="fa fa-arrow-right"></i>
			</button>
		</c:when>

		<c:otherwise>
			<button id="btnGetNext" type="button" onclick="javascript:submitMethod('getNextQuestion');"
				class="btn btn-sm btn-default voffset10 pull-right">
				<fmt:message key="button.getNextQuestion" />
				&nbsp; <i class="fa fa-arrow-right"></i>
			</button>
		</c:otherwise>
	</c:choose>

</div>
<!-- End pane body -->
