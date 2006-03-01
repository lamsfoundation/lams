
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="dolly" %>

	<!---------------------------Basic Tab Content ------------------------>
	<table class="forms">
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
			<td class="formcontrol"><html:text property="title" /></td>
		</tr>
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.instruction" />:</td>
			<td NOWRAP width="700">
				<dolly:SetEditor id="Instructions" text="${authoring.instruction}"/>
			<!-- remove <FCK:editor id="instructions"
				basePath="/lams/fckeditor/" height="150" width="85%">
				<c:out value="${authoring.instruction}" escapeXml="false"/>
			</FCK:editor> -->
			</td>
		</tr>
		<tr><td colspan="2"><html:errors/></td></tr>
		</table>
