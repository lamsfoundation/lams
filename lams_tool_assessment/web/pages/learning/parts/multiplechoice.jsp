<%@ include file="/common/taglibs.jsp"%>
<c:set var="mcqInstructions">
	<c:choose>
		<c:when test="${question.multipleAnswersAllowed}">
			<fmt:message key="label.learning.choose.at.least.one.answer" />
		</c:when>
		<c:otherwise>
			<fmt:message key="label.learning.choose.one.answer" />
		</c:otherwise>
	</c:choose>
</c:set>

<div class="card-subheader" id="instructions-${questionIndex}">
	${mcqInstructions}
</div>

<fieldset>
	<legend class="visually-hidden">
		${mcqInstructions}
	</legend>
		
	<div class="table table-sm div-hover px-3">
		<c:forEach var="option" items="${question.optionDtos}"  varStatus="answerStatus">
			<div class="row">
				<div class="col">
					<c:choose>
						<c:when test="${question.multipleAnswersAllowed}">
							<c:set var="inputName">question${questionIndex}_${option.uid}</c:set>
							<c:set var="inputId" value="${inputName}" />
							<input type="checkbox" name="${inputName}" id="${inputId}" class="me-2"
									value="${true}"
								    onclick="javascript:logLearnerInteractionEvent($(this).is(':checked') ? 2 : 3, ${question.uid}, ${option.uid})" 
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
								<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
								aria-labelledby="option-name-${option.uid}"
							/>
						</c:when>
						
						<c:otherwise>
							<c:set var="inputName">question${questionIndex}</c:set>
							<c:set var="inputId">${inputName}_${option.uid}</c:set>
							<input type="radio" name="${inputName}" id="${inputId}" class="me-2"
									value="${option.uid}"
								    onclick="javascript:logLearnerInteractionEvent(1, ${question.uid}, ${option.uid})" 
		 						<c:if test="${option.answerBoolean}">checked="checked"</c:if>
		 						<c:if test="${!hasEditRight}">disabled="disabled"</c:if>
		 						aria-labelledby="option-name-${option.uid}"
							/>
						</c:otherwise>
					</c:choose>
					
			 		<c:if test="${question.prefixAnswersWithLetters}">
			 			&nbsp;${option.formatPrefixLetter(answerStatus.index)}
	 	              </c:if>
					
					<label for="${inputId}" id="option-name-${option.uid}">
						<c:out value="${option.name}" escapeXml="false" />
					</label>
				</div>
			</div>
		</c:forEach>
	</div>
</fieldset>
