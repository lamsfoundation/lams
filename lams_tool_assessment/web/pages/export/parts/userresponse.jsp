					<c:choose>
						<c:when test="${question.type == 1}">
							<c:forEach var="questionOption" items="${question.questionOptions}">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerBoolean && (optionAnswer.questionOptionUid == questionOption.uid)}">
										responseStr += "${questionOption.optionString}";
									</c:if>
								</c:forEach>					
							</c:forEach>						
						</c:when>
						<c:when test="${question.type == 2}">
							responseStr +='<table style="padding: 0px; margin: 0px; border: none; " cellspacing="0" cellpadding="0">';
								<c:forEach var="questionOption" items="${question.questionOptions}">
									responseStr +='<tr>';
										responseStr +='<td style="width:40%; background: none; padding: 0px; margin: 0px; border: none;">';
											responseStr +="${questionOption.question}";
										responseStr +='</td>';
										responseStr +='<td style="background: none; padding: 0px; margin: 0px; border: none; vertical-align: middle;">';
											responseStr +='-'; 
											<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
												<c:if test="${questionOption.uid == optionAnswer.questionOptionUid}">
													<c:forEach var="questionOption2" items="${question.questionOptions}">
														<c:if test="${questionOption2.uid == optionAnswer.answerInt}">
															responseStr +="${questionOption2.optionString}";
														</c:if>
													</c:forEach>
												</c:if>
											</c:forEach>										
										responseStr +='</td>';
									responseStr +='</tr>';
								</c:forEach>
							responseStr +='</table>';
						</c:when>
						<c:when test="${question.type == 3}">
							responseStr +="${questionResult.answerString}";
						</c:when>
						<c:when test="${question.type == 4}">
							responseStr +="${questionResult.answerString}";
						</c:when>
						<c:when test="${question.type == 5}">
							<c:if test="${questionResult.answerString != null}">			
								responseStr +="${questionResult.answerBoolean}";
							</c:if>
						</c:when>
						<c:when test="${question.type == 6}">
							responseStr +="${questionResult.answerString}";
						</c:when>
						<c:when test="${question.type == 7}">
							<c:forEach var="j" begin="0" end="${fn:length(questionResult.optionAnswers) - 1}" step="1">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerInt == j}">		
										<c:forEach var="questionOption" items="${question.questionOptions}">
											<c:if test="${optionAnswer.questionOptionUid == questionOption.uid}">
												responseStr +="${questionOption.optionString}";
											</c:if>
										</c:forEach>
									</c:if>								
								</c:forEach>
							</c:forEach>
						</c:when>						
	
					</c:choose>