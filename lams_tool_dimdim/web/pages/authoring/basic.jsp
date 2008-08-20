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
			<fmt:message key="label.authoring.basic.maxParticipants" />
			:
			<html:select property="maxParticipants">
				<html:option value="20"></html:option>
				<html:option value="40"></html:option>
				<html:option value="60"></html:option>
				<html:option value="80"></html:option>
				<html:option value="100"></html:option>
				<html:option value="200"></html:option>
				<html:option value="300"></html:option>
				<html:option value="400"></html:option>
				<html:option value="500"></html:option>
			</html:select>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.meetingDurationInHours" />
			:
			<html:select property="meetingDurationInHours">
				<html:option value="1"></html:option>
				<html:option value="2"></html:option>
				<html:option value="3"></html:option>
				<html:option value="4"></html:option>
				<html:option value="5"></html:option>
			</html:select>
		</td>
	</tr>

	<tr>
		<td>
			<html:checkbox property="allowVideo" value="1" styleClass="noBorder"
				styleId="allowVideo"></html:checkbox>
			<label for="allowVideo">
				<fmt:message key="label.authoring.basic.allowVideo" />
			</label>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="label.authoring.basic.attendeeMikes" />
			:
			<html:select property="attendeeMikes">
				<html:option value="1"></html:option>
				<html:option value="2"></html:option>
				<html:option value="3"></html:option>
				<html:option value="4"></html:option>
				<html:option value="5"></html:option>
			</html:select>
		</td>
	</tr>

</table>
