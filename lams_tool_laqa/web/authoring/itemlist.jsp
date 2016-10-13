<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />

<div id="itemList">
	<h2 class="spacer-left">
		<fmt:message key="label.questions" />
		<img src="${ctxPath}/images/indicator.gif"
			style="display:none" id="resourceListArea_Busy" />
	</h2>

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

	<table id="itemTable" class="alternative-color">
		<c:set var="queIndex" scope="request" value="0" />

		<c:forEach items="${questionDTOs}" var="currentDTO"
			varStatus="status">
			<c:set var="queIndex" scope="request" value="${queIndex +1}" />
			<c:set var="question" scope="request" value="${currentDTO.question}" />
			<c:set var="feedback" scope="request" value="${currentDTO.feedback}" />
			<c:set var="displayOrder" scope="request"
				value="${currentDTO.displayOrder}" />

			<tr>
				<td width="10%" class="field-name align-right">
					<fmt:message key="label.question" />
				</td>

				<td width="60%" class="align-left">
					<c:out value="${question}" escapeXml="false" />
				</td>

				<td width="12%" class="align-right">
					<c:if test="${totalQuestionCount != 1}">

						<c:if test="${queIndex == 1}">
							<img src="<c:out value="${tool}"/>images/down.gif" border="0"
								title="<fmt:message key='label.tip.moveQuestionDown'/>"
								onclick="javascript:submitModifyAuthoringQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">
							<img src="<c:out value="${tool}"/>images/up_disabled.gif"
								border="0">
						</c:if>

						<c:if test="${queIndex == totalQuestionCount}">
							<img src="<c:out value="${tool}"/>images/down_disabled.gif"
								border="0">
							<img src="<c:out value="${tool}"/>images/up.gif" border="0"
								title="<fmt:message key='label.tip.moveQuestionUp'/>"
								onclick="javascript:submitModifyAuthoringQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">
						</c:if>

						<c:if
							test="${(queIndex != 1)  && (queIndex != totalQuestionCount)}">

							<img src="<c:out value="${tool}"/>images/down.gif" border="0"
								title="<fmt:message key='label.tip.moveQuestionDown'/>"
								onclick="javascript:submitModifyAuthoringQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">
							<img src="<c:out value="${tool}"/>images/up.gif" border="0"
								title="<fmt:message key='label.tip.moveQuestionUp'/>"
								onclick="javascript:submitModifyAuthoringQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">
						</c:if>

					</c:if>
				</td>

				<td width="10%" class="align-right">

					<img src="<c:out value="${tool}"/>images/edit.gif" border="0"
						title="<fmt:message key='label.tip.editQuestion'/>"
						onclick="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newEditableQuestionBox&questionIndex=${queIndex}&contentFolderID=$${formBean.contentFolderID}&httpSessionID=${formBean.httpSessionID}&toolContentID=${formBean.toolContentID}&usernameVisible=${formBean.usernameVisible}&lockWhenFinished=${formBean.lockWhenFinished}&questionsSequenced=${formBean.questionsSequenced}"/>');">
				</td>

				<td width="10%" class="align-right">
					<img src="<c:out value="${tool}"/>images/delete.gif" border="0"
						title="<fmt:message key='label.tip.deleteQuestion'/>"
						onclick="removeQuestion(${queIndex});">
				</td>
			</tr>
		</c:forEach>

	</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
	var win = null;
	try {
		if (window.parent && window.parent.hideMessage) {
			win = window.parent;
		} else if (window.top && window.top.hideMessage) {
			win = window.top;
		}
	} catch(err) {
		// mute cross-domain iframe access errors
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('resourceListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>
