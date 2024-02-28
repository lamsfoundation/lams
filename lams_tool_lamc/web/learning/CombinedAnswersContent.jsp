<%@ include file="/common/taglibs.jsp"%>

<!--options content goes here-->
<c:forEach var="answerDto" items="${requestScope.learnerAnswerDtos}">
  	<c:set var="questionUid" value="${answerDto.questionUid}"/>

	<div class="card lcard">
		<div class="card-header">
			<div class="row align-items-center">
				<div style="width: 60px;">
					${answerDto.displayOrder}.
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
					
	            	<c:forEach var="option" items="${answerDto.options}" varStatus="status">
						<div class="row text-nowrap">
	                      	<div class="form-check">
		                      	<input type="radio" id="${questionUid}-${option.qbOption.uid}" name="checkedCa${questionUid}" class="form-check-input" 
		                         		value="${questionUid}-${option.qbOption.uid}" 
		                           		<c:if test="${option.selected}">checked="checked"</c:if>
		                            	<c:if test="${!hasEditRight}">disabled="disabled"</c:if> 
		                      	>
	
		                       	<label for="${questionUid}-${option.qbOption.uid}" class="form-check-label">
			                     	<c:if test="${isPrefixAnswersWithLetters}">
			                     		<c:set var="seqLetter" value="${status.index}"/>
			                     		<%=Character.toChars(97 + (Integer)pageContext.getAttribute("seqLetter"))%>)
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
</c:forEach>

<form:hidden path="continueOptionsCombined" value="Continue" />
<c:if test="${hasEditRight}">
	<div class="activity-bottom-buttons">
		<button name="continueButton" class="btn btn-primary na" onclick="doSubmit();" type="button" id="continueButton">
	    	<fmt:message key="button.continue" />
		</button>
	</div>
</c:if>

<!--options content ends here-->