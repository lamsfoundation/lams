<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="conditionList">
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.conditions.list.title" />
		<img src="${ctxPath}/images/indicator.gif"	style="display:none" id="conditionListArea_Busy" />
	</h2>
	
	<table class="alternative-color" id="conditionTable" cellspacing="0">
		<tr>
			<th width="14%" align="left">
				<fmt:message key="label.authoring.conditions.order" />
			</th>
			<th align="left" colspan="4">
				<fmt:message key="label.authoring.conditions.condition.name" />
			</th>
		</tr>

		<c:if test="${empty sessionMap.conditionList}">
			<tr>
                <td colspan="5">
					<fmt:message key="label.authoring.conditions.empty.condition.list" />
				</td>
			</tr>
		</c:if>

		<c:forEach var="condition" items="${sessionMap.conditionList}" varStatus="status">
			<tr>
                <td>
					${status.index + 1}
				</td>
			
                <td>
					${condition.displayName}
				</td>

				<td width="40px" align="center">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/images/up.gif'/>"
							border="0" title="<fmt:message key="label.authoring.up"/>"
							onclick="upCondition(${status.index},'${sessionMapID}')">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/images/down_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.down"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/images/up_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.up"/>">
						</c:if>

						<img src="<html:rewrite page='/images/down.gif'/>"
							border="0" title="<fmt:message key="label.authoring.down"/>"
							onclick="downCondition(${status.index},'${sessionMapID}')">
					</c:if>
				</td>
				
				<td width="20px">
					<img src="${tool}images/edit.gif"
						title="<fmt:message key="label.authoring.edit" />"
						onclick="editCondition(${status.index},'${sessionMapID}')" />
                </td>
                
				<td width="20px">
					<img src="${tool}images/cross.gif"
						title="<fmt:message key="label.authoring.delete" />"
						onclick="deleteCondition(${status.index},'${sessionMapID}')" />
				</td>
			</tr>
		</c:forEach>
	</table>
</div>

<%-- This script will works when a new resoruce Condition submit in order to refresh "TaskList List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.hideConditionMessage) {
		win = window;
	} else if (window.parent && window.parent.hideConditionMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideConditionMessage) {
		win = window.top;
	}
	if (win) {
		win.hideConditionMessage();
		var obj = win.document.getElementById('conditionsArea');
		obj.innerHTML= document.getElementById("conditionList").innerHTML;
	}
</script>