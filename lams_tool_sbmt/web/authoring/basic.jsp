<%@include file="/common/taglibs.jsp"%>

<!-- Basic Tab Content -->
<table cellpadding="0">
	<tr>
		<td>
			<lams:SetEditor id="Title" text="${authoring.title}" small="true" key="label.authoring.basic.title" />
		</td>
	</tr>
	<tr>
		<td>
			<lams:SetEditor id="Instructions" text="${authoring.instruction}" key="label.authoring.basic.instruction" />
		</td>
	</tr>
</table>
