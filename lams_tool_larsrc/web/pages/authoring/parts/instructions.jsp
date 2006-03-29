<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="instructionArea">
	<form id="instructionForm">
	<input type="hidden" name="instructionCount" id="instructionCount">
	<table>
		<tr>
			<td colspan="5">
				<fmt:message key="label.authoring.basic.resource.instructions"/>
				<input type="button" onclick="addInstruction()" value="<fmt:message key="label.authoring.basic.resource.add.instruction"/>" class="buttonStyle">  
				<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="instructionArea_Busy" />
			</td>
		</tr>
		<c:forEach var="item" items="${instructionList}" varStatus="status">
			<tr id="instructionItem${status.index}">
				<td width="10px">${status.index+1}</td>
				<td width="100px"><input type="text" name="instructionItem${status.index}" size="70" value="${item}"></td>
				<td width="40px"><img src="<html:rewrite page='/includes/images/uparrow.gif'/>" border="0"></td>
				<td width="40px"><img src="<html:rewrite page='/includes/images/downarrow.gif'/>" border="0"> </td>
				<td width="40px">
					<a href="javascript:;" onclick="removeInstruction('${status.index}')">
						<img src="<html:rewrite page='/includes/images/cross.gif'/>" border="0">
					</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	</form>
</div>

<script type="text/javascript">
	$("instructionCount").value="${fn:length(instructionList)}";
	var obj = window.top.document.getElementById('reourceInputArea');
	obj.style.height=obj.contentWindow.document.body.scrollHeight+'px';
</script>
