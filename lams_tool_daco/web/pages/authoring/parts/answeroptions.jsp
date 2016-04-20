<%@ include file="/common/taglibs.jsp"%>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="listSize" value="${fn:length(answerOptionList)}" />
<c:set var="ordinal"><fmt:message key="label.authoring.basic.answeroption.ordinal"/></c:set>

<div id="answerOptionsArea" class="space-bottom-top">
<form id="answerOptionsForm"><input type="hidden" id="answerOptionCount" name="answerOptionCount" value="${listSize}"/>
<div class="field-name"><fmt:message key="label.authoring.basic.answeroption" /></div>
<a href="#" onclick="javascript:addAnswerOption()" class="btn btn-default btn-sm pull-left"><fmt:message
	key="label.authoring.basic.answeroption.add" /></a>
	
		<%-- This image is shown when answer options are downloaded from a server. --%>
		<img src="${ctxPath}/includes/images/indicator.gif"
			style="display:none;" id="answerOptionsArea_Busy" name="answerOptionsArea_Busy"/>
<table class="table table-condensed">
	<c:forEach var="item" items="${answerOptionList}" varStatus="status">
		<tr>
			<td width="3px">${fn:substring(ordinal,status.index,status.index+1)})</td>
			<td><input type="text" name="answerOptionItemDesc${status.index+1}" id="answerOptionItemDesc${status.index+1}" size="60" value="<c:out  value='${item}'/>"></td>

			<td class="arrows">
			<!-- Don't display up icon if first line -->
			<c:if test="${not status.first}">
 				<lams:Arrow state="up" title="<fmt:message key='label.authoring.basic.answeroption.up'/>" onclick="javascript:upItem('${status.index+1}')"/>
 			</c:if>
			<!-- Don't display down icon if last line -->
			<c:if test="${not status.last}">
				<lams:Arrow state="down" title="<fmt:message key='label.authoring.basic.answeroption.down'/>" onclick="javascript:downItem('${status.index+1}','${listSize}')"/>
 			</c:if>
			</td>
			<!-- Don't display remove icon if there is less than 2 answer options -->
			<c:if test="${listSize > 2}">
				<td><i class="fa fa-times" title="<fmt:message key='label.common.delete'/>" onclick="javascript:removeAnswerOption('${status.index+1}')"></i></td>
			</c:if>
		</tr>
	</c:forEach>
</table> 
</form>
</div>

<%-- This script will adjust question input area height according to the new answer option count. --%>
<script type="text/javascript">
	resizeQuestionInputArea ();
</script>