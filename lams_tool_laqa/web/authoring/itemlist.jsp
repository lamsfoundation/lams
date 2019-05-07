<%@ include file="/common/taglibs.jsp"%>
<c:set var="httpSessionID" value="${authoringForm.httpSessionID}" />
<c:set var="sessionMap" value="${sessionScope[httpSessionID]}" />

<div id="itemList">
	<div class="panel panel-default add-file">
	
	<div class="panel-heading panel-title">
		<fmt:message key="label.questions" />
		
		<div class="pull-right">
			<c:url var="tempUrl" value="">
				<c:param name="output">
					<c:url value='/authoring/importQbQuestion.do'/>?httpSessionID=${httpSessionID}
				</c:param>
			</c:url>
			<c:set var="returnUrl" value="${fn:substringAfter(tempUrl, '=')}" />
		
			<a href="<lams:LAMSURL/>/searchQB/start.do?returnUrl=${returnUrl}&toolContentId=${sessionMap.toolContentID}&KeepThis=true&TB_iframe=true&modal=true" 
				class="btn btn-default btn-xs thickbox"> 
				Import from question bank
			</a>
		</div> 
	</div>

	<table class="table table-striped table-condensed">
		<c:forEach items="${questionDTOs}" var="currentDTO"	varStatus="status">
			<tr>
				<td>
					<c:out value="${currentDTO.question}" escapeXml="false" />
				</td>
				<td class="arrows" style="width:5%">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
		 				<lams:Arrow state="up" title="<fmt:message key='label.tip.moveQuestionUp'/>"
		 							onclick="javascript:submitModifyAuthoringQuestion(${status.count},'moveQuestionUp')" />
		 			</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" title="<fmt:message key='label.tip.moveQuestionDown'/>"
									onclick="javascript:submitModifyAuthoringQuestion(${status.count},'moveQuestionDown') "/>
		 			</c:if>
				</td>
				<td align="center" style="width:5%"><i class="fa fa-pencil"	title="<fmt:message key="label.tip.editQuestion" />"
					onclick="javascript:showMessage('<lams:WebAppURL/>authoring/newEditableQuestionBox.do?questionIndex=${status.count}&contentFolderID=${authoringForm.contentFolderID}&httpSessionID=${httpSessionID}&toolContentID=${authoringForm.toolContentID}')"></i>
				</td>
				<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.tip.deleteQuestion" />"
					onclick="removeQuestion(${status.count})"></i>
				</td>
			</tr>
		</c:forEach>

	</table>
	</div>
</div>