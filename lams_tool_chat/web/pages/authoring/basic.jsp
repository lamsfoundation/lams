<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- ========== Basic Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td class="field-name" width="20%">
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<lams:SetEditor id="Title" text="${authoringForm.title}" small="true" />
			</td>
		</tr>

		<tr>
			<td class="field-name" width="20%">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<lams:SetEditor id="Instructions" text="${authoringForm.instructions}" />
			</td>
		</tr>
	</tbody>
</table>
