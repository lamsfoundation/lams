<%@ include file="/common/taglibs.jsp"%>

<!--options content goes here-->

<form:hidden path="nextQuestionSelected" />
<form:hidden path="continueOptionsCombined" />
<form:hidden path="questionIndex" value="${mcGeneralLearnerFlowDTO.questionIndex}" />

<c:forEach var="answerDto" varStatus="status" items="${requestScope.learnerAnswerDtos}">
	<c:set var="questionUid" value="${answerDto.questionUid}"/>
	
	<c:if test="${answerDto.displayOrder == mcGeneralLearnerFlowDTO.questionIndex}">
		<div class="card lcard">
			<div class="card-header">
				<div class="row align-items-center">
					<div style="width:50px;">
						${answerDto.displayOrder})
					</div>
					
					<div class="col">
						<c:if test="${not sessionMap.hideTitles}">
							<c:out value="${answerDto.questionName}" escapeXml="false" />
		                </c:if>
					</div>
				</div>
				
				<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
					<div class="badge text-bg-light float-end">
						<fmt:message key="label.mark" /> 
						<c:out value="${answerDto.mark}" /> 
					</div>
				</c:if>
			</div>

			<div class="card-body">					
				<div class="mb-3">
		           	<c:out value="${answerDto.questionDescription}" escapeXml="false" />
				</div>
						
				<div class="div-hover mx-2">
					<fieldset>
						<legend class="visually-hidden">
							<c:out value="${answerDto.questionDescription}" escapeXml="false" />
						</legend>
					
						<c:forEach var="option" varStatus="status" items="${answerDto.options}">
							<div class="row text-nowrap">
								<div class="form-check">
									<input type="radio"	id="${questionUid}-${option.qbOption.uid}" name="checkedCa" class="form-check-input"
											value="${questionUid}-${option.qbOption.uid}" 
											<c:if test="${option.selected}">checked="checked"</c:if> 
									/> 
		
									<label for="${questionUid}-${option.qbOption.uid}" class="form-check-label">
										<c:if test="${isPrefixAnswersWithLetters}">
											<c:set var="seqLetter" value="${status.index}" />
											<%=Character.toChars(97 + (Integer) pageContext.getAttribute("seqLetter"))%>)
			           					</c:if>
		                              					 
										<c:out value="${option.qbOption.name}" escapeXml="false" />
									</label>
								</div>
							</div>
						</c:forEach>
					</fieldset>
				</div>
									
				<c:if test="${sessionMap.content.enableConfidenceLevels}">
					<div class="bootstrap-slider mt-3">
						<div class="card-subheader">
							<label for="confidenceLevel${questionUid}">
								<fmt:message key="label.confidence" />
							</label>
						</div>
						
						<div class="col-12 col-sm-8 col-md-6 col-lg-4">
							<input type="range" name="confidenceLevel${questionUid}" id="confidenceLevel${questionUid}" class="form-range"
								list="slider-step-list-${questionUid}" 
								min="0" max="10" step="5" 
								<c:if test="${answerDto.confidenceLevel != -1}">value="${answerDto.confidenceLevel}"</c:if>
								<c:if test="${!hasEditRight}">disabled</c:if>
							>
							<datalist id="slider-step-list-${questionUid}">
								<option value="0" label="0%"/>
								<option value="5" label="50%"/>
								<option value="10" label="100%"/>
							</datalist>
						</div>
					</div>
            	</c:if>
			</div>
		</div>
	</c:if>
</c:forEach>

<div class="activity-bottom-buttons">
	<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached != 'true'  &&  mcGeneralLearnerFlowDTO.totalQuestionCount != '1'}">
		<button name="continueOptionsCombined" type="button" onclick="submitNextQuestionSelected();"
				class="btn btn-primary na">
			<fmt:message key="button.continue" />
		</button>
	</c:if>
	
	<c:if test="${mcGeneralLearnerFlowDTO.totalQuestionCount == '1'}">
		<button name="continueOptionsCombined" type="button" onclick="doSubmit();" class="btn btn-primary na">
			<fmt:message key="button.continue" />
		</button>
	</c:if>
	
	<c:if test="${mcGeneralLearnerFlowDTO.totalCountReached == 'true'}">
		<button name="continueOptionsCombined" type="button" onclick="submitAllAnswers();"
				class="btn btn-primary na">
			<fmt:message key="button.submit" />
		</button>
	</c:if>
</div>

<!--options content ends here-->
