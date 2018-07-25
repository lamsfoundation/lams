<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="questionList">
<%-- This image is shown when a question is being downloaded from a server. --%>

<div class="panel panel-default voffset5">
	<div class="panel-heading panel-title">
		<fmt:message key="label.authoring.basic.list.title" /> 
		<i class="fa fa-refresh fa-spin fa-3x fa-fw" style="display: none" id="questionListArea_Busy"></i>
	</div>

	<table class="table table-striped table-condensed" id="questionTable">
	<tr>
		<th width="24%"><fmt:message key="label.authoring.basic.list.header.type" /></th>
		<th colspan="3"><fmt:message key="label.authoring.basic.list.header.question" /></th>
	</tr>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:forEach var="question" items="${sessionMap.questionList}" varStatus="status">
		<tr>
			<td><label>
				<c:choose>
					<c:when test="${question.type == 1}">
						<span class="field-name"> <fmt:message key="label.authoring.basic.textfield" />
					</c:when>		
					<c:when test="${question.type == 2}">
						<fmt:message key="label.authoring.basic.textarea" />
					</c:when>
					<c:when test="${question.type == 3}">
						<fmt:message key="label.authoring.basic.number" />
					</c:when>
					<c:when test="${question.type == 4}">
						<fmt:message key="label.authoring.basic.date" />
					</c:when>
					<c:when test="${question.type == 5}">
						<fmt:message key="label.authoring.basic.file" />
					</c:when>
					<c:when test="${question.type == 6}">
						<fmt:message key="label.authoring.basic.image" />
					</c:when>
					<c:when test="${question.type == 7}">
						<fmt:message key="label.authoring.basic.radio" />
					</c:when>
					<c:when test="${question.type == 8}">
						<fmt:message key="label.authoring.basic.dropdown" />
					</c:when>
					<c:when test="${question.type == 9}">
						<fmt:message key="label.authoring.basic.checkbox" />
					</c:when>
					<c:when test="${question.type == 10}">
						 <fmt:message key="label.authoring.basic.longlat" />
					</c:when>
				</c:choose>
				</label></td>
				<td>${question.description}</td>
				<td><i class="fa fa-pencil"	title="<fmt:message key="label.common.edit" />"
					onclick="javascript:editQuestion(${status.index},'${sessionMapID}')"></i></td>
				<td><i class="fa fa-times"	title="<fmt:message key="label.common.delete" />"
					onclick='javascript:if (confirm("<fmt:message key="message.authoring.delete.question" />")) deleteQuestion(${status.index},"${sessionMapID}");'></i></td>
		</tr>
	</c:forEach>
	</table>
</div>
</div>

<%-- This script will work when a new question is submited in order to refresh "Question List" panel. --%>
<script type="text/javascript">
	hideQuestionInputArea();
	var obj = document.getElementById('questionListArea');
	obj.innerHTML= document.getElementById("questionList").innerHTML;
</script>
