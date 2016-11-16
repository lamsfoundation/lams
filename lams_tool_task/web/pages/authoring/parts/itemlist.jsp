<%@ include file="/common/taglibs.jsp"%>

<div id="itemList">
	<h4>
		<fmt:message key="label.authoring.basic.task.list.title" />
		<i class="fa fa-spinner" style="display: none" id="taskListListArea_Busy"></i>
	</h4>
	
	<table class="table table-condensed table-striped" id="itemTable">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<c:forEach var="taskList" items="${sessionMap.taskListList}" varStatus="status">
			<tr>
				<td width="5%" class="field-name align-right">
					<fmt:message key="label.authoring.basic.resource.task" />
				</td>
                 <td>
					<c:out value="${taskList.title}" escapeXml="true"/>
				</td>

				<td class="arrows" style="width:5%">
					<c:if test="${not status.first}">
						<c:set var="title"><fmt:message key='label.authoring.up'/></c:set>
						<lams:Arrow state="up" title="${title}" onclick="upQuestion(${status.index},'${sessionMapID}')"/>
					</c:if>
					<c:if test="${not status.last}">
						<c:set var="title"><fmt:message key='label.authoring.down'/></c:set>
						<lams:Arrow state="down" title="<fmt:message key='label.authoring.down'/>" onclick="downQuestion(${status.index},'${sessionMapID}')"/>
					</c:if>
				</td>
				
				<td align="center" style="width:5%">
					<i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.resource.edit" />"
						onclick="editItem(${status.index},'${sessionMapID}')"></i>
                </td>
                
				<td  align="center" style="width:5%">
					<i class="fa fa-times"	title="<fmt:message key="label.authoring.basic.resource.delete" />"
						onclick="deleteItem(${status.index},'${sessionMapID}')"></i>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>

<%-- This script will works when a new item is submitted in order to refresh "TaskList List" panel. --%>
<script lang="javascript">
	if ($("#resourceInputArea").is(':visible')) { 
		hideMessage();
		var tasks = $("#itemList", "#resourceInputArea").html();
		$("#taskListListArea").html(itemList);
	}
</script>
