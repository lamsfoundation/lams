<%@ include file="/common/taglibs.jsp"%>					
					
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
			<div>
				<div style="float: left;">
					${option.matchingPair}
				</div>
				<div style=" float: right; width: 50%;">
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
			<br>
		</c:forEach>
	</c:when>
						
	<c:when test="${question.type == 3}">
		${fn:escapeXml(questionResult.answerString)}
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
						</c:if>
					</c:forEach>										
				</div>
			</div>
			<br>
		</c:forEach>
							
	<c:if test="${question.hedgingJustificationEnabled}">
		${questionResult.answerString}
	</c:if>
	</c:when>						
	
</c:choose>
