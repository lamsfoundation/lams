<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="questionList">
<%-- This image is shown when a question is being downloaded from a server. --%>
<h2 class="spacer-left"><fmt:message key="label.authoring.basic.list.title" /> <img
	src="${ctxPath}/includes/images/indicator.gif" style="display: none" id="questionListArea_Busy" /></h2>

<table class="alternative-color" id="questionTable" cellspacing="0">
	<tr>
		<th width="24%"><fmt:message key="label.authoring.basic.list.header.type" /></th>
		<th colspan="3"><fmt:message key="label.authoring.basic.list.header.question" /></th>
	</tr>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:forEach var="question" items="${sessionMap.questionList}" varStatus="status">
		<tr>
			<td><span class="field-name">
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
				</span></td>
				<td>${question.description}</td>
				<td><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="javascript:editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick='javascript:if (confirm("<fmt:message key="message.authoring.delete.question" />")) deleteQuestion(${status.index},"${sessionMapID}");' /></td>
		</tr>
	</c:forEach>
</table>
</div>

<%-- This script will work when a new question is submited in order to refresh "Question List" panel. --%>
<script type="text/javascript">
	var win = null;
	try {
		if (window.parent && window.parent.hideQuestionInputArea) {
			win = window.parent;
		} else if (window.top && window.top.hideQuestionInputArea) {
			win = window.top;
		}
	} catch(err) {
		// mute cross-domain iframe access errors
	}
	if (win) {
		win.hideQuestionInputArea();
		var obj = win.document.getElementById('questionListArea');
		obj.innerHTML= document.getElementById("questionList").innerHTML;
	}
</script>