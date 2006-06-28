<%@ include file="/includes/taglibs.jsp"%>

<!-- Basic Tab Content  -->

<table cellpadding="0">
	<tr>
		<td>
			<lams:SetEditor id="title" text="${NbAuthoringForm.title}" small="true" key="basic.title" />
		</td>
	</tr>
	<tr>
		<td>
			<lams:SetEditor id="content" text="${NbAuthoringForm.content}" key="basic.content" />
		</td>
	</tr>
</table>
