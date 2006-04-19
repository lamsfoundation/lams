<%@ include file="/common/taglibs.jsp"%>

<c:set var="authoringForm" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<!-- ========== Basic Tab ========== -->
<div id="authoring_basic">
	<table class="forms">
		
		<%-- ========== TITLE FIELD ========== --%>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.basic.title" />
			</td>
			<td>
				<lams:SetEditor id="Title" text="${authoringForm.title}" small="true" />
			</td>
		</tr>

		<%-- ========== INSTRUCTIONS FIELD ========== --%>
		<tr>
			<td class="formlabel">
				<fmt:message key="label.authoring.basic.instructions" />
			</td>
			<td>
				<lams:SetEditor id="Instructions" text="${authoringForm.instructions}" />
			</td>
		</tr>
	</table>
</div>
