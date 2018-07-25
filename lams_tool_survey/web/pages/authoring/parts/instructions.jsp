<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"
	scope="request" />
<c:set var="listSize" value="${fn:length(instructionList)}" />
<div id="instructionArea">
	<script type="text/javascript"></script>
	
	<form id="instructionForm">
		<input type="hidden" name="instructionCount" id="instructionCount">
		<table id="questionTable" class="table table-condensed table-no-border">
			<c:forEach var="item" items="${instructionList}" varStatus="status">
				<c:set var="item">
					<c:out value="${item}" escapeXml="true"/>
				</c:set>
					
				<tr id="instructionItem${status.index}">
					<td width="5%">
						${status.index+1}
					</td>
					<td width="100px">
						<input type="text" name="instructionItemDesc${status.index}"
							id="instructionItemDesc${status.index}" size="60" value="${item}" class="form-control">
					</td>

					<td class="arrows" style="width:5%">
						<c:if test="${not status.first}">
							<c:set var="title"><fmt:message key="label.up"/></c:set>
							<lams:Arrow state="up" title="${title}" onclick="upItem('${status.index}')"/>
						</c:if>
						<c:if test="${not status.last}">
							<c:set var="title"><fmt:message key="label.down"/></c:set>
							<lams:Arrow state="down" title="${title}" onclick="downItem('${status.index}')"/>
						</c:if>
					</td>
					<td class="text-center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.delete"/>"
							onclick="removeInstruction('${status.index}')"></i>
					</td>
				</tr>
			</c:forEach>
		</table>
	</form>
</div>
<script type="text/javascript">
	document.getElementById("instructionCount").value="${listSize}";
</script>
