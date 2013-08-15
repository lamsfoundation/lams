<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="conditionList">
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.conditions.list.title" />
		<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="conditionListArea_Busy" />
	</h2>
	
	<table class="alternative-color" id="conditionTable" cellspacing="0">
		<tr>
			<th align="left" style="width:1%;">
				<!--<fmt:message key="label.authoring.conditions.condition.number" />-->
				Nº
			</th>
			<th align="left" colspan="10">
				<fmt:message key="label.authoring.conditions.condition.name" />
			</th>
		</tr>

		<c:if test="${empty sessionMap.conditionsList}">
			<tr>
                <td colspan="15">
					<fmt:message key="label.authoring.conditions.empty.condition.list" />
				</td>
			</tr>
		</c:if>

		<c:forEach var="condition" items="${sessionMap.conditionsList}" varStatus="status">
			<tr>
				<td>
					${status.index + 1}
				</td>
                <td>
					${condition.name}
				</td>
				<td width="40px" align="center">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.up"/>"
							onclick="upCondition(${status.index},'${sessionMapID}')">
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
							onclick="downCondition(${status.index},'${sessionMapID}')">
					</c:if>
				</td>
				
				<td width="20px">
					<img src="${tool}includes/images/edit.gif"
						title="<fmt:message key="label.authoring.basic.resource.edit" />"
						onclick="editCondition(${status.index},'${sessionMapID}')" />
                </td>
                
				<td width="20px">
					<img src="${tool}includes/images/cross.gif"
						title="<fmt:message key="label.authoring.basic.resource.delete" />"
						onclick="deleteCondition(${status.index},'${sessionMapID}')" />
				</td>
			</tr>
		</c:forEach>

	</table>
</div>


<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideConditionMessage) {
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
