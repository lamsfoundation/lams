<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<%@ page import="org.lamsfoundation.lams.qb.service.IQbService" %>
<script>
	// Inform author whether the QB question was modified
	var qbQuestionModified = ${empty qbQuestionModified ? 0 : qbQuestionModified},
		qbMessage = null;
	switch (qbQuestionModified) {
		case <%= IQbService.QUESTION_MODIFIED_UPDATE %>: 
			qbMessage = '<fmt:message key="message.qb.modified.update" />';
			break;
		case <%= IQbService.QUESTION_MODIFIED_VERSION_BUMP %>: 
			qbMessage = '<fmt:message key="message.qb.modified.version" />';
			break;
		case <%= IQbService.QUESTION_MODIFIED_ID_BUMP %>: 
			qbMessage = '<fmt:message key="message.qb.modified.new" />';
			break;
	}
	if (qbMessage) {
		alert(qbMessage);
	}
</script>

<div class="panel panel-default voffset5">
	<table class="table table-condensed" id="referencesTable">
		<thead>
			<tr>
				<th>
					<fmt:message key="label.authoring.basic.list.header.question" />
				</th>
				<th colspan="4">
					<fmt:message key="label.authoring.basic.list.header.mark" />
				</th>
			</tr>
		</thead>	

		<c:forEach var="questionReference" items="${sessionMap.questionReferences}" varStatus="status">
			<c:set var="question" value="${questionReference.question}" />
			<tr>
				<td>
					<c:choose>
						<c:when test="${questionReference.randomQuestion}">
							<fmt:message key="label.authoring.basic.type.random.question" />
						</c:when>
						<c:otherwise>
							<c:out value="${question.qbQuestion.name}" escapeXml="true"/>
						</c:otherwise>
					</c:choose>
					
					<c:if test="${!questionReference.randomQuestion}">
				        <span class='pull-right alert-info btn-xs loffset5 roffset5'>
				       		v.&nbsp;${question.qbQuestion.version}
				        </span>
			        </c:if>
			        
			       	<span class='pull-right alert-info btn-xs'>
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
	       			</span>
				</td>
				
				<td width="70px" style="padding-right: 10px;">
					<input name="maxMark${questionReference.sequenceId}" value="${questionReference.maxMark}"
						id="maxMark${questionReference.sequenceId}" class="form-control input-sm">
				</td>
				
				<td class="arrows">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
		 				<lams:Arrow state="up" title="<fmt:message key='label.authoring.basic.up'/>" onclick="javascript:upQuestionReference(${status.index})"/>
		 			</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" title="<fmt:message key='label.authoring.basic.down'/>" onclick="javascript:downQuestionReference(${status.index})"/>
		 			</c:if>
				</td>
				
				<td width="30px">
					<a href="#nogo" class="thickbox roffset5x" style="color: black;" id="edit-ref-${status.index}"
						onclick="javascript:createEditQuestionHref(${status.index})"> 
						<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.edit" />"></i>
					</a>
				</td>

				<td width="30px">
					<i class="fa fa-times" title="<fmt:message key="label.authoring.basic.delete" />"
						onclick="javascript:deleteQuestionReference(${status.index})"></i>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
