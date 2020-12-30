<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:if test="${mode == null}"><c:set var="mode" value="${sessionMap.mode}" /></c:if>
<c:set var="isAuthoringRestricted" value="${mode == 'teacher'}" />

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

<div id="itemList">
	<div class="panel panel-default add-file">
	
	<div class="panel-heading panel-title">
		<fmt:message key="label.questions" />
	</div>

	<table class="table table-striped table-condensed">
		<c:forEach items="${sessionMap.questions}" var="qaQuestion" varStatus="status">
			<tr>
				<td>
					<c:out value="${qaQuestion.qbQuestion.name}" escapeXml="false" />
				</td>
				
				<td class="arrows" style="width:5%">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
		 				<lams:Arrow state="up" titleKey="label.tip.moveQuestionUp"
		 							onclick="javascript:moveQuestion(${status.index},'moveQuestionUp')" />
		 			</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" titleKey="label.tip.moveQuestionDown"
									onclick="javascript:moveQuestion(${status.index},'moveQuestionDown') "/>
		 			</c:if>
				</td>
								
				<td align="center" style="width:5%">
					<i class="fa fa-xs fa-asterisk ${qaQuestion.answerRequired ? 'text-danger' : ''}" 
								title="<fmt:message key="label.answer.required"/>" 
								alt="<fmt:message key="label.answer.required"/>"
								onClick="javascript:toggleQuestionRequired(${status.index}, this)"></i>
				</td>
				
				<td align="center" style="width:5%">
					<c:set var="editItemUrl" >
						<c:url value='/authoring/newEditableQuestionBox.do'/>?sessionMapID=${sessionMapID}&questionIndex=${status.index}&KeepThis=true&TB_iframe=true&modal=true
					</c:set>		
					<a href="${editItemUrl}" class="thickbox"> 
						<i class="fa fa-pencil"	title="<fmt:message key='label.tip.editQuestion' />"/></i>
					</a>
				</td>	
				
				<c:if test="${!isAuthoringRestricted}">
					<td  align="center" style="width:5%">
						<i class="fa fa-times"	title="<fmt:message key="label.tip.deleteQuestion" />"
							onclick="removeQuestion(${status.index})">
						</i>
					</td>
				</c:if>
			</tr>
		</c:forEach>

	</table>
	</div>
</div>