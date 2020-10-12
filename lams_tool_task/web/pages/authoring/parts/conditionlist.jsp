<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<div id="conditionList">
	<table class="table table-striped table-condensed" id="conditionTable" >
		<tr>
			<th width="8%">
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
					${condition.name}
				</td>

				<td class="arrows" style="width:5%">
					<c:if test="${not status.first}">
						<lams:Arrow state="up" titleKey="label.authoring.up" onclick="upCondition(${status.index},'${sessionMapID}')"/>
					</c:if>
					<c:if test="${not status.last}">
						<lams:Arrow state="down" titleKey="label.authoring.down" onclick="downCondition(${status.index},'${sessionMapID}')"/>
					</c:if>
				</td>
				
				<td align="center" style="width:5%"><i class="fa fa-pencil"	title="<fmt:message key="label.authoring.basic.resource.edit" />"
					onclick="editCondition(${status.index},'${sessionMapID}')"></i></td>
					
				<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.authoring.basic.resource.delete" />" 
					onclick="deleteCondition(${status.index},'${sessionMapID}')"></i></td>
				
			</tr>
		</c:forEach>
	</table>
</div>

<%-- This script will works when a new resoruce Condition submit in order to refresh "TaskList List" panel. --%>
<script lang="javascript"> 
	hideConditionMessage();
	var obj = document.getElementById('conditionsArea');
	obj.innerHTML= document.getElementById("conditionList").innerHTML;
</script>
