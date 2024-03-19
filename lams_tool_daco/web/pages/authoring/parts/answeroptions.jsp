<%@ include file="/common/taglibs.jsp"%>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="listSize" value="${fn:length(answerOptionList)}" />
<c:set var="ordinal"><spring:escapeBody javaScriptEscape='true'><fmt:message key="label.authoring.basic.answeroption.ordinal"/></spring:escapeBody></c:set>

<div id="answerOptionsArea" class="voffset10">
<form id="answerOptionsForm"><input type="hidden" id="answerOptionCount" name="answerOptionCount" value="${listSize}"/>

<div class="form-group"><label><fmt:message key="label.authoring.basic.answeroption" /></label>
<a href="#nogo" onclick="javascript:addAnswerOption()" class="btn btn-default btn-xs loffset5">
	<i class="fa fa-plus"></i>&nbsp;<fmt:message key="label.authoring.basic.answeroption.add" />
</a>

<%-- This image is shown when answer options are downloaded from a server. --%>
<i class="fa fa-refresh fa-spin fa-1x fa-fw" style="display:none;" id="answerOptionsArea_Busy" name="answerOptionsArea_Busy"></i>
	
<table class="table table-condensed table-no-border table-nonfluid">
	<c:forEach var="item" items="${answerOptionList}" varStatus="status">
		<tr>
			<td width="3px">${fn:substring(ordinal,status.index,status.index+1)})</td>
			<td><input type="text" name="answerOptionItemDesc${status.index+1}" id="answerOptionItemDesc${status.index+1}" size="60" value="<c:out  value='${item}'/>" class="form-control input-sm"></td>

			<td class="arrows">
			<!-- Don't display up icon if first line -->
			<c:if test="${not status.first}">
 				<lams:Arrow state="up" titleKey="label.authoring.basic.answeroption.up" onclick="javascript:upItem('${status.index+1}')"/>
 			</c:if>
			<!-- Don't display down icon if last line -->
			<c:if test="${not status.last}">
				<lams:Arrow state="down" titleKey="label.authoring.basic.answeroption.down" onclick="javascript:downItem('${status.index+1}','${listSize+1}')"/>
 			</c:if>
			</td>
			<!-- Don't display remove icon if there is less than 2 answer options -->
			<c:if test="${listSize > 2}">
				<td><i class="fa fa-times" title="<fmt:message key='label.common.delete'/>" onclick="javascript:removeAnswerOption('${status.index+1}')"></i></td>
			</c:if>
		</tr>
	</c:forEach>
</table> 
</div>

</form>
</div>

<%-- This script will adjust question input area height according to the new answer option count. --%>
<!-- <script type="text/javascript">
	resizeQuestionInputArea ();
</script> -->
