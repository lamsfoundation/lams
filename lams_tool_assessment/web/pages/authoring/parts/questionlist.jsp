<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="availableQuestions" value="${sessionMap.availableQuestions}" />

<div>
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.basic.question.list.title" />
		<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="referencesArea_Busy" />
	</h2>

	<table class="alternative-color" id="referencesTable" cellspacing="0">
		<tr>
			<th width="20%"><fmt:message key="label.authoring.basic.list.header.type" /></th>
			<th width="40%"><fmt:message key="label.authoring.basic.list.header.question" /></th>
			<th colspan="3"><fmt:message key="label.authoring.basic.list.header.mark" /></th>
		</tr>	

		<c:forEach var="questionReference" items="${sessionMap.questionReferences}" varStatus="status">
			<c:set var="question" value="${questionReference.question}" />
			<tr>
				<td>
					<span class="field-name">
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
						</c:choose>
					</span>
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
					<input name="grade${questionReference.sequenceId}" id="grade${questionReference.sequenceId}" value="${questionReference.defaultGrade}">
				</td>
				
				<td width="40px" style="vertical-align:middle;">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.basic.up"/>"
							onclick="upQuestionReference(${status.index})">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.down"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.up"/>">
						</c:if>

						<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.basic.down"/>"
							onclick="downQuestionReference(${status.index})">
					</c:if>
				</td>
                
				<td width="20px" style="vertical-align:middle;">
					<img src="${tool}includes/images/cross.gif"
						title="<fmt:message key="label.authoring.basic.delete" />"
						onclick="deleteQuestionReference(${status.index})" />
				</td>
			</tr>
		</c:forEach>
	</table>
	
	
	<!-- Dropdown menu for choosing a question from question bank -->
	<c:if test="${fn:length(sessionMap.questionReferences) < fn:length(sessionMap.questionList)}">
		<p>
			<select id="questionSelect" style="float: left">
				<c:if test="${fn:length(availableQuestions) > 1}">
					<option value="-1" selected="selected"><fmt:message key="label.authoring.basic.select.random.question" /></option>
				</c:if>
				
				<c:forEach var="question" items="${availableQuestions}" varStatus="status">
					<option value="${question.sequenceId}"><c:out value="${question.title}" escapeXml="true"/></option>
				</c:forEach>
			</select>
			
			<a onclick="addQuestionReference();return false;" href="" class="button-add-item space-left" id="newQuestionInitHref2">  
				<fmt:message key="label.authoring.basic.add.question.to.list" />
			</a>
		</p>
	</c:if>
</div>
<br>

<div>
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.basic.question.bank.title" />
		<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="assessmentListArea_Busy" />
	</h2>

	<table class="alternative-color" id="questionTable" cellspacing="0">
		<tr>
			<th width="20%"><fmt:message key="label.authoring.basic.list.header.type" /></th>
			<th colspan="3"><fmt:message key="label.authoring.basic.list.header.question" /></th>
		</tr>	
	
		<c:forEach var="question" items="${sessionMap.questionList}" varStatus="status">
			<tr>
				<td>
					<span class="field-name">
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
						</c:choose>
					</span>
				</td>

				<td>
					<c:out value="${question.title}" escapeXml="true" />
				</td>
				
				<td width="20px" style="vertical-align:middle;">
					<c:set var="editQuestionUrl" >
						<c:url value='/authoring/editQuestion.do'/>?sessionMapID=${sessionMapID}&questionIndex=${status.index}&KeepThis=true&TB_iframe=true&height=540&width=850&modal=true
					</c:set>		
					<a href="${editQuestionUrl}" class="thickbox" style="margin-left: 20px;"> 
						<img src="<html:rewrite page='/includes/images/edit.gif'/>" 
							title="<fmt:message key="label.authoring.basic.edit" />" style="border-style: none;"/>
					</a>				
                </td>
                
				<td width="20px" style="vertical-align:middle;">
					<img src="${tool}includes/images/cross.gif"
						title="<fmt:message key="label.authoring.basic.delete" />"
						onclick="deleteQuestion(${status.index})" />
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
