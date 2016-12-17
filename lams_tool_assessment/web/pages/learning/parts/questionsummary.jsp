<%@ include file="/common/taglibs.jsp"%>

<c:if test="${finishedLock && (fn:length(question.questionResults) > 1) && assessment.allowHistoryResponses}">
	<div id="quesion-summary-title">
		<fmt:message key="label.learning.question.summary.history.responces" />
	</div>

	<table class="forum question-summary-table">
		<tr>
			<th>
				#
			</th>		
			<th style="width: 120px;" >
				<fmt:message key="label.learning.summary.completed.on" />
			</th>
			<th>
				<fmt:message key="label.learning.question.summary.response" />
			</th>
			<c:if test="${assessment.allowGradesAfterAttempt}">			
				<th style="width: 70px;" >
					<fmt:message key="label.learning.question.summary.grade" />
				</th>	
			</c:if>		
		</tr>
		<c:forEach var="item" items="${question.questionResults}" varStatus="status">
			<c:set var="questionResult" value="${item[0]}" />
			<c:set var="currentAssessmentResult" value="${item[1]}" />
			<tr>
				<td>
					${status.index + 1} 
				</td>
				<td>
					<div>
						<lams:Date value="${currentAssessmentResult.finishDate}" />  
					</div>
				</td>					
				<td>
					<c:choose>
						<c:when test="${question.type == 1}">
							<c:forEach var="option" items="${question.options}">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerBoolean && (optionAnswer.optionUid == option.uid)}">
										${option.optionString}
									</c:if>
								</c:forEach>					
							</c:forEach>						
						</c:when>
						<c:when test="${question.type == 2}">
							<table style="padding: 0px; margin: 0px; border: none; " cellspacing="0" cellpadding="0">
								<c:forEach var="option" items="${question.options}">
									<tr>
										<td style="width:40%; background: none; padding: 0px; margin: 0px; border: none;">
											${option.question}
										</td>
										<td style="background: none; padding: 0px; margin: 0px; border: none; vertical-align: middle;">
											- 
											<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
												<c:if test="${option.uid == optionAnswer.optionUid}">
													<c:forEach var="questionOption2" items="${question.options}">
														<c:if test="${questionOption2.uid == optionAnswer.answerInt}">
															${questionOption2.optionString}
														</c:if>
													</c:forEach>
												</c:if>
											</c:forEach>										
										</td>
									</tr>
								</c:forEach>
							</table>
						</c:when>
						<c:when test="${question.type == 3}">
							${questionResult.answerString}
						</c:when>
						<c:when test="${question.type == 4}">
							${questionResult.answerString}
						</c:when>
						<c:when test="${question.type == 5}">
							<c:if test="${questionResult.answerString != null}">			
								${questionResult.answerBoolean}
							</c:if>
						</c:when>
						<c:when test="${question.type == 6}">
							${questionResult.answerString}
						</c:when>
						<c:when test="${question.type == 7}">
							<c:forEach var="i" begin="0" end="${fn:length(questionResult.optionAnswers) - 1}" step="1">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerInt == i}">		
										<c:forEach var="option" items="${question.options}">
											<c:if test="${optionAnswer.optionUid == option.uid}">
												${option.optionString}
											</c:if>
										</c:forEach>
									</c:if>								
								</c:forEach>
							</c:forEach>
						</c:when>
					</c:choose>
				</td>
				<c:if test="${assessment.allowGradesAfterAttempt}">
					<td style="padding-left: 0px;" >
						<div style="text-align: center;">
							<fmt:formatNumber value="${questionResult.mark}" maxFractionDigits="3"/>
						</div>
					</td>
				</c:if>		
			</tr>
		</c:forEach>

	</table>
</c:if>							