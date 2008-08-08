<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="questionList">
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
			<%-- Questions type:1=textfield,2=textarea,3=number,4=date,5=File,6=image,7=radio,8=dropdown,9=checkbox,10=longitude/latitude --%>
			<c:if test="${question.type == 1}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.textfield" /></span></td><td>
				${question.description}</td>
				<td width="5%"><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td width="5%"><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>

			<c:if test="${question.type == 2}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.textarea" /></span></td><td>
				${question.description}</td>
				<td width="5%"><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td width="5%"><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>

			<c:if test="${question.type == 3}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.number" /></span></td><td>
				${question.description}</td>
				<td width="5%"><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td width="5%"><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>

			<c:if test="${question.type == 4}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.date" /></span></td><td>
				${question.description}</td>
				<td width="5%"><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td width="5%"><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>

			<c:if test="${question.type == 5}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.file" /></span></td><td>
				${question.description}</td>
				<td><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>
			<c:if test="${question.type == 6}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.image" /></span></td><td>
				${question.description}</td>
				<td><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>
			<c:if test="${question.type == 7}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.radio" /></span></td><td>
				${question.description}</td>
				<td><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>
			<c:if test="${question.type == 8}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.dropdown" /></span></td><td>
				${question.description}</td>
				<td><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>
			<c:if test="${question.type == 9}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.checkbox" /></span></td><td>
				${question.description}</td>
				<td><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>
			<c:if test="${question.type == 10}">
				<td><span class="field-name"> <fmt:message key="label.authoring.basic.longlat" /></span></td><td>
				${question.description}</td>
				<td><img src="${tool}includes/images/edit.gif"
					title="<fmt:message key="label.common.edit" />"
					onclick="editQuestion(${status.index},'${sessionMapID}')" /></td>
				<td><img src="${tool}includes/images/cross.gif"
					title="<fmt:message key="label.common.delete" />"
					onclick="deleteQuestion(${status.index},'${sessionMapID}')" /></td>
			</c:if>
		</tr>
	</c:forEach>
</table>
</div>

<%-- This script will work when a new question submit in order to refresh "Question List" panel. --%>
<script type="text/javascript">
	if(window.top != null){
		window.top.hideQuestionInputArea();
		var obj = window.top.document.getElementById('questionListArea');
		obj.innerHTML= document.getElementById("questionList").innerHTML;
	}
</script>