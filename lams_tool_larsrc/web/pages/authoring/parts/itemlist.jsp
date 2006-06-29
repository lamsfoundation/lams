<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="itemList">
<h2><fmt:message key="label.authoring.basic.resource.list.title" />
<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="resourceListArea_Busy" /></h2>

<table id="itemTable" style="align:left;width:650px" >
<c:forEach var="resource" items="${resourceList}" varStatus="status">
	<tr>
	<%-- Resource Type:1=URL,2=File,3=Website,4=Learning Object --%>
	<c:if test="${resource.type == 1}">
		<td width="200px" align="right" class="field-name">
			<fmt:message key="label.authoring.basic.resource.url" />:
		</td>
		<td>${resource.title}</td>
		<td width="130px" nowrap="nowrap" align="center">
			<a href="#" onclick="previewItem(1,${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.verify.url" />
			</a>
		</td>
		<td width="80px" align="center">
			<a href="#" onclick="editItem(${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.edit" />
			</a>
		</td>
		<td width="80px" align="center">
			<a href="#" onclick="deleteItem(${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.delete" />
			</a>
		</td>
	</c:if>
	<c:if test="${resource.type == 2}">
		<td  width="200px" align="right" class="field-name">
			<fmt:message key="label.authoring.basic.resource.file" />:
		</td>
		<td>${resource.title}</td>
		<td width="130px" nowrap="nowrap" align="center">
			<a href="#" onclick="previewItem(2,${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.preview" />
			</a>
		</td>
		<td width="80px" align="center">
			<a href="#" onclick="editItem(${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.edit" />
			</a>
		</td>
		<td width="80px" align="center">
			<a href="#" onclick="deleteItem(${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.delete" />
			</a>
		</td>
	</c:if>
	<c:if test="${resource.type == 3}">
		<td width="200px" align="right" class="field-name">
			<fmt:message key="label.authoring.basic.resource.website" />:
		</td>
		<td>${resource.title}</td>
		<td width="130px" nowrap="nowrap" align="center">
			<a href="#" onclick="previewItem(3,${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.preview" />
			</a>
		</td>
			<td width="80px" align="center">
			<a href="#" onclick="editItem(${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.edit" />
			</a>
		</td>
		<td width="80px" align="center">
			<a href="#" onclick="deleteItem(${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.delete" />
			</a>
		</td>
	</c:if>
	<c:if test="${resource.type == 4}">
		<td width="200px" align="right" class="field-name">
			<fmt:message key="label.authoring.basic.resource.learning.object" />:
		</td>
		<td>${resource.title}</td>
		<td width="130px" nowrap="nowrap" align="center">
			<a href="#" onclick="previewItem(4,${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.preview" />
			</a>
		</td>
		<td width="80px" align="center">
			<a href="#" onclick="editItem(${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.edit" />
			</a>
		</td>
		<td width="80px" align="center">
			<a href="#" onclick="deleteItem(${status.index})" class="button">
				<fmt:message key="label.authoring.basic.resource.delete" />
			</a>
		</td>
	</c:if>
	</tr>
</c:forEach>
</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">

	if(window.top != null){
		window.top.hideMessage();
		var obj = window.top.document.getElementById('resourceListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>