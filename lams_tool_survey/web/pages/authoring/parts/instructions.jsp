<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="listSize" value="${fn:length(instructionList)}"/>
<div id="instructionArea">
	<form id="instructionForm">
	<input type="hidden" name="instructionCount" id="instructionCount">
	<table border="0" style="width:400px" id="questionTable">
		<c:forEach var="item" items="${instructionList}" varStatus="status">
			<tr id="instructionItem${status.index}">
				<td width="3px">${status.index+1}</td>
				<td width="100px"><input type="text" name="instructionItemDesc${status.index}" id="instructionItemDesc${status.index}" size="60" value="${item}"></td>
				
				<td width="20px">
					<%-- Don't display down icon if last line --%>
					<c:if test="${0 != status.index}">
						<a href="javascript:;" onclick="upItem('${status.index}')">
							<img src="<html:rewrite page='/includes/images/uparrow.gif'/>" border="0"  title="<fmt:message key="label.up"/>">
						</a>
					</c:if>
				</td>
				<td width="20px">
					<%-- Don't display down icon if last line --%>
					<c:if test="${listSize != status.count}">
						<a href="javascript:;" onclick="downItem('${status.index}','${listSize}')">
							<img src="<html:rewrite page='/includes/images/downarrow.gif'/>" border="0"  title="<fmt:message key="label.down"/>"> 
						</a>
					</c:if>
				</td>
				<td width="30px" align="center">
					<a href="javascript:;" onclick="removeInstruction('${status.index}')">
						<img src="<html:rewrite page='/includes/images/cross.gif'/>" border="0" title="<fmt:message key="label.delete"/>">
					</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	</form>
</div>

<%-- This script will adjust survey item input area height according to the new instruction item amount. --%>
<script type="text/javascript">
	$("instructionCount").value="${listSize}";
	var obj = window.top.document.getElementById('questionInputArea');
	obj.style.height=obj.contentWindow.document.body.scrollHeight+'px';
</script>
