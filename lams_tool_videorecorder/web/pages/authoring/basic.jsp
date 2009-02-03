<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean"
	value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
<c:set var="sessionMapID" value="${formBean.sessionMapID}" />

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
				value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:FCKEditor>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="advanced.allowUseVoice" />
		</td>
		<td>
			<html:checkbox property="allowUseVoice" styleClass="noBorder"></html:checkbox>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="advanced.allowUseCamera" />
		</td>
		<td>
			<html:checkbox property="allowUseCamera" styleClass="noBorder"></html:checkbox>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="advanced.allowLearnerVideoVisibility" />
		</td>
		<td>
			<html:checkbox property="allowLearnerVideoVisibility" styleClass="noBorder"></html:checkbox>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="advanced.allowLearnerVideoExport" />
		</td>
		<td>
			<html:checkbox property="allowLearnerVideoExport" styleClass="noBorder"></html:checkbox>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="advanced.allowComments" />
		</td>
		<td>
			<html:checkbox property="allowComments" styleClass="noBorder"></html:checkbox>
		</td>
	</tr>
	<tr>
		<td class="field-name">
			<fmt:message key="advanced.allowRatings" />
		</td>
		<td>
			<html:checkbox property="allowRatings" styleClass="noBorder"></html:checkbox>
		</td>
	</tr>
</table>
