<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<div id="itemList">
<div class="panel panel-default voffset5">
	<div class="panel-heading panel-title">
		<fmt:message key="label.authoring.basic.survey.list.title" />
		<i class="fa fa-spinner" style="display: none" id="resourceListArea_Busy"></i>
	</div>

	<table class="table table-striped table-condensed" id="itemTable">
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<c:forEach var="question" items="${sessionMap.questionList}" varStatus="status">
			<tr>
				<td width="20px" class="text-center">
					<c:choose>
						<c:when test="${question.type == 1}">
							<img src="${ctxPath}/includes/images/icon_single.gif" />
						</c:when>
						<c:when test="${question.type == 2}">
							<img src="${ctxPath}/includes/images/icon_multiple.gif" />
						</c:when>
						<c:when test="${question.type == 3}">
							<img src="${ctxPath}/includes/images/icon_text.gif" />
						</c:when>
					</c:choose>
				</td>

				<td>
					<c:out value="${question.shortTitle}" escapeXml="true"/>
				</td>

				<td width="80px" class="text-center">
					<c:if test="${question.optional}">
						<fmt:message key="label.optional" />
					</c:if>
				</td>

				<td class="arrows" style="width:5%">
					<c:if test="${not status.first}">
						<lams:Arrow state="up" titleKey="label.up" onclick="upQuestion(${status.index},'${sessionMapID}')"/>
					</c:if>
					<c:if test="${not status.last}">
						<lams:Arrow state="down" titleKey="label.down" onclick="downQuestion(${status.index},'${sessionMapID}')"/>
					</c:if>
				</td>

				<td class="text-center" style="width:5%"><i class="fa fa-pencil"	
						title="<fmt:message key="label.authoring.basic.survey.edit"/>"
						onclick="editItem(${status.index},${question.type},'${sessionMapID}','${contentFolderID}')"></i>

				</td>
				<td  class="text-center"  style="width:5%"><i class="fa fa-clone"	
						title="<fmt:message key="label.copy"/>"
						onclick="copyItem(${status.index},${question.type},'${sessionMapID}','${contentFolderID}')">
				</td>
				<td  class="text-center"  style="width:5%"><i class="fa fa-times"	
						title="<fmt:message	key="label.authoring.basic.survey.delete" />"
						onclick="deleteItem(${status.index},'${sessionMapID}')"></i>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
</div>

<%-- This script will works when a new resource item submit in order to refresh "Survey List" panel. --%>
<script lang="javascript">
	if ($("#questionInputArea").is(':visible')) {
		hideMessage();
		var itemList = $("#itemList", "#questionInputArea").html();
		$("#surveyListArea").html(itemList);
	}
</script>
