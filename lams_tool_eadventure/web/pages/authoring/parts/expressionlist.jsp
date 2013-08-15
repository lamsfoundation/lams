<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="expressionList">
		
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.conditions.expression.list.title" />
		<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="expressionArea_Busy" />
	</h2>
	<table class="alternative-color" id="expressionsTable" cellspacing="0">
	
		<tr>
			<th align="left" style="width:1%;">
				<!--<fmt:message key="label.authoring.conditions.condition.number" />-->
				Nº
			</th>
			<th align="left" colspan="4">
				<fmt:message key="label.authoring.conditions.condition.var1" />
			</th>
			<th align="left" colspan="4">
				<fmt:message key="label.authoring.conditions.condition.operator" />
			</th>
			<th align="left" colspan="8">
				<fmt:message key="label.authoring.conditions.condition.var2" />
			</th>
		</tr>
		<c:if test="${empty sessionMap.expressionList}">
			<tr>
                <td colspan="20">
					<fmt:message key="label.authoring.conditions.empty.expression.list" />
				</td>
			</tr>
		</c:if>
		
		<c:forEach var="expression" items="${sessionMap.expressionList}"
			varStatus="status">
		<tr>
			<td>
				${status.index + 1}
			</td>
			<td align="left" colspan="4">
				 ${expression.firstOp.name}
			</td>
			<td align="left" colspan="4">
				${expression.expresionOp}
			</td>
			<td align="left" colspan="4">
				<c:choose>
					<c:when test="${empty expression.valueIntroduced}">
						 ${expression.varIntroduced.name}
					</c:when>
					<c:otherwise>
						${expression.valueIntroduced}
					</c:otherwise>
				</c:choose>
			</td>
			<td width="40px" align="center">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.up"/>"
							onclick="upExpression(${status.index},'${sessionMapID}')">
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
							onclick="downExpression(${status.index},'${sessionMapID}')">
					</c:if>
				</td>
			<td width="10px">
				<img src="${tool}includes/images/edit.gif"
							title="<fmt:message key="label.authoring.basic.resource.edit" />"
							onclick="editExpression(${status.index},'${sessionMapID}')" />
			</td>
			<td width="10px">
				<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteExpression(${status.index},'${sessionMapID}')" />
			</td>
		</tr>	
		</c:forEach>	
	</table>
</div>



<script lang="javascript">
	var area = null;
	if (window.parent) {
		area = window.parent.document.getElementById('conditionInputArea');
	}
	if (!area && window.top) {
		area = window.top.document.getElementById('conditionInputArea');
	}
	if (area) {
		area.contentWindow.hideExpressionMessage();
		var obj = area.contentDocument.getElementById("expressionArea");
		obj.innerHTML = document.getElementById("expressionList").innerHTML;
	}
</script>