<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"	scope="request" />
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="questionList">
	<h2 class="spacer-left">
		<fmt:message key="label.authoring.basic.question.list.title" />
		<img src="${ctxPath}/includes/images/indicator.gif"	style="display:none" id="assessmentListArea_Busy" />
	</h2>

	<table class="alternative-color" id="questionTable" cellspacing="0">
		<tr>
			<th width="20%"><fmt:message key="label.authoring.basic.list.header.type" /></th>
			<th colspan="4"><fmt:message key="label.authoring.basic.list.header.question" /></th>
		</tr>	
	
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

		<c:forEach var="question" items="${sessionMap.questionList}" varStatus="status">
			<tr>
				<td>
					<span class="field-name">
						<c:choose>
							<c:when test="${question.type == 1}">
								<fmt:message key="label.authoring.basic.type.multiple.choice" />
							</c:when>
							<c:when test="${question.type == 2}">
								<fmt:message key="label.authoring.basic.type.matching.pairs" />
							</c:when>
							<c:when test="${question.type == 3}">
								<fmt:message key="label.authoring.basic.type.short.answer" />
							</c:when>
							<c:when test="${question.type == 4}">
								<fmt:message key="label.authoring.basic.type.numerical" />
							</c:when>
							<c:when test="${question.type == 5}">
								<fmt:message key="label.authoring.basic.type.true.false" />
							</c:when>
							<c:when test="${question.type == 6}">
								<fmt:message key="label.authoring.basic.type.essay" />
							</c:when>
						</c:choose>
					</span>
				</td>

				<td>
					${question.title}
				</td>
				
				<td width="40px" style="vertical-align:middle;">
					<c:if test="${not status.first}">
						<img src="<html:rewrite page='/includes/images/uparrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.basic.up"/>"
							onclick="upQuestion(${status.index},'${sessionMapID}')">
						<c:if test="${status.last}">
							<img
								src="<html:rewrite page='/includes/images/downarrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.down"/>">
						</c:if>
					</c:if>

					<c:if test="${not status.last}">
						<c:if test="${status.first}">
							<img
								src="<html:rewrite page='/includes/images/uparrow_disabled.gif'/>"
								border="0" title="<fmt:message key="label.authoring.basic.up"/>">
						</c:if>

						<img src="<html:rewrite page='/includes/images/downarrow.gif'/>"
							border="0" title="<fmt:message key="label.authoring.basic.down"/>"
							onclick="downQuestion(${status.index},'${sessionMapID}')">
					</c:if>
				</td>
				
				<td width="20px" style="vertical-align:middle;">
					<c:set var="editQuestionUrl" >
						<c:url value='/authoring/editQuestion.do'/>?sessionMapID=${sessionMapID}&questionIndex=${status.index}&KeepThis=true&TB_iframe=true&height=540&width=850&modal=true
					</c:set>		
					<a href="${editQuestionUrl}" class="thickbox" style="margin-left: 20px;"> 
						<img src="<html:rewrite page='/includes/images/edit.gif'/>" 
							title="<fmt:message key="label.authoring.basic.edit" />" style="border-style: none;"/>
					</a>				
                </td>
                
				<td width="20px" style="vertical-align:middle;">
					<img src="${tool}includes/images/cross.gif"
						title="<fmt:message key="label.authoring.basic.delete" />"
						onclick="deleteQuestion(${status.index},'${sessionMapID}')" />
				</td>
			</tr>
		</c:forEach>
	</table>
</div>

<%-- This script will works when a new resoruce item submit in order to refresh "Assessment List" panel. --%>
<script lang="javascript">
	//if(window.top != null){
	//	window.top.hideMessage();
	//	var obj = window.top.document.getElementById('questionListArea');
	//	obj.innerHTML = document.getElementById("questionList").innerHTML;
	//}
</script>
