<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- ========== Basic Tab ========== -->
<table cellpadding="0">
	<tbody>
		<tr>
			<td>
				<lams:SetEditor id="Title" text="${authoringForm.title}" small="true" key="label.authoring.basic.title" />
			</td>
		</tr>
		<tr>
			<td>
				<lams:SetEditor id="Instructions" text="${authoringForm.instructions}" key="label.authoring.basic.instructions" />
			</td>
		</tr>
	</tbody>
</table>
