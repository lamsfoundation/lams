<% pageContext.setAttribute("newLineChar", "\r\n"); %>
<%@ include file="/common/taglibs.jsp"%>


<c:set var="qbQuestion" value="${toolQuestion.qbQuestion}" />
<c:set var="isTbl" value="${qbQuestion.isVsaAndCompatibleWithTbl()}" />
<c:set var="optionsArray" value="${qbQuestion.qbOptions.toArray()}" />

<div class="panel panel-default">
	<div class="panel-heading">
		<div class="panel-title">
			<c:out value="${qbQuestion.name}" escapeXml="true"/>
		</div>
	</div>
		
	<div class="panel-body">
		<lams:errors/>
         
        <c:if test="${not empty qbQuestion.description}">
	        <div class="question-description">
	        	<c:out value="${qbQuestion.description}" escapeXml="false"/>
			</div>
		</c:if>
		
        <!--allocate responses-->
		<div class="row">
			<div class="col-sm-4 text-center">
				<c:set var="option0" value="${optionsArray[0]}"/>
				<h4>
					<c:choose>
						<c:when test="${isTbl && option0.maxMark == 1}">
							<i class="fa fa-check fa-lg text-success"></i> <fmt:message key="label.correct" />
						</c:when>
						<c:when test="${isTbl && option0.maxMark == 0}">
							<i class="fa fa-times fa-lg text-danger"></i> <fmt:message key="label.incorrect" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.authoring.basic.option.grade"/>: ${option0.maxMark}
						</c:otherwise>
					</c:choose>
				</h4>
				
				<div class="list-group col sortable-on ${isTbl ? 'tbl-correct-list' : ''}" 
					 data-question-uid="${toolQuestion.uid}"
					 data-option-uid="${option0.uid}" id="answer-group${option0.uid}"></div>
				
				<c:if test="${not empty option0.name}">
					<fmt:message key="label.answer.alternatives" />:
					<div class="answer-alternatives" id="answer-alternatives${option0.uid}"
						 data-question-uid="${toolQuestion.uid}"
						 data-option-uid="${option0.uid}"
						 data-option-correct="${option0.maxMark > 0}">
						<c:forEach var="answer" items="${fn:split(option0.name, newLineChar)}">
							<button type="button" class="btn btn-xs ${isTbl ? 'btn-success' : 'btn-primary'}"
									title='<fmt:message key="label.vsa.deallocate.button.tip" />'>${answer}</button>
						</c:forEach>
					</div>
				</c:if>
			</div>
			
			<div class="col-sm-4 text-center">
            	<h4>
            		<fmt:message key="label.answer.queue" />
            		<span id="answer-queue-size${toolQuestion.uid}"></span>
            	</h4>
           		
           		<div class="list-group col sortable-on answer-queue"
           			 data-question-uid="${toolQuestion.uid}" 
           			 data-option-uid="-1"
           			 id="answer-queue${toolQuestion.uid}">
            		<c:forEach var="answer" items="${notAllocatedAnswers}">
            			<div class="list-group-item">
            				<c:choose>
            					<c:when test="${empty answer.value}">
            						<div class="portrait-anonymous portrait-generic-sm"></div>
            					</c:when>
            					<c:otherwise>
            						<lams:Portrait userId="${answer.value}"/>
            					</c:otherwise>
            				</c:choose>
            				
            				<span class="answer-text">${answer.key}</span>
            			</div>
            		</c:forEach>
           		</div>		
			</div>
			
			<div class="col-sm-4 text-center">
				<c:set var="option1" value="${optionsArray[1]}"/>
				<h4>
					<c:choose>
						<c:when test="${isTbl && option1.maxMark == 1}">
							<i class="fa fa-check fa-lg text-success"></i> <fmt:message key="label.correct" />
						</c:when>
						<c:when test="${isTbl && option1.maxMark == 0}">
							<i class="fa fa-times fa-lg text-danger"></i>	<fmt:message key="label.incorrect" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.authoring.basic.option.grade"/>: ${option1.maxMark}
						</c:otherwise>
					</c:choose>
				</h4>
				
				<div class="list-group col sortable-on ${isTbl ? 'tbl-incorrect-list' : ''}"
					 data-question-uid="${toolQuestion.uid}"
					 data-option-uid="${option1.uid}" id="answer-group${option1.uid}"></div>
					 	
				<c:if test="${not empty option1.name}">
					<fmt:message key="label.answer.alternatives" />:
					<div class="answer-alternatives" id="answer-alternatives${option0.uid}"
						 data-question-uid="${toolQuestion.uid}"
						 data-option-uid="${option1.uid}"
						 data-option-correct="${option1.maxMark > 0}">
						<c:forEach var="answer" items="${fn:split(option1.name, newLineChar)}">
							<button type="button" class="btn btn-xs ${isTbl ? 'btn-danger' : 'btn-primary'}"
									title='<fmt:message key="label.vsa.deallocate.button.tip" />'>${answer}</button>
						</c:forEach>
					</div>
				</c:if>
			</div>
		</div>
		
		<c:forEach var="option" items="${optionsArray}" begin="2" varStatus="status">
		
			<c:if test="${status.count % 3 == 1}">
				<div class="row">
			</c:if>
		
			<div class="col-sm-4 text-center">
				<h4>
					<fmt:message key="label.authoring.basic.option.grade"/>: ${option.maxMark}
				</h4>
				
				<div class="list-group col sortable-on" data-question-uid="${toolQuestion.uid}"
					 data-option-uid="${option.uid}" id="answer-group${option.uid}"></div>
					 	
				<c:if test="${not empty option.name}">
					<fmt:message key="label.answer.alternatives" />:
					<div class="answer-alternatives" id="answer-alternatives${option.uid}"
						 data-question-uid="${toolQuestion.uid}"
						 data-option-uid="${option.uid}"
						 data-option-correct="${option.maxMark > 0}">
						<c:forEach var="answer" items="${fn:split(option.name, newLineChar)}">
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