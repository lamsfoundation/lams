<%@ include file="/common/taglibs.jsp"%>					
					
<c:choose>
	<c:when test="${question.type == 1}">
		<c:forEach var="option" items="${question.optionDtos}">
			<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
				<c:if test="${optionAnswer.answerBoolean && (optionAnswer.optionUid == option.uid)}">
					responseStr += "${option.nameEscaped}";
				</c:if>
			</c:forEach>					
		</c:forEach>						
	</c:when>
						
	<c:when test="${question.type == 2}">
		<c:forEach var="option" items="${question.optionDtos}">
			responseStr +='<div>';
			responseStr +=	'<div style="float: left;">';
			responseStr +=		"${option.matchingPairEscaped}";
			responseStr +=	'</div>';
			responseStr +=	'<div style=" float: right; width: 50%;">';
			responseStr +=		' - '; 
			<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
				<c:if test="${option.uid == optionAnswer.optionUid}">
					<c:forEach var="questionOption2" items="${question.optionDtos}">
						<c:if test="${questionOption2.uid == optionAnswer.answerInt}">
							responseStr +="${questionOption2.nameEscaped}";
						</c:if>
					</c:forEach>
				</c:if>
			</c:forEach>										
			responseStr +=	'</div>';
			responseStr +='</div>';
			responseStr +='<br>';
		</c:forEach>
	</c:when>
						
	<c:when test="${question.type == 3}">
		responseStr +="${fn:escapeXml(questionResult.answerEscaped)}";
	</c:when>
						
	<c:when test="${question.type == 4}">
		responseStr +="${questionResult.answerEscaped}";
	</c:when>
						
	<c:when test="${question.type == 5}">
		<c:if test="${questionResult.answer != null}">			
			responseStr +="${questionResult.answerBoolean}";
		</c:if>
	</c:when>
						
	<c:when test="${question.type == 6}">
		responseStr +="${questionResult.answerEscaped}";
	</c:when>
						
	<c:when test="${question.type == 7}">
		<c:forEach var="j" begin="0" end="${fn:length(questionResult.optionAnswers) - 1}" step="1">
			<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
				<c:if test="${optionAnswer.answerInt == j}">		
					<c:forEach var="option" items="${question.optionDtos}">
						<c:if test="${optionAnswer.optionUid == option.uid}">
							responseStr +="${option.nameEscaped}";
						</c:if>
					</c:forEach>
				</c:if>								
			</c:forEach>
		</c:forEach>
	</c:when>
						
	<c:when test="${question.type == 8}">
		<c:forEach var="option" items="${question.optionDtos}">
			responseStr +='<div>';
			responseStr +=	'<div style="float: left;">';
			responseStr +=		"${option.nameEscaped}";
			responseStr +=	'</div>';
			responseStr +=	'<div style=" float: right; width: 20%;">';
			responseStr +=		' - '; 
			<c:forEach var="optionAnswer" items="${questionResult.optionAnswers}">
				<c:if test="${option.uid == optionAnswer.optionUid}">
					responseStr +="${optionAnswer.answerInt}";
				</c:if>
			</c:forEach>										
			responseStr +=	'</div>';
			responseStr +='</div>';
			responseStr +='<br>';
		</c:forEach>
	</c:when>						
	
</c:choose>

<c:if test="${not empty questionResult.justification}">
	responseStr += '<br><br><i><fmt:message key="label.answer.justification" /></i><br>';
	responseStr += "${questionResult.justificationEscaped}"
</c:if>
