<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="itemList">
<h2><fmt:message key="label.authoring.basic.survey.list.title" />
<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="surveyListArea_Busy" /></h2>

<table id="itemTable" style="align:left;width:650px" >
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

<c:forEach var="survey" items="${sessionMap.surveyList}" varStatus="status">
	<tr>
	<%-- Survey Type:1=URL,2=File,3=Website,4=Learning Object --%>
	<c:if test="${survey.type == 1}">
		<td width="200px" align="right" class="field-name">
			<fmt:message key="label.authoring.basic.survey.url" />:
		</td>
		<td>${survey.title}</td>
		<td width="130px" nowrap="nowrap" align="center">
			<a href="#" onclick="previewItem(1,${status.index},'${sessionMapID}')" class="button">
				<fmt:message key="label.authoring.basic.survey.verify.url" />
			</a>
		</td>
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
	</c:if>
	<c:if test="${survey.type == 2}">
		<td  width="200px" align="right" class="field-name">
			<fmt:message key="label.authoring.basic.survey.file" />:
		</td>
		<td>${survey.title}</td>
		<td width="130px" nowrap="nowrap" align="center">
			<a href="#" onclick="previewItem(2,${status.index},'${sessionMapID}')" class="button">
				<fmt:message key="label.authoring.basic.survey.preview" />
			</a>
		</td>
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
	</c:if>
	<c:if test="${survey.type == 3}">
		<td width="200px" align="right" class="field-name">
			<fmt:message key="label.authoring.basic.survey.website" />:
		</td>
		<td>${survey.title}</td>
		<td width="130px" nowrap="nowrap" align="center">
			<a href="#" onclick="previewItem(3,${status.index},'${sessionMapID}')" class="button">
				<fmt:message key="label.authoring.basic.survey.preview" />
			</a>
		</td>
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
	</c:if>
	<c:if test="${survey.type == 4}">
		<td width="200px" align="right" class="field-name">
			<fmt:message key="label.authoring.basic.survey.learning.object" />:
		</td>
		<td>${survey.title}</td>
		<td width="130px" nowrap="nowrap" align="center">
			<a href="#" onclick="previewItem(4,${status.index},'${sessionMapID}')" class="button">
				<fmt:message key="label.authoring.basic.survey.preview" />
			</a>
		</td>
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
	</c:if>
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
