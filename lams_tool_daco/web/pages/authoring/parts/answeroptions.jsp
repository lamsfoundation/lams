<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="listSize" value="${fn:length(answerOptionList)}" />
<c:set var="ordinal"><fmt:message key="label.authoring.basic.answeroption.ordinal"/></c:set>
<div id="answerOptionsArea" class="space-bottom-top">
<form id="answerOptionsForm"><input type="hidden" id="answerOptionCount" name="answerOptionCount" value="${listSize}"/>
<div class="field-name"><fmt:message key="label.authoring.basic.answeroption" /></div>
<a href="#" onclick="javascript:addAnswerOption()" class="button left-buttons"><fmt:message
	key="label.authoring.basic.answeroption.add" /></a>
		<img src="${ctxPath}/includes/images/indicator.gif"
			style="display:none;" id="answerOptionsArea_Busy" name="answerOptionsArea_Busy"/>
<table>
	<c:forEach var="item" items="${answerOptionList}" varStatus="status">
		<tr>
			<td width="3px">${fn:substring(ordinal,status.index,status.index+1)})</td>
			<td><input type="text" name="answerOptionItemDesc${status.index+1}" id="answerOptionItemDesc${status.index+1}" size="60" value="${item}"></td>

			<td><%-- Don't display down icon if last line --%>
			<c:choose>
				<c:when test="${status.index > 0}">
					<img src="<html:rewrite page='/includes/images/uparrow.gif'/>" border="0"
						title="<fmt:message key="label.up"/>" onclick="javascript:upItem('${status.index+1}')">
				</c:when>
				<c:otherwise>
					<img src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>" border="0"
						title="<fmt:message key="label.up"/>">
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${status.count < listSize}">
					<img src="<html:rewrite page='/includes/images/downarrow.gif'/>" border="0"
						title="<fmt:message key="label.down"/>" onclick="javascript:downItem('${status.index+1}','${listSize}')">
				</c:when>
				<c:otherwise>
					<img src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>" border="0"
						title="<fmt:message key="label.down"/>">
				</c:otherwise>

			</c:choose> <%-- Don't display remove icon if less than 2 question answers --%></td>
			<c:if test="${listSize > 2}">
				<td><img src="<html:rewrite page='/includes/images/cross.gif'/>" border="0"
					title="<fmt:message key="label.delete"/>" onclick="javascript:removeAnswerOption('${status.index+1}')"></td>
			</c:if>
		</tr>
	</c:forEach>
</table>
</form>
</div>

<%-- This script will adjust question input area height according to the new answer option count. --%>
<script type="text/javascript">
	var obj = window.top.document.getElementById('questionInputArea');
	obj.style.height=obj.contentWindow.document.body.scrollHeight+'px';
</script>