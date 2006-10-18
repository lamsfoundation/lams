<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- ========== Basic Tab ========== -->
<table cellpadding="0">
		<tr>
			<td>
				<div class="field-name">
					<fmt:message key="label.authoring.basic.title"></fmt:message>
				</div>
				<html:text property="title" style="width: 99%;"></html:text>
			</td>
		</tr>
		<tr>
			<td>
				<div class="field-name">
					<fmt:message key="label.authoring.basic.instructions"></fmt:message>
				</div>
				<lams:FCKEditor id="instructions"
					value="${authoringForm.instructions}"
					contentFolderID="${authoringForm.contentFolderID}"></lams:FCKEditor>
			</td>
		</tr>
</table>
