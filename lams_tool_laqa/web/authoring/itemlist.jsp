<%@ include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

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
					onclick="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newEditableQuestionBox&questionIndex=${status.count}&contentFolderID=${formBean.contentFolderID}&httpSessionID=${formBean.httpSessionID}&toolContentID=${formBean.toolContentID}&usernameVisible=${formBean.usernameVisible}&lockWhenFinished=${formBean.lockWhenFinished}&questionsSequenced=${formBean.questionsSequenced}"/>')"></i>
				</td>
				<td  align="center" style="width:5%"><i class="fa fa-times"	title="<fmt:message key="label.tip.deleteQuestion" />"
					onclick="removeQuestion(${status.count})"></i>
				</td>
			</tr>
		</c:forEach>

	</table>
</div>