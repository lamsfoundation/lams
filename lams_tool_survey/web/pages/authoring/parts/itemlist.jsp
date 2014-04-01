<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />
<div id="itemList">
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.basic.survey.list.title" />
		<img src="${ctxPath}/includes/images/indicator.gif"
			style="display:none" id="surveyListArea_Busy" />
	</h2>

	<table id="itemTable" class="alternative-color" cellspacing="0">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<c:forEach var="question" items="${sessionMap.questionList}"
			varStatus="status">
			<tr>
				<td width="20px" align="center">
					<c:choose>
						<c:when test="${question.type == 1}">
							<img src="${ctxPath}/includes/images/icon_single.gif" />
						</c:when>
						<c:when test="${question.type == 2}">
							<img src="${ctxPath}/includes/images/icon_multiple.gif" />
						</c:when>
						<c:when test="${question.type == 3}">
							<img src="${ctxPath}/includes/images/icon_text.gif" />
						</c:when>
					</c:choose>
				</td>

				<td>
					<c:out value="${question.shortTitle}" escapeXml="true"/>
				</td>

				<td width="80px" align="center">
					<c:if test="${question.optional}">
						<fmt:message key="label.optional" />
					</c:if>
				</td>

				<td width="40px" align="center">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.up"/>"
							onclick="upQuestion(${status.index},'${sessionMapID}')">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.down"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.up"/>">
						</c:if>

						<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
							border="0" title="<fmt:message key="label.down"/>"
							onclick="downQuestion(${status.index},'${sessionMapID}')">
					</c:if>
				</td>

				<td width="20px" align="center">

					<img src="<html:rewrite page='/includes/images/edit.gif'/>"
						border="0"
						title="<fmt:message key="label.authoring.basic.survey.edit"/>"
						onclick="editItem(${status.index},${question.type},'${sessionMapID}','${contentFolderID}')">

				</td>
				<td width="20px" align="center">

					<img src="<html:rewrite page='/includes/images/page_copy.png'/>"
						border="0"
						title="<fmt:message key="label.copy"/>"
						onclick="copyItem(${status.index},${question.type},'${sessionMapID}','${contentFolderID}')">

				</td>
				<td width="20px" align="center">

					<img src="<html:rewrite page='/includes/images/cross.gif'/>" border="0"
						title="<fmt:message
							key="label.authoring.basic.survey.delete" />"
						onclick="deleteItem(${status.index},'${sessionMapID}')">
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Survey List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('surveyListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>
