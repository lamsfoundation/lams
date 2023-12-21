<%@ include file="/common/taglibs.jsp"%>

<c:if test="${isQuestionEtherpadEnabled and not empty allGroupUsers}">
	<%-- Prepare same content for each question Etherpad. Each group participant's first and last name --%>
	<c:set var="questionEtherpadContent">
		<c:forEach items="${allGroupUsers}" var="user">
			<c:out value="${user.firstName}" />&nbsp;<c:out
				value="${user.lastName}" />:<br />
			<br />
			<br />
		</c:forEach>
	</c:set>
</c:if>

<form id="answers" name="answers" method="post" action="<c:url value='/learning/submitAll.do?sessionMapID=${sessionMapID}'/>">
	<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
		<c:set var="questionIndex" value="${status.index}"/>
		<input type="hidden" name="questionUid${questionIndex}" id="questionUid${questionIndex}" value="${question.uid}" />						
							
		<div class="card lcard">
			<div class="card-header">
				<div class="card-title">
					<span id="question-title-${questionIndex}">
						<c:if test="${assessment.numbered}">
							${questionIndex + sessionMap.questionNumberingOffset}.
						</c:if>
		  				
		  				<c:if test="${not sessionMap.hideTitles}">
							${question.title}
						</c:if>
					</span>

					<c:if test="${assessment.displayMaxMark}">
						<span class="float-end badge alert alert-info fw-normal m-1 p-1">
							<fmt:message key="label.learning.max.mark">
								<fmt:param value="${question.maxMark}" />
							</fmt:message>
						</span>
					</c:if>
										
					<c:if test="${question.answerRequired}">
						<span class="asterisk float-end badge alert alert-warning m-1 p-1"> 
							<i class="fa fa-asterisk text-danger"
							title="<fmt:message key="label.answer.required"/>"
							alt="<fmt:message key="label.answer.required"/>"></i>
						</span>
					</c:if>
				</div>
			</div>
					
			<div class="card-body question-area" id="question-area-${questionIndex}">
				<div class="mb-4" id="question-description-${question.uid}">
					${question.question}
				</div>
				
				<c:choose>
					<c:when test="${question.type == 1}">
						<c:set var="justificationEligible" value="true" />
						<%@ include file="multiplechoice.jsp"%>
					</c:when>
					<c:when test="${question.type == 2}">
						<c:set var="justificationEligible" value="true" />
						<%@ include file="matchingpairs.jsp"%>
					</c:when>
					<c:when test="${question.type == 3}">
						<c:set var="justificationEligible" value="false" />
						<%@ include file="vsa.jsp"%>
					</c:when>
					<c:when test="${question.type == 4}">
						<c:set var="justificationEligible" value="true" />
						<%@ include file="numerical.jsp"%>
					</c:when>
					<c:when test="${question.type == 5}">
						<c:set var="justificationEligible" value="true" />
						<%@ include file="truefalse.jsp"%>
					</c:when>
					<c:when test="${question.type == 6}">
						<c:set var="justificationEligible" value="false" />
						<%@ include file="essay.jsp"%>
					</c:when>
					<c:when test="${question.type == 7}">
						<c:set var="justificationEligible" value="true" />
						<%@ include file="ordering.jsp"%>
					</c:when>
					<c:when test="${question.type == 8}">
						<c:set var="justificationEligible" value="true" />
						<c:choose>
							<c:when test="${question.responseSubmitted}">
								<%@ include file="../results/markhedging.jsp"%>
							</c:when>
							<c:otherwise>
								<div id="mark-hedging-question-${question.uid}">
									<%@ include file="markhedging.jsp"%>
								</div>
							</c:otherwise>
						</c:choose>
					</c:when>						
				</c:choose>
				
				<c:if test="${assessment.enableConfidenceLevels and question.type != 8}">
					<%@ include file="confidencelevel.jsp"%>
				</c:if>
				
				<%--Display jsutification for each question --%>
				<c:if test="${(assessment.allowAnswerJustification || (question.type == 8 && question.hedgingJustificationEnabled)) 
							&& justificationEligible && (!isLeadershipEnabled or isUserLeader)}">
					<div class="answer-justification-container mt-4 mb-2">
						<button type="button" data-bs-toggle="collapse"
							data-bs-target="#answer-justification-${questionIndex}"
							class="btn btn-light btn-sm collapsed card-subheader"
							id="justification-button-${questionIndex}"
						>
							<span class="if-collapsed"> 
								<i class="fa-regular fa-square-plus fa-xs me-2" aria-hidden="true"></i>
							</span> 
							<span class="if-not-collapsed"> 
								<i class="fa-regular fa-square-minus fa-xs me-2" aria-hidden="true"></i>
							</span>
							<fmt:message key="label.answer.justification" />
						</button>

						<div id="answer-justification-${questionIndex}" class="collapse">
							<textarea name="answerJustification${questionIndex}" class="form-control mt-2" rows="6" 
									  placeholder='<fmt:message key="label.answer.justification.prompt"/>'
									  aria-labelledby="question-title-${questionIndex} justification-button-${questionIndex}"
							>${question.justification}</textarea>
						</div>
					</div>
				</c:if>
			</div>		
		</div>
		
		<%--Display Etherpad for each question --%>
		<c:if test="${isQuestionEtherpadEnabled}">
			<div class="question-etherpad-container mb-3">
				<button type="button" data-bs-toggle="collapse" data-bs-target="#question-etherpad-${question.uid}" class="btn btn-light btn-sm collapsed">
					<span class="if-collapsed">
						<i class="fa-regular fa-square-plus fa-xs me-2" aria-hidden="true"></i>
					</span>
		 			<span class="if-not-collapsed">
		 				<i class="fa-regular fa-square-minus fa-xs me-2" aria-hidden="true"></i>
		 			</span>
					<fmt:message key="label.etherpad.discussion" />
				</button>
				
				<div id="question-etherpad-${question.uid}" class="question-etherpad-collapse collapse">
					<div class="panel panel-default question-etherpad">
						<lams:Etherpad groupId="etherpad-assessment-${toolSessionID}-question-${question.uid}" 
						   showControls="${mode eq 'teacher'}" showChat="false" heightAutoGrow="true" showOnDemand="true"
						>${questionEtherpadContent}</lams:Etherpad>
					</div>
				</div>
			</div>
		</c:if>
	</c:forEach>
</form>
