<%@ include file="/common/taglibs.jsp" %>

<h2><fmt:message key="label.authoring.basic.resource.list.title" /></h2>
<table class="forms">
<forEach var="item" value="${resourceList}">
	<tr>
	<%-- Resource Type:1=URL,2=File,3=Website,4=Learning Object --%>
	<c:if test="${item.type == 1}">
		<td>
			<fmt:message key="label.authoring.basic.resource.url" />
		</td>
		<td>${resource.title}</td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.verify.url" />"></td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.edit" />"></td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.delete" />"></td>
	</c:if>
	<c:if test="${item.type == 2}">
		<td>
			<fmt:message key="label.authoring.basic.resource.file" />
		</td>
		<td>${resource.title}</td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.preview" />"></td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.edit" />"></td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.delete" />"></td>
	</c:if>
	<c:if test="${item.type == 3}">
		<td>
			<fmt:message key="label.authoring.basic.resource.website" />
		</td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.preview" />"></td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.edit" />"></td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.delete" />"></td>
	</c:if>
	<c:if test="${item.type == 4}">
		<td>
			<fmt:message key="label.authoring.basic.resource.learning.object" />
		</td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.preview" />"></td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.edit" />"></td>
		<td><input type="button" name="<fmt:message key="label.authoring.basic.resource.delete" />"></td>
	</c:if>
	</tr>
</forEach>
</table>