<%@include file="/common/taglibs.jsp"%>
<c:set var="formBean"
	value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- Basic Tab Content -->
<table cellpadding="0">
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="title" style="width: 99%;" maxlength="255"></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:CKEditor id="instructions"
				value="${formBean.instructions}"
				contentFolderID="${formBean.contentFolderID}">
			</lams:CKEditor>
		</td>
	</tr>
</table>
