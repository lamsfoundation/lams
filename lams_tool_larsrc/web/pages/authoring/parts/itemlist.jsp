<%@ include file="/common/taglibs.jsp" %>

<div id="itemList">
<h2><fmt:message key="label.authoring.basic.resource.list.title" /></h2>
<table class="forms">
<c:forEach var="resource" items="${resourceList}" varStatus="status">
	<tr>
	<%-- Resource Type:1=URL,2=File,3=Website,4=Learning Object --%>
	<c:if test="${resource.type == 1}">
		<td>
			<fmt:message key="label.authoring.basic.resource.url" />
		</td>
		<td>${resource.title}</td>
		<td>
			<input type="button" name="verifyUrl" value="<fmt:message key="label.authoring.basic.resource.verify.url" />" 
				onclick="verifyUrl('${resource.url}','Verify URL')">
		</td>
		<td>
			<input type="button" name="editItem" value="<fmt:message key="label.authoring.basic.resource.edit" />"
				onclick="editItem(${status.index})">
		</td>
		<td>
			<input type="button" name="deleteItem"  value="<fmt:message key="label.authoring.basic.resource.delete" />"
				onclick="deleteItem(${status.index})">
		</td>
	</c:if>
	<c:if test="${resource.type == 2}">
		<td>
			<fmt:message key="label.authoring.basic.resource.file" />
		</td>
		<td>${resource.title}</td>
		<td><input type="button" name="previewItem" value="<fmt:message key="label.authoring.basic.resource.preview" />"></td>
		<td><input type="button" name="editItem"  value="<fmt:message key="label.authoring.basic.resource.edit" />"></td>
		<td><input type="button" name="deleteItem" value="<fmt:message key="label.authoring.basic.resource.delete" />"></td>
	</c:if>
	<c:if test="${resource.type == 3}">
		<td>
			<fmt:message key="label.authoring.basic.resource.website" />
		</td>
		<td><input type="button" name="previewItem" value="<fmt:message key="label.authoring.basic.resource.preview" />"></td>
		<td><input type="button" name="editItem"  value="<fmt:message key="label.authoring.basic.resource.edit" />"></td>
		<td><input type="button" name="deleteItem" value="<fmt:message key="label.authoring.basic.resource.delete" />"></td>
	</c:if>
	<c:if test="${resource.type == 4}">
		<td>
			<fmt:message key="label.authoring.basic.resource.learning.object" />
		</td>
		<td><input type="button" name="previewItem" value="<fmt:message key="label.authoring.basic.resource.preview" />"></td>
		<td><input type="button" name="editItem"  value="<fmt:message key="label.authoring.basic.resource.edit" />"></td>
		<td><input type="button" name="deleteItem" value="<fmt:message key="label.authoring.basic.resource.delete" />"></td>
	</c:if>
	</tr>
</c:forEach>
</table>
</div>
<script lang="javascript">
	window.top.hideMessage();
	var obj = window.top.document.getElementById('resourceListArea');
	obj.innerHTML= document.getElementById("itemList").innerHTML;
</script>