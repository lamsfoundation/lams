<%@ include file="/includes/taglibs.jsp"%>

<!-- Basic Tab Content  -->

<table cellpadding="0">
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="basic.title" />
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<FCK:editor id="title" basePath="/lams/fckeditor/" height="100" width="100%">
				<c:out value="${NbAuthoringForm.title}" escapeXml="false" />
			</FCK:editor>
		</td>
	</tr>

	<tr>
		<td class="field-name">
			<fmt:message key="basic.content" />
		</td>
	</tr>
	<tr>
		<td>
			<FCK:editor id="content" basePath="/lams/fckeditor/" width="100%" height="200">
				<c:out value="${NbAuthoringForm.content}" escapeXml="false" />
			</FCK:editor>
		</td>
	</tr>
</table>
