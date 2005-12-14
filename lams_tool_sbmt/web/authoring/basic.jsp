	<!---------------------------Basic Tab Content ------------------------>
	<div id='content_b' class="tabbody content_b" >
	<h2><fmt:message key="label.authoring.heading.basic.desc" /></h2>
	<table class="forms">
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.title" />:</td>
			<td class="formcontrol"><html:text property="title" /></td>
		</tr>
		<tr>
			<td class="formlabel"><fmt:message key="label.authoring.basic.instruction" />:</td>
			<td class="formcontrol"><FCK:editor id="instructions"
				basePath="/lams/fckeditor/" height="150" width="85%">
				<c:out value="${authoring.instruction}" escapeXml="false"/>
			</FCK:editor></td>
		</tr>
		<tr><td colspan="2"><html:errors/></td></tr>
		</table>		
		<!-- Button Row -->
		<p align="right">
			<html:submit property="action" styleClass="button">
				<fmt:message key="label.authoring.save.button" />
			</html:submit>
			<html:button property="cancel"
				onclick="window.close()" styleClass="button">
				<fmt:message key="label.authoring.cancel.button" />
			</html:button>
		</p>

	</div>
	