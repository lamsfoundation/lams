<%@ include file="/common/taglibs.jsp"%>

<c:if test="${assessment.allowHistoryResponses && (fn:length(question.questionResults) > 1)}">
	<div class="card-subheader mt-2">
		<fmt:message key="label.learning.question.summary.history.responces" />
	</div>

	<div class="table-sm ltable">
			<div class="row">
				<div style="width: 70px">
					#
				</div>		
				<div class="w-25">
					<fmt:message key="label.learning.summary.completed.on" />
				</div>
				<div class="col">
					<fmt:message key="label.learning.question.summary.response" />
				</div>
				<c:if test="${assessment.allowGradesAfterAttempt}">			
					<div style="width: 70px" >
						<fmt:message key="label.authoring.basic.list.header.mark" />
					</div>	
				</c:if>		
			</div>
		
		<c:forEach var="item" items="${question.questionResults}" varStatus="status">
			<c:set var="questionResult" value="${item[0]}" />
			<c:set var="currentAssessmentResult" value="${item[1]}" />
			<div class="row">
				<div style="width: 70px">
					${status.index + 1} 
				</div>
				<div class="w-25">
					<div>
						<lams:Date value="${currentAssessmentResult.finishDate}" timeago="true"/> 
					</div>
				</div>					
				<div class="col">
					<c:choose>
						<c:when test="${question.type == 1}">
							<c:forEach var="option" items="${question.optionDtos}">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerBoolean && (optionAnswer.optionUid == option.uid)}">
										${option.name}
									</c:if>
								</c:forEach>					
							</c:forEach>						
						</c:when>
						<c:when test="${question.type == 2}">
							<c:forEach var="option" items="${question.optionDtos}">
								<div class="row" style="border: none;">
									<div style="width: 40%;">
										${option.matchingPair}
									</div>
									<div class="col">
										-
										<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
											<c:if test="${option.uid == optionAnswer.optionUid}">
												<c:forEach var="questionOption2" items="${question.optionDtos}">
													<c:if test="${questionOption2.uid == optionAnswer.answerInt}">
														${questionOption2.name}
													</c:if>
												</c:forEach>
											</c:if>
										</c:forEach>
									</div>
								</div>
							</c:forEach>
						</c:when>
						<c:when test="${question.type == 3}">
							${questionResult.answer}
						</c:when>
						<c:when test="${question.type == 4}">
							${questionResult.answer}
						</c:when>
						<c:when test="${question.type == 5}">
							<c:if test="${questionResult.answer != null}">			
								${questionResult.answerBoolean}
							</c:if>
						</c:when>
						<c:when test="${question.type == 6}">
							${questionResult.answer}
						</c:when>
						<c:when test="${question.type == 7}">
							<c:forEach var="i" begin="0" end="${fn:length(questionResult.optionAnswers) - 1}" step="1">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerInt == i}">		
										<c:forEach var="option" items="${question.optionDtos}">
											<c:if test="${optionAnswer.optionUid == option.uid}">
												${option.name}
											</c:if>
										</c:forEach>
									</c:if>								
								</c:forEach>
							</c:forEach>
						</c:when>
					</c:choose>
				</div>
				<c:if test="${assessment.allowGradesAfterAttempt}">
					<div style="width: 70px; padding-left: 0px;" >
						<div class="text-center">
							<fmt:formatNumber value="${questionResult.mark}" maxFractionDigits="3"/>
						</div>
					</div>
				</c:if>		
			</div>
		</c:forEach>
	</table>
	</div>
</c:if>							