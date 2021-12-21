<% pageContext.setAttribute("newLineChar", "\r\n"); %>

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">
			<c:out value="${questionDto.title}" escapeXml="true"/>
		</div>
	</div>
		
	<div class="panel-body">
		<lams:errors/>
         
        <c:if test="${not empty questionDto.question}">
	        <div class="question-description">
	        	<c:out value="${questionDto.question}" escapeXml="false"/>
			</div>
		</c:if>
		
        <!--allocate responses-->
		<div class="row">
			<div class="col-sm-4 text-center">
				<c:set var="option0" value="${questionDto.optionDtos.toArray()[0]}"/>
				<h4>
					<c:choose>
						<c:when test="${questionSummary.tbl && option0.maxMark == 1}">
							<i class="fa fa-check fa-lg text-success"></i> <fmt:message key="label.correct" />
						</c:when>
						<c:when test="${questionSummary.tbl && option0.maxMark == 0}">
							<i class="fa fa-times fa-lg text-danger"></i>	<fmt:message key="label.incorrect" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.authoring.basic.option.grade"/>: ${option0.maxMark}
						</c:otherwise>
					</c:choose>
				</h4>
				
				<div class="list-group col sortable-on ${questionSummary.tbl ? 'tbl-correct-list' : ''}" 
					 data-question-uid="${questionDto.uid}"
					 data-option-uid="${option0.uid}" id="answer-group${option0.uid}"></div>
				
				<c:if test="${not empty option0.name}">
					<fmt:message key="label.answer.alternatives" />:
					<div class="answer-alternatives" id="answer-alternatives${option0.uid}"
						 data-question-uid="${questionDto.uid}"
						 data-option-uid="${option0.uid}">
						<c:forEach var="answer" items="${fn:split(option0.name, newLineChar)}">
							<button type="button" class="btn btn-xs ${questionSummary.tbl ? 'btn-success' : 'btn-primary'}"
									title='<fmt:message key="label.vsa.deallocate.button.tip" />'>${answer}</button>
						</c:forEach>
					</div>
				</c:if>
			</div>
			
			<div class="col-sm-4 text-center">
            	<h4>
            		<fmt:message key="label.answer.queue" />
            		<span id="answer-queue-size${questionDto.uid}"></span>
            	</h4>
           		
           		<div class="list-group col sortable-on answer-queue"
           			 data-question-uid="${questionDto.uid}" 
           			 data-option-uid="-1"
           			 id="answer-queue${questionDto.uid}">
            		<c:forEach var="questionResult" items="${questionSummary.notAllocatedQuestionResults}">
            			<div class="list-group-item">
            				<lams:Portrait userId="${questionResult.assessmentResult.user.userId}"/>
            				<span class="answer-text">${questionResult.answer}</span>
            			</div>
            		</c:forEach>
           		</div>		
			</div>
			
			<div class="col-sm-4 text-center">
				<c:set var="option1" value="${questionDto.optionDtos.toArray()[1]}"/>
				<h4>
					<c:choose>
						<c:when test="${questionSummary.tbl && option1.maxMark == 1}">
							<i class="fa fa-check fa-lg text-success"></i> <fmt:message key="label.correct" />
						</c:when>
						<c:when test="${questionSummary.tbl && option1.maxMark == 0}">
							<i class="fa fa-times fa-lg text-danger"></i>	<fmt:message key="label.incorrect" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.authoring.basic.option.grade"/>: ${option1.maxMark}
						</c:otherwise>
					</c:choose>
				</h4>
				
				<div class="list-group col sortable-on ${questionSummary.tbl ? 'tbl-incorrect-list' : ''}"
					 data-question-uid="${questionDto.uid}"
					 data-option-uid="${option1.uid}" id="answer-group${option1.uid}"></div>
					 	
				<c:if test="${not empty option1.name}">
					<fmt:message key="label.answer.alternatives" />:
					<div class="answer-alternatives" id="answer-alternatives${option0.uid}"
						 data-question-uid="${questionDto.uid}"
						 data-option-uid="${option1.uid}">
						<c:forEach var="answer" items="${fn:split(option1.name, newLineChar)}">
							<button type="button" class="btn btn-xs ${questionSummary.tbl ? 'btn-danger' : 'btn-primary'}"
									title='<fmt:message key="label.vsa.deallocate.button.tip" />'>${answer}</button>
						</c:forEach>
					</div>
				</c:if>
			</div>
		</div>
		
		<c:forEach var="optionDto" items="${questionDto.optionDtos}" begin="2" varStatus="status">
		
			<c:if test="${status.count % 3 == 1}">
				<div class="row">
			</c:if>
		
			<div class="col-sm-4 text-center">
				<h4>
					<fmt:message key="label.authoring.basic.option.grade"/>: ${optionDto.maxMark}
				</h4>
				
				<div class="list-group col sortable-on" data-question-uid="${questionDto.uid}"
					 data-option-uid="${optionDto.uid}" id="answer-group${optionDto.uid}"></div>
					 	
				<c:if test="${not empty optionDto.name}">
					<fmt:message key="label.answer.alternatives" />:
					<div class="answer-alternatives" id="answer-alternatives${optionDto.uid}"
						 data-question-uid="${questionDto.uid}"
						 data-option-uid="${optionDto.uid}">
						<c:forEach var="answer" items="${fn:split(optionDto.name, newLineChar)}">
							<button type="button" class="btn btn-xs btn-primary"
									title='<fmt:message key="label.vsa.deallocate.button.tip" />'>${answer}</button>
						</c:forEach>
					</div>
				</c:if>
			</div>
			
			<c:if test="${status.count % 3 == 0 || status.last}">
				</div>
			</c:if>
		</c:forEach>
	</div>
</div>