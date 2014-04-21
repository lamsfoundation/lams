<%@ include file="/common/taglibs.jsp"%>

					<c:choose>
						<c:when test="${question.type == 1}">
							<c:forEach var="option" items="${question.options}">
								<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
									<c:if test="${optionAnswer.answerBoolean && (optionAnswer.optionUid == option.uid)}">
										responseStr += "${option.optionStringEscaped}";
									</c:if>
								</c:forEach>					
							</c:forEach>						
						</c:when>
						<c:when test="${question.type == 2}">
							responseStr +='<table style="padding: 0px; margin: 0px; border: none; " cellspacing="0" cellpadding="0">';
								<c:forEach var="option" items="${question.options}">
									responseStr +='<tr>';
										responseStr +='<td style="width:40%; background: none; padding: 0px; margin: 0px; border: none;">';
											responseStr +="${option.questionEscaped}";
										responseStr +='</td>';
										responseStr +='<td style="background: none; padding: 0px; margin: 0px; border: none; vertical-align: middle;">';
											responseStr +='-'; 
											<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
												<c:if test="${option.uid == optionAnswer.optionUid}">
													<c:forEach var="option2" items="${question.options}">
														<c:if test="${option2.uid == optionAnswer.answerInt}">
															responseStr +="${option2.optionStringEscaped}";
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
							responseStr +="${questionResult.answerStringEscaped}";
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
										<c:forEach var="option" items="${question.options}">
											<c:if test="${optionAnswer.optionUid == option.uid}">
												responseStr +="${option.optionStringEscaped}";
											</c:if>
										</c:forEach>
									</c:if>								
								</c:forEach>
							</c:forEach>
						</c:when>						
	
					</c:choose>