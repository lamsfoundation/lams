<%@ include file="/common/taglibs.jsp"%>
		<form id="answers" name="answers" method="post" action="<c:url value='/learning/submitAll.do?sessionMapID=${sessionMapID}'/>">
			<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
						
				<input type="hidden" name="questionUid${status.index}" id="questionUid${status.index}" value="${question.uid}" />						
							
				<div class="panel panel-default">
					<div class="panel-heading">
						<c:if test="${assessment.numbered}">
							<span class="question-numbers">
								${status.index + sessionMap.questionNumberingOffset}.
							</span>
						</c:if>
									
						<c:if test="${question.answerRequired}">
							<span class="asterisk">
								<i class="fa fa-xs fa-asterisk text-danger pull-right" title="<fmt:message key="label.answer.required"/>" alt="<fmt:message key="label.answer.required"/>"></i>
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
								<%@ include file="shortanswer.jsp"%>
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
								
								<div id="mark-hedging-question-${question.uid}">
									<%@ include file="markhedging.jsp"%>
								</div>
							</c:when>						
						</c:choose>
								
						<%@ include file="questionsummary.jsp"%>
					</div>
					
				</div>
			</c:forEach>
		</form>
		
<!--Paging-->
<c:if test="${fn:length(sessionMap.pagedQuestions) > 1}">
	<div id="pager" class="voffset10">
		<fmt:message key="label.learning.page" />
		<c:forEach var="questions" items="${sessionMap.pagedQuestions}" varStatus="status">
			<c:choose>
				<c:when	test="${(status.index+1) == pageNumber}">
					<a href="#nogo" onclick="return nextPage(${status.index + 1})" class="color-red">
				</c:when>
				<c:otherwise>
					<a href="#nogo" onclick="return nextPage(${status.index + 1})">
				</c:otherwise>
			</c:choose>				
				${status.index + 1} 
			</a>
		</c:forEach>		
	</div>
</c:if>
