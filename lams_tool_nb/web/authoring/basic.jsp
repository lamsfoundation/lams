<%@ include file="/includes/taglibs.jsp"%>
<!-- Basic Tab Content  -->
<table cellpadding="0">
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="basic.title"></fmt:message>
			</div>
			<html:text property="title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="basic.content"></fmt:message>
			</div>
			<lams:FCKEditor id="content" value="${NbAuthoringForm.content}"
				contentFolderID="${NbAuthoringForm.contentFolderID}"></lams:FCKEditor>
		</td>
	</tr>
</table>
