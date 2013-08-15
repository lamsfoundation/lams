<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />
<div id="itemList">
	<h2 class="spacer-left">
		<fmt:message key="label.questions" />
		<img src="${ctxPath}/includes/images/indicator.gif"
			style="display:none" id="resourceListArea_Busy" />
	</h2>

	<c:set var="formBean"
		value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

	<table id="itemTable" class="alternative-color">
		<c:set var="queIndex" scope="request" value="0" />

		<c:forEach items="${listQuestionContentDTO}" var="currentDTO"
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
						onclick="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newEditableQuestionBox&questionIndex=${queIndex}&contentFolderID=${qaGeneralAuthoringDTO.contentFolderID}&httpSessionID=${qaGeneralAuthoringDTO.httpSessionID}&toolContentID=${qaGeneralAuthoringDTO.toolContentID}&activeModule=${qaGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${qaGeneralAuthoringDTO.defaultContentIdStr}&synchInMonitor=${qaGeneralAuthoringDTO.synchInMonitor}&usernameVisible=${qaGeneralAuthoringDTO.usernameVisible}&lockWhenFinished=${qaGeneralAuthoringDTO.lockWhenFinished}&questionsSequenced=${qaGeneralAuthoringDTO.questionsSequenced}"/>');">
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
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('resourceListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>
