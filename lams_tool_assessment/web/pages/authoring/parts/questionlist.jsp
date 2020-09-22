<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="availableQuestions" value="${sessionMap.availableQuestions}" />

<div class="panel panel-default voffset5">
	<div class="panel-heading panel-title">
		<fmt:message key="label.authoring.basic.question.list.title" />
	</div>

	<table class="table table-condensed" id="referencesTable">
		<tr>
			<th width="20%">
				<fmt:message key="label.authoring.basic.list.header.type" />
			</th>
			<th width="40%">
				<fmt:message key="label.authoring.basic.list.header.question" />
			</th>
			<th colspan="3">
				<fmt:message key="label.authoring.basic.list.header.mark" />
			</th>
		</tr>	

		<c:forEach var="questionReference" items="${sessionMap.questionReferences}" varStatus="status">
			<c:set var="question" value="${questionReference.question}" />
			<tr>
				<td>
					<c:choose>
						<c:when test="${questionReference.randomQuestion}">
							<fmt:message key="label.authoring.basic.type.random.question" />
						</c:when>
						<c:when test="${question.type == 1}">
							<fmt:message key="label.authoring.basic.type.multiple.choice" />
						</c:when>
						<c:when test="${question.type == 2}">
							<fmt:message key="label.authoring.basic.type.matching.pairs" />
						</c:when>
						<c:when test="${question.type == 3}">
							<fmt:message key="label.authoring.basic.type.short.answer" />
						</c:when>
						<c:when test="${question.type == 4}">
							<fmt:message key="label.authoring.basic.type.numerical" />
						</c:when>
						<c:when test="${question.type == 5}">
							<fmt:message key="label.authoring.basic.type.true.false" />
						</c:when>
						<c:when test="${question.type == 6}">
							<fmt:message key="label.authoring.basic.type.essay" />
						</c:when>
						<c:when test="${question.type == 7}">
							<fmt:message key="label.authoring.basic.type.ordering" />
						</c:when>
						<c:when test="${question.type == 8}">
							<fmt:message key="label.authoring.basic.type.mark.hedging" />
						</c:when>
					</c:choose>
				</td>

				<td>
					<c:choose>
						<c:when test="${questionReference.randomQuestion}">
							<fmt:message key="label.authoring.basic.random.question" />
						</c:when>
						<c:otherwise>
							<c:out value="${question.title}" escapeXml="true"/>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<input name="grade${questionReference.sequenceId}" value="${questionReference.defaultGrade}"
						id="grade${questionReference.sequenceId}" class="form-control input-sm" style="width: 50%;">
				</td>
				
				<td class="arrows">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
		 				<lams:Arrow state="up" titleKey="label.authoring.basic.up" onclick="javascript:upQuestionReference(${status.index})"/>
		 			</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" titleKey="label.authoring.basic.down" onclick="javascript:downQuestionReference(${status.index})"/>
		 			</c:if>
				</td>			

				<td width="30px">
					<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.delete" />"
						onclick="javascript:deleteQuestionReference(${status.index})"></i>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
	
<!-- Dropdown menu for choosing a question from question bank -->
<c:if test="${fn:length(sessionMap.questionReferences) < fn:length(sessionMap.questionList)}">
	<div class="form-inline form-group">
	
		<select id="questionSelect" class="form-control input-sm roffset5">
			<c:if test="${fn:length(availableQuestions) > 1}">
				<option value="-1" selected="selected">
					<fmt:message key="label.authoring.basic.select.random.question" />
				</option>
			</c:if>
				
			<c:forEach var="question" items="${availableQuestions}" varStatus="status">
				<option value="${question.sequenceId}">
					<c:out value="${question.title}" escapeXml="true"/>
				</option>
			</c:forEach>
		</select>
			
		<a onclick="addQuestionReference();return false;" href="#nogo" class="btn btn-sm btn-default button-add-item" id="newQuestionInitHref2">  
			<fmt:message key="label.authoring.basic.add.question.to.list" />
		</a>
	</div>
</c:if>

<div class="panel panel-default voffset20">
	<div class="panel-heading panel-title">
		<fmt:message key="label.authoring.basic.question.bank.title" />
		<div class="btn-group btn-group-xs pull-right" role="group">
			<a href="#nogo" onClick="javascript:importQTI()" class="btn btn-default">
				<fmt:message key="label.authoring.basic.import.qti" /> 
			</a>
			<a href="#nogo" onClick="javascript:exportQTI()" class="btn btn-default">
				<fmt:message key="label.authoring.basic.export.qti" />
			</a>
		</div>
		<div class="btn-group btn-group-xs pull-right roffset5" role="group">		
			<c:set var="importInitUrl" >
				<c:url value='/authoring/importInit.do'/>?sessionMapID=${sessionMapID}&KeepThis=true&TB_iframe=true
			</c:set>
			<a href="${importInitUrl}" class="btn btn-default btn-xs loffset5 thickbox" id="importButton">  
				<fmt:message key="label.authoring.basic.import.questions" />
			</a>
			<a onclick="javascript:exportQuestions();" class="btn btn-default btn-xs " id="exportButton">  
				<fmt:message key="label.authoring.basic.export.questions" />
			</a>
		</div>
	</div>

	<table class="table table-condensed" id="questionTable">
		<tr>
			<th width="20%">
				<fmt:message key="label.authoring.basic.list.header.type" />
			</th>
			<th colspan="2">
				<fmt:message key="label.authoring.basic.list.header.question" />
			</th>
		</tr>	
	
		<c:forEach var="question" items="${sessionMap.questionList}" varStatus="status">
			<tr>
				<td>
					<c:choose>
						<c:when test="${question.type == 1}">
							<fmt:message key="label.authoring.basic.type.multiple.choice" />
						</c:when>
						<c:when test="${question.type == 2}">
							<fmt:message key="label.authoring.basic.type.matching.pairs" />
						</c:when>
						<c:when test="${question.type == 3}">
							<fmt:message key="label.authoring.basic.type.short.answer" />
						</c:when>
						<c:when test="${question.type == 4}">
							<fmt:message key="label.authoring.basic.type.numerical" />
						</c:when>
						<c:when test="${question.type == 5}">
							<fmt:message key="label.authoring.basic.type.true.false" />
						</c:when>
						<c:when test="${question.type == 6}">
							<fmt:message key="label.authoring.basic.type.essay" />
						</c:when>
						<c:when test="${question.type == 7}">
							<fmt:message key="label.authoring.basic.type.ordering" />
						</c:when>
						<c:when test="${question.type == 8}">
							<fmt:message key="label.authoring.basic.type.mark.hedging" />
						</c:when>
					</c:choose>
				</td>
				<td>
					<c:out value="${question.title}" escapeXml="true" />
				</td>
				
				<td class="text-right">
					<c:set var="editQuestionUrl" >
						<c:url value='/authoring/editQuestion.do'/>?sessionMapID=${sessionMapID}&questionSequenceId=${question.sequenceId}&KeepThis=true&TB_iframe=true&modal=true
					</c:set>
					<a href="${editQuestionUrl}" class="thickbox roffset5" style="margin-left: 20px;"> 
						<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.edit" />"></i>
					</a>
						
					<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.delete" />"
						onclick="javascript:deleteQuestion(${question.sequenceId})"></i>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
