<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />
<c:set var="listSize" value="${fn:length(instructionList)}" />
<div id="instructionArea">
	<form id="instructionForm">
		<input type="hidden" name="instructionCount" id="instructionCount">
		<table id="questionTable">
			<c:forEach var="item" items="${instructionList}" varStatus="status">
				<c:set var="item">
					<c:out value="${item}" escapeXml="true"/>
				</c:set>
					
				<tr id="instructionItem${status.index}">
					<td width="3px">
						${status.index+1}
					</td>
					<td width="100px">
						<input type="text" name="instructionItemDesc${status.index}"
							id="instructionItemDesc${status.index}" size="60" value="${item}">
					</td>

					<td width="40px">
						<%-- Don't display down icon if last line --%>
						<c:choose>
							<c:when test="${0 != status.index}">
								<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
									border="0" title="<fmt:message key="label.up"/>"
									onclick="upItem('${status.index}')">
							</c:when>
							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.up"/>">
							</c:otherwise>
						</c:choose>


						<c:choose>
							<c:when test="${listSize != status.count}">
								<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
									border="0" title="<fmt:message key="label.down"/>"
									onclick="downItem('${status.index}','${listSize}')">
							</c:when>
							<c:otherwise>
								<img
									src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
									border="0" title="<fmt:message key="label.down"/>">
							</c:otherwise>
						</c:choose>

						<%-- Don't display down icon if last line --%>
					</td>
					<td width="30px" align="center">
						<img src="<html:rewrite page='/includes/images/cross.gif'/>"
							border="0" title="<fmt:message key="label.delete"/>"
							onclick="removeInstruction('${status.index}')">
					</td>
				</tr>
			</c:forEach>
		</table>
	</form>
</div>

<%-- This script will adjust survey item input area height according to the new instruction item amount. --%>
<script type="text/javascript">
	$("instructionCount").value="${listSize}";
	var obj = window.document.getElementById('questionInputArea');
	if (!obj && window.parent) {
		 obj = window.parent.document.getElementById('questionInputArea');
	}  
	if (!obj) {
		obj = window.top.document.getElementById('questionInputArea');
	}
	obj.style.height=obj.contentWindow.document.body.scrollHeight+'px';
</script>
