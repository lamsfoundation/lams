<%@ include file="/common/taglibs.jsp"%>
<c:set var="httpSessionID" value="${authoringForm.httpSessionID}" />

<div id="itemList">
	<div class="panel panel-default add-file">
	
	<div class="panel-heading panel-title">
		<fmt:message key="label.questions" />
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