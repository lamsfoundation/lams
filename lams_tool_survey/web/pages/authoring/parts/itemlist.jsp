<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="itemList">
<h2><fmt:message key="label.authoring.basic.survey.list.title" />
<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="surveyListArea_Busy" /></h2>

<table id="itemTable" style="align:left;width:650px" >
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<c:forEach var="survey" items="${sessionMap.questionList}" varStatus="status">
	<tr>
		<td width="20px" align="center">
			<c:choose>
				<c:when test="${survey.type == 1}">
					<img src="${ctxPath}/includes/images/icon_single.gif"/>
				</c:when>
				<c:when test="${survey.type == 2}">
					<img src="${ctxPath}/includes/images/icon_multiple.gif"/>
				</c:when>
				<c:when test="${survey.type == 3}">
					<img src="${ctxPath}/includes/images/icon_text.gif"/>
				</c:when>
			</c:choose>
		</td>
		<td>${survey.shortTitle}</td>
		<td width="80px" align="center">
			<a href="#" onclick="editItem(${status.index},'${sessionMapID}')" class="button">
				<fmt:message key="label.authoring.basic.survey.edit" />
			</a>
		</td>
		<td width="80px" align="center">
			<a href="#" onclick="deleteItem(${status.index},'${sessionMapID}')" class="button">
				<fmt:message key="label.authoring.basic.survey.delete" />
			</a>
		</td>
	</tr>
</c:forEach>
</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Survey List" panel. --%>
<script lang="javascript">

	if(window.top != null){
		window.top.hideMessage();
		var obj = window.top.document.getElementById('surveyListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>
