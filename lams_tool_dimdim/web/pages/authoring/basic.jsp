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
			<lams:FCKEditor id="instructions" value="${formBean.instructions}"
				contentFolderID="${sessionMap.contentFolderID}"></lams:FCKEditor>
		</td>
	</tr>

	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.basic.dimdimSettings" />
			</div>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.topic" />
			:
			<html:text property="topic"></html:text>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.meetingKey" />
			:
			<html:text property="meetingKey"></html:text>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.maxAttendeeMikes" />
			:
			<html:select property="maxAttendeeMikes">
				<html:option value="1"></html:option>
				<html:option value="2"></html:option>
				<html:option value="3"></html:option>
				<html:option value="4"></html:option>
				<html:option value="5"></html:option>
			</html:select>
		</td>
	</tr>

</table>
