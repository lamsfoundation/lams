<%@ include file="/taglibs.jsp"%>

<c:set var="ALPHABET" value="${fn:split('a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z', ',')}" scope="request"/>
<c:set var="ALPHABET_CAPITAL_LETTERS" value="${fn:split('A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z', ',')}" scope="request"/>

<div class="card mb-3" tabindex="0" id="">
	<h5 class="card-header" data-bs-toggle="collapse" data-bs-target="#collapse-question-results">
		<a class="text-decoration-none">
			<fmt:message key="label.monitoring.teams.question.results" />
		</a>
	</h5>

	<div id="collapse-question-results" class="card-body collapse show">
		<div class="table-responsive">
			<table class="table table-condensed">
				<thead>
					<tr>
						<th>
							<a class="text-decoration-none" onClick="javascript:loadTeamsQuestionResults('default')">
								<fmt:message key="label.monitoring.teams.question.results.question" />
							</a>
						</th>
						<c:if test="${not empty iraAnswerCountForOptions}">
							<th>
								<a class="text-decoration-none" onClick="javascript:loadTeamsQuestionResults('ira')">
									<fmt:message key="label.ira" />
								</a>
							</th>
						</c:if>
						<c:if test="${not empty traAnswerCountForOptions}">
							<th>
								<a class="text-decoration-none" onClick="javascript:loadTeamsQuestionResults('tra')">
									<fmt:message key="label.tra" />
								</a>
							</th>
						</c:if>
						<c:if test="${not empty averageAnswerCountForOptions}">
							<th>
								<a class="text-decoration-none" onClick="javascript:loadTeamsQuestionResults('average')">
									<fmt:message key="label.ira.tra.average" />
								</a>
							</th>
						</c:if>
					</tr>
				</thead>
				
				<c:forEach var="questionEntry" items="${questions}" varStatus="status">
					<c:set var="question" value="${questionEntry.value}" />
					<tbody>
						<tr class="question-results-question-row" data-bs-toggle="collapse" data-bs-target="#collapse-question-results-options-${question.uid}">
							<td>
								<a class="text-decoration-none">${questionEntry.key}.&nbsp<c:out value="${question.name}" /></a>
							</td>
							
							<c:if test="${not empty iraAnswerCountForOptions}">
								<c:set var="answersForOptionsDtos" value="${iraAnswerCountForOptions}" />
								<%@ include file="teamsQuestionResultsCell.jsp"%>
							</c:if>
							
							<c:if test="${not empty traAnswerCountForOptions}">
								<c:set var="answersForOptionsDtos" value="${traAnswerCountForOptions}" />
								<%@ include file="teamsQuestionResultsCell.jsp"%>
							</c:if>
							
							<c:if test="${not empty averageAnswerCountForOptions}">
								<c:set var="answersForOptionsDtos" value="${averageAnswerCountForOptions}" />
								<%@ include file="teamsQuestionResultsCell.jsp"%>
							</c:if>
						</tr>
					</tbody>
					
					<tbody id="collapse-question-results-options-${question.uid}" class="collapse">
						<c:forEach var="option" items="${question.qbOptions}" varStatus="optionStatus">
							<tr class="${option.correct ? 'fw-bold' : ''}">
								<td class="ps-4">
									${ALPHABET[optionStatus.index]}. <c:out value="${option.name}" escapeXml="false"/>
								</td>
								<c:if test="${not empty iraAnswerCountForOptions}">
									<c:set var="answersForOptionsDtos" value="${iraAnswerCountForOptions}" />
									<%@ include file="teamsOptionResultsCell.jsp"%>
								</c:if>
								
								<c:if test="${not empty traAnswerCountForOptions}">
									<c:set var="answersForOptionsDtos" value="${traAnswerCountForOptions}" />
									<%@ include file="teamsOptionResultsCell.jsp"%>
								</c:if>
								
								<c:if test="${not empty averageAnswerCountForOptions}">
									<c:set var="answersForOptionsDtos" value="${averageAnswerCountForOptions}" />
									<%@ include file="teamsOptionResultsCell.jsp"%>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</c:forEach>							
			</table>
		</div>
	</div>
</div>