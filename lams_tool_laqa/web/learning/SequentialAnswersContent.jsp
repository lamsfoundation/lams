<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 

<!--question content goes here-->
<c:forEach var="questionEntry" items="${generalLearnerFlowDTO.mapQuestionContentLearner}">

	<c:if test="${questionEntry.key == generalLearnerFlowDTO.currentQuestionIndex}">

		<div class="row no-gutter">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="panel-title">
							<strong>
								<c:out value="${questionEntry.key}" />. <c:out value="${questionEntry.value.name}" escapeXml="false" />
							</strong>
						</div>
					</div>
					<div class="panel-body">
						<div class="panel">
							<c:out value="${questionEntry.value.description}" escapeXml="false" />
						</div>
						<div class="answer-req">
							<fmt:message key="label.learning.yourAnswer" />
							<c:if test="${questionEntry.value.required}">
								<small>
									<mark>
										<fmt:message key="label.required" />
									</mark>
								</small>
							</c:if>
							<c:if test="${questionEntry.value.minWordsLimit != 0}">
								<br />
								<fmt:message key="label.words.required" />: <span id="words-required-"></span>
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

						<input type="hidden" name="currentQuestionIndex" value="${questionEntry.key}" />
	</c:if>
</c:forEach>


<div class="">
	<!--question content ends here-->
	<c:choose>
		<c:when
			test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
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

		<c:when
			test="${(generalLearnerFlowDTO.currentQuestionIndex == generalLearnerFlowDTO.totalQuestionCount) && 
				  				  (generalLearnerFlowDTO.totalQuestionCount == 1) }">
			<div align="right-buttons">
				<button id="btnDone" type="button" onclick="javascript:submitMethod('submitAnswersContent');"
					class="btn btn-primary pull-right voffset10">
					<fmt:message key="button.done" />
				</button>
			</div>

		</c:when>

		<c:when
			test="${generalLearnerFlowDTO.currentQuestionIndex != generalLearnerFlowDTO.totalQuestionCount && 
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


<script type="text/javascript">
	window.onload = function() {
		document.getElementById("answer").focus();
	}
</script>
