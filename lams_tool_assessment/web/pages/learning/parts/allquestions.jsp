<%@ include file="/common/taglibs.jsp"%>
		<form id="answers" name="answers" method="post" action="<c:url value='/learning/submitAll.do?sessionMapID=${sessionMapID}'/>">
			<table cellspacing="0" class="alternative-color">
				<c:forEach var="question" items="${sessionMap.pagedQuestions[pageNumber-1]}" varStatus="status">
					<tr>
						<c:if test="${assessment.numbered}">
							<td style="padding: 15px 15px 15px; width: 10px; font-weight: bold;" >
								${status.index + sessionMap.questionNumberingOffset} 
							</td>
						</c:if>
						
						<td style="padding-left: 0px;">
							<input type="hidden" name="questionUid${status.index}" id="questionUid${status.index}" value="${question.uid}" />						
							
							<div class="field-name" style="padding: 10px 15px 15px;">
								${question.question}
							</div>
							
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
							</c:choose>
							
							<%@ include file="questionsummary.jsp"%>
						</td>
					</tr>
				</c:forEach>
			</table>
		</form>
		
		
		<!--Paging-->
		<c:if test="${fn:length(sessionMap.pagedQuestions) > 1}">
			<div style="text-align: center; padding-top: 60px;">
				<fmt:message key="label.learning.page" />
				<c:forEach var="questions" items="${sessionMap.pagedQuestions}" varStatus="status">
					<c:choose>
						<c:when	test="${(status.index+1) == pageNumber}">
							<a href="#nogo" onclick="return nextPage(${status.index + 1})" style="margin-left: 10px; font-size: 130%; color: red;">
						</c:when>
						<c:otherwise>
							<a href="#nogo" onclick="return nextPage(${status.index + 1})" style="margin-left: 10px; font-size: 130%;">
						</c:otherwise>
					</c:choose>				
						${status.index + 1} 
					</a>
				</c:forEach>		
			</div>
		</c:if>
