<%@include file="/common/taglibs.jsp"%>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- Basic Tab Content -->
<table cellpadding="0">
	<tr>
		<td>
			<lams:SetEditor id="Title" text="${formBean.title}" small="true" key="label.authoring.basic.title" />
		</td>
	</tr>
	<tr>
		<td>
			<lams:SetEditor id="Instructions" text="${formBean.instructions}" key="label.authoring.basic.instruction" />
		</td>
	</tr>
</table>
