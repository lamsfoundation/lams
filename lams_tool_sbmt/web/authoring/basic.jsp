
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<!---------------------------Basic Tab Content ------------------------>
	<table class="forms">
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
			<td NOWRAP width="700"><lams:SetEditor id="Title" text="${authoring.title}" small="true"/></td>
		</tr>
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.instruction" />:</td>
			<td NOWRAP width="700">
				<lams:SetEditor id="Instructions" text="${authoring.instruction}"/>
			</td>
		</tr>
		<tr><td colspan="2"><html:errors/></td></tr>
		</table>
