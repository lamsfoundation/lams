<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<!--question content goes here-->
<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

	<c:if test="${questionEntry.key == generalLearnerFlowDTO.currentQuestionIndex}">

		<div class="row no-gutter">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title">
							<strong> <fmt:message key="label.question" /> <c:out value="${questionEntry.key}" />
							</strong>
						</div>
					</div>
					<div class="panel-body">
						<div class="panel">
							<c:out value="${questionEntry.value.question}" escapeXml="false" />
						</div>
						<div class="answer-req">
							<fmt:message key="label.learning.yourAnswer" />
							<c:if test="${questionEntry.value.required}">
								<small><mark>
										<fmt:message key="label.required" />
									</mark></small>
							</c:if>
							<c:if test="${questionEntry.value.minWordsLimit != 0}">
								<br />
								<small><em>- <fmt:message key="label.minimum.number.words">
											<fmt:param>&nbsp;${questionEntry.value.minWordsLimit}</fmt:param>
										</fmt:message>
								</em></small>
							</c:if>
						</div>

						<div data-sequence-id="${questionEntry.key}" data-is-ckeditor="${generalLearnerFlowDTO.allowRichEditor}"
							data-min-words-limit="${questionEntry.value.minWordsLimit}"
							<c:if test="${questionEntry.value.minWordsLimit != 0}">class="min-words-limit-enabled"</c:if>>
							<c:choose>
								<c:when test="${generalLearnerFlowDTO.allowRichEditor}">
									<lams:CKEditor id="answer" value="${generalLearnerFlowDTO.currentAnswer}"
										contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner">
									</lams:CKEditor>
								</c:when>

								<c:otherwise>
									<lams:textarea name="answer" id="answer" rows="5" class="form-control">
										<c:out value='${generalLearnerFlowDTO.currentAnswer}' escapeXml='false' />
									</lams:textarea>
								</c:otherwise>
							</c:choose>
						</div>

						<html:hidden property="currentQuestionIndex" value="${questionEntry.key}" />
	</c:if>
</c:forEach>


<div class="">
	<!--question content ends here-->
	<c:choose>
		<c:when
			test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount != 1) }">

			<button id="btnGetPrevious" onclick="javascript:submitMethod('getPreviousQuestion');"
				class="btn btn-sm btn-default voffset10">
				<i class="fa fa-arrow-left"></i>
				<fmt:message key="button.getPreviousQuestion" />
			</button>

			<div align="right-buttons">
				<button id="btnDone" onclick="javascript:submitMethod('submitAnswersContent');"
					class="btn btn-primary voffset10 pull-right">
					<fmt:message key="button.done" />
				</button>
			</div>

		</c:when>

		<c:when
			test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount == 1) }">
			<div align="right-buttons">
				<button id="btnDone" onclick="javascript:submitMethod('submitAnswersContent');"
					class="btn btn-primary pull-right voffset10">
					<fmt:message key="button.done" />
				</button>
			</div>

		</c:when>

		<c:when
			test="${generalLearnerFlowDTO.currentQuestionIndex != generalLearnerFlowDTO.totalQuestionCount && 
				 				  generalLearnerFlowDTO.currentQuestionIndex > 1}">

			<button id="btnGetPrevious" onclick="javascript:submitMethod('getPreviousQuestion');"
				class="btn btn-sm btn-default voffset10">
				<i class="fa fa-arrow-left"></i>&nbsp;
				<fmt:message key="button.getPreviousQuestion" />
			</button>
			<button id="btnGetNext" onclick="javascript:submitMethod('getNextQuestion');"
				class="btn btn-sm btn-default voffset10 pull-right">
				<fmt:message key="button.getNextQuestion" />
				&nbsp; <i class="fa fa-arrow-right"></i>
			</button>

		</c:when>

		<c:otherwise>
			<button id="btnGetNext" onclick="javascript:submitMethod('getNextQuestion');"
				class="btn btn-sm btn-default voffset10 pull-right">
				<fmt:message key="button.getNextQuestion" />
				&nbsp; <i class="fa fa-arrow-right"></i>
			</button>
		</c:otherwise>
	</c:choose>

</div>
<!-- End pane body -->


<script type="text/javascript">
	window.onload = function() {
		document.getElementById("answer").focus();
	}
</script>
