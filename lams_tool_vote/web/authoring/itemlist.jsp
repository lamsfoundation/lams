<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<div id="itemList">
	<h2 class="spacer-left">
		<fmt:message key="label.vote.nominations" />
		<img src="${ctxPath}/images/indicator.gif" style="display:none" id="resourceListArea_Busy" />
	</h2>

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<table id="itemTable" class="alternative-color" cellspacing="0">
		<c:set var="queIndex" scope="request" value="0" />

		<c:forEach items="${listQuestionDTO}" var="currentDTO" varStatus="status">
			<c:set var="queIndex" scope="request" value="${queIndex +1}" />
			<c:set var="question" scope="request" value="${currentDTO.question}" />
			<c:set var="displayOrder" scope="request" value="${currentDTO.displayOrder}" />
			<c:if test="${status.first}">
				<input type="hidden" id="firstNomination" />
			</c:if>
			
			<tr>
				<td width="10%" class="field-name align-right">
					<fmt:message key="label.nomination" />
				</td>

				<td width="60%" align="left">
					<c:out value="${question}" escapeXml="false" />
				</td>

				<td width="10%" class="align-right">
					<c:if test="${fn:length(listQuestionDTO) != 1}">

						<c:if test="${queIndex == 1}">
							<img src="<c:out value="${tool}"/>images/down.gif" border="0"
								title="<fmt:message key='label.tip.moveNominationDown'/>"
								onclick="javascript:submitModifyAuthoringNomination('<c:out value="${queIndex}"/>','moveNominationDown');">
							<img src="<c:out value="${tool}"/>images/up_disabled.gif"
								border="0">
						</c:if>

						<c:if test="${queIndex == fn:length(listQuestionDTO)}">
							<img src="<c:out value="${tool}"/>images/down_disabled.gif"
								border="0">
							<img src="<c:out value="${tool}"/>images/up.gif" border="0"
								title="<fmt:message key='label.tip.moveNominationUp'/>"
								onclick="javascript:submitModifyAuthoringNomination('<c:out value="${queIndex}"/>','moveNominationUp');">
						</c:if>

						<c:if
							test="${(queIndex != 1)  && (queIndex != fn:length(listQuestionDTO))}">
							<img src="<c:out value="${tool}"/>images/down.gif" border="0"
								title="<fmt:message key='label.tip.moveNominationDown'/>"
								onclick="javascript:submitModifyAuthoringNomination('<c:out value="${queIndex}"/>','moveNominationDown');">
							<img src="<c:out value="${tool}"/>images/up.gif" border="0"
								title="<fmt:message key='label.tip.moveNominationUp'/>"
								onclick="javascript:submitModifyAuthoringNomination('<c:out value="${queIndex}"/>','moveNominationUp');">
						</c:if>

					</c:if>
				</td>

				<td width="10%" class="align-right">
					<img src="<c:out value="${tool}"/>images/edit.gif" border="0"
						title="<fmt:message key='label.tip.editNomination'/>"
						onclick="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newEditableNominationBox&questionIndex=${queIndex}&contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&minNominationCount=${voteGeneralAuthoringDTO.minNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}"/>');">
				</td>

				<td width="10%" class="align-right">
					<img src="<c:out value="${tool}"/>images/delete.gif" border="0"
						title="<fmt:message key='label.tip.deleteNomination'/>"
						onclick="removeNomination(${queIndex});">
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
        
        win.changeMinMaxVotes(-1, -1);
	}
</script>

