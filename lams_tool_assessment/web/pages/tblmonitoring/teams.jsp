<%@ include file="/common/taglibs.jsp"%>

<c:forEach var="questionResult" items="${assessmentResult.questionResults}" varStatus="i">
	<c:set var="question" value="${questionResult.questionDto}"/>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<span class="float-left space-right">Q${i.index+1})</span> 
				<c:out value="${question.title}" escapeXml="false"/>
			</h4> 
		</div>
	              
		<div class="panel-body">
			<div class="table-responsive">
				<table class="table table-striped">
					<tbody>
						<tr>
							<td>
								<c:choose>
									<c:when test="${question.type == 1}">
										<c:forEach var="option" items="${question.optionDtos}">
											<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
												<c:if test="${optionAnswer.answerBoolean && (optionAnswer.optionUid == option.uid)}">
													<div class="clearfix">
														${option.name}
													
														<c:choose>
															<c:when test="${option.maxMark == 1}">
																<i class="fa fa-check-square"></i>
															</c:when>
															<c:when test="${option.maxMark <= 0}">
																<i class="fa fa-minus-square"></i>
															</c:when>
														</c:choose>
													</div>
												</c:if>
											</c:forEach>					
										</c:forEach>						
									</c:when>
														
									<c:when test="${question.type == 2}">
										<c:forEach var="option" items="${question.optionDtos}">
											<div>
												<div style="float: left;">
													${option.matchingPair}
												</div>
												<div style=" float: right; width: 50%;">
																 - 
													<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
														<c:if test="${option.uid == optionAnswer.optionUid}">
															<c:forEach var="optionIter" items="${question.optionDtos}">
																<c:if test="${optionIter.uid == optionAnswer.answerInt}">
																	${optionIter.name}
																	
																	<c:choose>
																		<c:when test="${optionAnswer.optionUid == optionAnswer.answerInt}">
																			<i class="fa fa-check-square"></i>
																		</c:when>
																		<c:otherwise>
																			<i class="fa fa-minus-square"></i>
																		</c:otherwise>
																	</c:choose>
																</c:if>
															</c:forEach>
														</c:if>
													</c:forEach>										
												</div>
											</div>
											<br>
										</c:forEach>
									</c:when>
														
									<c:when test="${question.type == 3 || question.type == 4}">
										${fn:escapeXml(questionResult.answer)}

										<c:choose>
											<c:when test="${not empty questionResult.qbOption && questionResult.qbOption.maxMark == 1}">
												<i class="fa fa-check-square"></i>
											</c:when>
											<c:when test="${empty questionResult.qbOption || not empty questionResult.qbOption && questionResult.qbOption.maxMark <= 0}">
												<i class="fa fa-minus-square"></i>
											</c:when>
										</c:choose>
									</c:when>
														
									<c:when test="${question.type == 5}">
										<c:if test="${questionResult.answer != null}">			
											${questionResult.answerBoolean}
											
											<c:choose>
												<c:when test="${questionResult.answerBoolean == question.correctAnswer}">
													<i class="fa fa-check-square"></i>
												</c:when>
												<c:otherwise>
													<i class="fa fa-minus-square"></i>
												</c:otherwise>
											</c:choose>
										</c:if>
									</c:when>
														
									<c:when test="${question.type == 6}">
										${questionResult.answer}
									</c:when>
														
									<c:when test="${question.type == 7}">
										<c:forEach var="j" begin="0" end="${fn:length(questionResult.optionAnswers) - 1}" step="1">
											<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
												<c:if test="${optionAnswer.answerInt == j}">		
													<c:forEach var="option" items="${question.optionDtos}">
														<c:if test="${optionAnswer.optionUid == option.uid}">
															${option.name}
														</c:if>
													</c:forEach>
												</c:if>								
											</c:forEach>
										</c:forEach>
									</c:when>
														
									<c:when test="${question.type == 8}">
										<c:forEach var="option" items="${question.optionDtos}">
											<div>';
												<div style="float: left;">
													${option.name}
												</div>
												<div style=" float: right; width: 20%;">
													- 
													<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
														<c:if test="${option.uid == optionAnswer.optionUid}">
															${optionAnswer.answerInt}
															
															<c:if test="${optionAnswer.answerInt > 0 && option.correct}">
																<i class="fa fa-check-square"></i>
															</c:if>
														</c:if>
													</c:forEach>										
												</div>
											</div>
											<br>
										</c:forEach>
															
										<c:if test="${question.hedgingJustificationEnabled}">
											${questionResult.answer}
										</c:if>
									</c:when>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:forEach>
