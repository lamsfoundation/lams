<%@ include file="/common/taglibs.jsp"%>

<div id="itemList">
	<table class="table table-striped table-condensed">
		<tr>
			<th colspan="5"><fmt:message key="label.questions"/></th>
		</tr>

		<c:forEach items="${questionDTOs}" var="currentDTO"	varStatus="status">
			<tr>
				<td>
					<c:out value="${currentDTO.question}" escapeXml="false" />
				</td>
				<td class="arrows" style="width:5%">
					<!-- Don't display up icon if first line -->
					<c:if test="${not status.first}">
		 				<lams:Arrow state="up" titleKey="label.tip.moveQuestionUp"
		 							onclick="javascript:submitModifyAuthoringQuestion(${status.count},'moveQuestionUp')" />
		 			</c:if>
					<!-- Don't display down icon if last line -->
					<c:if test="${not status.last}">
						<lams:Arrow state="down" titleKey="label.tip.moveQuestionDown"
									onclick="javascript:submitModifyAuthoringQuestion(${status.count},'moveQuestionDown') "/>
		 			</c:if>
				</td>
				<td align="center" style="width:5%"><i class="fa fa-pencil"	title="<fmt:message key="label.tip.editQuestion" />"
					onclick="javascript:showMessage('<lams:WebAppURL/>authoring/newEditableQuestionBox.do?questionIndex=${status.count}&contentFolderID=${authoringForm.contentFolderID}&httpSessionID=${authoringForm.httpSessionID}&toolContentID=${authoringForm.toolContentID}&usernameVisible=${authoringForm.usernameVisible}&lockWhenFinished=${authoringForm.lockWhenFinished}&questionsSequenced=${authoringForm.questionsSequenced}')"></i>
				</td>
				<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.tip.deleteQuestion" />"
					onclick="removeQuestion(${status.count})"></i>
				</td>
			</tr>
		</c:forEach>

	</table>
</div>