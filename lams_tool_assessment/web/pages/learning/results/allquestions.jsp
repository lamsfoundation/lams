<%@ include file="/common/taglibs.jsp"%>
<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">						
							
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
					<%@ include file="multiplechoice.jsp"%>
				</c:when>
				<c:when test="${question.type == 2}">
					<%@ include file="matchingpairs.jsp"%>
				</c:when>
				<c:when test="${question.type == 3}">
					<%@ include file="vsa.jsp"%>
				</c:when>
				<c:when test="${question.type == 4}">
					<%@ include file="numerical.jsp"%>
				</c:when>
				<c:when test="${question.type == 5}">
					<%@ include file="truefalse.jsp"%>
				</c:when>
				<c:when test="${question.type == 6}">
					<%@ include file="essay.jsp"%>
				</c:when>
				<c:when test="${question.type == 7}">
					<%@ include file="ordering.jsp"%>
				</c:when>
				<c:when test="${question.type == 8}">
					<c:set var="questionIndex" value="${status.index}"/>
					<%@ include file="markhedging.jsp"%>
				</c:when>			
			</c:choose>
			
			<c:if test="${question.type != 8}">
				<%@ include file="markandpenaltyarea.jsp"%>
			</c:if>
								
			<%@ include file="historyresponses.jsp"%>
			
			<c:if test="${assessment.enableConfidenceLevels}">
				<%@ include file="confidencelevel.jsp"%>
			</c:if>
			
			<c:if test="${assessment.allowAnswerJustification and not empty question.justification}">
				<div class="question-type">
					<fmt:message key="label.answer.justification" />
				</div>
				<p>
					<c:out value="${question.justificationHtml}" escapeXml="false" />
				</p>
			</c:if>
		</div>
					
	</div>
</c:forEach>
