<%@ include file="/common/taglibs.jsp"%>					
					
					<c:choose>
						<c:when test="${question.type == 1}">
							<c:forEach var="questionOption" items="${question.questionOptions}">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerBoolean && (optionAnswer.questionOptionUid == questionOption.uid)}">
										responseStr += "${questionOption.optionStringEscaped}";
									</c:if>
								</c:forEach>					
							</c:forEach>						
						</c:when>
						<c:when test="${question.type == 2}">
							<c:forEach var="questionOption" items="${question.questionOptions}">
								responseStr +='<div>';
									responseStr +='<div style="float: left;">';
										responseStr +="${questionOption.questionEscaped}";
									responseStr +='</div>';
									responseStr +='<div style=" float: right; width: 50%;">';
										responseStr +=' - '; 
										<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
											<c:if test="${questionOption.uid == optionAnswer.questionOptionUid}">
												<c:forEach var="questionOption2" items="${question.questionOptions}">
													<c:if test="${questionOption2.uid == optionAnswer.answerInt}">
														responseStr +="${questionOption2.optionStringEscaped}";
													</c:if>
												</c:forEach>
											</c:if>
										</c:forEach>										
									responseStr +='</div>';
								responseStr +='</div>';
								responseStr +='<br>';
								
							</c:forEach>

						</c:when>
						<c:when test="${question.type == 3}">
							responseStr +="${fn:escapeXml(questionResult.answerStringEscaped)}";
						</c:when>
						<c:when test="${question.type == 4}">
							responseStr +="${questionResult.answerStringEscaped}";
						</c:when>
						<c:when test="${question.type == 5}">
							<c:if test="${questionResult.answerString != null}">			
								responseStr +="${questionResult.answerBoolean}";
							</c:if>
						</c:when>
						<c:when test="${question.type == 6}">
							responseStr +="${questionResult.answerStringEscaped}";
						</c:when>
						<c:when test="${question.type == 7}">
							<c:forEach var="j" begin="0" end="${fn:length(questionResult.optionAnswers) - 1}" step="1">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerInt == j}">		
										<c:forEach var="questionOption" items="${question.questionOptions}">
											<c:if test="${optionAnswer.questionOptionUid == questionOption.uid}">
												responseStr +="${questionOption.optionStringEscaped}";
											</c:if>
										</c:forEach>
									</c:if>								
								</c:forEach>
							</c:forEach>
						</c:when>						
	
					</c:choose>