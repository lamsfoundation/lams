<%@ include file="/common/taglibs.jsp"%>

<c:if test="${isQuestionEtherpadEnabled and not empty allGroupUsers}">
	<%-- Prepare same content for each question Etherpad. Each group participant's first and last name --%>
	<c:set var="questionEtherpadContent">
		<c:forEach items="${allGroupUsers}" var="user"><c:out value="${user.firstName}" />&nbsp;<c:out value="${user.lastName}" />:<br />
	<br />
	<br /></c:forEach>
	</c:set>
</c:if>

<form id="answers" name="answers" method="post" action="<c:url value='/learning/submitAll.do?sessionMapID=${sessionMapID}'/>">
	<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
						
		<input type="hidden" name="questionUid${status.index}" id="questionUid${status.index}" value="${question.uid}" />						
							
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title" style="margin-bottom: 10px;font-size: initial;">
					<c:if test="${assessment.numbered}">
							${status.index + sessionMap.questionNumberingOffset}.
					</c:if>
	  				
	  				<c:if test="${not sessionMap.hideTitles}">
						${question.title}
					</c:if>
				</h3>
									
				<c:if test="${question.answerRequired}">
					<span class="asterisk pull-right">
						<i class="fa fa-xs fa-asterisk text-danger" title="<fmt:message key="label.answer.required"/>" 
								alt="<fmt:message key="label.answer.required"/>"></i>
					</span>
				</c:if>
							
				<c:if test="${empty question.question}">
					<!--  must have something here otherwise the question-numbers span does not float properly -->
					&nbsp;
				</c:if>
				${question.question}
			</div>
					
			<div class="panel-body" id="question-area-${status.index}">
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
						<c:set var="questionIndex" value="${status.index}"/>
						
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
			</div>
					
		</div>
		
		<%--Display jsutification for each question --%>
		<c:if test="${assessment.allowAnswerJustification && justificationEligible && (!isLeadershipEnabled or isUserLeader)}">
			<div class="form-group answer-justification-container">
				<a data-toggle="collapse" data-target="#answer-justification-${question.uid}" role="button" class="collapsed">
					<span class="if-collapsed"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i></span>
		 				<span class="if-not-collapsed"><i class="fa fa-xs fa-minus-square-o roffset5" aria-hidden="true"></i></span>
					<fmt:message key="label.answer.justification" />
				</a>
				
				<div id="answer-justification-${question.uid}" class="collapse">
					<textarea name="answerJustification${status.index}" class="form-control" rows="6" 
							  placeholder='<fmt:message key="label.answer.justification.prompt"/>'
					>${question.justification}</textarea>
				</div>
			</div>
		</c:if>
		
		<%--Display Etherpad for each question --%>
		<c:if test="${isQuestionEtherpadEnabled}">
			<div class="form-group question-etherpad-container">
				<a data-toggle="collapse" data-target="#question-etherpad-${question.uid}" role="button" class="collapsed">
					<span class="if-collapsed"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i></span>
		 				<span class="if-not-collapsed"><i class="fa fa-xs fa-minus-square-o roffset5" aria-hidden="true"></i></span>
					<fmt:message key="label.etherpad.discussion" />
				</a>
				
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
