<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<div class="card">
  <input type="hidden" id="selected-question-uid" value="${question.uid}">
  
  <div class="card-header d-flex justify-content-end align-items-center">
  	<c:choose>
		<c:when test="${fn:length(otherVersions) == 1}">
			<button class="btn" disabled="disabled">
			    <fmt:message key="label.version"/>&nbsp;${question.version}
			</button>
		</c:when>

		<c:otherwise>
			<div class="dropdown">
				<button class="btn btn-outline-dark dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    	Version ${question.version}&nbsp;
				</button>
				
				<ul class="dropdown-menu">
					<c:forEach items="${otherVersions}" var="otherVersion">
			    		<li class="dropdown-item <c:if test="${question.version == otherVersion.version}"> disabled</c:if>"
			    			onclick="javascript:loadQuestionDetailsArea(${otherVersion.uid});" href="#">
			    			Version ${otherVersion.version}
			    		</li>
			    	</c:forEach>
				</ul>
			</div>			
		</c:otherwise>
	</c:choose>
		
	<button class="btn btn-outline-dark ml-2" type="button"
			onClick='javascript:window.open("<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=${question.uid}", "_blank")' title='Show stats'>
		<i class='fa fa-bar-chart'></i>&nbsp;
		Stats
	</button>
	
    <!-- hide import button in case of VSA which is not compatible with TBL -->
	<c:choose>
		<c:when test="${(question.type != 3) || !isScratchie || isVsaAndCompatibleWithTbl}">
			<button id="import-button" class="btn btn-outline-dark ml-2 button-add-item" type="button"
				title="Import question from the question bank">
				<fmt:message key="button.import"/>
			</button>
		</c:when>
		<c:otherwise>
			<div class="alert alert-warning ml-2 mb-0" role="alert">
				Not compatible with TBL
			</div>
		</c:otherwise>
	</c:choose>
 </div>
 
 <div class="card-body">
	<div class="card-title">
		<c:out value="${question.name}" escapeXml="false"/>
	</div>
	
	<div class="card-text question-description">				
		<c:out value="${question.description}" escapeXml="false"/>
	</div>
 		
	<c:choose>
		<c:when test="${question.type == 1 || question.type == 3 || question.type == 4 || question.type == 8}">
			<table class="table table-hover table-sm">
				<c:forEach var="option" items="${question.qbOptions}" varStatus="i">
					<c:set var="isOptionCorrect" value="${option.correct || option.maxMark == 1}"/>
					<tr>
						<td width="5px" style="padding-right: 0;">
							<c:if test="${isOptionCorrect}">
								<i class="fa fa-check text-success"></i>
							</c:if>
						</td>

						<td width="10px">
							<span 
								<c:if test="${isOptionCorrect}">class="text-success"</c:if>>
								${i.index+1})
							</span>
						</td>
							
						<td>
							<c:if test="${isOptionCorrect}">
								<div class="text-success">
							</c:if>
							
							<c:choose>
								<c:when test="${question.type == 1 || question.type == 3 || question.type == 8}">
									<c:out value="${option.name}" escapeXml="false"/>
								</c:when>
								<c:otherwise>
									<c:out value="${option.numericalOption}" escapeXml="false"/>
								</c:otherwise>
							</c:choose>
							
							<c:if test="${isOptionCorrect}">
								</div>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		
		<c:when test="${question.type == 2}">
			<table class="table table-hover table-sm">
				<c:forEach var="option" items="${question.qbOptions}" varStatus="i">
					<tr>
						<td width="10px">
							<span 
								<c:if test="${option.correct}">class="text-success"</c:if>>
								${i.index+1})
							</span>
						</td>	
						<td style="width: 100px;">
							<c:out value="${option.matchingPair}" escapeXml="false" />
						</td>
						
						<td >
							<c:out value="${option.name}" escapeXml="false" />
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		
		<c:when test="${question.type == 5}">
			<div class="voffset10">
				Correct answer: ${question.correctAnswer}
			</div>
		</c:when>
		
		<c:when test="${question.type == 7}">
			<table class="table table-hover table-sm">
				<c:forEach var="option" items="${question.qbOptions}" varStatus="i">
					<tr>
						<td width="10px">
							<span>
								${i.index+1})
							</span>
						</td>
							
						<td>
							<c:out value="${option.name}" escapeXml="false"/>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
	</c:choose>
 </div>
</div>