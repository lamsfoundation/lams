<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="itemList">
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.basic.task.list.title" />
		<img src="${ctxPath}/includes/images/indicator.gif"
			style="display:none" id="taskListListArea_Busy" />
	</h2>
	
	<table class="alternative-color" id="itemTable" cellspacing="0">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<c:forEach var="taskList" items="${sessionMap.taskListList}"
			varStatus="status">
			<tr>
				<td width="4%" class="field-name align-right">
					<fmt:message key="label.authoring.basic.resource.task" />
				</td>
                                <td>
					<c:out value="${taskList.title}" escapeXml="true"/>
				</td>

				<td width="40px" align="center">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.up"/>"
							onclick="upQuestion(${status.index},'${sessionMapID}')">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.down"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.up"/>">
						</c:if>

						<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.down"/>"
							onclick="downQuestion(${status.index},'${sessionMapID}')">
					</c:if>
				</td>
				
				<td width="20px">
					<img src="${tool}includes/images/edit.gif"
						title="<fmt:message key="label.authoring.basic.resource.edit" />"
						onclick="editItem(${status.index},'${sessionMapID}')" />
                </td>
                
				<td width="20px">
					<img src="${tool}includes/images/cross.gif"
						title="<fmt:message key="label.authoring.basic.resource.delete" />"
						onclick="deleteItem(${status.index},'${sessionMapID}')" />
				</td>
			</tr>
		</c:forEach>
	</table>
</div>

<%-- This script will works when a new resoruce item submit in order to refresh "TaskList List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('taskListListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>
