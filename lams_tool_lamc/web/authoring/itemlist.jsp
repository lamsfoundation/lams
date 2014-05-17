<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<div id="itemList">
	<h2 class="spacer-left">
		<fmt:message key="label.questions" />
	</h2>
	<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="resourceListArea_Busy" />
	</h2>

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<table id="itemTable" class="alternative-color" cellspacing="0">
		<c:set var="queIndex" scope="request" value="0" />

		<tr>
			<th>

			</th>

			<th width="70px" class="align-center">
				<c:if test="${totalQuestionCount > 0}">
					<fmt:message key="label.question.marks" />
				</c:if>
			</th>

			<th colspan="3">
				&nbsp;
			</th>
		</tr>

		<c:forEach items="${listQuestionContentDTO}" var="currentDTO"
			varStatus="status">
			<c:set var="queIndex" scope="request" value="${queIndex +1}" />
			<c:set var="question" scope="request" value="${currentDTO.question}" />
			<c:set var="feedback" scope="request" value="${currentDTO.feedback}" />
			<c:set var="mark" scope="request" value="${currentDTO.mark}" />
			<c:set var="displayOrder" scope="request"
				value="${currentDTO.displayOrder}" />

			<tr>
				<td>
					<div style="overflow: auto;">
						<strong> <fmt:message key="label.question" />: </strong>
						<c:out value="${question}" escapeXml="false" />
					</div>					
				</td>


				<td class="align-center">
					<c:out value="${mark}" />
				</td>

				<td width="40px">
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
								onclick="javascript:submitModifyAuthoringQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');" />
							<img src="<c:out value="${tool}"/>images/up.gif" border="0"
								title="<fmt:message key='label.tip.moveQuestionUp'/>"
								onclick="javascript:submitModifyAuthoringQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');" />
						</c:if>

					</c:if>
				</td>

				<td width="20px">
					<c:set var="editItemUrl" >
						<html:rewrite page="/authoring.do"/>?dispatch=newEditableQuestionBox&questionIndex=${queIndex}&contentFolderID=${mcGeneralAuthoringDTO.contentFolderID}&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}&toolContentID=${mcGeneralAuthoringDTO.toolContentID}&sln=${mcGeneralAuthoringDTO.sln}&showMarks=${mcGeneralAuthoringDTO.showMarks}&randomize=${mcGeneralAuthoringDTO.randomize}&questionsSequenced=${mcGeneralAuthoringDTO.questionsSequenced}&retries=${mcGeneralAuthoringDTO.retries}&reflect=${mcGeneralAuthoringDTO.reflect}&KeepThis=true&TB_iframe=true&height=540&width=950&modal=true
					</c:set>
					<a href="${editItemUrl}" class="thickbox"> 
						<img src="${tool}images/edit.gif" border="0" title="<fmt:message key='label.tip.editQuestion'/>">
					</a>
				</td>

				<td width="20px" align="center">
					<img src="<c:out value="${tool}"/>images/delete.gif" border="0"
						title="<fmt:message key='label.tip.deleteQuestion'/>"
						onclick="removeQuestion(${queIndex});">
				</td>
			</tr>
		</c:forEach>

	</table>
</div>

