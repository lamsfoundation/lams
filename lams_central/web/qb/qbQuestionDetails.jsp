<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<div class="panel panel-default" style="background-color: #f9f8f8aa;">
<div class="panel-body">
	<input type="hidden" id="selected-question-uid" value="${question.uid}">
	
	<!-- hide import button in case of VSA which is not compatible with TBL -->
	<c:choose>
		<c:when test="${(question.type != 3) || !isScratchie || isVsaAndCompatibleWithTbl}">
			<a id="import-button" class="btn btn-xs btn-secondary pull-right button-add-item" href="#nogo"
				title="Import question from the question bank">
				<fmt:message key="button.import"/>
			</a>
		</c:when>
		<c:otherwise>
			<span class="alert-warning pull-right loffset10">
				Not compatible with TBL
			</span>
		</c:otherwise>
	</c:choose>
	
	<a class="btn btn-xs btn-secondary pull-right loffset5" href="#nogo" onClick='javascript:window.open("<lams:LAMSURL/>qb/stats/show.do?qbQuestionUid=${question.uid}", "_blank")' title='Show stats'>
		<i class='fa fa-bar-chart'></i>&nbsp;
		Stats
	</a>
	
	<div class="pull-right">
		<c:choose>
			<c:when test="${fn:length(otherVersions) == 1}">
				<button class="btn btn-secondary btn-xs dropdown-toggle2" disabled="disabled">
				    <fmt:message key="label.version"/> &nbsp; ${question.version}
				</button>
			</c:when>

			<c:otherwise>
				<div class="dropdown">
					<button class="btn btn-secondary btn-xs dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    	Version ${question.version}&nbsp;<span class="caret"></span>
					</button>
					
					<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenu1">
						<c:forEach items="${otherVersions}" var="otherVersion">
				    		<li <c:if test="${question.version == otherVersion.version}">class="disabled"</c:if>>
				    			<a href="#nogo" onclick="javascript:loadQuestionDetailsArea(${otherVersion.uid});">Version ${otherVersion.version}</a>
				    		</li>
				    	</c:forEach>
					</ul>
				</div>			
			</c:otherwise>
		</c:choose>
	</div>

	<div class="">
		<c:out value="${question.name}" escapeXml="false"/>
	</div>
	
	<div class="question-description">				
		<c:out value="${question.description}" escapeXml="false"/>
	</div>
 		
	<c:choose>
		<c:when test="${question.type == 1 || question.type == 3 || question.type == 4 || question.type == 8}">
			<table class="table table-striped table-hover table-condensed">
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
			<table class="table table-hover table-condensed">
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
		
		<c:when test="${question.type == 6}">
		</c:when>
		
		<c:when test="${question.type == 7}">
			<table class="table table-striped table-hover table-condensed">
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
