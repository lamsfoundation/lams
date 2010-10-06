<%@ include file="/common/taglibs.jsp"%>

<c:set var="formBean"
	value="<%=request
									.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
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
			<lams:CKEditor id="instructions" value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>
		</td>
	</tr>

	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.dimdimSettings" />
			</div>

			<fmt:message key="label.authoring.basic.maxAttendeeMikes" />
			:
			<html:select property="maxAttendeeMikes">
				<html:option value="1"></html:option>
				<html:option value="2"></html:option>
				<html:option value="3"></html:option>
			</html:select>
		</td>
	</tr>

</table>
