<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

<!-- Basic Tab Content -->
<table>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.title"></fmt:message>
			</div>
			<html:text property="peerreview.title" style="width: 99%;"></html:text>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div class="field-name">
				<fmt:message key="label.authoring.basic.instruction"></fmt:message>
			</div>
			<lams:CKEditor id="peerreview.instructions"
				value="${formBean.peerreview.instructions}"
				contentFolderID="${formBean.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>
</table>

<div id="criterias-holder">
	<lams:AuthoringRatingCriteria criterias="${sessionMap.ratingCriterias}" hasRatingLimits="true"
		formContentPrefix="peerreview"/>
</div>
