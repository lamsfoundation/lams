<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="itemList">
<h2><fmt:message key="label.authoring.basic.resource.list.title" />
<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="resourceListArea_Busy" /></h2>
<table class="forms">
<c:forEach var="resource" items="${resourceList}" varStatus="status">
	<tr>
	<%-- Resource Type:1=URL,2=File,3=Website,4=Learning Object --%>
	<c:if test="${resource.type == 1}">
		<td width="200px" align="right">
			<fmt:message key="label.authoring.basic.resource.url" />
		</td>
		<td>${resource.title}</td>
		<td width="50px">
			<input type="button" name="vu" value="<fmt:message key="label.authoring.basic.resource.verify.url" />" 
				onclick="verifyUrl('${resource.url}','VerifyURL')" class="button">
		</td>
		<td width="50px">
			<input type="button" name="ei" value="<fmt:message key="label.authoring.basic.resource.edit" />"
				onclick="editItem(${status.index})" class="button">
		</td>
		<td width="50px">
			<input type="button" name="di"  value="<fmt:message key="label.authoring.basic.resource.delete" />"
				onclick="deleteItem(${status.index})" class="button">
		</td>
	</c:if>
	<c:if test="${resource.type == 2}">
		<td  width="200px" align="right">
			<fmt:message key="label.authoring.basic.resource.file" />
		</td>
		<td>${resource.title}</td>
		<td width="50px">
			<input type="button" name="pi" value="<fmt:message key="label.authoring.basic.resource.preview" />"
				onclick="preview()" class="button">
		</td>
		<td width="50px">
			<input type="button" name="ei"  value="<fmt:message key="label.authoring.basic.resource.edit" />
				onclick="editItem(${status.index})" class="button">
		</td>
		<td width="50px">
			<input type="button" name="di" value="<fmt:message key="label.authoring.basic.resource.delete" />"
					onclick="deleteItem(${status.index})" class="button">
		</td>
	</c:if>
	<c:if test="${resource.type == 3}">
		<td width="200px" align="right">
			<fmt:message key="label.authoring.basic.resource.website" />
		</td>
		<td width="50px">
			<input type="button" name="pi" value="<fmt:message key="label.authoring.basic.resource.preview" />">
		</td>
		<td width="50px">
			<input type="button" name="ei"  value="<fmt:message key="label.authoring.basic.resource.edit" />"
				onclick="editItem(${status.index})" class="button">
		</td>
		<td width="50px">
			<input type="button" name="di" value="<fmt:message key="label.authoring.basic.resource.delete" />"
					onclick="deleteItem(${status.index})" class="button">
		</td>
	</c:if>
	<c:if test="${resource.type == 4}">
		<td width="200px" align="right">
			<fmt:message key="label.authoring.basic.resource.learning.object" />
		</td>
		<td width="50px">
			<input type="button" name="pi" value="<fmt:message key="label.authoring.basic.resource.preview" />">
		</td>
		<td width="50px">
			<input type="button" name="ei"  value="<fmt:message key="label.authoring.basic.resource.edit" />"
				onclick="editItem(${status.index})" class="button">
		</td>
		<td width="50px">
			<input type="button" name="di" value="<fmt:message key="label.authoring.basic.resource.delete" />"
					onclick="deleteItem(${status.index})" class="button">
		</td>
	</c:if>
	</tr>
</c:forEach>
</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
	window.top.hideMessage();
	var obj = window.top.document.getElementById('resourceListArea');
	obj.innerHTML= document.getElementById("itemList").innerHTML;
</script>